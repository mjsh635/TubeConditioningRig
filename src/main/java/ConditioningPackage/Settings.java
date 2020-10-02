/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author mjsh6
 */
public class Settings implements Serializable {
    String FilamentCurrentLimit;
    String FilamentPreHeat;
    String TotalStepCount;
    String TimeBetweenSteps;
    String StartingKV;
    String StartingMA;
    String TargetKV;
    String TargetMA;
    String NumberOnOffCycles;
    String OnCycleTime;
    String OffCycleTime;
    String TotalArcsBeforeStop;
    String ArcKVStep;
    String ArcMAStep;
    String ArcRecoveryTime;
    String ConditingFolderLocation;
    String SettingsFileLocation;
    String WarmupTime;
    String WarmupKV;
    String WarmupMA;
    String TubeSerialNumber;
    
    
    public void SetDefault(){
        this.FilamentCurrentLimit = "0.75";
        this.FilamentPreHeat = "0.8";
        this.TotalStepCount = "15";
        this.TimeBetweenSteps = "1.0";
        this.StartingKV = "12.0";
        this.StartingMA = "0.5";
        this.TargetKV = "20.0";
        this.TargetMA = "3.0";
        this.NumberOnOffCycles = "3";
        this.OnCycleTime = "60";
        this.OffCycleTime = "60";
        this.TotalArcsBeforeStop = "3";
        this.ArcKVStep = "1";
        this.ArcMAStep = "1";
        this.ArcRecoveryTime = "1";
        this.WarmupTime = "7";
        this.WarmupKV = "20";
        this.WarmupMA = "3";
        this.TubeSerialNumber = "";
    }
}
