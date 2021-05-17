
package ConditioningPackage;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * This class is a thread creator to handle reading out the values from a supply
 * @author mjsh635
 */
public class AutoReadOutHandler extends Thread {
    IHighVoltagePowerSupply HV;
    JLabel volt;
    JLabel curr;
    JTextField voltTF,currTF,FillTF;
    
    boolean StartReading = false;
    /**create the handler with all the requested data, when "Startreading" is
     * set true will continually read and update display boxes
     * 
     * @param supply power supply that inherits IHighvoltagePowerSupply
     * @param ManualVoltageDisplay JLabel for display
     * @param ManualCurrentDisplay JLabel for display
     * @param VoltField JTextField for display
     * @param CurrField JTextField for display
     * @param FillTextField JTextField for display
     */
    public AutoReadOutHandler(IHighVoltagePowerSupply supply, JLabel ManualVoltageDisplay,JLabel ManualCurrentDisplay, 
            JTextField VoltField, JTextField CurrField, JTextField FillTextField) {
        
        HV = supply;
        volt = ManualVoltageDisplay;
        curr = ManualCurrentDisplay;
        voltTF = VoltField;
        currTF = CurrField;
        FillTF = FillTextField;
        
        
    }
 
    
    public void run(){
        /*
        Code will be executed when called
        */
        System.out.println("Starting");
        while (true){
            
            if(StartReading){
                // try and read values from the supply every time interval.
                try{
                Double[] values = HV.Get_Voltage_Current_Filament();
                volt.setText(String.format("%.2f", values[0]));
                curr.setText(String.format("%.2f",values[1]));
                voltTF.setText(String.format("%.2f",values[0]));
                currTF.setText(String.format("%.2f",values[1]));
                FillTF.setText(String.format("%.2f",values[2]));
                // update text boxes
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
                // Do nothing for time interval
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
