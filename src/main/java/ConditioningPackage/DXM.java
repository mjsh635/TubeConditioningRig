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
public class DXM {

    String address;
    int port;
    boolean connected = false;
    public String modelNumber = "";
    boolean ArcPresent = false;
    boolean OverTemperature = false;
    boolean OverVoltage = false;
    boolean UnderVoltage = false;
    boolean OverCurrent = false;
    boolean UnderCurrent = false;
    boolean HighVoltageState = false;
    boolean InterlockOpen = false;
    boolean FaultPresent = false;
    boolean RemoteMode = false;
    Double MaxKV = 0.0;
    Double MaxMA = 0.0;
    Double MaxWatt = 0.0;
    Double voltageScaleFactor;
    Double currentScaleFactor;
    Lock SendCommandLock = new ReentrantLock();
    
    public DXM(String address, int port) {
        
        this.address = address;
        this.port = port;
        
        try{
            this.modelNumber = this.Get_Model_Type();
        }
        catch(Error e){
            if (e.getMessage()== "ERR001"){
                System.out.println("Could not connect to Supply");
            }
        }
    }

    public void set_IP_Address(String new_address) {
        this.address = new_address;
    }

    public String get_IP_Address(){
        return this.address;
    }

    public Boolean Set_Voltage(double voltage){
        try{
            this._Set_Voltage(voltage);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public Boolean Set_Current(double current){
        try{
            this._Set_Current(current);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public String Get_Model_Type() {
        // sets the supply to remote mode, and returns the supply type in X#### format
        try {
            this._Set_Mode_Remote();
            this.connected = true;
            return this._Read_Model_Type();
            
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            return "no connection";
        }
    }
    
    
    public boolean Get_Interlock_Status() {
        try {
            this._Update_Status_Signals();
            return this.InterlockOpen;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public void Reset_Faults() {
        try {
            this._Send_Command(31, "");
            this._Update_Status_Signals();
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
        }
    }

    public boolean Is_Emmitting() {
        try {
            this._Update_Fault_States();
            this._Update_Status_Signals();
            return this.HighVoltageState;

        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public ArrayList<String> Are_There_Any_Faults() {
        try {
            this._Update_Fault_States();
            ArrayList<String> Faults = new ArrayList<String>();
            if (this.ArcPresent) {
                Faults.add("Arc Present");
            }
            if (this.OverTemperature) {
                Faults.add("Over Temperature");
            }
            if (this.OverVoltage) {
                Faults.add("Over Voltage");
            }
            if (this.UnderVoltage) {
                Faults.add("Under Voltage");
            }
            if (this.OverCurrent) {
                Faults.add("Over Current");
            }
            if (this.UnderCurrent) {
                Faults.add("Under Current");
            }
            return Faults;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public String Read_Voltage_Out_String() {
        try {
            return this._Get_Voltage().toString();
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }

    }

    public Double Read_Voltage_Out_Double() {
        try {
            return this._Get_Voltage();
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public String Read_Current_Out_String() {
        try {
            return this._Get_Current().toString();

        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public Double Read_Current_Out_Double() {
        try {
            return this._Get_Current();
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }
    public String[] Get_About_Information(){
        try{
            this._Read_Model_Type();
            Double setVoltage = this._Get_Set_Voltage();
            Double setCurrent = this._Get_Set_Current();
            Double setFilLim =  this._Get_Set_Filament_Limit();
            Double setPreHeat =  this._Get_Set_Preheat();
            return new String[]{String.valueOf(setVoltage),String.valueOf(setCurrent),String.valueOf(setFilLim),String.valueOf(setPreHeat)};
            
        }catch (ArrayIndexOutOfBoundsException OB){
            System.out.println(OB);
            throw new Error("ERR001");
        }catch (Error e){
            System.out.print(e);
            throw new Error("ERR001");
        }
    }
    

    public Double[] Get_Voltage_Current_Filament() {
        
        try {
            String[] response = this._Send_Command(19, "");
            
            Double voltage = Double.parseDouble(response[1]);
            Double current = Double.parseDouble(response[2]);
            Double filCurrent = Double.parseDouble(response[3]);
            Double scaledVoltage = 0.0;
            Double scaledCurrent = 0.0;
            Double scaledFilCurrent = filCurrent * 0.001221;
            
            scaledVoltage = voltage * this.voltageScaleFactor;
            scaledCurrent = current * this.currentScaleFactor;
                 
            return new Double[]{scaledVoltage, scaledCurrent, scaledFilCurrent};
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Voltage() {
        // send command, and then parse response to a double.
        try {
            String[] response = this._Send_Command(19, "");
            Double voltage = Double.parseDouble(response[1]);
            Double scaledVoltage = 0.0;
            // depending on the model type, apply different weights
            scaledVoltage = voltage * voltageScaleFactor;
                    
            return scaledVoltage;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Current() {
        try {
            String[] response = this._Send_Command(19, "");
            Double current = Double.parseDouble(response[2]);
            Double scaledCurrent = 0.0;
            // depending on the model type, apply different weights
            scaledCurrent = current * currentScaleFactor;
                    
            return scaledCurrent;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Set_Preheat() {
        try {
            String[] response = this._Send_Command(17, "");
            return (Double.valueOf(response[1]) * 0.0006105);
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Set_Filament_Limit() {
        try {
            String[] response = this._Send_Command(16, "");
            return (Double.valueOf(response[1]) * 0.001221);
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Set_Voltage() {
        try {
            String[] response = this._Send_Command(14, "");
            Double voltage = Double.parseDouble(response[1]);
            Double scaledVoltage = 0.0;
            // depending on the model type, apply different weights
            scaledVoltage = voltage * voltageScaleFactor;
                    
            return scaledVoltage;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private Double _Get_Set_Current() {
        try {
            String[] response = this._Send_Command(15, "");
            Double current = Double.parseDouble(response[1]);
            Double scaledCurrent = 0.0;
            
            scaledCurrent = current * currentScaleFactor;
            
            return scaledCurrent;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private void _Set_Voltage(double value) {
        if(value >= this.MaxKV){
            value = this.MaxKV;
        }
        try {
            int scaledValue = 0;
            scaledValue = (int) Math.round(Double.valueOf(value) / voltageScaleFactor);
            
            this._Send_Command(10, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private void _Set_Current(double value) {
        if(value >= this.MaxMA){
            value = this.MaxMA;
        }
        try {
            int scaledValue = 0;
            scaledValue = (int) Math.round(Double.valueOf(value) / currentScaleFactor);
            
            this._Send_Command(11, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public void Set_Filament_Limit(double value) {
        try {
            int scaledValue = (int) Math.round(Double.valueOf(value) / 0.001221);
            this._Send_Command(12, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public void Set_Filament_Preheat(double value) {
        try {
            int scaledValue = (int) Math.round(Double.valueOf(value) / 0.0006105);
            this._Send_Command(13, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private void _Update_Status_Signals() {
        /*Command to read status
        <ARG1>  1 = HvOn, 0 = HvOff
        <ARG2>  1 = Interlock 1 Open, 0 = Interlock 1 Closed
        <ARG3>  1 = Fault Condition, 0 = No Fault
        <ARG4>  1 = Remote Mode, 0 = Local Mode
         */
        try {
            String[] response = this._Send_Command(22, "");
            this.HighVoltageState = (response[2] == "1");
            this.InterlockOpen = (response[3] == "1");
            this.FaultPresent = (response[4] == "1");
            this.RemoteMode = (response[5] == "1");
            this.connected = true;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }

    }

    private void _Update_Fault_States() {
        /*return args of faults
        ARG1 = ARC
        ARG2 = Over Temperature
        ARG3 = Over Voltage
        ARG4 = Under Voltage
        ARG5 = Over Current
        ARG6 = Under Current*/
        try {
            String[] response = this._Send_Command(68, "");
            this.ArcPresent = (response[2] == "1");
            this.OverTemperature = (response[3] == "1");
            this.OverVoltage = (response[4] == "1");
            this.UnderVoltage = (response[5] == "1");
            this.OverCurrent = (response[6] == "1");
            this.UnderCurrent = (response[7] == "1");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public boolean Xray_On() {
        // command the supply to turn on xrays, returns true if command received successful
        try {
            String[] response = this._Send_Command(98, "1");
            if (response[1] == "$") {
                return true;
            } else {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    public boolean Xray_Off() {
        // command the supply to turn off xrays, returns true if command received successful
        try {
            String[] response = this._Send_Command(98, "0");
            if (response[1] == "$") {
                return true;
            } else {
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }

    private String _Read_Model_Type() {
        // call send command, and return the 1st index (this contains the model number)
        try {
            String response = this._Send_Command(26, "")[1];
            String model;
            switch (response) //if the model type is DXM based, switch to X####, else use X####
            {
                case "DXM02":
                    model = "X3481";
                    this.voltageScaleFactor = 0.007326007;
                    this.currentScaleFactor = 0.002442002 ;
                    break;
                case "X3481":
                    model = "X3481";
                    this.voltageScaleFactor = 0.007326007;
                    this.currentScaleFactor = 0.002442002 ;
                    break;
                case "DXM20":
                    model = "X413";
                    this.voltageScaleFactor = 0.007326007;
                    this.currentScaleFactor = 0.004884004;
                    break;
                case "X413":
                    model = "X413";
                    this.voltageScaleFactor = 0.007326007;
                    this.currentScaleFactor = 0.004884004;
                    break;
                case "DXM21":
                    model = "X4911";
                    this.voltageScaleFactor = 0.0097680097;
                    this.currentScaleFactor = 0.00366300;
                    break;
                case "X4911":
                    model = "X4911";
                    this.voltageScaleFactor = 0.0097680097;
                    this.currentScaleFactor = 0.00366300;
                    break;
                case "DXM33":
                    model = "X4087";
                    this.voltageScaleFactor = 0.0097680097;
                    this.currentScaleFactor = 0.007326;
                    break;
                case "X4087":
                    model = "X4087";
                    this.voltageScaleFactor = 0.0097680097;
                    this.currentScaleFactor = 0.007326;
                    break;
                case "DXM41":
                    model = "DXM41";
                    this.voltageScaleFactor = 0.0183150183;
                    this.currentScaleFactor = 0.003907203;
                    break;
                case "DXM35":
                    model = "X4974";
                    this.voltageScaleFactor = 0.014652014;
                    this.currentScaleFactor = 0.004884004;
                    break;
                case "X4974":
                    model = "X4974";
                    this.voltageScaleFactor = 0.014652014;
                    this.currentScaleFactor = 0.004884004;
                    break;
                default:
                    model = response;
                    this.voltageScaleFactor = 0.0;
                    this.currentScaleFactor = 0.0;
            }
            
            this.modelNumber = model;
            _Supply_Limits();
            return model;
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println(OB);
            throw new Error("ERR001");
        }
    }
    
    private void _Supply_Limits(){
        switch (this.modelNumber) //if the model type is DXM based, switch to X####, else use X####
            {
                
                case "X3481":
                    this.MaxKV = 30.0;
                    this.MaxMA = 10.0;
                    this.MaxWatt = 300.0;
                    break;
                
                case "X413":
                    this.MaxKV = 30.0;
                    this.MaxMA = 20.0;
                    this.MaxWatt = 600.0;
                    break;
                
                case "X4911":
                    this.MaxKV = 40.0;
                    this.MaxMA = 15.0;
                    this.MaxWatt = 600.0;
                    break;
                
                case "X4087":
                    this.MaxKV = 40.0;
                    this.MaxMA = 30.0;
                    this.MaxWatt = 1200.0;
                    break;
                case "DXM41":
                    this.MaxKV = 75.0;
                    this.MaxMA = 16.0;
                    this.MaxWatt = 1200.0;
                    break;
                case "X4974":
                    this.MaxKV = 0.0;
                    this.MaxMA = 0.0;
                    this.MaxWatt = 1200.0;
                    break;
                    
                default:
                    this.MaxKV = 0.0;
                    this.MaxMA = 0.0;
                    this.MaxWatt = 0.0;
            }
    }

    private void _Set_Mode_Remote() {
        // set the supply to remote mode
        this._Send_Command(99, "1");
    }

    private String[] _Send_Command(int Command, String Argument) {
        SendCommandLock.lock();
        //System.out.println("lock");
        
        try{
        // Send commands to the supply
        if (Argument != "") {
            // if the argument isnt blank, it requires a comma after
            Argument = Argument + ",";
        }
        String reply = "";
        Socket sock = new Socket();
        //(Socket sock = new Socket(this.address,this.port))
        try  {   // open socket, create message, send message, receive response, convert to string
            
            sock.connect(new InetSocketAddress(this.address,this.port), 2000);
            sock.setSoTimeout(2000);
            
            String message = String.format("\002%1$s,%2$s\003", Command, Argument);
            byte[] byteResponse = new byte[40];
            byte[] byteArrayMessage = message.getBytes(StandardCharsets.US_ASCII);
            OutputStream outStream = sock.getOutputStream();
            InputStream inStream = sock.getInputStream();
            outStream.write(byteArrayMessage);
            Thread.sleep(200);
            inStream.read(byteResponse);

            reply = new String(byteResponse);
        } catch (java.net.ConnectException CE) {
            System.out.println(String.format("Connection Timeout on Address: %s", this.address));
            CE.printStackTrace();
            this.connected = false;
            
        } catch (SocketTimeoutException STE) {
            System.out.println(String.format("Communication Timeout on Address: %s", this.address));
            //STE.printStackTrace();
            this.connected = false;
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally{
            sock.close();
            
        }
        
        // split response into string array
        String[] splitResponse = reply.split(",");
        //return the string array
        return splitResponse;
        
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            
            SendCommandLock.unlock();
            //System.out.println("unlock");
        }
        throw new IndexOutOfBoundsException();
    }

}
