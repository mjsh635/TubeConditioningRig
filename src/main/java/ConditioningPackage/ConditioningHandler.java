/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import javax.swing.JProgressBar;

/**
 *
 * @author mjsh6
 */
public class ConditioningHandler extends Thread{
    Settings vals;
    DXM HV;
    LocalTime Time;
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
    Double CurrentSetMA;
    Double NextToSetKV;
    Double NextToSetMA;
   
    
    public ConditioningHandler(Settings userSettings, DXM supply, JProgressBar pb) {
        this.vals = userSettings;
        this.HV = supply;
        this.pb = pb;
    }
    
    public void run(){
        
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
        this._TearDown();
        this.pb.setValue(100);
        
        
    }
    private void _StartUp(){
        this.HV.Set_Filament_Limit(Double.valueOf(this.vals.FilamentCurrentLimit));
        this.HV.Set_Filament_Preheat(Double.valueOf(this.vals.FilamentPreHeat));
        this.StepDwellTime = Double.valueOf(this.vals.TimeBetweenSteps);
        this.StepCount = Double.valueOf(this.vals.TotalStepCount);
        this.StartKV =Double.valueOf(this.vals.StartingKV);
        this.TargetKV = Double.valueOf(this.vals.TargetKV);
        this.StartMA=Double.valueOf(this.vals.StartingMA);
        this.TargetMA = Double.valueOf(this.vals.TargetMA);
        this.KVStepSize = (this.TargetKV - (this.StartKV/this.StepCount));
        this.MAStepSize = (this.TargetMA - (this.StartMA/this.StepCount));
        
    }
    private void _TearDown(){
        this.HV.Xray_Off();
        
    }
    private void _Wait_Ramping(Double kv, Double ma){
        boolean ramped = false;
        while(!ramped){
            if((this.HV.Read_Voltage_Out_Double() < (kv * 0.90)) && (this.HV.Read_Current_Out_Double() < (ma * 0.90))){
                ramped = true;
            }
            try{
                Thread.sleep(300);
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("Ramping");
        }
    }
    
    private Boolean _Time_Before_Check(){
        return (this.Time.isBefore(this.Time.plusMinutes(Math.round(this.StepDwellTime))));
    }
    
    private void _ChangeVoltageTracker(Double kv){
        this.CurrentSetKV = NextToSetKV;
        this.NextToSetKV = CurrentSetKV + kv;
    }
    private void _ChangeCurrentTracker(Double ma){
        this.CurrentSetMA = NextToSetMA;
        this.NextToSetMA = CurrentSetMA + ma;
    }
   
    private void _KVInitialRamp(){
        System.out.println("Starting KV Initial Ramp");
        this.CurrentSetKV = this.StartKV;
        this.CurrentSetMA = this.StartMA;
        this.NextToSetKV = CurrentSetKV + this.KVStepSize;
        this.NextToSetMA = CurrentSetMA + this.MAStepSize;
        
        this.HV.Set_Voltage(this.CurrentSetKV);
        this.HV.Set_Current(this.CurrentSetMA);
        
        this.HV.Xray_On();
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetKV < TargetKV){
            this.HV.Set_Voltage(this.CurrentSetKV);
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            while(_Time_Before_Check()){
            
            }
            _ChangeVoltageTracker(KVStepSize);
        }
        System.out.println("KV Ramping Complete");
    }
    
    private void _MAInitialRamp(){
        System.out.println("Starting Ma Ramp");
        
        this.NextToSetKV = (this.CurrentSetKV *0.75);
        _ChangeCurrentTracker(0.0);
        this.HV.Set_Voltage(this.CurrentSetKV);
        _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
        
        while(CurrentSetMA < TargetMA){
            this.HV.Set_Current(CurrentSetMA);
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            while(_Time_Before_Check()){
                
            }
            _ChangeCurrentTracker(MAStepSize);
        }
        
        System.out.println("MA Ramp Complete");
    }
    private void _KVReramp(){
        System.out.println("Starting KV Reramp");
        
        while(CurrentSetKV < TargetKV){
            this.HV.Set_Voltage(CurrentSetKV);
            _Wait_Ramping(this.CurrentSetKV, this.CurrentSetMA);
            
            while(_Time_Before_Check()){
                
            }
            _ChangeVoltageTracker(KVStepSize);
        }
        
        System.out.println("KV Reramp Complete");
        
        
        
        
    }
    private void _OnOffCycle(){
        System.out.println("Starting On Off Cycles");
        System.out.println("On/Off Cycles Complete");
    }
    private void _Arc_Recovery(){
        System.out.println("Starting Arc Recovery");
        System.out.println("Arc Recovery Complete");
    }
    public void Start_Conditioning(){
        System.out.println("Starting");
        this.start();
    }
    
    public void Stop_Conditioning(){
        System.out.println("Stoping");
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
        
    }
    

}
