
package ConditioningPackage;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * The purpose of this class is to handle the operation of an Adafruit 
 * Analog to digital converter
 *
 * @author mjsh635
 */

public class ADS1115_ADC {
    I2CBus Bus;
    int BusAddress;
    I2CDevice device;
    
    /**construct a ADC object to read voltages on selected ADC Channel
     * 
     * @param bus i2c bus to be scanned
     * @param address address of the chip on the bus
     */
    public ADS1115_ADC(I2CBus bus, int address) {
        this.Bus = bus;
        this.BusAddress = address;
        try {
            
            this.device = Bus.getDevice(address); // create an i2c bus device
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JOptionPane(),ex,"Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**Read value from the one channel on ADC, divide by 1000 for voltage
     * 
     * @param channelNumber Chip Channel number 0-3
     * @param timeBetweenWriteAndRead Time to sleep after writing config to chip, default 500
     * @param PGA  ±6.144 V, ±4.096 V ±2.048 V (default), ±1.024 V, ±0.512 V, ±0.256 V enter as mV
     * @return 0-4095 value measured from chip
     */
    public double ReadVoltage(int channelNumber,int timeBetweenWriteAndRead, int PGA) throws UnsupportedOperationException, IOException, InterruptedException{
        // I wrote this class on 7 cups of coffee, it works, not sure what black magic I did to make it work
        
        byte config1byte = (byte)0xc4; 
        byte config2byte = (byte)0x23;
        switch(channelNumber){
            case 0:
                config1byte = (byte)0xc4;
            case 1:
                config1byte = (byte)0xd4;
            case 2:
                config1byte = (byte)0xe4;
            case 3:
                config1byte = (byte)0xf4;
            default:
                config1byte = (byte)0xc4;
                
        }
        int setPGA = 2048;
        switch(PGA){
            case 6144:
                ;
            case 4096:
                ;
            case 2048:
                ;
            case 1024:
                ;
            case 512:
                ;
            case 256:
                ;
            default:
                setPGA = 2048;
        }
        byte[] config = {config1byte,config2byte};   
        
        
        device.write(0x01,config,0,2);
        Thread.sleep(timeBetweenWriteAndRead);
        
        //read from conversion register
        byte[] response = new byte[2];
        device.read(0x00,response, 0, 2);
        
        //return value in volts
        int Value = (response[0] << 8 | response[1]);
        
            return ((Value*PGA)/32768);
            
        
        
        
        
    }
    
}
