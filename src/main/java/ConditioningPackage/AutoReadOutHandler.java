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
    boolean StartReading = false;
    
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
        System.out.println("Starting");
        while (true){
            if(StartReading){
                
                try{
                Double[] values = HV.Get_Voltage_Current_Filament();
                volt.setText(String.format("%.2f", values[0]));
                curr.setText(String.format("%.2f",values[1]));
                voltTF.setText(String.format("%.2f",values[0]));
                currTF.setText(String.format("%.2f",values[1]));
                FillTF.setText(String.format("%.2f",values[2]));
                
                Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                catch(Error e){
                System.out.println("Caught");
                e.printStackTrace();
                
                }
                
           
            }else{
                
                try{
                    
                Thread.sleep(2000);
                }
                catch(Exception e){
                
                e.printStackTrace();
                }
            }
        }
    }
    
    
}
