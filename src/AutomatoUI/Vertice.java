/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutomatoUI;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author fabio
 */
public class Vertice {

    private int x;
    private int y;
    private int raio;
    private String estado;
    private boolean focus;
    private Color cor;
    private boolean inicial;
    private boolean fim;
    
    private int pos;
    private boolean visitado;

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
    
    
    
    public Vertice(int x, int y, String estado) {
        this.raio = 20;
        this.x = x;
        this.y = y;
        this.estado = estado;
        this.focus = false;
        this.inicial = false;
        this.fim = false;

    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public void desenha(Graphics2D g) {
        if (focus) {
            g.setColor(Color.cyan);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillOval(x - raio, y - raio, raio * 2, raio * 2);

        if (fim) {
            g.setStroke(new java.awt.BasicStroke(1.5f));
            g.setColor(cor);
            this.desenhaCirculoBresenham(x, y, x, y+raio, g);
            this.desenhaCirculoBresenham(x, y, x, y+raio-3, g);
        } else {
            g.setStroke(new java.awt.BasicStroke(3f));
            g.setColor(cor);
            g.drawOval(x - raio, y - raio, raio * 2, raio * 2);
        }
        
        if(this.inicial){
            g.setStroke(new java.awt.BasicStroke(2f));
            g.drawLine(x-raio, y, x-2*raio, y+raio);
            g.drawLine(x-raio, y, x-2*raio, y-raio);
            g.drawLine(x-2*raio, y+raio, x-2*raio, y-raio);
        }
        g.drawString(estado, x - 4, y+4);
    }

    public void setColor(Color c) {
        this.cor = c;
    }

    Color getColor() {
        return this.cor;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public void setFim(boolean fim) {
        this.fim = fim;
    }

    public boolean isFim() {
        return fim;
    }
    
    public boolean isInicial(){
        return this.inicial;
    }

    public void desenhaCirculoBresenham(int x1, int y1, int x2, int y2, Graphics2D g) {
        int r, x, y, d, dE, dSE;
        r = (int) Math.round(Math.sqrt(Math.pow((double) (x2 - x1), 2) + Math.pow((double) (y2 - y1), 2)));
        x = 0;
        y = r;
        d = 1 - r;
        dE = 3;
        dSE = -2 * y + 5;

        simetria8(x, y, g, x1, y1);
        while (x <= y) {
            if (d > 0) {
                d += dSE;
                dSE += 4;
                y--;
            } else {
                d += dE;
                dSE += 2;
            }
            x++;
            dE += 2;
            simetria8(x, y, g, x1, y1);
        }

    }

    private void simetria8(int x, int y, Graphics2D g, int xc, int yc) {
        float contorno = 2f;

        g.fillRect(x + xc, y + yc, (int) contorno, (int) contorno);
        g.fillRect(-x + xc, y + yc, (int) contorno, (int) contorno);
        g.fillRect(x + xc, -y + yc, (int) contorno, (int) contorno);
        g.fillRect(-x + xc, -y + yc, (int) contorno, (int) contorno);
        g.fillRect(y + xc, x + yc, (int) contorno, (int) contorno);
        g.fillRect(-y + xc, x + yc, (int) contorno, (int) contorno);
        g.fillRect(y + xc, -x + yc, (int) contorno, (int) contorno);
        g.fillRect(-y + xc, -x + yc, (int) contorno, (int) contorno);

    }
}
