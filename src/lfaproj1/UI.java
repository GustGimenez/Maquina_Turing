/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfaproj1;

import Principal.UIPrincipal;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author fabio
 */
public class UI extends javax.swing.JFrame {

    private Regex reg;

    public UI() {
        initComponents();
        reg = new Regex();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textER = new javax.swing.JTextField();
        labelER = new javax.swing.JLabel();
        textEntrada1 = new javax.swing.JTextField();
        labelString1 = new javax.swing.JLabel();
        labelString2 = new javax.swing.JLabel();
        textEntrada2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        textER.setText("^$");
        textER.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textERFocusLost(evt);
            }
        });
        textER.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textERInputMethodTextChanged(evt);
            }
        });
        textER.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textERKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textERKeyTyped(evt);
            }
        });

        labelER.setText("Expressão Regular");

        textEntrada1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEntrada1KeyReleased(evt);
            }
        });

        labelString1.setText("String");

        labelString2.setText("String");

        textEntrada2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEntrada2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textER)
                    .addComponent(textEntrada1)
                    .addComponent(textEntrada2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelER)
                            .addComponent(labelString1)
                            .addComponent(labelString2))
                        .addGap(0, 290, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelER)
                .addGap(5, 5, 5)
                .addComponent(textER, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(labelString1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textEntrada1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelString2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textEntrada2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textERInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textERInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_textERInputMethodTextChanged

    private void textERKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textERKeyTyped
        this.textEntrada1.setBackground(Color.WHITE);
        this.textEntrada2.setBackground(Color.WHITE);
    }//GEN-LAST:event_textERKeyTyped

    private void textERKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textERKeyReleased
        String aux = this.textER.getText();
        int e = reg.verificaER(aux);
        if (e > 0) {
            this.textER.setBackground(Color.RED);
        } else {
            this.textER.setBackground(Color.WHITE);
        }


    }//GEN-LAST:event_textERKeyReleased

    private void textEntrada1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEntrada1KeyReleased
        String aux = this.textEntrada1.getText();

        if (reg.avaliaExpr(aux)) {
            this.textEntrada1.setBackground(Color.GREEN);
        } else {
            this.textEntrada1.setBackground(Color.RED);
        }
    }//GEN-LAST:event_textEntrada1KeyReleased

    private void textERFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textERFocusLost
        String aux1 = this.textEntrada1.getText();
        String aux2 = this.textEntrada2.getText();
        if (!aux1.isEmpty()) {
            if (reg.avaliaExpr(aux1)) {
                this.textEntrada1.setBackground(Color.GREEN);
            } else {
                this.textEntrada1.setBackground(Color.RED);
            }
        }

        if (!aux2.isEmpty()) {
            if (reg.avaliaExpr(aux2)) {
                this.textEntrada2.setBackground(Color.GREEN);
            } else {
                this.textEntrada2.setBackground(Color.RED);
            }
        }
    }//GEN-LAST:event_textERFocusLost

    private void textEntrada2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEntrada2KeyReleased
        String aux = this.textEntrada2.getText();

        if (reg.avaliaExpr(aux)) {
            this.textEntrada2.setBackground(Color.GREEN);
        } else {
            this.textEntrada2.setBackground(Color.RED);
        }
    }//GEN-LAST:event_textEntrada2KeyReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIPrincipal().setVisible(true);
            }
        });
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelER;
    private javax.swing.JLabel labelString1;
    private javax.swing.JLabel labelString2;
    private javax.swing.JTextField textER;
    private javax.swing.JTextField textEntrada1;
    private javax.swing.JTextField textEntrada2;
    // End of variables declaration//GEN-END:variables
}
