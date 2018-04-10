/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutomatoUI;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author fabio
 */
public class Linha {
    private String id;
    private String[] vert;
    private boolean fim;
    private boolean inicial;
    
    public Linha(String id, int comp,boolean fim){
        vert = new String[comp];
        this.id = id;
        this.fim = fim;
        this.inicial = false;
    }

    public boolean isFim() {
        return fim;
    }

    public void setFim(boolean fim) {
        this.fim = fim;
    }

    public String getId() {
        return id;
    }

    public String[] getVert() {
        return vert;
    }
    
    public void add(String cod, int i){
        this.vert[i] = cod;
    }
    
    public static String geraID(ArrayList<Integer> conj){
        Collections.sort(conj);
        String aux = "";
        for(Integer i: conj){
            aux += i.toString();
        }
        return aux;
    }
    
     public String exibe(){
         String aux = "";
         
         for(int i = 0 ; i < this.vert.length;i++){
             if(this.vert[i]== null){
                 aux += " - |";
             }else{
                 aux += " " + vert[i] + " |";
             }
         }
         
        return this.id + "|" + aux;
    }

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }
     
     
}
