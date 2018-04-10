/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gramatica;

import AutomatoUI.Aresta;
import AutomatoUI.Automato;
import AutomatoUI.Vertice;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Diovanni
 */
public class Gramatica {

    private No[] estados;
    private int numVert;
    private int inicial;
    private boolean[] terminal;
    private ArrayList<Character> nTerminais;
    private ArrayList<Integer> vazio;

    public Gramatica(ArrayList<String> simbs) {
        this.nTerminais = new ArrayList();
        char ch;
        String strAux;
        String strAux2;
        int aux;
        for (int i = 0; i < simbs.size(); i++) {
            strAux = simbs.get(i);
            if (i % 2 == 0) {
                ch = strAux.charAt(0);
            } else if (strAux.length() == 2) {
                ch = strAux.charAt(1);
            } else {
                continue;
            }
            if (!this.nTerminais.contains(ch)) {
                this.nTerminais.add(ch);
            }
        }

        this.numVert = this.nTerminais.size() + 1;
        this.estados = new No[this.numVert];
        No novo;
        int aux2;
        for (int i = 1; i < simbs.size(); i += 2) {
            strAux = simbs.get(i);
            strAux2 = simbs.get(i - 1);
            aux = this.nTerminais.indexOf(strAux2.charAt(0));
            if (strAux.charAt(0) == '\u03bb' || strAux.length() == 1) {
                novo = new No(this.numVert - 1);

            } else {
                aux2 = this.nTerminais.indexOf(strAux.charAt(1));
                novo = new No(aux2);
            }
            novo.getTransicao().add(strAux.charAt(0));
            novo.setProx(this.estados[aux]);
            this.estados[aux] = novo;
        }
        this.terminal = new boolean[numVert];
        this.terminal[numVert - 1] = true;
        this.vazio = new ArrayList();
    }

    public boolean busca(String s) { // Verifica validade de uma String (Chamada na interface)
        boolean aux = busca(s, this.inicial, 0);
        return aux;

    }

    private boolean busca(String s, int vert, int pos) { //Executa busca de verificação
        if (pos == s.length()) {
            No verifica = this.estados[vert];
            while (verifica != null) {
                if (verifica.getTransicao().contains('\u03bb')) {
                    return true;
                }
                verifica = verifica.getProx();
            }
            return (this.terminal[vert]);
        }
        No aux;

        char c1 = s.charAt(pos);

        aux = this.estados[vert];
        while (aux != null) {
            for (Character c : aux.getTransicao()) {
                if (c == c1) {
                    this.vazio.removeAll(vazio);//O ERRO
                    if (busca(s, aux.getEstado(), pos + 1)) {
                        return true;

                    }
                }
                if (c == '\u03bb') {

                    vazio.add(aux.getEstado());
                    if (busca(s, aux.getEstado(), pos)) {
                        return true;
                    }

                }
            }
            aux = aux.getProx();
        }
        return false;
    }

    public Automato gr2af() {
        Automato auto = new Automato();
        Vertice v = null;
        No aux;
        for (int i = 0; i < this.estados.length; i++) {
            v = new Vertice(100, 100 + 50 * i, "q");
            auto.addVertice(v);
        }
        if (v != null) {
            v.setFim(true);
        }
        
        for (int i = 0; i < this.estados.length; i++) {
            aux = this.estados[i];
            while(aux != null){
                for(Character c: aux.getTransicao()){
                    Aresta a = auto.addAresta(auto.getVertices().get(i), auto.getVertices().get(aux.getEstado()));
                    a.addEstado(c);
                }
                aux = aux.getProx();
            }
        }
        auto.getVertices().get(0).setInicial(true);
        auto.setInicial(auto.getVertices().get(0));
        
        return auto;
    }
}
