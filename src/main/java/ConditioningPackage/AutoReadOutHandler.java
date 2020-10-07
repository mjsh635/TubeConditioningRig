/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mjsh6
 */
public class AutoReadOutHandler extends Thread {
    DXM HV;
    JLabel volt;
    JLabel curr;
    JTextField voltTF,currTF,FillTF;
    
    public AutoReadOutHandler(DXM supply, JLabel ManualVoltageDisplay,JLabel ManualCurrentDisplay, 
            JTextField VoltField, JTextField CurrField, JTextField FillTextField) {
        HV = supply;
        volt = ManualVoltageDisplay;
        curr = ManualCurrentDisplay;
        voltTF = VoltField;
        currTF = CurrField;
        FillTF = FillTextField;
        
    }
    
    public void run(){
        while (true){
            
            try{
                Double[] values = HV.Get_Voltage_Current_Filament();
                volt.setText(String.valueOf(values[0]));
                curr.setText(String.valueOf(values[1]));
                voltTF.setText(String.valueOf(values[0]));
                currTF.setText(String.valueOf(values[1]));
                FillTF.setText(String.valueOf(values[2]));
                
                Thread.sleep(500);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void Start_Auto_Readout(){
        System.out.println("Starting Auto-Readout");
        this.run();
    }
    
    public void Stop_Auto_Readout(){
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
