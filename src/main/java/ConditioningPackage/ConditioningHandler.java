/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;


import java.time.LocalTime;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.swing.JProgressBar;
import javax.swing.plaf.synth.SynthGraphicsUtils;

/**
 *
 * @author mjsh6
 */
public class ConditioningHandler extends Thread{
    Settings vals;
    DXM HV;
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
    LocalTime ConditioningStartTime;
    LocalTime ConditioningStopTime;
    int MaxArcsBeforeStop;
    int ArcCount = 0;
    int ConcurrentArcCount = 0;
    
    int OnOffCycleCount;
    
    public ConditioningHandler(Settings userSettings, DXM supply, JProgressBar pb, LoggingController log) {
        this.vals = userSettings;
        this.HV = supply;
        this.pb = pb;
        this.log = log;
        
    }
    
    @Override
    public void run(){
        log.Append_To_Log("Conditioning|| Starting Conditioning Routine");
        this._StartUp();
        this.pb.setValue(16);
        this._KVInitialRamp();
        this.pb.setValue(32);
        this._MAInitialRamp();
        this.pb.setValue(48);
        this._KVReramp();
        this.pb.setValue(64);
        this._OnOffCycle();
        this.pb.setValue(80);
        this.pb.setValue(100);
        Stop_Conditioning();
        
        
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
        this.MaxArcsBeforeStop = Integer.valueOf(this.vals.TotalArcsBeforeStop);
        this.ArcRecoveryDwellTime = Double.valueOf(this.vals.ArcRecoveryTime);
        log.Append_To_Log(String.format("Conditioning|| Voltage: %s --> %s, with %s Steps", StartKV,TargetKV, KVStepSize));
        log.Append_To_Log(String.format("Conditioning|| Current: %s --> %s, with %s Steps", StartMA,TargetMA,MAStepSize));
        log.Append_To_Log(String.format("Conditioning|| Filament Limit: %s, PreHeat: %s", this.vals.FilamentCurrentLimit,this.vals.FilamentPreHeat));
        log.Append_To_Log(String.format("Conditioning|| Total Steps: %s, Dwelling for: %s mins", StepCount,StepDwellTime));
        log.Append_To_Log(String.format("Conditioning|| Number of On/Off Cycles: %s, %s min ON, %s min OFF", OnOffCycleCount,OnTime,OffTime));
    }
    private void _TearDown(){
        log.Append_To_Log("Conditioning|| Xrays commanded off");
        this.HV.Xray_Off();
        ConditioningStopTime = LocalTime.now();
        log.Append_To_Log("Conditioning|| Conditioning complete");
        log.Append_To_Log(String.format("Conditioning|| Start Time: %s",ConditioningStartTime));
        log.Append_To_Log(String.format("Conditioning|| End Time: %s",ConditioningStopTime));
        log.Append_To_Log(String.format("Conditioning|| Tube Serial Number: %s",vals.TubeSerialNumber));
        log.Append_To_Log(String.format("Conditioning|| Supply Model Used: %s",HV.modelNumber));
        log.Append_To_Log(String.format("Conditioning|| Max KV reached: %s not implemented",MaxKVReached));
        log.Append_To_Log(String.format("Conditioning|| Max MA reached: %s not implemented",MaxMAReached));
        log.Append_To_Log(String.format("Conditioning|| Average Filament Current: %s not implemented",MaxMAReached));
        log.Append_To_Log(String.format("Conditioning|| Total Arcs Detected: %s",ArcCount));
    }
    private void _Wait_Ramping(Double kv, Double ma){
        boolean ramped = false;
        log.Append_To_Log(String.format("Conditioning|| Ramping up: %s kv, %s ma",kv,ma));
        while(!ramped){
            if((this.HV.Read_Voltage_Out_Double() < (kv * 0.98)) && (this.HV.Read_Current_Out_Double() < (ma * 0.98))){
                ramped = false;
            }
            else{
                ramped = true;
            }
            try{
                Thread.sleep(500);
            }catch(Exception e){
                this.interrupt();
                e.printStackTrace();
            }
           
        }
        log.Append_To_Log("Conditioning|| Ramping Complete");
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
            if(!this.HV.Is_Emmitting()){
                if(this.HV.ArcPresent){
                    log.Append_To_Log(String.format("Conditioning|| Arc Detected: %s kv, %s ma", CurrentSetKV, CurrentSetMA));
                    ArcCount++;
                    _Arc_Recovery(kvArc);
                    ConcurrentArcCount = 0;
                }
                else if(this.HV.InterlockOpen){
                    JOptionPane.showMessageDialog(null, "Xrays are off, Interlock is open. Stopping");
                    log.Append_To_Log("Conditioning|| Interlock open, stopping conditioning Cycle");
                    this.Stop_Conditioning();
                }
            }
            else{
                if(this.HV.ArcPresent){
                    ArcCount++;
                    _Arc_Recovery(kvArc);
                    ConcurrentArcCount = 0;
                } 
            }
            Thread.sleep(1000);
            
        } catch (Exception e) {
            e.printStackTrace();
            }
    }
    private void _KVInitialRamp(){
        log.Append_To_Log("Conditioning|| Starting KV Initial Ramp");
        System.out.println("Starting KV Initial Ramp");
        this.CurrentSetKV = this.StartKV;
        this.CurrentSetMA = this.StartMA;
        this.NextToSetKV = CurrentSetKV + this.KVStepSize;
        this.NextToSetMA = CurrentSetMA + this.MAStepSize;
        
        this.HV.Set_Voltage(this.CurrentSetKV);
        this.HV.Set_Current(this.CurrentSetMA);
        log.Append_To_Log(String.format("Conditioning|| Voltage set to: %s, Current set to: %s",CurrentSetKV,CurrentSetMA));
        this.HV.Xray_On();
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetKV <= TargetKV){
            this.HV.Set_Voltage(this.CurrentSetKV);
            log.Append_To_Log(String.format("Conditioning|| Voltage set to: %s",CurrentSetKV));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
           
            while(_Time_Before_Check(TimeEnd)){
                CheckXrayStatus(true);
            }
            
            _ChangeVoltageTracker(KVStepSize);
            log.Append_To_Log("Conditioning|| Stepping up");
           
        }
        System.out.println("KV Ramping Complete");
        log.Append_To_Log("Conditioning|| Finished KV Initial Ramp");
    }
    
    private void _MAInitialRamp(){
        System.out.println("Starting Ma Ramp");
        log.Append_To_Log("Conditioning|| Starting MA Initial Ramp");
        CurrentSetKV = TargetKV;
        this.NextToSetKV = (CurrentSetKV *0.75);
        _ChangeVoltageTracker(0.0);
        
        this.HV.Set_Voltage(this.CurrentSetKV);
        log.Append_To_Log(String.format("Conditioning|| Voltage set to: %s",CurrentSetKV));
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetMA <= TargetMA){
            this.HV.Set_Current(CurrentSetMA);
            log.Append_To_Log(String.format("Conditioning|| Current set to: %s",CurrentSetMA));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
            
            while(_Time_Before_Check(TimeEnd)){
                CheckXrayStatus(false);
            }
            
            _ChangeCurrentTracker(MAStepSize);
            log.Append_To_Log("Conditioning|| Stepping up");
        }
        _ChangeCurrentTracker(MAStepSize);
        System.out.println("MA Ramp Complete");
        log.Append_To_Log("Conditioning|| Finished MA Initial Ramp");
    }
    private void _KVReramp(){
        System.out.println("Starting KV Reramp");
        log.Append_To_Log("Conditioning|| Starting KV Re-ramp");
        
        while(CurrentSetKV <= TargetKV){
            
            this.HV.Set_Voltage(CurrentSetKV);
            log.Append_To_Log(String.format("Conditioning|| Voltage set to: %s",CurrentSetKV));
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.StepDwellTime*60));
            
            while(_Time_Before_Check(TimeEnd)){
                CheckXrayStatus(true);  
            }
            _ChangeVoltageTracker(KVStepSize);
            log.Append_To_Log("Conditioning|| Stepping up");
        }
        
        System.out.println("KV Reramp Complete");
        log.Append_To_Log("Conditioning|| Finished KV Re-ramp");
        
        
        
        
    }
    private void _OnOffCycle(){
        
        System.out.println("Starting On Off Cycles");
        log.Append_To_Log("Conditioning|| Starting On/Off Cycles");
        for (int count = 1; count <= this.OnOffCycleCount; count++) {
            log.Append_To_Log(String.format("Conditioning|| On Off cycle %s",count));
            log.Append_To_Log(String.format("Conditioning|| Turning on for: %s min",OnTime));
            this.HV.Xray_On();
            log.Append_To_Log("Conditioning|| Xray Commanded On");
            CurrentSetKV = TargetKV;
            CurrentSetMA = TargetMA;
            this.HV.Set_Voltage(CurrentSetKV);
            this.HV.Set_Current(CurrentSetMA);
            log.Append_To_Log(String.format("Conditioning|| Voltage set to: %s, Current set to: %s",TargetKV,TargetMA));
            _Wait_Ramping(TargetKV,TargetMA);

            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.OnTime*60));
                
            while(_Time_Before_Check(TimeEnd)){
                CheckXrayStatus(true);
                }
            
            log.Append_To_Log(String.format("Conditioning|| Turning off for: %s min",OffTime));
            this.HV.Xray_Off();
            log.Append_To_Log("Conditioning|| Xray Commanded Off");
            TimeEnd = LocalTime.now().plusSeconds(Math.round(this.OffTime*60));
                
            while(_Time_Before_Check(TimeEnd)){
                // do nothing as xrays are off;
                }
        
        }
        System.out.println("On/Off Cycles Complete");
        log.Append_To_Log("Conditioning|| Finished On/Off Cycles");
        
    }
    
    private void _Arc_Recovery(boolean KVInsteadOfMA){
        System.out.println("Starting Arc Recovery");
        log.Append_To_Log(String.format("Conditioning|| Starting Arc Recovery attempt: %s",ConcurrentArcCount));
        
        // if kv arc, reduce kv by 1 step, not below start kv
        // wait for arc recover time
        // if arc again, call itself
        // if arc Arc-count-limit times, interrupt()
        // return kv to pre-arc value
        // exit
        
        if(ConcurrentArcCount <= MaxArcsBeforeStop){
            if(KVInsteadOfMA){
            //Arc recover should reduce KV to previous set
                if ((this.PreviousSetKV - (KVStepSize*ConcurrentArcCount)) >= StartKV){
                    this.HV.Set_Voltage((this.PreviousSetKV - (KVStepSize*ConcurrentArcCount)));
                }else{
                    this.HV.Set_Voltage(StartKV);
                }
                
            }else{
            //Arc recover should reduce MA to previous set
                if ((this.PreviousSetMA - (MAStepSize*ConcurrentArcCount)) >= StartMA){
                    this.HV.Set_Current(this.PreviousSetMA - (MAStepSize*ConcurrentArcCount));
                }else{
                    this.HV.Set_Current(StartMA);
                }
            }
            
            ConcurrentArcCount ++;
            ArcTimeEnd = LocalTime.now().plusSeconds(Math.round(this.ArcRecoveryDwellTime*60));
            while(_Time_Before_Check(ArcTimeEnd)){
            CheckXrayStatus(KVInsteadOfMA);
        }
        }else{
            JOptionPane.showMessageDialog(null, "Too Many Concurrent Arcs. Stopping");
            log.Append_To_Log(String.format("Conditioning|| Too many concurrent arcs, stopping : %s Arcs",ConcurrentArcCount));
            this.Stop_Conditioning();
        }
        System.out.println("Arc Recovery Complete");
        log.Append_To_Log("Conditioning|| Arc Recovered");
        
    }
    
    
    public void Stop_Conditioning(){
        System.out.println("Stoping");
        log.Append_To_Log("Conditioning|| Stop command called");
        _TearDown();
        int stop_attempt = 0;
        
        while (this.isAlive() && stop_attempt<3){
            this.interrupt();
            try{
            this.join(500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            stop_attempt++;
        }
        this.HV.Xray_Off();
        
    }
    

}
