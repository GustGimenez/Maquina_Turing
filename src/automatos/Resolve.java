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
import javax.swing.JOptionPane;

/**
 *
 * @author fabio
 */
public class Resolve {

    private final No[] estados;
    private final int numVert;
    private final int inicial;
    private No caminho;
    private final boolean[] terminal;
    private boolean valido;
    private int numIt;
    private int lim;
    private final char VAZIO = '\u25A1';
    private boolean stopExec;
    private int numFitas;
    private String[] result;

    public Resolve(Automato auto) { // Transforma a representação de desenho para uma representação lógica
        this.numVert = auto.getVertices().size();
        this.valido = true;
        this.estados = new No[numVert];
        this.terminal = new boolean[numVert];
        this.inicial = auto.getIni();
        ArrayList<Aresta> arestas = auto.getArestas();
        ArrayList<String> trans;
        int o, d; // o - Origem, d- Destino
        No aux;
        int i = 0;
        for (Vertice v : auto.getVertices()) {
            this.terminal[i++] = v.isFim();
        }
        for (Aresta a : arestas) {
            trans = a.getTrans();
            o = a.getOrigem().getPos();
            if (this.terminal[o]) {
                this.valido = false;
            }
            d = a.getDestino().getPos();
            aux = new No(d, trans);
            aux.setProx(this.estados[o]);
            this.estados[o] = aux;
        }
    }

    public String[] getResult() {
        return result;
    }

    
    public boolean busca(String[] s) { // Verifica validade de uma String (Chamada na interface)
        this.caminho = null;
        this.numIt = 0;
        this.lim = 500;
        this.stopExec = false;
        this.numFitas = s.length;

        boolean aux = busca(s, this.inicial, new int[this.numFitas]);
        this.result = s;
        return aux;

    }

    public boolean isValido() {
        return this.valido;
    }

    public int getNumIt() {
        return this.numIt;
    }

    private boolean busca(String[] str, int vert, int[] pos) { //Executa busca de verificação
        this.numIt++;
        if (numIt > this.lim) {
            int resp = JOptionPane.showConfirmDialog(null, "Numero de iteracoes passou de " + this.lim + ". Deseja continuar?");
            if (resp == JOptionPane.OK_OPTION) {
                this.lim *= 2;
            } else {

                this.stopExec = true;
                return false;

            }
        }
        No aux1 = new No(vert); //Estado Atual
        No aux; // Possivel proximo estado
        char ch;
        StringBuilder strB;
        String[] split;
        ArrayList<String> trans;
        String novoStr;
        String[] sFitas;
        boolean falha;
        ArrayList<Character> direcao = new ArrayList(),escreve = new ArrayList();
        if (this.terminal[vert]) { // Chegou em um estado final
            aux1.setProx(this.caminho);
            this.caminho = aux1;
            return true;
        }
        aux = this.estados[vert];
        while (aux != null) { // Verifica se existe uma transicao valida
            trans = aux.getTransicao();
            for (String sf : trans) {
                sFitas = sf.split("[\u007C]");
                int fitaCount = 0;
                falha = false;
                for (String s : sFitas) {
                    split = s.split("[;]");
                    ch = split[0].replaceAll("&pv", ";").replaceAll("&bv", "|").charAt(0); //split(";")[0] -> valor de leitura 
                    if (ch == str[fitaCount].charAt(pos[fitaCount])) { // Se o caracter de leitura corresponde a posicao inicial
                        fitaCount++;
                    } else {
                        falha = true;
                        break;
                    }
                }
                if (!falha) {
                    //Grava o novo valor
                    fitaCount = 0;
                    for (String s : sFitas) {
                        split = s.split("[;]");
                        direcao.add(split[2].charAt(0));
                        escreve.add(split[1].charAt(0));
                        strB = new StringBuilder(str[fitaCount]);
                        strB.setCharAt(pos[fitaCount], split[1].replaceAll("&pv", ";").replaceAll("&bv", "|").charAt(0));
                        novoStr = strB.toString();
                        if ("L".equals(split[2])) { // Se vai para esquerda
                            if (pos[fitaCount] == 0) { // Se está no inicio da fita
                                novoStr = this.VAZIO + novoStr;
                            } else {
                                pos[fitaCount] = pos[fitaCount] - 1;
                            }
                        } else if ("R".equals(split[2])) { // Se vai pra direita
                            pos[fitaCount] = pos[fitaCount] + 1;
                            if (pos[fitaCount] >= str[fitaCount].length()) {
                                novoStr += this.VAZIO;
                            }
                        }
                        str[fitaCount++] = novoStr;
                    }
                    if (this.busca(str, aux.getEstado(), pos)) { // Se chegou encontrou uma solucao
                        aux1.setDirecao(direcao);
                        aux1.setEscreve(escreve);
                        aux1.setProx(this.caminho);
                        this.caminho = aux1;
                        return true;
                    }
                }
                if (this.stopExec) {
                    return false;
                }
            }

            aux = aux.getProx(); // Testa proxima transicao
        }
        return false; // Caso nenhuma transicao desse estado funcione
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

}
