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
public class DXM implements IHighVoltagePowerSupply {

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

    public DXM(String address, int port) {

        this.address = address;
        this.port = port;

        this.modelNumber = this.Get_Model_Type();

    }

    @Override
    public void set_IP_Address(String new_address) {
        this.setAddress(new_address);
    }

    @Override
    public String get_IP_Address() {
        return this.getAddress();
    }

    @Override
    public void Set_Voltage(double voltage) {

        this._Set_Voltage(voltage);

    }

    @Override
    public void Set_Current(double current) {

        this._Set_Current(current);

    }

    @Override
    public void updates() {
        _Update_Fault_States();
        _Update_Status_Signals();
    }

    @Override
    public String Get_Model_Type() {
        // sets the supply to remote mode, and returns the supply type in X#### format

        this._Set_Mode_Remote();
        this.setConnected(true);
        return this._Read_Model_Type();

    }

    @Override
    public boolean Get_Interlock_Status() {

        this._Update_Status_Signals();
        return this.isInterlockOpen();

    }

    @Override
    public void Reset_Faults() {
        try {
            this._Send_Command(31, "");
            this._Update_Status_Signals();
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    @Override
    public boolean Is_Emmitting() {

        this._Update_Fault_States();
        this._Update_Status_Signals();
        return this.isHighVoltageState();

    }

    @Override
    public ArrayList<String> Are_There_Any_Faults() {

        this._Update_Fault_States();
        ArrayList<String> Faults = new ArrayList<String>();
        if (this.isArcPresent()) {
            Faults.add("Arc Present");
        }
        if (this.isOverTemperature()) {
            Faults.add("Over Temperature");
        }
        if (this.isOverVoltage()) {
            Faults.add("Over Voltage");
        }
        if (this.isUnderVoltage()) {
            Faults.add("Under Voltage");
        }
        if (this.isOverCurrent()) {
            Faults.add("Over Current");
        }
        if (this.isUnderCurrent()) {
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
        return new Double[]{_Get_Set_Voltage(), _Get_Set_Current()};
    }

    @Override
    public Double[] Get_Voltage_Current_Filament() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(19, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            Double voltage = Double.parseDouble(response[1]);
            Double current = Double.parseDouble(response[2]);
            Double filCurrent = Double.parseDouble(response[3]);
            Double scaledVoltage = 0.0;
            Double scaledCurrent = 0.0;
            Double scaledFilCurrent = filCurrent * 0.001221;

            scaledVoltage = voltage * this.getVoltageScaleFactor();
            scaledCurrent = current * this.getCurrentScaleFactor();

            return new Double[]{scaledVoltage, scaledCurrent, scaledFilCurrent};
        }

    }

    private Double _Get_Voltage() {
        // send command, and then parse response to a double.
        String[] response = new String[10];
        try {
            response = this._Send_Command(19, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0"};
        } finally {
            Double voltage = Double.parseDouble(response[1]);
            Double scaledVoltage = 0.0;
            // depending on the model type, apply different weights
            scaledVoltage = voltage * getVoltageScaleFactor();

            return scaledVoltage;
        }
    }

    private Double _Get_Current() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(19, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {

            Double current = Double.parseDouble(response[2]);
            Double scaledCurrent = 0.0;
            // depending on the model type, apply different weights
            scaledCurrent = current * getCurrentScaleFactor();

            return scaledCurrent;
        }
    }

    private Double _Get_Set_Preheat() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(17, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            return (Double.valueOf(response[1]) * 0.0006105);
        }
    }

    private Double _Get_Set_Filament_Limit() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(16, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            return (Double.valueOf(response[1]) * 0.001221);
        }
    }

    private Double _Get_Set_Voltage() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(14, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            Double voltage = Double.parseDouble(response[1]);
            Double scaledVoltage = 0.0;
            // depending on the model type, apply different weights
            scaledVoltage = voltage * getVoltageScaleFactor();

            return scaledVoltage;
        }
    }

    private Double _Get_Set_Current() {
        String[] response = new String[10];
        try {
            response = this._Send_Command(15, "");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            Double current = Double.parseDouble(response[1]);
            Double scaledCurrent = 0.0;

            scaledCurrent = current * getCurrentScaleFactor();

            return scaledCurrent;
        }
    }

    private void _Set_Voltage(double value) {
        if (value >= this.getMaxKV()) {
            value = this.getMaxKV();
        }
        try {
            int scaledValue = 0;
            scaledValue = (int) Math.round(Double.valueOf(value) / getVoltageScaleFactor());

            this._Send_Command(10, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    private void _Set_Current(double value) {
        if (value >= this.getMaxMA()) {
            value = this.getMaxMA();
        }
        try {
            int scaledValue = 0;
            scaledValue = (int) Math.round(Double.valueOf(value) / getCurrentScaleFactor());

            this._Send_Command(11, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    @Override
    public void Set_Filament_Limit(double value) {
        try {
            int scaledValue = (int) Math.round(Double.valueOf(value) / 0.001221);
            this._Send_Command(12, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    @Override
    public void Set_Filament_Preheat(double value) {
        try {
            int scaledValue = (int) Math.round(Double.valueOf(value) / 0.0006105);
            this._Send_Command(13, String.valueOf(scaledValue));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
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
            this.setHighVoltageState(response[1].equals("1"));
            this.setInterlockOpen(response[2].equals("1"));
            this.setFaultPresent(response[3].equals("1"));
            this.setRemoteMode(response[4].equals("1"));
            this.setConnected(true);
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
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
            this.setArcPresent(response[1].equals("1"));
            this.setOverTemperature(response[2].equals("1"));
            this.setOverVoltage(response[3].equals("1"));
            this.setUnderVoltage(response[4].equals("1"));
            this.setOverCurrent(response[5].equals("1"));
            this.setUnderCurrent(response[6].equals("1"));
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    @Override
    public boolean Xray_On() {
        String[] response = new String[10];
        // command the supply to turn on xrays, returns true if command received successful
        try {
            response = this._Send_Command(98, "1");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            if (response[1].equals("$")) {
                return true;
            } else {
                return false;
            }
        }

    }

    @Override
    public boolean Xray_Off() {
        String[] response = new String[10];
        // command the supply to turn off xrays, returns true if command received successful
        try {
            response = this._Send_Command(98, "0");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
            response = new String[]{"E", "0.0", "0.0", "0.0"};
        } finally {
            if (response[1].equals("$")) {
                return true;
            } else {
                return false;
            }
        }
    }

    private String _Read_Model_Type() {
        String[] response = new String[]{"", "", "", "", "", ""};
        // call send command, and return the 1st index (this contains the model number)
        try {
            response = this._Send_Command(26, "");

        } catch (ArrayIndexOutOfBoundsException OB) {

            System.out.println("bad response from Supply");
            response = new String[]{"E", "BadResponse", "0.0", "0.0"};
        } finally {

            String model;
            switch (response[1]) //if the model type is DXM based, switch to X####, else use X####
            {
                case "DXM02":
                    model = "X3481";
                    this.setVoltageScaleFactor((Double) 0.007326007);
                    this.setCurrentScaleFactor((Double) 0.002442002);
                    break;
                case "X3481":
                    model = "X3481";
                    this.setVoltageScaleFactor((Double) 0.007326007);
                    this.setCurrentScaleFactor((Double) 0.002442002);
                    break;
                case "DXM20":
                    model = "X413";
                    this.setVoltageScaleFactor((Double) 0.007326007);
                    this.setCurrentScaleFactor((Double) 0.004884004);
                    break;
                case "X413":
                    model = "X413";
                    this.setVoltageScaleFactor((Double) 0.007326007);
                    this.setCurrentScaleFactor((Double) 0.004884004);
                    break;
                case "DXM21":
                    model = "X4911";
                    this.setVoltageScaleFactor((Double) 0.0097680097);
                    this.setCurrentScaleFactor((Double) 0.00366300);
                    break;
                case "X4911":
                    model = "X4911";
                    this.setVoltageScaleFactor((Double) 0.0097680097);
                    this.setCurrentScaleFactor((Double) 0.00366300);
                    break;
                case "DXM33":
                    model = "X4087";
                    this.setVoltageScaleFactor((Double) 0.0097680097);
                    this.setCurrentScaleFactor((Double) 0.007326);
                    break;
                case "X4087":
                    model = "X4087";
                    this.setVoltageScaleFactor((Double) 0.0097680097);
                    this.setCurrentScaleFactor((Double) 0.007326);
                    break;
                case "DXM41":
                    model = "DXM41";
                    this.setVoltageScaleFactor((Double) 0.0183150183);
                    this.setCurrentScaleFactor((Double) 0.003907203);
                    break;
                case "DXM35":
                    model = "X4974";
                    this.setVoltageScaleFactor((Double) 0.014652014);
                    this.setCurrentScaleFactor((Double) 0.004884004);
                    break;
                case "X4974":
                    model = "X4974";
                    this.setVoltageScaleFactor((Double) 0.014652014);
                    this.setCurrentScaleFactor((Double) 0.004884004);
                    break;
                default:
                    model = response[1];
                    this.setVoltageScaleFactor((Double) 0.0);
                    this.setCurrentScaleFactor((Double) 0.0);
            }

            this.setModelNumber(model);
            _Supply_Limits();
            return model;
        }
    }

    private void _Supply_Limits() {
        switch (this.getModelNumber()) //if the model type is DXM based, switch to X####, else use X####
        {

            case "X3481":
                this.setMaxKV((Double) 30.0);
                this.setMaxMA((Double) 10.0);
                this.setMaxWatt((Double) 300.0);
                break;

            case "X413":
                this.setMaxKV((Double) 30.0);
                this.setMaxMA((Double) 20.0);
                this.setMaxWatt((Double) 600.0);
                break;

            case "X4911":
                this.setMaxKV((Double) 40.0);
                this.setMaxMA((Double) 15.0);
                this.setMaxWatt((Double) 600.0);
                break;

            case "X4087":
                this.setMaxKV((Double) 40.0);
                this.setMaxMA((Double) 30.0);
                this.setMaxWatt((Double) 1200.0);
                break;
            case "DXM41":
                this.setMaxKV((Double) 75.0);
                this.setMaxMA((Double) 16.0);
                this.setMaxWatt((Double) 1200.0);
                break;
            case "X4974":
                this.setMaxKV((Double) 0.0);
                this.setMaxMA((Double) 0.0);
                this.setMaxWatt((Double) 1200.0);
                break;

            default:
                this.setMaxKV((Double) 0.0);
                this.setMaxMA((Double) 0.0);
                this.setMaxWatt((Double) 0.0);
        }
    }

    private void _Set_Mode_Remote() {
        // set the supply to remote mode
        try {
            this._Send_Command(99, "1");
        } catch (ArrayIndexOutOfBoundsException OB) {
            System.out.println("bad response from Supply");
        }
    }

    private String[] _Send_Command(int Command, String Argument) {
        getSendCommandLock().lock();
        //System.out.println("lock");

        try {
            // Send commands to the supply
            if (Argument != "") {
                // if the argument isnt blank, it requires a comma after
                Argument = Argument + ",";
            }
            String reply = "";
            Socket sock = new Socket();
            //(Socket sock = new Socket(this.address,this.port))
            try {   // open socket, create message, send message, receive response, convert to string

                sock.connect(new InetSocketAddress(this.getAddress(), this.getPort()), 2000);
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
                System.out.println(String.format("Connection Timeout on Address: %s", this.getAddress()));
//            CE.printStackTrace();
this.setConnected(false);

            } catch (SocketTimeoutException STE) {
                System.out.println(String.format("Communication Timeout on Address: %s", this.getAddress()));
                //STE.printStackTrace();
                this.setConnected(false);

            } catch (Exception e) {
                System.out.println("DXM Send Command Unknown Internal Exception caught:");
                e.printStackTrace();

            } finally {
                sock.close();

            }

            // split response into string array
            if (reply.equals("")) {
                throw new Exception("E");
            }

            String[] splitResponse = reply.split(",");
            //return the string array
            return splitResponse;

        } catch (Exception e) {

            if (!e.getMessage().equals("E")) {
                System.out.println("DXM Send Command Unknown External Exception caught:");
                e.printStackTrace();
            }
            throw new ArrayIndexOutOfBoundsException();

        } finally {

            getSendCommandLock().unlock();
            //System.out.println("unlock");
        }

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
