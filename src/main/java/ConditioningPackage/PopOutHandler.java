/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author mjsh6
 */
public class PopOutHandler {

    JPanel panel;
    JPanel panelParent;
    Point Location;
    JDialog Window;
    boolean isPOP = false;
    String title;
    public PopOutHandler(String Name,JPanel panelToPop, JPanel panelPoppedFrom) {
        title = Name;
        panel = panelToPop;
        panel.setSize(panelToPop.getSize());
        panelParent = panelPoppedFrom;
        _originalPosition();
    }
    
    public void POP(){
        if(!isPOP){
        Window = new JDialog();
        Window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        Window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt){
                UNPOP();
            }
        });
        
        Window.setSize(400,310);
        }
        Window.setTitle(title);
        Window.add(panel);
        Window.setVisible(true);
        isPOP = true;
    }
    private void _originalPosition(){
        Location = panel.getLocation();
    }
    public Point UNPOP(){
        panelParent.add(panel);
        Window.dispose();
        isPOP = false;
        return Location;
    }
    
    
}
