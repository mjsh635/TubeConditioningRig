/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.io.Serializable;
import javax.swing.JCheckBox;

/**
 *
 * @author mjsh6
 */
public class SetupSettings implements Serializable {
    String Supply1IP,Supply2IP,Supply3IP,Supply4IP;
    String Supply1Port,Supply2Port,Supply3Port,Supply4Port;
    boolean Use1,Use2,Use3,Use4,Df3Mode;
    
    
    public void Defaults(){
        Use1 = true;
        Supply1IP = "192.168.1.4";
        Supply1Port = "50001";
        Use2 = false;
        Supply2IP = "192.168.1.4";
        Supply2Port = "50001";
        Use3 = false;
        Supply3IP = "192.168.1.4";
        Supply3Port = "50001";
        Use4 = false;
        Supply4IP = "192.168.1.4";
        Supply4Port = "50001";
        Df3Mode = false;
    }
    
}
