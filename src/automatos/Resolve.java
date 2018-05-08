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
    
    private final String VAZIO = "\u25A1";

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

}
