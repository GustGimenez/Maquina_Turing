/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatos;

import AutomatoUI.Aresta;
import AutomatoUI.Automato;
import AutomatoUI.Vertice;
import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class Resolve {

    private No[] estados;
    private int numVert;
    private int inicial;
    private No caminho;
    private boolean[] terminal;
    private ArrayList<Integer> vazio;

    public Resolve(Automato auto) { // Transforma a representação de desenho para uma representação lógica
        this.numVert = auto.getVertices().size();
        this.estados = new No[numVert];
        this.terminal = new boolean[numVert];
        this.inicial = auto.getIni();
        ArrayList<Aresta> arestas = auto.getArestas();
        ArrayList<String> trans;
        int o, d;
        No aux;
        for (Aresta a : arestas) {
            trans = a.getTrans();
            o = a.getOrigem().getPos();
            d = a.getDestino().getPos();
            aux = new No(d, trans);
            aux.setProx(this.estados[o]);
            this.estados[o] = aux;

        }
        int i = 0;
        for (Vertice v : auto.getVertices()) {
            this.terminal[i++] = v.isFim();
        }
        this.vazio = new ArrayList();
    }

    public boolean busca(String s) { // Verifica validade de uma String (Chamada na interface)
        this.caminho = null;
        boolean aux = busca(s, this.inicial, 0);
        return aux;

    }

    private boolean busca(String s, int vert, int pos) { //Executa busca de verificação
        No aux1 = new No(vert);

        No aux;
        if (pos == s.length()) {
            if (this.terminal[vert]) {
                aux1.setProx(this.caminho);
                this.caminho = aux1;
            } else {
                aux = this.estados[vert];
                while (aux != null) {
                    for (Character c : aux.getTransicao()) {
                        if (c == '\u25A1') {

                            if (!vazio.contains(aux.getEstado())) {

                                vazio.add(aux.getEstado());
                                if (busca(s, aux.getEstado(), pos)) {
                                    this.caminho.getTransicao().add(c);
                                    aux1.setProx(this.caminho);
                                    this.caminho = (aux1);
                                    return true;
                                }
                            }
                        }
                    }
                    aux = aux.getProx();
                }
            }
            return (this.terminal[vert]);
        }

        char c1 = s.charAt(pos);

        aux = this.estados[vert];
        while (aux != null) {
            for (Character c : aux.getTransicao()) {
                if (c == c1) {
                    this.vazio.removeAll(vazio);//O ERRO
                    if (busca(s, aux.getEstado(), pos + 1)) {
                        this.caminho.getTransicao().add(c);
                        aux1.setProx(this.caminho);
                        this.caminho = (aux1);
                        return true;

                    }
                }
                if (c == '\u25A1') {

                    if (!vazio.contains(aux.getEstado())) {

                        vazio.add(aux.getEstado());
                        if (busca(s, aux.getEstado(), pos)) {
                            this.caminho.getTransicao().add(c);
                            aux1.setProx(this.caminho);
                            this.caminho = (aux1);
                            return true;
                        }
                    }
                }
            }
            aux = aux.getProx();
        }
        return false;
    }

    public void exibeCaminho() { //exibe o caminho percorrido para encontrar a solucao
        No aux = caminho;
        while (aux != null) {
            System.out.print(aux.getEstado() + "<<");
            aux = aux.getProx();
        }
        System.out.println();

    }

    public No getCaminnho() {

        return this.caminho;
    }

    public void arrumaCaminho(No atual) { //Formata a saída da solução
        if (atual.getProx() == null) {
            return;
        }
        try {
            atual.getTransicao().set(0, atual.getProx().getTransicao().get(0));
        } catch (IndexOutOfBoundsException e) {
            atual.getTransicao().add(atual.getProx().getTransicao().get(0));
        }
        arrumaCaminho(atual.getProx());

    }

    public String AF2GR() {
        StringBuilder result = new StringBuilder("G = ({");
        No aux;
        ArrayList<Character> t, alfabeto = new ArrayList();
        for (int i = 0; i < this.numVert; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append("q" + i);

            aux = this.estados[i];
            while (aux != null) {
                t = aux.getTransicao();
                for (Character c : t) {
                    if (!alfabeto.contains(c) && c != '\u25A1') {
                        alfabeto.add(c);
                    }
                }
                aux = aux.getProx();
            }
        }
        result.append("}, {");
        int j = 0;
        for (Character c : alfabeto) {
            result.append(c);
            if (j++ < alfabeto.size() - 1) {
                result.append(", ");
            }
        }
        result.append("}, P, q" + this.inicial + ")\n\n\nP = {\n");

        aux = this.estados[inicial];
        while (aux != null) {
            t = aux.getTransicao();
            for (Character c : t) {
                result.append("q" + inicial + " -> " + c + " q" + aux.getEstado() + "\n");
            }
            aux = aux.getProx();
        }

        for (int i = 0; i < this.numVert; i++) {
            if (i != inicial) {
                aux = this.estados[i];
                while (aux != null) {
                    t = aux.getTransicao();
                    for (Character c : t) {
                        result.append("q" + i + " -> " + c + " q" + aux.getEstado() + "\n");
                    }
                    aux = aux.getProx();
                }
            }
            if (this.terminal[i]) {
                result.append("q" + i + " -> \u25A1 \n");
            }
        }
        result.append("\n}");

        return result.toString();
    }

    public String AF2ER() {
        StringBuilder result = new StringBuilder();
        String expresao = "";
        String vetor[] = new String[100];
        String strAux;

        int cont = 0;
        int k = 0;
        for (int i = 0; i < this.numVert; i++) {
            No aux = this.estados[i];
            cont = 0;
            while (aux != null) {
                ArrayList<Character> transAux = aux.getTransicao();

                if (i == aux.getEstado()) {
                    strAux = "";
                    for (Character c : transAux) {
                        strAux += c;
                    }
                    vetor[k] = "(" + strAux + ")* ";
                    k++;
                    System.out.println("if 1");
                } else if (this.estados[i].getProx() == null) {
                    strAux = "";
                    for (Character c : transAux) {
                        strAux += c;
                    }
                    vetor[k] = "(" + strAux + ")";
                    k++;
                    System.out.println("if 2");
                } else if (cont == 0) {
                    strAux = "";
                    for (Character c : transAux) {
                        strAux += c;
                    }
                    vetor[k] = "" + strAux + "";
                    k++;
                    System.out.println("if 3");
                } else if (aux.getProx() == null) {
                    strAux = "";
                    for (Character c : transAux) {
                        strAux += c;
                    }
                    vetor[k] = "+" + strAux + ")";
                    k++;
                    System.out.println("if 4");
                } else {
                    strAux = "";
                    for (Character c : transAux) {
                        strAux += c;
                    }
                    vetor[k] = "+" + strAux;
                    k++;
                    System.out.println("if 5");
                }
                cont++;
                System.out.println("cont: " + cont);
                aux = aux.getProx();
            }
        }

        for (int i = 0; vetor[i] != null; i++) {
            expresao += vetor[i];
        }
        return expresao;
    }

}
