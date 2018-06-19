/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatos;

import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class No {
    private int estado;
    private ArrayList<String> transicao;
    private ArrayList<Character> direcao;
    private boolean terminal;
    private No prox;
    private ArrayList<Character> escreve;

    public ArrayList<Character> getEscreve() {
        return escreve;
    }

    public void setEscreve(ArrayList<Character> escreve) {
        this.escreve = escreve;
    }
    
    
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ArrayList<String> getTransicao() {
        return transicao;
    }

    public void setTransicao(ArrayList transicao) {
        this.transicao = transicao;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }

    public ArrayList getDirecao() {
        return direcao;
    }

    public void setDirecao(ArrayList direcao) {
        this.direcao = direcao;
    }
    
    

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }
    
    

    public No(int estado, ArrayList transicao) {
        this.estado = estado;
        this.transicao = transicao;
        this.prox = null;
    }
    
   public No(int estado){
       this.estado = estado;
       this.transicao = new ArrayList();
   }
    
   
}
