/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConditioningPackage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author mjsh6
 */
public class SetupFrame extends javax.swing.JFrame {

    /**
     * Creates new form SetupFrame
     */
    Boolean UseSupply1 = false;
    String Supply1Address;
    String Supply1Port;
    Boolean UseSupply2 = false;
    String Supply2Address;
    String Supply2Port;
    Boolean UseSupply3 = false;
    String Supply3Address;
    String Supply3Port;
    Boolean UseSupply4 = false;
    String Supply4Address;
    String Supply4Port;
    
    public SetupFrame() {
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

        SupplyPanel = new JPanel();
        Supply1Label = new JLabel();
        Supply1UseCheckbox = new JCheckBox();
        Supply1IPAddressTBox = new JTextField();
        Supply1IPAddressLabel = new JLabel();
        Supply1PortLabel = new JLabel();
        Supply1PortTBox = new JTextField();
        SupplyPanel1 = new JPanel();
        Supply2Label1 = new JLabel();
        Supply2UseCheckbox1 = new JCheckBox();
        Supply2IPAddressTBox = new JTextField();
        Supply2IPAddressLabel2 = new JLabel();
        Supply2PortLabel = new JLabel();
        Supply2PortTBox = new JTextField();
        SupplyPanel2 = new JPanel();
        Supply3Label2 = new JLabel();
        Supply3UseCheckbox2 = new JCheckBox();
        Supply3IPAddressTBox = new JTextField();
        Supply3IPAddressLabel4 = new JLabel();
        Supply3PortLabel = new JLabel();
        Supply3PortTBox = new JTextField();
        SupplyPanel4 = new JPanel();
        Supply4Label4 = new JLabel();
        Supply4UseCheckbox4 = new JCheckBox();
        Supply4IPAddressTBox = new JTextField();
        Supply4IPAddressLabel8 = new JLabel();
        Supply4PortLabel = new JLabel();
        Supply4PortTBox = new JTextField();
        ContinueButton = new JButton();
        LogFileLocationTBox = new JTextField();
        LogFileLocationButton = new JButton();
        SettingFileLocationButton = new JButton();
        SettingFileLocationTBox = new JTextField();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 600));

        SupplyPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        SupplyPanel.setPreferredSize(new Dimension(300, 200));

        Supply1Label.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        Supply1Label.setHorizontalAlignment(SwingConstants.CENTER);
        Supply1Label.setText("Supply 1");

        Supply1UseCheckbox.setSelected(true);
        Supply1UseCheckbox.setText("Use this supply");

        Supply1IPAddressTBox.setText("192.168.1.4");

        Supply1IPAddressLabel.setText("Enter IP Address: ");

        Supply1PortLabel.setText("Enter IP Port: ");

        Supply1PortTBox.setText("50001");

        GroupLayout SupplyPanelLayout = new GroupLayout(SupplyPanel);
        SupplyPanel.setLayout(SupplyPanelLayout);
        SupplyPanelLayout.setHorizontalGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(Supply1Label, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SupplyPanelLayout.createSequentialGroup()
                        .addGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(Supply1UseCheckbox)
                            .addGroup(SupplyPanelLayout.createSequentialGroup()
                                .addComponent(Supply1IPAddressLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply1IPAddressTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                            .addGroup(SupplyPanelLayout.createSequentialGroup()
                                .addComponent(Supply1PortLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply1PortTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 76, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SupplyPanelLayout.setVerticalGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Supply1Label)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Supply1UseCheckbox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply1IPAddressLabel)
                    .addComponent(Supply1IPAddressTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply1PortLabel)
                    .addComponent(Supply1PortTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        SupplyPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        SupplyPanel1.setPreferredSize(new Dimension(300, 200));

        Supply2Label1.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        Supply2Label1.setHorizontalAlignment(SwingConstants.CENTER);
        Supply2Label1.setText("Supply 2");

        Supply2UseCheckbox1.setText("Use this supply");

        Supply2IPAddressTBox.setText("192.168.1.5");

        Supply2IPAddressLabel2.setText("Enter IP Address: ");

        Supply2PortLabel.setText("Enter IP Port: ");

        Supply2PortTBox.setText("50001");

        GroupLayout SupplyPanel1Layout = new GroupLayout(SupplyPanel1);
        SupplyPanel1.setLayout(SupplyPanel1Layout);
        SupplyPanel1Layout.setHorizontalGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(Supply2Label1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SupplyPanel1Layout.createSequentialGroup()
                        .addGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(Supply2UseCheckbox1)
                            .addGroup(SupplyPanel1Layout.createSequentialGroup()
                                .addComponent(Supply2IPAddressLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply2IPAddressTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                            .addGroup(SupplyPanel1Layout.createSequentialGroup()
                                .addComponent(Supply2PortLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply2PortTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 76, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SupplyPanel1Layout.setVerticalGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Supply2Label1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Supply2UseCheckbox1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply2IPAddressLabel2)
                    .addComponent(Supply2IPAddressTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply2PortLabel)
                    .addComponent(Supply2PortTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        SupplyPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        SupplyPanel2.setPreferredSize(new Dimension(300, 200));

        Supply3Label2.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        Supply3Label2.setHorizontalAlignment(SwingConstants.CENTER);
        Supply3Label2.setText("Supply 3");

        Supply3UseCheckbox2.setText("Use this supply");

        Supply3IPAddressTBox.setText("192.168.1.6");

        Supply3IPAddressLabel4.setText("Enter IP Address: ");

        Supply3PortLabel.setText("Enter IP Port: ");

        Supply3PortTBox.setText("50001");

        GroupLayout SupplyPanel2Layout = new GroupLayout(SupplyPanel2);
        SupplyPanel2.setLayout(SupplyPanel2Layout);
        SupplyPanel2Layout.setHorizontalGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(Supply3Label2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SupplyPanel2Layout.createSequentialGroup()
                        .addGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(Supply3UseCheckbox2)
                            .addGroup(SupplyPanel2Layout.createSequentialGroup()
                                .addComponent(Supply3IPAddressLabel4)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply3IPAddressTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                            .addGroup(SupplyPanel2Layout.createSequentialGroup()
                                .addComponent(Supply3PortLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply3PortTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 76, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SupplyPanel2Layout.setVerticalGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Supply3Label2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Supply3UseCheckbox2)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply3IPAddressLabel4)
                    .addComponent(Supply3IPAddressTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply3PortLabel)
                    .addComponent(Supply3PortTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        SupplyPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        SupplyPanel4.setPreferredSize(new Dimension(300, 200));

        Supply4Label4.setFont(new Font("Tahoma", 0, 18)); // NOI18N
        Supply4Label4.setHorizontalAlignment(SwingConstants.CENTER);
        Supply4Label4.setText("Supply 4");

        Supply4UseCheckbox4.setText("Use this supply");

        Supply4IPAddressTBox.setText("192.168.1.7");

        Supply4IPAddressLabel8.setText("Enter IP Address: ");

        Supply4PortLabel.setText("Enter IP Port: ");

        Supply4PortTBox.setText("50001");

        GroupLayout SupplyPanel4Layout = new GroupLayout(SupplyPanel4);
        SupplyPanel4.setLayout(SupplyPanel4Layout);
        SupplyPanel4Layout.setHorizontalGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(Supply4Label4, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SupplyPanel4Layout.createSequentialGroup()
                        .addGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(Supply4UseCheckbox4)
                            .addGroup(SupplyPanel4Layout.createSequentialGroup()
                                .addComponent(Supply4IPAddressLabel8)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply4IPAddressTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
                            .addGroup(SupplyPanel4Layout.createSequentialGroup()
                                .addComponent(Supply4PortLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Supply4PortTBox, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 76, Short.MAX_VALUE)))
                .addContainerGap())
        );
        SupplyPanel4Layout.setVerticalGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(SupplyPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Supply4Label4)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Supply4UseCheckbox4)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply4IPAddressLabel8)
                    .addComponent(Supply4IPAddressTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SupplyPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(Supply4PortLabel)
                    .addComponent(Supply4PortTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        ContinueButton.setText("Continue with these settings");
        ContinueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ContinueButtonActionPerformed(evt);
            }
        });

        LogFileLocationTBox.setEditable(false);

        LogFileLocationButton.setText("Select Log File Storage Location");

        SettingFileLocationButton.setText("Select Setting File Location");

        SettingFileLocationTBox.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ContinueButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SupplyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SupplyPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SupplyPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SupplyPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LogFileLocationTBox, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LogFileLocationButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(SettingFileLocationTBox, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SettingFileLocationButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(SupplyPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(SupplyPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(SupplyPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(SupplyPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(LogFileLocationTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogFileLocationButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(SettingFileLocationTBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(SettingFileLocationButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(ContinueButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ContinueButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_ContinueButtonActionPerformed
        if (Supply1UseCheckbox.isSelected()){
            UseSupply1 = true;
            Supply1Address = Supply1IPAddressTBox.getText();
            Supply1Port = Supply1PortTBox.getText();
        }
        if (Supply2UseCheckbox1.isSelected()){
            UseSupply2 = true;
            Supply2Address = Supply2IPAddressTBox.getText();
            Supply2Port = Supply2PortTBox.getText();
        }
        if (Supply3UseCheckbox2.isSelected()){
            UseSupply3 = true;
            Supply3Address = Supply3IPAddressTBox.getText();
            Supply3Port = Supply3PortTBox.getText();
        }
        if (Supply4UseCheckbox4.isSelected()){
            UseSupply4 = true;
            Supply4Address = Supply4IPAddressTBox.getText();
            Supply4Port = Supply4PortTBox.getText();
        }
        this.WindowMaker();
        this.setVisible(false);
    }//GEN-LAST:event_ContinueButtonActionPerformed

    public void WindowMaker(){
        System.out.println("In WindowMaker");
        JFrame mainWindow = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        if(UseSupply1){
         tabbedPane.add("Supply1",new SupplyScreen(Supply1Address, Supply1Port).getContentPane());
        }
        if(UseSupply2){
         tabbedPane.add("Supply2",new SupplyScreen(Supply2Address, Supply2Port).getContentPane());
        }
        if(UseSupply3){
         tabbedPane.add("Supply3",new SupplyScreen(Supply3Address, Supply3Port).getContentPane());
        }
        if(UseSupply4){
         tabbedPane.add("Supply4",new SupplyScreen(Supply4Address, Supply4Port).getContentPane());
        }
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        
        mainWindow.add(tabbedPane);
        mainWindow.setSize(800,600);
        mainWindow.setVisible(true);
        System.out.println("Launching WindowMaker");
    }
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
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetupFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetupFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton ContinueButton;
    private JButton LogFileLocationButton;
    private JTextField LogFileLocationTBox;
    private JButton SettingFileLocationButton;
    private JTextField SettingFileLocationTBox;
    private JLabel Supply1IPAddressLabel;
    private JTextField Supply1IPAddressTBox;
    private JLabel Supply1Label;
    private JLabel Supply1PortLabel;
    private JTextField Supply1PortTBox;
    private JCheckBox Supply1UseCheckbox;
    private JLabel Supply2IPAddressLabel2;
    private JTextField Supply2IPAddressTBox;
    private JLabel Supply2Label1;
    private JLabel Supply2PortLabel;
    private JTextField Supply2PortTBox;
    private JCheckBox Supply2UseCheckbox1;
    private JLabel Supply3IPAddressLabel4;
    private JTextField Supply3IPAddressTBox;
    private JLabel Supply3Label2;
    private JLabel Supply3PortLabel;
    private JTextField Supply3PortTBox;
    private JCheckBox Supply3UseCheckbox2;
    private JLabel Supply4IPAddressLabel8;
    private JTextField Supply4IPAddressTBox;
    private JLabel Supply4Label4;
    private JLabel Supply4PortLabel;
    private JTextField Supply4PortTBox;
    private JCheckBox Supply4UseCheckbox4;
    private JPanel SupplyPanel;
    private JPanel SupplyPanel1;
    private JPanel SupplyPanel2;
    private JPanel SupplyPanel4;
    // End of variables declaration//GEN-END:variables
}
