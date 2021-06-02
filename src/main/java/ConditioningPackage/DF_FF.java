package ConditioningPackage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.pi4j.io.i2c.*;
import com.pi4j.io.gpio.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * To use this class the dependency Pi4J is required.
 *
 * Please see the expected IO Mapping below: -------------------- AD5675R 8
 * channel DAC --------------------- DAC addr: 0x0C (A1/A2 pulled to GND) Vout0
 * Supply 1 Voltage Set Vout1 Supply 1 Current Set Vout2 Supply 2 Voltage Set
 * Vout3 Supply 2 Current Set Vout4 Supply 3 Voltage Set Vout5 Supply 3 Current
 * Set Vout6 Supply 4 Voltage Set Vout7 Supply 4 Current Set
 *
 * -------------------- ADS1115 Adafruit 4 channel ADC -------------------- ADC1
 * 0x48 (ADDR pulled to GND) Vin0 Supply 1 Voltage In Vin1 Supply 1 Current In
 * Vin2 Supply 2 Voltage In Vin3 Supply 2 Current In
 *
 * ADC2 0x49 (ADDR pulled to VDD) Vin0 Supply 3 Voltage In Vin1 Supply 3 Current
 * In Vin2 Supply 4 Voltage In Vin3 Supply 4 Current In
 *
 * ---------------------------------- GPIO PINOUT using WiringPI numbers
 * --------------------------------- wiringPi #0 GPIO#17 Supply 1 Input: Xray
 * are On 
 * wiringPi #1 GPIO#18 Supply 1 Input: Fault Present wiringPi #2 GPIO#27
 * Supply 1 Input: KV Regulation Erro wiringPi #3 GPIO#22 Supply 1 Output: Xray
 * ON/OFF Command wiringPi #4 GPIO#23 Supply 1 Output: Supply Reset wiringPi #5
 * GPIO#24 Supply 2 Input: Xray are On wiringPi #6 GPIO#25 Supply 2 Input: Fault
 * Present wiringPi #7 GPIO#4 Supply 2 Input: KV Regulation Erro wiringPi #12
 * GPIO#10 Supply 2 Output: Xray ON/OFF Command wiringPi #13 GPIO#9 Supply 2
 * Output: Supply Reset wiringPi #14 GPIO#11 Supply 3 Input: Xray are On
 * wiringPi #10 GPIO#8 Supply 3 Input: Fault Present wiringPi #11 GPIO#7 Supply
 * 3 Input: KV Regulation Erro wiringPi #21 GPIO#5 Supply 3 Output: Xray ON/OFF
 * Command wiringPi #22 GPIO#6 Supply 3 Output: Supply Reset wiringPi #26
 * GPIO#12 Supply 4 Input: Xray are On wiringPi #23 GPIO#13 Supply 4 Input:
 * Fault Present wiringPi #24 GPIO#19 Supply 4 Input: KV Regulation Erro
 * wiringPi #27 GPIO#16 Supply 4 Output: Xray ON/OFF Command wiringPi #25
 * GPIO#26 Supply 4 Output: Supply Reset
 *
 * Supply 1
 *
 * @author mjsh635
 */
public class DF_FF implements IHighVoltagePowerSupply {

    private String address;
    private int port;
    private boolean connected = false;
    private String modelNumber = "";
    private boolean ArcPresent = false;
    private boolean OverTemperature = false;
    private boolean OverVoltage = false;
    private boolean UnderVoltage = false;
    private boolean OverCurrent = false;
    private boolean UnderCurrent = false;
    private boolean HighVoltageState = false;
    private boolean InterlockOpen = true;
    private boolean FaultPresent = true;
    private boolean RemoteMode = false;
    private Double MaxKV = 0.0;
    private Double MaxMA = 0.0;
    private Double MaxWatt = 0.0;
    private Double voltageScaleFactor;
    private Double currentScaleFactor;
    private Lock SendCommandLock = new ReentrantLock();
    GpioController GPIO;
    I2CBus i2c;
    GpioPinDigitalInput XRaysOnSupply;
    GpioPinDigitalInput PowerSupplyFault;
    GpioPinDigitalInput KVRegulatorError;
    GpioPinDigitalOutput XrayOnCommand;
    GpioPinDigitalOutput ResetFaults;

    AD5675RBRUZ_DAC AnalogOutputs;
    ADS1115_ADC AnalogInputChip1;
    ADS1115_ADC AnalogInputChip2;
    int intVOutDacNumber;
    int intCOutDacNumber;
    int VInADCNumber;
    ADS1115_ADC VoltageADCChip;
    int CInADCNumber;
    ADS1115_ADC CurrentADCChip;
    int supplyNumber;

    public DF_FF(I2CBus bus, GpioController gpioController, Lock Lock, AD5675RBRUZ_DAC AnalogOut, ADS1115_ADC AnalogIn1, ADS1115_ADC AnalogIn2, int SupplyNumber) {

        this.modelNumber = this.Get_Model_Type();
        this.GPIO = gpioController;
        this.i2c = bus;
        this.supplyNumber = SupplyNumber;
        this.AnalogOutputs = AnalogOut;
        this.AnalogInputChip1 = AnalogIn1;
        this.AnalogInputChip2 = AnalogIn2;

        switch (SupplyNumber) {
            case 1:
                XRaysOnSupply = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_00);
                PowerSupplyFault = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_01);
                KVRegulatorError = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_02);
                XrayOnCommand = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_03);
                ResetFaults = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_04);
                intVOutDacNumber = 0;
                intCOutDacNumber = 1;
                VInADCNumber = 0;//(this.AnalogInputChip1, 0);
                VoltageADCChip = this.AnalogInputChip1;
                CurrentADCChip = this.AnalogInputChip1;
                CInADCNumber = 1;//.put(this.AnalogInputChip1, 1);
                break;
            case 2:
                XRaysOnSupply = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_05);
                PowerSupplyFault = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_06);
                KVRegulatorError = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_07);
                XrayOnCommand = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_12);
                ResetFaults = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_13);
                intVOutDacNumber = 2;
                intCOutDacNumber = 3;
                VoltageADCChip = this.AnalogInputChip1;
                CurrentADCChip = this.AnalogInputChip1;
                VInADCNumber = 2; //.put(this.AnalogInputChip1, 2);
                CInADCNumber = 3;//.put(this.AnalogInputChip1, 3);
                break;

            case 3:
                XRaysOnSupply = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_14);
                PowerSupplyFault = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_10);
                KVRegulatorError = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_11);
                XrayOnCommand = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_21);
                ResetFaults = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_22);
                intVOutDacNumber = 4;
                intCOutDacNumber = 5;
                VoltageADCChip = this.AnalogInputChip2;
                CurrentADCChip = this.AnalogInputChip2;
                VInADCNumber = 0;//.put(this.AnalogInputChip2, 0);
                CInADCNumber = 1;//.put(this.AnalogInputChip2, 1);
                break;
            case 4:
                XRaysOnSupply = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_26);
                PowerSupplyFault = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_23);
                KVRegulatorError = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_24);
                XrayOnCommand = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_27);
                ResetFaults = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25);
                intVOutDacNumber = 6;
                intCOutDacNumber = 7;
                VoltageADCChip = this.AnalogInputChip2;
                CurrentADCChip = this.AnalogInputChip2;
                VInADCNumber = 6;//.put(this.AnalogInputChip2, 2);
                CInADCNumber = 7;// .put(this.AnalogInputChip2, 3);
                break;
        }

    }

    @Override
    public void set_IP_Address(String new_address) {
        return;
    }

    @Override
    public String get_IP_Address() {
        return "0.0.0.0";
    }

    @Override
    public void Set_Voltage(double voltage) {
        if (voltage >= 0.0 && voltage <= 60.0){
            _Set_Voltage(voltage);
        }
    }

    @Override
    public void Set_Current(double current) {
        if (current >= 0.0 && current <= 80.0){
            _Set_Current(current);
        }
    }

    @Override
    public void updates() {
        _Update_Fault_States();
        _Update_Status_Signals();
    }

    @Override
    public String Get_Model_Type() {

        setConnected(true);
        return this._Read_Model_Type();

    }

    @Override
    public boolean Get_Interlock_Status() {

        this._Update_Status_Signals();
        return isInterlockOpen();

    }

    @Override
    public void Reset_Faults() {
        try{
            //SendCommandLock.lock();
            ResetFaults.pulse((long) 2000);    
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        }finally{
           // SendCommandLock.unlock();
        }
    }

    @Override
    public boolean Is_Emmitting() {

        this._Update_Fault_States();
        this._Update_Status_Signals();
        return isHighVoltageState();

    }

    @Override
    public ArrayList<String> Are_There_Any_Faults() {

        this._Update_Fault_States();
        ArrayList<String> Faults = new ArrayList<String>();
        if (isArcPresent()) {
            Faults.add("Arc Present");
        }
        if (isOverTemperature()) {
            Faults.add("Over Temperature");
        }
        if (isOverVoltage()) {
            Faults.add("Over Voltage");
        }
        if (isUnderVoltage()) {
            Faults.add("Under Voltage");
        }
        if (isOverCurrent()) {
            Faults.add("Over Current");
        }
        if (isUnderCurrent()) {
            Faults.add("Under Current");
        }
        return Faults;

    }

    @Override
    public String Read_Voltage_Out_String() {

        return this._Get_Voltage().toString();

    }

    @Override
    public Double Read_Voltage_Out_Double() {

        return this._Get_Voltage();

    }

    @Override
    public String Read_Current_Out_String() {

        return this._Get_Current().toString();

    }

    @Override
    public Double Read_Current_Out_Double() {

        return this._Get_Current();

    }

    @Override
    public String[] Get_About_Information() {

        this._Read_Model_Type();
        Double setVoltage = this._Get_Set_Voltage();
        Double setCurrent = this._Get_Set_Current();
        Double setFilLim = this._Get_Set_Filament_Limit();
        Double setPreHeat = this._Get_Set_Preheat();
        return new String[]{String.valueOf(setVoltage), String.valueOf(setCurrent), String.valueOf(setFilLim), String.valueOf(setPreHeat)};

    }

    @Override
    public Double[] Get_Set_Voltage_Current() {
        return new Double[]{0.0,0.0};
    }

    @Override
    public Double[] Get_Voltage_Current_Filament() {
        Double[] values = new Double[]{_Get_Voltage(),_Get_Current(),0.0};
        return values;

    }
    
    private Double _Get_Voltage() {
        double voltage = 0.0;
        try {
          //  SendCommandLock.lock();
            
            voltage = VoltageADCChip.ReadVoltage(VInADCNumber, 300, 2048);
            voltage = voltage*6;
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        }finally{
           // SendCommandLock.unlock();
            return voltage;
        }
    }

    private Double _Get_Current() {
        double current = 0.0;
        try {
            //SendCommandLock.lock();
            current = CurrentADCChip.ReadVoltage(CInADCNumber, 300, 2048);
            current = current * 8;
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            //SendCommandLock.unlock();
            return current;
        }
        
    }

    private Double _Get_Set_Preheat() {
        return 0.0; //do nothing as this feature is not supported on this supply model
    }

    private Double _Get_Set_Filament_Limit() {
        return 0.0; //do nothing as this feature is not supported on this supply model
    }

    private Double _Get_Set_Voltage() {
        return 0.0; //do nothing as this feature is not supported on this supply model
    }

    private Double _Get_Set_Current() {
        return 0.0; //do nothing as this feature is not supported on this supply model
    }

    private void _Set_Voltage(double value) {
        try {
            //SendCommandLock.lock();
            AnalogOutputs.SetVoltageScaled(intVOutDacNumber, value/6);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //SendCommandLock.unlock();
        }
    }

    private void _Set_Current(double value) {
        try {
            //SendCommandLock.lock();
            AnalogOutputs.SetVoltageScaled(intCOutDacNumber, value/8);
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            //SendCommandLock.unlock();
        }
    }

    @Override
    public void Set_Filament_Limit(double value) {
        return; //do nothing as this feature is not supported on this supply model
    }

    @Override
    public void Set_Filament_Preheat(double value) {
        return; //do nothing as this feature is not supported on this supply model
    }

    private void _Update_Status_Signals() {
        
        try {
           // SendCommandLock.lock();
            setRemoteMode(true);
            setFaultPresent(PowerSupplyFault.isLow());
            setConnected(true);
            setInterlockOpen(false);
            setHighVoltageState(XRaysOnSupply.isLow());
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        } finally{
           // SendCommandLock.unlock();
        }
    }

    private void _Update_Fault_States() {
        try {
           // SendCommandLock.lock();
            setArcPresent(KVRegulatorError.isHigh());
            setOverCurrent(false);
            setOverVoltage(false);
            setUnderCurrent(false);
            setUnderVoltage(false);
            setOverTemperature(false);
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        } finally{
            //SendCommandLock.unlock();
        }
    }

    @Override
    public boolean Xray_On() {
        
        try {
           // SendCommandLock.lock();
            XrayOnCommand.high();
            System.out.println("Xray On set high");
            
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        }finally{
           // SendCommandLock.unlock();
            return true;
        }
    }

    @Override
    public boolean Xray_Off() {
        
        try {
           // SendCommandLock.lock();
            
                XrayOnCommand.low();
                System.out.println("Xray On set Low");
            
        } catch (Exception e) {
            Logger.getLogger(DF_FF.class.getName()).log(Level.SEVERE, null, e);
        }finally{
           // SendCommandLock.unlock();
            return true;
        }
        
    }

    private String _Read_Model_Type() {

        setModelNumber("DF3");
        this.setVoltageScaleFactor((Double) 0.006);
        this.setCurrentScaleFactor((Double) 0.008);
        _Supply_Limits();
        return "DF3";
    }

    private void _Supply_Limits() {
        setMaxKV((double) 30);
        setMaxMA((double) 80);
        setMaxWatt((double) 3000);
    }

    private void _Set_Mode_Remote() {
        setRemoteMode(true);
    }

    /**
     * @return the address
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the port
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    @Override
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the connected
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    @Override
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @return the modelNumber
     */
    @Override
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * @param modelNumber the modelNumber to set
     */
    @Override
    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    /**
     * @return the ArcPresent
     */
    @Override
    public boolean isArcPresent() {
        return ArcPresent;
    }

    /**
     * @param ArcPresent the ArcPresent to set
     */
    @Override
    public void setArcPresent(boolean ArcPresent) {
        this.ArcPresent = ArcPresent;
    }

    /**
     * @return the OverTemperature
     */
    @Override
    public boolean isOverTemperature() {
        return OverTemperature;
    }

    /**
     * @param OverTemperature the OverTemperature to set
     */
    @Override
    public void setOverTemperature(boolean OverTemperature) {
        this.OverTemperature = OverTemperature;
    }

    /**
     * @return the OverVoltage
     */
    @Override
    public boolean isOverVoltage() {
        return OverVoltage;
    }

    /**
     * @param OverVoltage the OverVoltage to set
     */
    @Override
    public void setOverVoltage(boolean OverVoltage) {
        this.OverVoltage = OverVoltage;
    }

    /**
     * @return the UnderVoltage
     */
    @Override
    public boolean isUnderVoltage() {
        return UnderVoltage;
    }

    /**
     * @param UnderVoltage the UnderVoltage to set
     */
    @Override
    public void setUnderVoltage(boolean UnderVoltage) {
        this.UnderVoltage = UnderVoltage;
    }

    /**
     * @return the OverCurrent
     */
    @Override
    public boolean isOverCurrent() {
        return OverCurrent;
    }

    /**
     * @param OverCurrent the OverCurrent to set
     */
    @Override
    public void setOverCurrent(boolean OverCurrent) {
        this.OverCurrent = OverCurrent;
    }

    /**
     * @return the UnderCurrent
     */
    @Override
    public boolean isUnderCurrent() {
        return UnderCurrent;
    }

    /**
     * @param UnderCurrent the UnderCurrent to set
     */
    @Override
    public void setUnderCurrent(boolean UnderCurrent) {
        this.UnderCurrent = UnderCurrent;
    }

    /**
     * @return the HighVoltageState
     */
    @Override
    public boolean isHighVoltageState() {
        return HighVoltageState;
    }

    /**
     * @param HighVoltageState the HighVoltageState to set
     */
    @Override
    public void setHighVoltageState(boolean HighVoltageState) {
        this.HighVoltageState = HighVoltageState;
    }

    /**
     * @return the InterlockOpen
     */
    @Override
    public boolean isInterlockOpen() {
        return InterlockOpen;
    }

    /**
     * @param InterlockOpen the InterlockOpen to set
     */
    @Override
    public void setInterlockOpen(boolean InterlockOpen) {
        this.InterlockOpen = InterlockOpen;
    }

    /**
     * @return the FaultPresent
     */
    @Override
    public boolean isFaultPresent() {
        return FaultPresent;
    }

    /**
     * @param FaultPresent the FaultPresent to set
     */
    @Override
    public void setFaultPresent(boolean FaultPresent) {
        this.FaultPresent = FaultPresent;
    }

    /**
     * @return the RemoteMode
     */
    @Override
    public boolean isRemoteMode() {
        return RemoteMode;
    }

    /**
     * @param RemoteMode the RemoteMode to set
     */
    @Override
    public void setRemoteMode(boolean RemoteMode) {
        this.RemoteMode = RemoteMode;
    }

    /**
     * @return the MaxKV
     */
    @Override
    public Double getMaxKV() {
        return MaxKV;
    }

    /**
     * @param MaxKV the MaxKV to set
     */
    @Override
    public void setMaxKV(Double MaxKV) {
        this.MaxKV = MaxKV;
    }

    /**
     * @return the MaxMA
     */
    @Override
    public Double getMaxMA() {
        return MaxMA;
    }

    /**
     * @param MaxMA the MaxMA to set
     */
    @Override
    public void setMaxMA(Double MaxMA) {
        this.MaxMA = MaxMA;
    }

    /**
     * @return the MaxWatt
     */
    @Override
    public Double getMaxWatt() {
        return MaxWatt;
    }

    /**
     * @param MaxWatt the MaxWatt to set
     */
    @Override
    public void setMaxWatt(Double MaxWatt) {
        this.MaxWatt = MaxWatt;
    }

    /**
     * @return the voltageScaleFactor
     */
    @Override
    public Double getVoltageScaleFactor() {
        return voltageScaleFactor;
    }

    /**
     * @param voltageScaleFactor the voltageScaleFactor to set
     */
    @Override
    public void setVoltageScaleFactor(Double voltageScaleFactor) {
        this.voltageScaleFactor = voltageScaleFactor;
    }

    /**
     * @return the currentScaleFactor
     */
    @Override
    public Double getCurrentScaleFactor() {
        return currentScaleFactor;
    }

    /**
     * @param currentScaleFactor the currentScaleFactor to set
     */
    @Override
    public void setCurrentScaleFactor(Double currentScaleFactor) {
        this.currentScaleFactor = currentScaleFactor;
    }

    /**
     * @return the SendCommandLock
     */
    @Override
    public Lock getSendCommandLock() {
        return SendCommandLock;
    }

    /**
     * @param SendCommandLock the SendCommandLock to set
     */
    @Override
    public void setSendCommandLock(Lock SendCommandLock) {
        this.SendCommandLock = SendCommandLock;
    }

}
