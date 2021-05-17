/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;
import com.pi4j.io.i2c.*;
/**
 *
 * This is a class that will handle the control of a MCP4725 Adafruit DAC chip,
 * requires the inclusion of the pi4j.core dependencies
 * 
 * written by : mjsh635
 */
import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class MCP4725_DAC {
    I2CBus Bus;
    int BusAddress;
    I2CDevice device;
    
    /**
     * 
     * @param bus i2c bus to be scanned
     * @param address address of the chip on bus
     */
    public MCP4725_DAC(I2CBus bus, int address) {
        this.Bus = bus;
        this.BusAddress = address;
        try {
            
            this.device = Bus.getDevice(address);
//          JOptionPane.showMessageDialog(new JOptionPane(),device.toString(),"Device Found", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JOptionPane(),ex,"Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * 
     * @param Value_To_Set  int value 0 - 4095
     * @return true if set success, false otherwise
     */
    public boolean SetVoltageOut(int Value_To_Set) throws IOException{
        
        if(Value_To_Set > 4095){
            Value_To_Set = 4095;
        }
        if(Value_To_Set<0){
            Value_To_Set =0;
        }
        //bit shift data
        byte[] bytes = {(byte)((Value_To_Set >> 4) & 0xFF),(byte)((Value_To_Set << 4) & 0xFF)};
        device.write(0x60,bytes,0,2);
        
        return true;
    }
    
    
}
