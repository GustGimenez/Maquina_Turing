/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lfaproj1;

/**
 *
 * @author fabio
 */
public class Regex {
    private String ER;
    private boolean valido;
    private int erros;

    public Regex() {
        this.ER = "";
        this.valido = false;
        this.erros = 0;
    }
    
    public int verificaER(String aux){
        int l = aux.length() - 1;
        this.erros = 0;
        char c;
        StringBuilder strAux = new StringBuilder();
        if (l < 0) {
            this.erros++;
            this.valido = false;
            return this.erros;
        }
        if (aux.charAt(0) != '^' || aux.charAt(l) != '$') {
            this.erros++;
            valido = false;
        } else {
            
            for (int i = 1; i < l; i++) {
                c = aux.charAt(i);
                strAux.append(c);
                if (!Character.isAlphabetic(c) && !Character.isDigit(c) && c != '*' && c != '+' && c != '|' && c != '(' && c != ')') {
                    this.erros++;
                }
            }
            if (this.erros == 0) {
                this.valido = true;
            }else {
                this.valido = false;
            }
        }   this.ER = strAux.toString();
        return this.erros;
    }
    
    
    public boolean avaliaExpr(String aux){
        if(this.valido){
            return aux.matches(ER);
        }else return false;
    }
}
