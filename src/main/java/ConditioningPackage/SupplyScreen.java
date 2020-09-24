/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import com.sun.jdi.request.BreakpointRequest;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author mjsh6
 */
public class SupplyScreen extends javax.swing.JFrame {

    /**
     * Creates new form SupplyScreen
     */
    String IPAddress;
    int port;
    DXM supply;
    Warmup_Handler warmup;
    
    public SupplyScreen(String IPAddress,String port) {
        initComponents();
        this.IPAddress = IPAddress;
        this.port = Integer.valueOf(port);
        this.supply = new DXM(this.IPAddress,this.port);
        this.update_UI();
        this.warmup = new Warmup_Handler(this.supply, this.WarmupSelectionButtonGroup);
    }
    
    public SupplyScreen(){
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WarmupSelectionButtonGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        ManualControlPanel = new javax.swing.JPanel();
        XrayOnOffPanel = new javax.swing.JPanel();
        XrayOnButton = new javax.swing.JButton();
        XrayOffButton = new javax.swing.JButton();
        VoltageLabel = new javax.swing.JLabel();
        CurrentLabel = new javax.swing.JLabel();
        VoltageSetTBox = new javax.swing.JTextField();
        CurrentSetTBox = new javax.swing.JTextField();
        kvLabel = new javax.swing.JLabel();
        maLabel = new javax.swing.JLabel();
        TitleLabel = new javax.swing.JLabel();
        VoltageReadoutLabel = new javax.swing.JLabel();
        CurrentReadoutLabel = new javax.swing.JLabel();
        SupplySettingsPanel = new javax.swing.JPanel();
        PreHeatLabel = new javax.swing.JLabel();
        SupplyWattageTBox = new javax.swing.JTextField();
        SupplyWattageLabel = new javax.swing.JLabel();
        SupplyMaxKVTbox = new javax.swing.JTextField();
        SupplyMaxKVLabel = new javax.swing.JLabel();
        SupplyMaxMaLabel = new javax.swing.JLabel();
        SupplyMaxMATBox = new javax.swing.JTextField();
        FillCurrTBox = new javax.swing.JTextField();
        PreHeatTBox = new javax.swing.JTextField();
        FilCurrLabel = new javax.swing.JLabel();
        WarmupPanel = new javax.swing.JPanel();
        WarmupTitleLabel = new javax.swing.JLabel();
        RadioButton7min = new javax.swing.JRadioButton();
        RadioButton15min = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        XrayOnOffPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        XrayOnButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        XrayOnButton.setText("XRAY ON");
        XrayOnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XrayOnButtonActionPerformed(evt);
            }
        });

        XrayOffButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        XrayOffButton.setText("XRAY OFF");
        XrayOffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XrayOffButtonActionPerformed(evt);
            }
        });

        VoltageLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        VoltageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        VoltageLabel.setText("VOLTAGE(kV)");

        CurrentLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        CurrentLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CurrentLabel.setText("CURRENT(mA)");

        VoltageSetTBox.setText("0.0");

        CurrentSetTBox.setText("0.0");
        CurrentSetTBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentSetTBoxActionPerformed(evt);
            }
        });

        kvLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        kvLabel.setText("kV");

        maLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        maLabel.setText("mA");

        TitleLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        TitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TitleLabel.setText("Manual Xray Control");

        VoltageReadoutLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        VoltageReadoutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        VoltageReadoutLabel.setText("0.0");

        CurrentReadoutLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        CurrentReadoutLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CurrentReadoutLabel.setText("0.0");

        javax.swing.GroupLayout XrayOnOffPanelLayout = new javax.swing.GroupLayout(XrayOnOffPanel);
        XrayOnOffPanel.setLayout(XrayOnOffPanelLayout);
        XrayOnOffPanelLayout.setHorizontalGroup(
            XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(VoltageReadoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CurrentReadoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                                        .addComponent(kvLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VoltageSetTBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(maLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(CurrentSetTBox))
                                    .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                                        .addComponent(XrayOnButton, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(XrayOffButton, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                                .addComponent(VoltageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CurrentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2)))))
                .addContainerGap())
        );

        XrayOnOffPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {XrayOffButton, XrayOnButton});

        XrayOnOffPanelLayout.setVerticalGroup(
            XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(XrayOnOffPanelLayout.createSequentialGroup()
                .addComponent(TitleLabel)
                .addGap(18, 18, 18)
                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VoltageLabel)
                    .addComponent(CurrentLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CurrentReadoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(VoltageReadoutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(XrayOnButton)
                    .addComponent(XrayOffButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(XrayOnOffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VoltageSetTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CurrentSetTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kvLabel)
                    .addComponent(maLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        XrayOnOffPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {XrayOffButton, XrayOnButton});

        SupplySettingsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        PreHeatLabel.setText("Filament Pre-heat: ");

        SupplyWattageTBox.setEditable(false);
        SupplyWattageTBox.setText("0.0");

        SupplyWattageLabel.setText("Supply Wattage:");

        SupplyMaxKVTbox.setEditable(false);
        SupplyMaxKVTbox.setText("0.0");

        SupplyMaxKVLabel.setText("Supply Max kV:");

        SupplyMaxMaLabel.setText("Supply Max mA:");

        SupplyMaxMATBox.setEditable(false);
        SupplyMaxMATBox.setText("0.0");

        FillCurrTBox.setText("0.0");

        PreHeatTBox.setText("0.0");

        FilCurrLabel.setText("Filament Current Limit: ");

        javax.swing.GroupLayout SupplySettingsPanelLayout = new javax.swing.GroupLayout(SupplySettingsPanel);
        SupplySettingsPanel.setLayout(SupplySettingsPanelLayout);
        SupplySettingsPanelLayout.setHorizontalGroup(
            SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SupplySettingsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilCurrLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PreHeatLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SupplyMaxKVLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SupplyWattageLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SupplyMaxMaLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SupplyMaxMATBox)
                    .addComponent(SupplyMaxKVTbox)
                    .addComponent(SupplyWattageTBox)
                    .addComponent(PreHeatTBox)
                    .addComponent(FillCurrTBox, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        SupplySettingsPanelLayout.setVerticalGroup(
            SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SupplySettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FillCurrTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FilCurrLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PreHeatTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PreHeatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplyWattageTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SupplyWattageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplyMaxKVTbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SupplyMaxKVLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplySettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplyMaxMATBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SupplyMaxMaLabel))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        WarmupPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        WarmupTitleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        WarmupTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        WarmupTitleLabel.setText("Warm up");

        WarmupSelectionButtonGroup.add(RadioButton7min);
        RadioButton7min.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RadioButton7min.setText("7-min");

        WarmupSelectionButtonGroup.add(RadioButton15min);
        RadioButton15min.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RadioButton15min.setText("15-min");

        jButton1.setText("Start Warmup");

        jButton2.setText("Stop Warmup");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout WarmupPanelLayout = new javax.swing.GroupLayout(WarmupPanel);
        WarmupPanel.setLayout(WarmupPanelLayout);
        WarmupPanelLayout.setHorizontalGroup(
            WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WarmupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(WarmupTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(WarmupPanelLayout.createSequentialGroup()
                        .addGroup(WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RadioButton7min, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(RadioButton15min, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        WarmupPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {RadioButton15min, RadioButton7min});

        WarmupPanelLayout.setVerticalGroup(
            WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WarmupPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WarmupTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RadioButton7min)
                    .addComponent(RadioButton15min))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(WarmupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        WarmupPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        javax.swing.GroupLayout ManualControlPanelLayout = new javax.swing.GroupLayout(ManualControlPanel);
        ManualControlPanel.setLayout(ManualControlPanelLayout);
        ManualControlPanelLayout.setHorizontalGroup(
            ManualControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ManualControlPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(XrayOnOffPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ManualControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SupplySettingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(WarmupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        ManualControlPanelLayout.setVerticalGroup(
            ManualControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ManualControlPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(ManualControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ManualControlPanelLayout.createSequentialGroup()
                        .addComponent(SupplySettingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(WarmupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(XrayOnOffPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Manual Control", ManualControlPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CurrentSetTBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CurrentSetTBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CurrentSetTBoxActionPerformed

    public void update_UI(){
        switch (this.supply.modelNumber) {
                case "X4087":
                    this.SupplyMaxKVTbox.setText("40.0");
                    this.SupplyMaxMATBox.setText("30.0");
                    this.SupplyWattageTBox.setText("1200W");
                      
                    break;
                case "X3481":
                    this.SupplyMaxKVTbox.setText("30.0");
                    this.SupplyMaxMATBox.setText("10.0");
                    this.SupplyWattageTBox.setText("300W");
                    break;
                case "X4911":
                    this.SupplyMaxKVTbox.setText("40.0");
                    this.SupplyMaxMATBox.setText("15.0");
                    this.SupplyWattageTBox.setText("600W");
                    break;
                case "X4313":
                    this.SupplyMaxKVTbox.setText("30.0");
                    this.SupplyMaxMATBox.setText("20.0");
                    this.SupplyWattageTBox.setText("600W");
                    break;
                default:
                    break;
                     
    }
    }
    private void XrayOnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XrayOnButtonActionPerformed
        Double voltage = Double.valueOf(this.VoltageSetTBox.getText());
        Double current = Double.valueOf(this.CurrentSetTBox.getText());
        this.supply.Set_Filament_Limit(Double.valueOf(this.FillCurrTBox.getText()));
        this.supply.Set_Filament_Preheat(Double.valueOf(this.PreHeatTBox.getText()));
        
        if(this.supply.Is_Emmitting()){
            this.supply.Set_Voltage(voltage);
            this.supply.Set_Current(current);
        }else{
            this.supply.Set_Voltage(voltage);
            this.supply.Set_Current(current);
            this.supply.Xray_On();
        }
    }//GEN-LAST:event_XrayOnButtonActionPerformed

    private void XrayOffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XrayOffButtonActionPerformed
        if(this.supply.Is_Emmitting()){
            this.supply.Xray_Off();
        }
    }//GEN-LAST:event_XrayOffButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SupplyScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SupplyScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SupplyScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SupplyScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SupplyScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CurrentLabel;
    private javax.swing.JLabel CurrentReadoutLabel;
    private javax.swing.JTextField CurrentSetTBox;
    private javax.swing.JLabel FilCurrLabel;
    private javax.swing.JTextField FillCurrTBox;
    private javax.swing.JPanel ManualControlPanel;
    private javax.swing.JLabel PreHeatLabel;
    private javax.swing.JTextField PreHeatTBox;
    private javax.swing.JRadioButton RadioButton15min;
    private javax.swing.JRadioButton RadioButton7min;
    private javax.swing.JLabel SupplyMaxKVLabel;
    private javax.swing.JTextField SupplyMaxKVTbox;
    private javax.swing.JTextField SupplyMaxMATBox;
    private javax.swing.JLabel SupplyMaxMaLabel;
    private javax.swing.JPanel SupplySettingsPanel;
    private javax.swing.JLabel SupplyWattageLabel;
    private javax.swing.JTextField SupplyWattageTBox;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JLabel VoltageLabel;
    private javax.swing.JLabel VoltageReadoutLabel;
    private javax.swing.JTextField VoltageSetTBox;
    private javax.swing.JPanel WarmupPanel;
    private javax.swing.ButtonGroup WarmupSelectionButtonGroup;
    private javax.swing.JLabel WarmupTitleLabel;
    private javax.swing.JButton XrayOffButton;
    private javax.swing.JButton XrayOnButton;
    private javax.swing.JPanel XrayOnOffPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel kvLabel;
    private javax.swing.JLabel maLabel;
    // End of variables declaration//GEN-END:variables
}
