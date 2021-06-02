
package ConditioningPackage;


import java.awt.List;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JProgressBar;
import javax.swing.plaf.synth.SynthGraphicsUtils;

/**The purpose of this class is to create a thread that performs an intelligent
 * Conditioning of the Xray tubes. This class uses IHighvoltagePowerSupply as 
 * a passable supply, this will allow for future supplies to be created and this
 * class should still function so long as all the abstract functions be overridden
 *
 * @author mjsh635
 */
public class ConditioningHandler extends Thread{
    
    Settings vals;
    IHighVoltagePowerSupply HV;
    LoggingController log;
    
    LocalTime TimeEnd;
    LocalTime ArcTimeEnd;
    JProgressBar pb;
    Double StepCount; 
    Double KVStepSize;
    Double MAStepSize;
    Double StepDwellTime;
    Double StartKV;
    Double TargetKV;
    Double StartMA;
    Double TargetMA;
    Double CurrentSetKV;
    Double PreviousSetKV;
    Double CurrentSetMA;
    Double PreviousSetMA;
    Double NextToSetKV;
    Double NextToSetMA;
    Double OnTime; 
    Double OffTime;
    Double ArcKVStep;
    Double ArcMAStep;
    Double ArcRecoveryDwellTime;
    Double MaxKVReached = 0.0;
    Double MaxMAReached = 0.0;
    Double AverageFilamenCurrent;
    Double reducedKVSet;
    Double reducedMASet;
    ArrayList<Double> RecordedFilamentCurrents;
    
    LocalTime ConditioningStartTime;
    LocalTime ConditioningStopTime;
    int ConcurrentArcsBeforeStop;
    int MaxArcsBeforeStop;
    int ArcCount = 0;
    int ConcurrentArcCount = 0;
    int NumberOfConditioningCycles = 1;
    int OnOffCycleCount;
    JLabel TimeRemaining;
    LocalTime EstimatedEndTime;
    Double TotalTime;
    boolean Killsig = false;
    String Name;
    
    /**
     * 
     * @param userSettings settings file
     * @param supply Interface supply
     * @param pb progress bar
     * @param log log file handler
     * @param timeRemainingLabel countdown timer
     * @param name supply name
     */
    public ConditioningHandler(Settings userSettings, IHighVoltagePowerSupply supply, JProgressBar pb, LoggingController log, JLabel timeRemainingLabel, String name) {
        this.vals = userSettings;
        this.HV = supply;
        this.pb = pb;
        this.log = log;
        this.TimeRemaining = timeRemainingLabel;
        this.Name = name;
    }
    
    @Override
    public void run(){
       
        log.Append_To_Log("###########################################################################################");
        log.Append_To_Log("Conditioning|| Starting Conditioning Routine");
        this._StartUp(); // run start up, sets up various variables and settings
        for (int i = 1; i <= NumberOfConditioningCycles; i++) { //depending how many cycles they want to run, run below
            System.out.println(String.format("Conditioning|| Starting Conditioning cycle: %s", i));
            log.Append_To_Log(String.format("Conditioning|| Starting Conditioning cycle: %s", i));
            this.pb.setValue(16);
            if(this.vals.PerformKVRamp.equals("true")&& !Killsig){
            this._KVInitialRamp(); // ramp kv to target with low ma
            }
            this.pb.setValue(32);
            if(this.vals.PerformMARamp.equals("true")&& !Killsig){
            this._MAInitialRamp(); //kv set to 70% then ma ramped to target
            }
            this.pb.setValue(48);
            if(this.vals.PerformKVReramp.equals("true")&& !Killsig){
            this._KVReramp(); //ma held at target and kv ramped from 70 > 100%
            }
            this.pb.setValue(64);
            if(this.vals.PerformOnOffCycles.equals("true")&& !Killsig){
            this._OnOffCycle(); // cycle on and off for requested cycles
            }
            this.pb.setValue(100);
            log.Append_To_Log(String.format("Conditioning|| Complete Conditioning cycle: %s", i));
            System.out.println(String.format("Conditioning|| Complete Conditioning cycle: %s", i));
        }
        Stop_Conditioning(); // run cleanup code
        
        
       
    }
    
    private void _StartUp(){
        log.Append_To_Log("Conditioning|| Setting up");
        ConditioningStartTime = LocalTime.now();
        this.HV.Set_Filament_Limit(Double.valueOf(this.vals.FilamentCurrentLimit));
        this.HV.Set_Filament_Preheat(Double.valueOf(this.vals.FilamentPreHeat));
        this.StepDwellTime = Double.valueOf(this.vals.TimeBetweenSteps);
        this.OnTime = Double.valueOf(this.vals.OnCycleTime);
        this.OffTime = Double.valueOf(this.vals.OffCycleTime);
        this.OnOffCycleCount = Integer.valueOf(this.vals.NumberOnOffCycles);
        this.StepCount = Double.valueOf(this.vals.TotalStepCount);
        this.StartKV =Double.valueOf(this.vals.StartingKV);
        this.TargetKV = Double.valueOf(this.vals.TargetKV);
        this.StartMA=Double.valueOf(this.vals.StartingMA);
        this.TargetMA = Double.valueOf(this.vals.TargetMA);
        this.KVStepSize = ((this.TargetKV - this.StartKV)/this.StepCount);
        this.MAStepSize = ((this.TargetMA - this.StartMA)/this.StepCount);
        this.ArcKVStep = Double.valueOf(this.vals.ArcKVStep);
        this.ArcMAStep = Double.valueOf(this.vals.ArcMAStep);
        this.ConcurrentArcsBeforeStop = Integer.valueOf(this.vals.ConcurrentArcsBeforeStop);
        this.MaxArcsBeforeStop = Integer.valueOf(this.vals.TotalArcsBeforeStop);
        this.ArcRecoveryDwellTime = Double.valueOf(this.vals.ArcRecoveryTime);
        this.NumberOfConditioningCycles = Integer.valueOf(this.vals.NumberOnOffCycles);
        RecordedFilamentCurrents = new ArrayList<>();
        AverageFilamenCurrent = 0.0;
        log.Append_To_Log(String.format("Conditioning|| Voltage: %s --> %s, with %s Steps", StartKV,TargetKV, KVStepSize));
        log.Append_To_Log(String.format("Conditioning|| Current: %s --> %s, with %s Steps", StartMA,TargetMA,MAStepSize));
        log.Append_To_Log(String.format("Conditioning|| Filament Limit: %s, PreHeat: %s", this.vals.FilamentCurrentLimit,this.vals.FilamentPreHeat));
        log.Append_To_Log(String.format("Conditioning|| Total Steps: %s, Dwelling for: %s mins", StepCount,StepDwellTime));
        log.Append_To_Log(String.format("Conditioning|| Number of On/Off Cycles: %s, %s min ON, %s min OFF", OnOffCycleCount,OnTime,OffTime));
        log.Append_To_Log(String.format("Conditioning|| Number of Conditioning Cycles: %s", NumberOfConditioningCycles));
        TotalTime = (((3*(StepCount*StepDwellTime))+ (OnOffCycleCount*(OnTime+OffTime)))*1.05);
        _setTimeRemaining(TotalTime);
        
    }
    
    private void _setTimeRemaining(Double value){
        TimeRemaining.setText(String.format("%.2s min", String.valueOf(value)));
    }
    
    private void _TearDown(){
        log.Append_To_Log("Conditioning|| Xrays commanded off");
        this.HV.Xray_Off();
        ConditioningStopTime = LocalTime.now();
        log.Append_To_Log("Conditioning|| Conditioning complete");
        
        
        
        log.Append_To_Log(String.format("Conditioning|| Start Time: %s",ConditioningStartTime));
        log.Append_To_Log(String.format("Conditioning|| End Time: %s",ConditioningStopTime));
        log.Append_To_Log(String.format("Conditioning|| Tube Serial Number: %s",vals.TubeSerialNumber));
        log.Append_To_Log(String.format("Conditioning|| Supply Model Used: %s",HV.getModelNumber()));
        log.Append_To_Log(String.format("Conditioning|| Max KV reached: %s",MaxKVReached));
        log.Append_To_Log(String.format("Conditioning|| Max MA reached: %s",MaxMAReached));
        double RecordsSummed = 0.0;
        if (!RecordedFilamentCurrents.isEmpty()){
            
            for (Double record : RecordedFilamentCurrents) {
            RecordsSummed += record;
            }
            AverageFilamenCurrent = RecordsSummed/RecordedFilamentCurrents.size();
            log.Append_To_Log(String.format("Conditioning|| Average Filament Current: %s not implemented",MaxMAReached));
        }
        
        log.Append_To_Log(String.format("Conditioning|| Total Arcs Detected: %s",ArcCount));
        log.Append_To_Log("###########################################################################################");
        _setTimeRemaining(0.0);
    }
    
    private void _Wait_Ramping(Double kv, Double ma){
        
        boolean ramped = false;
        log.Append_To_Log(String.format("Conditioning|| Ramping up: %s kv, %s ma",kv,ma));
        while(!ramped && !Killsig){
            if((this.HV.Read_Voltage_Out_Double() < (kv * 0.85)) && (this.HV.Read_Current_Out_Double() < (ma * 0.85))){
                ramped = false;
            }
            else{
                ramped = true;
            }
            try{
                Thread.sleep(500);
            
            }catch(InterruptedException ie){
                    this.interrupt();
            }
            catch(Exception e){
                e.printStackTrace();
                log.Append_To_Log("Conditioning Ramping|| Logic Exception occured");
                
            }
           
        }
        log.Append_To_Log("Conditioning Ramping|| Ramping Complete");
        System.out.println("Ramping Complete");
    }
    
    private Boolean _Time_Before_Check(LocalTime timeToCheck){
        
        return (LocalTime.now().isBefore(timeToCheck));
    }
    
    private void _ChangeVoltageTracker(Double kv){
        this.PreviousSetKV = CurrentSetKV;
        this.CurrentSetKV = NextToSetKV;
        this.NextToSetKV = CurrentSetKV + kv;
        
    }
    private void _ChangeCurrentTracker(Double ma){
        this.PreviousSetMA = CurrentSetMA;
        this.CurrentSetMA = NextToSetMA;
        this.NextToSetMA = CurrentSetMA + ma;
    }
    
    private void CheckXrayStatus(boolean kvArc){
        try {
            if(!this.HV.Is_Emmitting()&& !Killsig){
                if(this.HV.isArcPresent() ){
                    Double[] SetValuesResponse = this.HV.Get_Set_Voltage_Current();
                    
                    if(kvArc){
                        log.Append_To_Log(String.format("Conditioning Check Status|| Arc Detected: %s kv, %s ma",SetValuesResponse[0],SetValuesResponse[1]));
                    }else{
                        log.Append_To_Log(String.format("Conditioning Check Status|| Arc Detected: %s kv, %s ma",SetValuesResponse[0], SetValuesResponse[1] ));
                    }
                    
                    ArcCount++;
                    if(ArcCount >= MaxArcsBeforeStop){
                        JOptionPane.showMessageDialog(null, String.format("Total Allowed Arcs Reached, Stopping %s",this.Name));
                        log.Append_To_Log(String.format("Conditioning Check Status|| Total Allowed Arcs Reached: %s, Stopping", MaxArcsBeforeStop));
                        this.Stop_Conditioning();
                    }
                    _Arc_Recovery(kvArc);
                    ConcurrentArcCount = 0;
                }
                else if(this.HV.isInterlockOpen()){
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Interlock is open. Stopping %s", this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Interlock open, stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                else if(this.HV.isOverCurrent()){
                   
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Over Current Detected. Stopping %s", this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Over Current Detected. Stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                else if(this.HV.isUnderCurrent()){
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Under Current Detected. Stopping %s", this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Under Current Detected. Stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                else if(this.HV.isOverVoltage()){
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Over Voltage Detected. Stopping %s", this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Over Voltage Detected. Stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                else if(this.HV.isUnderVoltage()){
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Under Voltage Detected. Stopping %s",this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Under Voltage Detected. Stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                else if (this.HV.isOverTemperature()){
                    JOptionPane.showMessageDialog(null, String.format("Xrays are off, Over Temperature. Stopping %s",this.Name));
                    log.Append_To_Log("Conditioning Check Status|| Over Temperature. Stopping conditioning Cycle");
                    this.HV.Reset_Faults();
                    this.Stop_Conditioning();
                }
                this.HV.Reset_Faults();
            }
            else{
                if(this.HV.isArcPresent() && !Killsig){
                    ArcCount++;
                    if(ArcCount == MaxArcsBeforeStop){
                        JOptionPane.showMessageDialog(null, String.format("Total Allowed Arcs Reached, Stopping %s",this.Name));
                        log.Append_To_Log(String.format("Conditioning Check Status|| Total Allowed Arcs Reached: %s, Stopping", MaxArcsBeforeStop));
                        this.Stop_Conditioning();
                    }
                    _Arc_Recovery(kvArc);
                    ConcurrentArcCount = 0;
                } 
            }
            Thread.sleep(1000);
            TotalTime -= 0.01666667;
            _setTimeRemaining(TotalTime);
            
        }catch(InterruptedException ie){
            this.interrupt();
        }catch (Exception e) {
            e.printStackTrace();
            log.Append_To_Log("Conditioning Check Status|| Logic Exception occured");
            }
    }
    private void _KVInitialRamp(){
        log.Append_To_Log("Conditioning KV Ramp|| Starting KV Initial Ramp");
        System.out.println("Starting KV Initial Ramp");
         
        this.CurrentSetKV = this.StartKV;
        this.PreviousSetKV = this.StartKV;
        this.CurrentSetMA = this.StartMA;
        this.PreviousSetMA = this.StartMA;
        this.NextToSetKV = CurrentSetKV + this.KVStepSize;
        this.NextToSetMA = CurrentSetMA + this.MAStepSize;
        
        this.HV.Set_Voltage(this.CurrentSetKV);
        this.HV.Set_Current(this.CurrentSetMA);
        log.Append_To_Log(String.format("Conditioning KV Ramp|| Voltage set to: %s, Current set to: %s",CurrentSetKV,CurrentSetMA));
        this.HV.Xray_On();
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetKV <= TargetKV && !Killsig){
            this.HV.Set_Voltage(this.CurrentSetKV);
            log.Append_To_Log(String.format("Conditioning KV Ramp|| Voltage set to: %s",CurrentSetKV));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
           
            while(_Time_Before_Check(TimeEnd)&& !Killsig){
                CheckXrayStatus(true);
            }
            
            _ChangeVoltageTracker(KVStepSize);
            log.Append_To_Log("Conditioning KV Ramp|| Stepping up");
           MaxKVReached = PreviousSetKV;
        }
        if(!Killsig){
        MaxKVReached = TargetKV;
        System.out.println("KV Ramping Complete");
        System.out.println(String.format("Max KV of: %s reached", MaxKVReached));
        log.Append_To_Log("Conditioning KV Ramp|| Finished KV Initial Ramp");
        }
    }
    
    private void _MAInitialRamp(){
        System.out.println("Starting Ma Ramp");
        log.Append_To_Log("Conditioning MA Ramp|| Starting MA Initial Ramp");
        CurrentSetKV = TargetKV;
        this.NextToSetKV = (CurrentSetKV *0.75);
        _ChangeVoltageTracker(0.0);
        
        this.HV.Set_Voltage(this.CurrentSetKV);
        log.Append_To_Log(String.format("Conditioning MA Ramp|| Voltage set to: %s",CurrentSetKV));
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetMA <= TargetMA && !Killsig){
            this.HV.Set_Current(CurrentSetMA);
            log.Append_To_Log(String.format("Conditioning MA Ramp|| Current set to: %s",CurrentSetMA));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
            
            while(_Time_Before_Check(TimeEnd)&& !Killsig){
                CheckXrayStatus(false);
            }
            
            _ChangeCurrentTracker(MAStepSize);
            log.Append_To_Log("Conditioning MA Ramp|| Stepping up");
            MaxMAReached = PreviousSetMA;
        }
        if(!Killsig){
        _ChangeCurrentTracker(MAStepSize);
        MaxMAReached = TargetMA;
        System.out.println("MA Ramp Complete");
        System.out.println(String.format("Max MA of: %s reached", MaxMAReached));
        log.Append_To_Log("Conditioning MA Ramp|| Finished MA Initial Ramp");
        }
    }
    private void _KVReramp(){
        System.out.println("Starting KV Reramp");
        log.Append_To_Log("Conditioning 2nd KV Ramp|| Starting KV Re-ramp");
        
        while(CurrentSetKV <= TargetKV && !Killsig){
            
            this.HV.Set_Voltage(CurrentSetKV);
            log.Append_To_Log(String.format("Conditioning 2nd KV Ramp|| Voltage set to: %s",CurrentSetKV));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
            
            while(_Time_Before_Check(TimeEnd)&& !Killsig){
                CheckXrayStatus(true);  
            }
            _ChangeVoltageTracker(KVStepSize);
            log.Append_To_Log("Conditioning 2nd KV Ramp|| Stepping up");
        }
        if(!Killsig){
        System.out.println("KV Reramp Complete");
        log.Append_To_Log("Conditioning 2nd KV Ramp|| Finished KV Re-ramp");
        
        }
        
        
    }
    private void _OnOffCycle(){
        
        System.out.println("Starting On Off Cycles");
        log.Append_To_Log("Conditioning On/Off Cycle|| Starting On/Off Cycles");
        for (int count = 1; count <= this.OnOffCycleCount; count++) {
            if (!Killsig)
            {
            log.Append_To_Log(String.format("Conditioning On/Off Cycle|| On Off cycle %s",count));
            log.Append_To_Log(String.format("Conditioning On/Off Cycle|| Turning on for: %s min",OnTime));
            this.HV.Xray_On();
            log.Append_To_Log("Conditioning On/Off Cycle|| Xray Commanded On");
            CurrentSetKV = TargetKV;
            CurrentSetMA = TargetMA;
            this.HV.Set_Voltage(CurrentSetKV);
            this.HV.Set_Current(CurrentSetMA);
            log.Append_To_Log(String.format("Conditioning On/Off Cycle|| Voltage set to: %s, Current set to: %s",TargetKV,TargetMA));
            _Wait_Ramping(TargetKV,TargetMA);

            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.OnTime*60));
                
            while(_Time_Before_Check(TimeEnd) && !Killsig){
                CheckXrayStatus(true);
                
                double val = this.HV.Get_Voltage_Current_Filament()[2];
                if (val != 0.0){
                RecordedFilamentCurrents.add(val);
                }
            }
            
            log.Append_To_Log(String.format("Conditioning On/Off Cycle|| Turning off for: %s min",OffTime));
            this.HV.Xray_Off();
            log.Append_To_Log("Conditioning On/Off Cycle|| Xray Commanded Off");
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.OffTime*60));
                
            while(_Time_Before_Check(TimeEnd) && !Killsig){
                // do nothing as xrays are off;
                }
            }
        }
        if(!Killsig){
        System.out.println("On/Off Cycles Complete");
        log.Append_To_Log("Conditioning On/Off Cycle|| Finished On/Off Cycles");
        }
    }
    
    private void _Arc_Recovery(boolean KVInsteadOfMA){
        TotalTime += ArcRecoveryDwellTime;
        _setTimeRemaining(TotalTime);
        
        System.out.println("Starting Arc Recovery");
        log.Append_To_Log(String.format("Conditioning|| Starting Arc Recovery attempt: %s",ConcurrentArcCount));
        
        // if kv arc, reduce kv by 1 step, not below start kv
        // wait for arc recover time
        // if arc again, call itself
        // if arc Arc-count-limit times, interrupt()
        // return kv to pre-arc value
        // exit
        
        this.HV.Reset_Faults();
        log.Append_To_Log("Arc Recovery|| Reseting faults");
        if(ConcurrentArcCount <= ConcurrentArcsBeforeStop && !Killsig){
            if(KVInsteadOfMA){
            //Arc recover should reduce KV to previous set
                if ((this.PreviousSetKV - (KVStepSize*ConcurrentArcCount)) >= StartKV){
                    this.reducedKVSet = this.PreviousSetKV - (KVStepSize*ConcurrentArcCount);
                    this.HV.Set_Voltage(reducedKVSet);
                    log.Append_To_Log(String.format("Arc Recovery|| kV reduced to: %s", reducedKVSet));
                    this.HV.Xray_On();
                    log.Append_To_Log("Arc Recovery|| Xray Commanded On");
                }else{
                    this.HV.Set_Voltage(StartKV);
                    log.Append_To_Log(String.format("Arc Recovery|| kV reduced to: %s", StartKV));
                    this.HV.Xray_On();
                    log.Append_To_Log("Arc Recovery|| Xray Commanded On");
                }
                
            }else{
            //Arc recover should reduce MA to previous set
                if ((this.PreviousSetMA - (MAStepSize*ConcurrentArcCount)) >= StartMA){
                    this.reducedMASet = this.PreviousSetMA -(MAStepSize*ConcurrentArcCount);
                    this.HV.Set_Current(reducedMASet);
                    log.Append_To_Log(String.format("Arc Recovery|| mA reduced to: %s", reducedMASet));
                    this.HV.Xray_On();
                    log.Append_To_Log("Arc Recovery|| Xray Commanded On");
                }else{
                    this.HV.Set_Current(StartMA);
                    log.Append_To_Log(String.format("Arc Recovery|| mA reduced to: %s", StartMA));
                    this.HV.Xray_On();
                    log.Append_To_Log("Arc Recovery|| Xray Commanded On");
                }
            }
            
            ConcurrentArcCount ++;
            ArcTimeEnd = LocalTime.now().plusSeconds(Math.round(this.ArcRecoveryDwellTime*60));
            System.out.println(String.format("Arc Recovery %s ,time to complete at: %s", ConcurrentArcCount,ArcTimeEnd));
            while(_Time_Before_Check(ArcTimeEnd)&& !Killsig){
                try{
                    Thread.sleep(2000);
                   
                }
                catch(InterruptedException ie){
                    this.interrupt();
                }
                catch(Exception e){
                    e.printStackTrace();
                    log.Append_To_Log("Arc Recovery|| Logic Exception occured");
                }
                CheckXrayStatus(KVInsteadOfMA);
        }
        }else{
            if(!Killsig){
            JOptionPane.showMessageDialog(null, String.format("Too Many Concurrent Arcs. Stopping %s",this.Name));
            log.Append_To_Log(String.format("Conditioning|| Too many concurrent arcs, stopping : %s Arcs",ConcurrentArcCount));
            this.Stop_Conditioning();
            }
        }
        if(!Killsig){
        System.out.println("Arc Recovery Complete");
        ArcTimeEnd = LocalTime.now().plusSeconds(Math.round(this.ArcRecoveryDwellTime*60));
        log.Append_To_Log("Conditioning|| Arc Recovered");
        }
    }
    
    
    public void Stop_Conditioning(){
        Killsig = true;
        System.out.println("Stoping");
        log.Append_To_Log("Conditioning|| Stop command called");
        _TearDown();
        this.interrupt();
        
//        int stop_attempt = 0;
        
//        while (this.isAlive() && stop_attempt<3){
//            this.interrupt();
//            try{
//            this.join(500);
//            }catch(InterruptedException e){
//                
//                e.printStackTrace();
//            }
//            stop_attempt++;
//        }
        this.HV.Xray_Off();
        
    }
    

}
