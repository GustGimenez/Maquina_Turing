/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutomatoUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.awt.geom.QuadCurve2D;

/**
 *
 * @author fabio
 */
public class Aresta {

    private Vertice origem;
    private Vertice destino;
    private ArrayList<Character> trans;
    private int tipo;

    public Aresta(Vertice o, Vertice d, int tipo) {
        this.origem = o;
        this.destino = d;
        this.tipo = tipo;
        this.trans = new ArrayList();

    }

    public void draw(Graphics2D g) {
        int des;
        g.setStroke(new java.awt.BasicStroke(2.0f));
        g.setColor(this.origem.getColor());
        int r = this.origem.getRaio();
        Point po = new Point(this.origem.getX(), this.origem.getY());
        Point pd = new Point(this.destino.getX(), this.destino.getY());

        Point p1 = calculaAresta(po, pd, r);
        Point p2 = calculaAresta(pd, po, r);
        int x = (po.x + pd.x) / 2;
        int y = (po.y + pd.y) / 2;
        switch (this.tipo) {
            case 1:
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                des = -12;
                g.setStroke(new java.awt.BasicStroke(1f));
                for (Character c : trans) {
                    g.drawString(c.toString(), x, y + des);
                    des -= 13;
                }
                break;
            case 2: {
                QuadCurve2D q = new QuadCurve2D.Float((float) p1.x, (float) p1.y, (float) (p1.x + p2.x) / 2f, (float) (p1.y + p2.y) / 2f - 30, (float) p2.x, (float) p2.y);
                g.draw(q);
                des = -22;
                g.setStroke(new java.awt.BasicStroke(1f));
                for (Character c : trans) {
                    g.drawString(c.toString(), x, y + des);
                    des -= 13;
                }
                break;
            }
            case 4: {
                p2 = new Point((int) (Math.cos(1.04) * this.origem.getRaio() + this.origem.getX()), (int) (Math.sin(4.18) * this.origem.getRaio() + this.origem.getY()));
                p1 = new Point((int) (Math.cos(2.08) * this.origem.getRaio() + this.origem.getX()), (int) (Math.sin(5.93) * this.origem.getRaio() + this.origem.getY()) - 13);
                QuadCurve2D q = new QuadCurve2D.Float((float) p1.x, (float) p1.y, (float) this.origem.getX(), (float) this.destino.getY() - 100, (float) p2.x, (float) p2.y);
                g.draw(q);
                des = -65;
                g.drawLine(p2.x, p2.y, p2.x + 6, p2.y - 8);
                g.drawLine(p2.x, p2.y, p2.x - 8, p2.y - 7);
                g.setStroke(new java.awt.BasicStroke(1f));
                for (Character c : trans) {
                    int yAux = this.origem.getY() + des;
                    g.drawString(c.toString(), po.x - 4, yAux);
                    des -= 13;
                }
                break;
            }
            default: {
                QuadCurve2D q = new QuadCurve2D.Float((float) p1.x, (float) p1.y, (float) (p1.x + p2.x) / 2f, ((float) (p1.y + p2.y) / 2f) + 30, (float) p2.x, (float) p2.y);
                g.draw(q);
                des = 32;
                g.setStroke(new java.awt.BasicStroke(1f));
                for (Character c : trans) {
                    g.drawString(c.toString(), x, y + des);
                    des += 13;
                }
                break;
            }
        }

        this.drawArrowNew(g, po, pd, 6, r);

    }

    private void drawArrowNew(Graphics2D g2, Point s, Point t, int size, int deslocamento) {
        float r = (float) Math.sqrt(Math.pow(s.x - t.x, 2) + Math.pow(s.y - t.y, 2));

        float cos = (t.x - s.x) / r;
        float sen = (t.y - s.y) / r;
        int xAB = size + deslocamento;
        int yA = size;
        int yB = -size;

        Point pc = new Point(Math.round(deslocamento * -cos) + t.x, Math.round(deslocamento * -sen) + t.y);
        Point pa = new Point(Math.round(xAB * -cos - yA * -sen) + t.x, Math.round(xAB * -sen + yA * -cos) + t.y);
        Point pb = new Point(Math.round(xAB * -cos - yB * -sen) + t.x, Math.round(xAB * -sen + yB * -cos) + t.y);

        g2.drawLine(pc.x, pc.y, pa.x, pa.y);
        g2.drawLine(pc.x, pc.y, pb.x, pb.y);
    }

    private Point calculaAresta(Point s, Point t, int deslocamento) {
        float r = (float) Math.sqrt(Math.pow(s.x - t.x, 2) + Math.pow(s.y - t.y, 2));

        float cos = (t.x - s.x) / r;
        float sen = (t.y - s.y) / r;
        return new Point(Math.round(deslocamento * -cos) + t.x, Math.round(deslocamento * -sen) + t.y);

    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public ArrayList<Character> getTrans() {
        return trans;
    }

    public int getTipo() {
        return this.tipo;
    }

    public void addEstado(Character aux) {
        if (!this.trans.contains(aux)) {
            this.trans.add(aux);
        }
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean excluiTransicao(Point p) {
        int max = this.trans.size();
        Point po = new Point(this.origem.getX(), this.origem.getY());
        Point pd = new Point(this.destino.getX(), this.destino.getY());
        int offset = 0;
        int mult = -1;
        switch (this.tipo) {
            case 1:
                offset = -12;
                break;
            case 2:
                offset = -22;
                break;
            case 3:
                offset = 32;
                mult = 1;
                break;
            case 4:
                offset = -65;
                break;

        }
        int y = (po.y + pd.y) / 2 - 10 + offset;
        int x = (po.x + pd.x) / 2 - 2;
        for (int i = 0; i < max; i++) {
            if (p.x > x && p.x < x + 10 && p.y > y && p.y < y + 12) {
                this.trans.remove(i);
                break;
            }
            y += 13 * mult;
        }

        return (max > this.trans.size());
    }

}
