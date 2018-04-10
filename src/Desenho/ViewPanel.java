/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Desenho;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 *
 * @author fabio
 */
public class ViewPanel extends JPanel {

    private Automato automato;
    private Shape s;

    public ViewPanel(Automato automato) {
        this.automato = automato;
        this.setOpaque(true);
        this.setBackground(java.awt.Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        s = null;
        
        JScrollBar j1 = new JScrollBar();
        
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        Dimension d = this.getSize(),d1 = this.automato.getDimensao();
        if(d.width <d1.width){
            d.setSize(d1.width, d.height);
        }
        if(d.height <d1.height){
            d.setSize(d.width, d1.height);
        }
        this.setSize(d);
        super.paintComponent(g);
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
        
        ////configuração do rendering para obeter melhor qualidade
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        
        this.automato.draw(g2);
        if(s != null){
            g2.setStroke(new java.awt.BasicStroke(1.0f));
            g2.draw(s);
        }
        
        

    }
    
    

    public void setS(Shape s) {
        this.s = s;
        
    }

    public Shape getS() {
        return s;
    }

    
}
