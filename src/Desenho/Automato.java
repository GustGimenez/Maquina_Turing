/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Desenho;

import Desenho.Color.RainbowScale;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class Automato {

    ArrayList<Vertice> vertices;
    ArrayList<Aresta> arestas;

    public Automato() {
        this.vertices = new ArrayList();
        this.arestas = new ArrayList();
    }

    public void addVertice(Vertice v) {

        this.vertices.add(v);
        int num = vertices.size();
        int passo = 255 / num;
        int i = 0;
        RainbowScale cs = new RainbowScale();

        for (Vertice v1 : vertices) {
            v1.setColor(cs.getColor(i * passo));
            i++;
        }
    }

    public void draw(Graphics2D g) {
        for (Vertice v : vertices) {
            v.desenha(g);
        }

        for (Aresta a : arestas) {
            a.draw(g);
        }

    }

    public Vertice busca(int x, int y) {
        int x1, y1, r;
        r = 20;
        for (Vertice v : vertices) {
            x1 = Math.abs(v.getX() - x);
            y1 = Math.abs(v.getY() - y);

            if (x1 < r && y1 < r) {
                return v;
            }
        }
        return null;
    }

    public java.awt.Dimension getSize() {
        if (this.vertices.size() > 0) {
            float maxX = vertices.get(0).getX();
            float minX = vertices.get(0).getX();
            float maxY = vertices.get(0).getY();
            float minY = vertices.get(0).getY();

            //Encontra o maior e menor valores para X e Y
            for (Vertice v : this.vertices) {
                if (maxX < v.getX()) {
                    maxX = v.getX();
                } else if (minX > v.getX()) {
                    minX = v.getX();
                }

                if (maxY < v.getY()) {
                    maxY = v.getY();
                } else if (minY > v.getY()) {
                    minY = v.getY();
                }
            }

            int w = (int) (maxX + (this.vertices.get(0).getRaio() * 5)) + 350;
            int h = (int) (maxY + (this.vertices.get(0).getRaio() * 5));

            return new java.awt.Dimension(w, h);
        } else {
            return new java.awt.Dimension(500, 500);
        }

    }

    public Aresta addAresta(Vertice v1, Vertice v2) {
        for (Aresta a : arestas) {
            Vertice vo = a.getOrigem();
            Vertice vd = a.getDestino();

            if (v1.equals(vo) && v2.equals(vd)) {
                return a;
            }
        }
        for (Aresta a : arestas) {
            Vertice vo = a.getOrigem();
            Vertice vd = a.getDestino();
            if (v2.equals(vo) && v1.equals(vd)) {
                a.setTipo(2);
                Aresta a1 = new Aresta(v1, v2, 3);
                this.arestas.add(a1);
                return a1;
            }

        }
        Aresta a;
        if (v1.equals(v2)) {
            a = new Aresta(v1, v2, 4);
        } else {
            a = new Aresta(v1, v2, 1);
        }
        this.arestas.add(a);
        return a;
    }

    public Dimension getDimensao() {
        int x = 0, y = 0;
        for (Vertice v : vertices) {
            if (v.getX() > x) {
                x = v.getX();
            }

            if (v.getY() > y) {
                y = v.getY();
            }
        }

        return new Dimension(x + 50, y + 50);
    }

    public void removeTransicao(Point p) {
        for (Aresta a : this.arestas) {
            if (a.excluiTransicao(p)) {
                if (a.getTrans().size() == 0) {
                    if (a.getTipo() == 2 || a.getTipo() == 3) {
                        for (Aresta a1 : arestas) {
                            if (a1.getOrigem().equals(a.getDestino()) && a1.getDestino().equals(a.getOrigem())) {
                                a1.setTipo(1);
                            }
                        }
                    }
                    this.arestas.remove(a);
                }
                return;
            }
        }
    }

    public void removeVertice(Vertice vertice) {
        int num = this.arestas.size();
        Aresta a;
        for (int i = 0; i < num; i++) {
            a = this.arestas.get(i);
            if (a.getOrigem().equals(vertice) || a.getDestino().equals(vertice)) {
                this.arestas.remove(a);
                i--;
                num--;
            }
        }
        this.vertices.remove(vertice);
        setEstados();
    }

    private void setEstados() {
        int i = 0;
        for (Vertice v : vertices) {
            v.setEstado("q" + i++);
        }
    }
}
