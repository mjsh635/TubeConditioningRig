/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author mjsh6
 */
public class SettingsHandler {
    Settings appsettings;
    SetupSettings setupSettings;
    String settingsFilePath;
    File file;
    
    public SettingsHandler(String SettingsFilePath) {
        this.settingsFilePath = SettingsFilePath;
        file = new File(settingsFilePath);
        if (!Files.exists(Paths.get(file.getPath()))){
            try{
                System.out.println("No file, making new");
                this.file.createNewFile();
                this.SetDefaultSettings();
        }
        catch(Exception e){
            e.printStackTrace();
        }    
        }
        this.loadSettings();
        
    }
    public SettingsHandler(String Path, boolean UseSetupSettings){
        this.settingsFilePath = Path;
        file = new File(settingsFilePath);
        if (!Files.exists(Paths.get(file.getPath()))){
            try{
                System.out.println("No file, making new");
                this.file.createNewFile();
                SetSetupDefaultSettings();
        }
        catch(Exception e){
            e.printStackTrace();
        }    
        }
        this.loadSetupSettings();
    }
    
    private void loadSettings(){
        try (FileInputStream fis = new FileInputStream(this.settingsFilePath);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.appsettings = (Settings)ois.readObject();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void loadSetupSettings(){
        try (FileInputStream fis = new FileInputStream(this.settingsFilePath);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.setupSettings = (SetupSettings)ois.readObject();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void SetDefaultSettings(){
        Settings defaultSettings = new Settings();
        
        defaultSettings.SetDefault();
        
        try(FileOutputStream fos = new FileOutputStream(this.file);ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(defaultSettings);
            oos.flush();
            
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    public void SetSetupDefaultSettings(){
        SetupSettings defaultSetupSettings = new SetupSettings();
        
        defaultSetupSettings.Defaults();
        
        try(FileOutputStream fos = new FileOutputStream(this.file);ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(defaultSetupSettings);
            oos.flush();
            
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    
    public void Reset_To_Default(){
        this.appsettings.SetDefault();
    }
    
    public void SaveSettings(){
        
        try(FileOutputStream fos = new FileOutputStream(this.file);ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(this.appsettings);
            oos.flush();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void SaveSetupSettings(){
        
        try(FileOutputStream fos = new FileOutputStream(this.file);ObjectOutputStream oos = new ObjectOutputStream(fos)){
            
            oos.writeObject(this.setupSettings);
            oos.flush();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
    
    

