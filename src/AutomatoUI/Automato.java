/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutomatoUI;

import AutomatoUI.Color.RainbowScale;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 *
 * @author fabio
 */
public class Automato {

    ArrayList<Vertice> vertices;
    ArrayList<Aresta> arestas;
    private Vertice Inicial;

    public Automato() {
        this.vertices = new ArrayList();
        this.arestas = new ArrayList();
    }

    public ArrayList<Vertice> getVertices() {
        return vertices;
    }

    public ArrayList<Aresta> getArestas() {
        return arestas;
    }

    public void addVertice(Vertice v) {

        this.vertices.add(v);
        int num = vertices.size();
        v.setEstado(v.getEstado() + (num - 1));
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
            v.setPos(i);
        }
    }

    public void setPos() {
        int i = 0;
        for (Vertice v : vertices) {
            v.setPos(i++);
        }
    }

    Vertice setSelected(int estado) {
        Vertice v = this.vertices.get(estado);
        v.setFocus(true);
        return v;
    }

    public int getIni() {
        return this.Inicial.getPos();
    }

    public void setInicial(Vertice Inicial) {
        this.Inicial = Inicial;
    }

    public Vertice getInicial() {
        return Inicial;
    }

    public void afnd2afd() {
        this.removeVazio();

        this.setEstados();
        ArrayList<Character> alfabeto = this.getAlfabeto();
        ArrayList<Linha> tabela = new ArrayList();
        ArrayList<Integer> algo = new ArrayList();
        algo.add(this.Inicial.getPos());
        tabela.add(new Linha(Linha.geraID(algo), alfabeto.size(), this.Inicial.isFim()));
        tabela.get(0).setInicial(true);
        int i = 0;
        boolean fim = false;

        String id;
        ArrayList<Integer> vert = null;
        for (i = 0; i < tabela.size(); i++) {
            Linha l = tabela.get(i);
            String aux = l.getId();
            for (int k = 0; k < alfabeto.size(); k++) {
                fim = false;
                boolean add = true;
                vert = new ArrayList();
                for (int j = 0; j < aux.length(); j++) {

                    int origem = Integer.parseInt(Character.toString(aux.charAt(j)));
                    for (Aresta a : arestas) {
                        if (a.getOrigem().getPos() == origem) {
                            ArrayList<Character> trans = a.getTrans();
                            for (Character c : trans) {
                                if (c.equals(alfabeto.get(k))) {
                                    if (a.getDestino().isFim()) {
                                        fim = true;
                                    }
                                    if (!vert.contains(a.getDestino().getPos())) {
                                        vert.add(a.getDestino().getPos());
                                    }
                                }
                            }
                        }
                    }

                }
                if (!vert.isEmpty()) {
                    id = Linha.geraID(vert);
                    for (Linha l2 : tabela) {
                        if (l2.getId().equals(id)) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        tabela.add(new Linha(id, alfabeto.size(), fim));
                    }
                    l.add(id, k);
                }
            }
        }
        for (Linha l : tabela) {
            System.out.println(l.exibe());
        }

        this.vertices.removeAll(this.vertices);
        this.arestas.removeAll(arestas);
        i = 0;
        Hashtable<String, Integer> hash = new Hashtable();
        for (Linha l : tabela) {
            this.addVertice(new Vertice(50 + 100 * i, 100, "q"));
            hash.put(l.getId(), i++);
        }

        for (Linha l : tabela) {
            Vertice v = vertices.get(hash.get(l.getId()));
            v.setFim(l.isFim());
            v.setInicial(l.isInicial());
            String[] strAux = l.getVert();
            for (int j = 0; j < strAux.length; j++) {
                if (strAux[j] != null) {
                    Vertice v2 = this.vertices.get(hash.get(strAux[j]));
                    Aresta a = this.addAresta(v, v2);
                    a.addEstado(alfabeto.get(j));
                }
            }
        }

    }

    public ArrayList<Character> getAlfabeto() {
        ArrayList<Character> alfa = new ArrayList();

        for (Aresta a : arestas) {
            for (Character c : a.getTrans()) {
                if (!alfa.contains(c)) {
                    alfa.add(c);
                }
            }
        }

        return alfa;
    }

    private void copiaTrans(Vertice v1, Vertice vc) {
        for (int i = 0; i < this.arestas.size(); i++) {
            Aresta a = this.arestas.get(i);
            if (a.getOrigem().equals(v1)) {
                Aresta a2 = this.addAresta(vc, a.getDestino());
                for (Character c : a.getTrans()) {
                    a2.addEstado(c.charValue());
                }
            }
        }
    }

    private void resetVisita() {
        for (Vertice v : this.vertices) {
            v.setVisitado(false);
        }
    }

    private boolean tranVazia(Vertice v) {
        for (Aresta a : this.arestas) {
            if (a.getOrigem().equals(v)) {
                ArrayList<Character> trans = a.getTrans();
                for (Character c : trans) {
                    if (c.charValue() == '\u03bb') {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int tranVazia(Aresta a, int i) {
        ArrayList<Character> trans = a.getTrans();

        for (; i < a.getTrans().size(); i++) {
            Character c = a.getTrans().get(i);
            if (c == '\u03bb') {
                return i;

            }
            i++;
        }
        return -1;
    }

    private void removeVazio(Vertice v) {
        if (v.isVisitado()) {
            return;
        }
        v.setVisitado(true);
        if (!this.tranVazia(v)) {
            return;
        }
        int k = 0;
        for (int i = 0; i < this.arestas.size(); i++) {
            Aresta a = this.arestas.get(i);
            if (a.getOrigem().equals(v)) {
                k = this.tranVazia(this.arestas.get(i), k);
                if (k != -1) {
                    removeVazio(a.getDestino());
                    this.copiaTrans(a.getDestino(), v);
                    a.getTrans().remove(k);
                    if (a.getTrans().isEmpty()) {
                        this.arestas.remove(a);
                        i--;
                    }
                    v.setFim(a.getDestino().isFim());
                }
                k = 0;
            }
        }
    }

    public void removeVazio() {
        this.resetVisita();
        for (Vertice v : this.vertices) {
            this.removeVazio(v);
        }
    }

}
