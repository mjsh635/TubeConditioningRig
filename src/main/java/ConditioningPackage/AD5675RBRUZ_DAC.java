/*
 * 
 */
package ConditioningPackage;


import com.pi4j.io.i2c.*;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * requires the use of com.pi4j.io.i2c.* library to function.
 *
 * @author mjsh635
 */
public class AD5675RBRUZ_DAC {

    int CommandConfig = 0b0011 << 20;
    byte ConfigByte1;
    I2CBus Bus;
    int BusAddress;
    I2CDevice device;

    public AD5675RBRUZ_DAC(I2CBus bus, int address) {
        this.Bus = bus;
        this.BusAddress = address;
        try {

            this.device = Bus.getDevice(address);
//          JOptionPane.showMessageDialog(new JOptionPane(),device.toString(),"Device Found", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JOptionPane(), ex, "Exception Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Set the voltage on a selected DAC with scale of 0-10V
     *
     * value will be divided by 4 and scaled 0-65535
     *
     * @param DACNumber selected Vout
     * @param Voltage Voltage to be set on Selected Channel
     * @throws IndexOutOfBoundsException
     *
     */
    public void SetVoltageScaled(int DACNumber, double Voltage) throws IndexOutOfBoundsException, IOException {
        if (!(DACNumber >= 0 && DACNumber <= 7)) {
            throw new IndexOutOfBoundsException("DAC Number outside of bounds: " + DACNumber);
        } else {
            switch (DACNumber) {
                case 0:
                    ConfigByte1 = 0x30;
                    break;
                case 1:
                    ConfigByte1 = 0x31;
                    break;
                case 2:
                    ConfigByte1 = 0x32;
                    break;
                case 3:
                    ConfigByte1 = 0x33;
                    break;
                case 4:
                    ConfigByte1 = 0x34;
                    break;
                case 5:
                    ConfigByte1 = 0x35;
                    break;
                case 6:
                    ConfigByte1 = 0x36;
                    break;
                case 7:
                    ConfigByte1 = 0x37;
                    break;
                default:
                    break;

            }

            if (!(Voltage <= 10.0 && Voltage >= 0.0)) {
                throw new IndexOutOfBoundsException("Voltage out of bounds 0-10V: " + Voltage);

            }else{
                
                int value = (int)Math.ceil((((Voltage/4)/2.5))*65535);
                // split data into config byte, and MSB, and LSB
                byte dataByte1 = (byte)((value >>8) & 0xff); //MSB
                byte dataByte2 = (byte)(value & 0xff); //LSB
                
                
                byte[] data = {ConfigByte1,dataByte1,dataByte2}; // Data to write
                
                device.write(data, 0, 3);
                
                
            }
        }
    }
    
}
