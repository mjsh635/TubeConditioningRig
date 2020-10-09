/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.sql.Timestamp;
import javax.swing.JTextArea;

/**
 *
 * @author mjsh6
 */
public class SystemLoggingHandler {
 JTextArea LoggingArea;
    public SystemLoggingHandler(JTextArea area) {
        LoggingArea = area;
    }
    
    public void AppendToLog(String message){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LoggingArea.append(timestamp + "||" + message + "\n");
    }
    
    public void clearLog(){
        
    }
    
    public void SaveLogToFile(String SaveLocation){
        
    }
    
}
