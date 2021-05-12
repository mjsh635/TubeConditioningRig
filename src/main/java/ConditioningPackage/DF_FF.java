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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mjsh6
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
    
    public DF_FF() {
       
         this.modelNumber = this.Get_Model_Type();
        
    }
    

    
    @Override
    public void set_IP_Address(String new_address) {
        return;
    }

    @Override
    public String get_IP_Address(){
        return "0.0.0.0";
    }

    @Override
    public void Set_Voltage(double voltage){
        throw new UnsupportedOperationException("not supported yet");
    }
    
    @Override
    public void Set_Current(double current){
        throw new UnsupportedOperationException("not supported yet");
    }
    
    @Override
    public void updates(){
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
        throw new UnsupportedOperationException();
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
    public String[] Get_About_Information(){
        
            this._Read_Model_Type();
            Double setVoltage = this._Get_Set_Voltage();
            Double setCurrent = this._Get_Set_Current();
            Double setFilLim =  this._Get_Set_Filament_Limit();
            Double setPreHeat =  this._Get_Set_Preheat();
            return new String[]{String.valueOf(setVoltage),String.valueOf(setCurrent),String.valueOf(setFilLim),String.valueOf(setPreHeat)};
            
      
    }
    @Override
    public Double[] Get_Set_Voltage_Current(){
        throw new UnsupportedOperationException();
    }

    @Override
    public Double[] Get_Voltage_Current_Filament() {
        throw new UnsupportedOperationException();
        
           
    }

    private Double _Get_Voltage() {
        // send command, and then parse response to a double.
       throw new UnsupportedOperationException("not supported yet");
    }

    private Double _Get_Current() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private Double _Get_Set_Preheat() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private Double _Get_Set_Filament_Limit() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private Double _Get_Set_Voltage() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private Double _Get_Set_Current() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private void _Set_Voltage(double value) {
throw new UnsupportedOperationException("not supported yet");
    }

    private void _Set_Current(double value) {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public void Set_Filament_Limit(double value) {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public void Set_Filament_Preheat(double value) {
        throw new UnsupportedOperationException("not supported yet");
    }

    private void _Update_Status_Signals() {
        throw new UnsupportedOperationException("not supported yet");
        // set remote true, set fault false, set connected true, interlock closed, hv off
        

    }

    private void _Update_Fault_States() {
        /*return args of faults
        ARG1 = ARC
        ARG2 = Over Temperature
        ARG3 = Over Voltage
        ARG4 = Under Voltage
        ARG5 = Over Current
        ARG6 = Under Current*/
        throw new UnsupportedOperationException("not supported yet");
        
    }

    @Override
    public boolean Xray_On() {
        throw new UnsupportedOperationException("not supported yet");
       
    }

    @Override
    public boolean Xray_Off() {
        throw new UnsupportedOperationException("not supported yet");
    }

    private String _Read_Model_Type() {
        
        setModelNumber("DF3");
        this.setVoltageScaleFactor((Double) 0.006);
        this.setCurrentScaleFactor((Double) 0.008);
            _Supply_Limits();
            return "DF3";
        }
    
    
    private void _Supply_Limits(){
        setMaxKV((double)30);
        setMaxMA((double)80);
        setMaxWatt((double)3000);
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
