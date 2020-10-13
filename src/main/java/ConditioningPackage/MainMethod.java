/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 *
 * @author mjsh6
 */
public class MainMethod {

    public static void main(String[] args) {
                  
            System.out.println("Starting Window");
            SetupFrame setup = new SetupFrame();
            setup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setup.setVisible(true);
           
            
        
        
    }
}
