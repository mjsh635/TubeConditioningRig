/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

/**
 *
 * @author mjsh6
 */
public class ConditioningHandler extends Thread{
    Settings vals;
    public ConditioningHandler(Settings userSettings) {
        vals = userSettings;
    }
    
    public void run(){
        System.out.println("Executing");
        System.out.println(vals.StartingKV);
        System.out.println(vals.TargetKV);
        System.out.println(vals.StartingMA);
        System.out.println(vals.TargetMA);
        
    }
    public void Start_Conditioning(){
        System.out.println("Starting");
        this.start();
    }
    
    public void Stop_Conditioning(){
        System.out.println("Stoping");
        int stop_attempt = 0;
        
        while (this.isAlive() && stop_attempt<3){
            this.interrupt();
            try{
            this.join(500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            stop_attempt++;
        }      
        
    }
    
}
