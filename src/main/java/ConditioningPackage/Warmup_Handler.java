/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author mjsh6
 */
public class Warmup_Handler extends Thread {
    DXM supply;
    ButtonGroup btnGroup;
    int progress_number = 0;
    JProgressBar PBar;
    JTextField KVTBox,MATBox,FilCurTBox,PreHeatTBox;
    double TargetKV;
    double TargetMA;
    double FilCur;
    double PreHeat;
    LoggingController log;
    
    
    public Warmup_Handler(DXM supply, ButtonGroup btn, JProgressBar progressBar, 
            JTextField kv, JTextField ma, JTextField filCurr, JTextField pre,LoggingController log) {
        this.supply = supply;
        this.btnGroup = btn;
        this.PBar = progressBar;
        this.KVTBox = kv; 
        this.MATBox = ma;
        this.FilCurTBox = filCurr;
        this.PreHeatTBox =  pre;
        this.log = log;
    }
    
    public void run(){
        int runTime = Integer.valueOf(this.btnGroup.getSelection().getActionCommand());
        this.TargetKV = Double.valueOf(this.KVTBox.getText());
        this.TargetMA = Double.valueOf(this.MATBox.getText());
        this.FilCur =  Double.valueOf(this.FilCurTBox.getText());
        this.PreHeat = Double.valueOf(this.PreHeatTBox.getText());
        log.Append_To_Log("Warmup|| Started: 7 minute");
        int stepCount = runTime * 3;     
        double kvStepSize = (this.TargetKV - 12.0)/stepCount;
        double maStepSize = (this.TargetMA - 0.5)/stepCount;
        this.supply.Set_Voltage(12.0);
        this.supply.Set_Current(0.5);
        this.supply.Set_Filament_Limit(this.FilCur);
        this.supply.Set_Filament_Preheat(this.PreHeat);
        log.Append_To_Log(String.format("Warmup|| Target Voltage:%s , Target Current: %s,Filament Limit: %s,Pre-Heat: %s",
        this.TargetKV,this.TargetMA,this.FilCur,this.PreHeat));
        
        /////////////this.supply.Xray_On();
        int loopnum = 1;
        this.supply.Xray_On();
        log.Append_To_Log("Warmup|| Xray turned ON");
        while(!this.isInterrupted() && loopnum <= stepCount){
            try{
            Thread.sleep(20000);//sleep 20 seconds
            }catch(InterruptedException e){
                e.printStackTrace();
                this.interrupt();
            }
            System.out.println("Step: " + loopnum);
            log.Append_To_Log(String.format("Warmup|| Step: %s",loopnum));
            double kvSet = 12.0 + (kvStepSize* loopnum);
            double maSet = 0.5 + (maStepSize * loopnum);
            System.out.println(kvSet + " " + maSet);
            this.supply.Set_Voltage(kvSet);
            this.supply.Set_Current(maSet);
            log.Append_To_Log(String.format("Warmup|| Setting Voltage:%s ,Current: %s",
                    kvSet,maSet));
            
        loopnum++;
        double val = ((double)loopnum/(double)stepCount)*100;
        this.PBar.setValue((int)Math.rint(val));
        }
        
    }
    
    public void Stop_Warmup(){
        //if the thread is alive, call interrupt, and wait for join, try 3 times
        log.Append_To_Log("Warmup|| Stopping warmup");
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
        log.Append_To_Log("Warmup|| Xrays turned Off");
        this.supply.Xray_Off();
        // shut off supply on Exit
    }}
    

