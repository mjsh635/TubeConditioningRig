/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * interface for what a "high voltage supply" should be able to to.
 * creating a new supply type and inheriting this interface will allow the new
 * supply to be used in the program without any rewriting
 * @author mjsh635
 */
public interface IHighVoltagePowerSupply {

    public boolean OverTemperature = false;
    public boolean ArcPresent = false;
    public boolean UnderVoltage = false;
    public boolean OverVoltage = false;
    public boolean UnderCurrent = false;
    public boolean OverCurrent = false;
    public boolean InterlockOpen = false;
    public String modelNumber = "";
    public boolean connected = false;
    public Double MaxKV = 0.0;
    public Double MaxMA = 0.0;
    public Double MaxWatt = 0.0;
    public boolean FaultPresent = false;
    public boolean HighVoltageState = false;
    public boolean RemoteMode = false;
    
    
    ArrayList<String> Are_There_Any_Faults();

    String[] Get_About_Information();

    boolean Get_Interlock_Status();

    String Get_Model_Type();

    Double[] Get_Set_Voltage_Current();

    Double[] Get_Voltage_Current_Filament();

    boolean Is_Emmitting();

    Double Read_Current_Out_Double();

    String Read_Current_Out_String();

    Double Read_Voltage_Out_Double();

    String Read_Voltage_Out_String();

    void Reset_Faults();

    void Set_Current(double current);

    void Set_Filament_Limit(double value);

    void Set_Filament_Preheat(double value);

    void Set_Voltage(double voltage);

    boolean Xray_Off();

    boolean Xray_On();

    String get_IP_Address();

    void set_IP_Address(String new_address);

    void updates();
    
    String getAddress();
    void setAddress(String address);
    int getPort(); 
    void setPort(int port);
    boolean isConnected();
    void setConnected(boolean connected);
    String getModelNumber();
    void setModelNumber(String modelNumber);
    boolean isArcPresent();
    void setArcPresent(boolean ArcPresent);
    boolean isOverTemperature();
    void setOverTemperature(boolean OverTemperature);
    boolean isOverVoltage();
    void setOverVoltage(boolean OverVoltage);
    boolean isUnderVoltage();
    void setUnderVoltage(boolean UnderVoltage);
    boolean isOverCurrent();
    void setOverCurrent(boolean OverCurrent);
    boolean isUnderCurrent();
    void setUnderCurrent(boolean UnderCurrent);
    boolean isHighVoltageState();
    void setHighVoltageState(boolean HighVoltageState);
    boolean isInterlockOpen();
    void setInterlockOpen(boolean InterlockOpen);
    boolean isFaultPresent() ;
    void setFaultPresent(boolean FaultPresent);
    boolean isRemoteMode();
    void setRemoteMode(boolean RemoteMode);
    Double getMaxKV();
    void setMaxKV(Double MaxKV) ;
    Double getMaxMA();
    void setMaxMA(Double MaxMA);
    Double getMaxWatt();
    void setMaxWatt(Double MaxWatt) ;
    Double getVoltageScaleFactor();
    void setVoltageScaleFactor(Double voltageScaleFactor);
    Double getCurrentScaleFactor() ;
    void setCurrentScaleFactor(Double currentScaleFactor);
    Lock getSendCommandLock();
    void setSendCommandLock(Lock SendCommandLock);

}


