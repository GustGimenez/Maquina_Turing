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

/**
 *
 * @author fabio
 */
public class Automato {

    private ArrayList<Vertice> vertices;
    private ArrayList<Aresta> arestas;
    private Vertice Inicial; // Mantém o inicial para ser mais facilmente manipulado
    private int numFitas;

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

    public void setNumFitas(int numFitas) {
        this.numFitas = numFitas;
    }

    public int getNumFitas() {
        return this.numFitas;
    }

    //Retona a posição do inicial
    public int getIni() {
        return this.Inicial.getPos();
    }

    // Seta um novo vertice inicial
    public void setInicial(Vertice Inicial) {
        this.Inicial = Inicial;
    }

    // Retona o objeto vertice inicial
    public Vertice getInicial() {
        return Inicial;
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

    // retona a string de uma transição que foi clicada
    public String getStrTrans(Point p) {
        String str;
        for (Aresta a : this.arestas) {
            str = a.editaTransicao(p);
            if (str != null) {
                return str;
            }
        }
        return null;
    }

    //retorna uma aresta que foi clicada
    public Aresta getArestas(Point p) {
        String str;
        for (Aresta a : this.arestas) {
            str = a.editaTransicao(p);
            if (str != null) {
                return a;
            }
        }
        return null;
    }

    // Retorna uma dimensão baseado nos vertices mais distantes em relação a origem
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

    // Normaliza os estados apos uma alteração (remoção de um estado)
    private void setEstados() {
        int i = 0;
        for (Vertice v : vertices) {
            v.setEstado("q" + i++);
            v.setPos(i);
        }
    }

    // Normaliza as posições do vertice de acordo com ArrayList
    public void setPos() {
        int i = 0;
        for (Vertice v : vertices) {
            v.setPos(i++);
        }
    }

    /*
    *@arg estado posição do estado que está sendo selecionado
    Seta estado como selecionado e retorna para que possa ser manipulado
     */
    public Vertice setSelected(int estado) {
        Vertice v = this.vertices.get(estado);
        v.setFocus(true);
        return v;
    }

    // Percorre as transições para recuperar o alfabeto
    public ArrayList<Character> getAlfabeto() {
        ArrayList<Character> alfa = new ArrayList();

        for (Aresta a : arestas) {
            for (String s : a.getTrans()) {
                String[] arrayStr = s.split(";");
                if (!alfa.contains(arrayStr[0].charAt(0))) {
                    alfa.add(arrayStr[0].charAt(0));
                }
                if (!alfa.contains(arrayStr[1].charAt(0))) {
                    alfa.add(arrayStr[1].charAt(0));
                }
            }
        }

        return alfa;
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

    public void verificaLabel(Point p) {
        int yC, yB, xD, xE;
        int largura;
        for (Vertice v : this.vertices) {
            if (v.getLabel() != null) {
                largura = v.getLabel().length() * 5 + 15;
                yC = v.getY() + v.getRaio();
                xD = v.getX() + v.getRaio() + largura / 2;
                yB = v.getY() + v.getRaio() + 15;
                xE = v.getX() - v.getRaio() - largura / 2;

                if (p.getX() >= xE && p.getX() <= xD) {
                    if (p.getY() >= yC && p.getY() <= yB) {
                        v.setLabel(null);
                    }
                }
            }//Verifica se tem label

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

    /*
    Pega as transições de v1 para os outros vertices e copia para v2
    ou seja, para cada transição de v1 para vx, cria uma transição v2 para vx
     */
    private void copiaTrans(Vertice v1, Vertice vc) {
        for (int i = 0; i < this.arestas.size(); i++) {
            Aresta a = this.arestas.get(i);
            if (a.getOrigem().equals(v1)) {
                Aresta a2 = this.addAresta(vc, a.getDestino());
                for (String s : a.getTrans()) {
                    a2.addTransicao(s, null);
                }
            }
        }
    }

    // nome auto explicativo
    private void resetVisita() {
        for (Vertice v : this.vertices) {
            v.setVisitado(false);
        }
    }

    //verifica se existe uma transição vazia saindo de v
    private boolean tranVazia(Vertice v) {
        for (Aresta a : this.arestas) {
            if (a.getOrigem().equals(v)) {
                ArrayList<String> trans = a.getTrans();
                for (String s : trans) {
                    if (s.charAt(0) == '\u25A1') {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // dada uma aresta, verificar qual posição da aresta corresponde a transição vazia
    // retorna -1 em caso não exista tal transição
    private int tranVazia(Aresta a, int i) {
        for (; i < a.getTrans().size(); i++) {
            String c = a.getTrans().get(i);
            if (c.charAt(0) == '\u25A1') {
                return i;

            }
            i++;
        }
        return -1;
    }

    //remove transição vazia de um vertice
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

    // Remove transições vazias (Função de automatos) de todos os vertices
    public void removeVazio() {
        this.resetVisita();
        for (Vertice v : this.vertices) {
            this.removeVazio(v);
        }
    }

    // Cria label de descrição para um vertice
    public void criarLabel(String label, Vertice vertice) {
        for (int i = 0; i < this.vertices.size(); i++) {
            if (this.vertices.get(i).equals(vertice)) {
                this.vertices.get(i).setLabel(label);
            }
        }
    }

}
