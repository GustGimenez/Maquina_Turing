/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatos;

import AutomatoUI.Aresta;
import AutomatoUI.Automato;
import AutomatoUI.Vertice;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author Gustavo
 */
public class Arquivador {

    private Automato automato;

    public Arquivador(Automato automato) {
        this.automato = automato;
    }

    public void salvarAutomato(String nomeArq) {
        ArrayList<Vertice> vertices;
        ArrayList<Aresta> transicoes;

        vertices = this.automato.getVertices();
        transicoes = this.automato.getArestas();

        try {
            PrintWriter file = new PrintWriter(nomeArq, "UTF-8");
            this.escreveCabecalho(file);

            // Escreve cada um dos vértices
            file.println("      <!--The list of states.-->&#13;");
            for (Vertice v : vertices) {
                this.escreveEstado(file, v);
            }

            // Escreve cada um das transições
            file.println("<!--The list of transitions.-->&#13;");
            for (Aresta t : transicoes) {
                this.escreveTransicao(file, t);
            }
            
            // Escreve as máquinas
            file.println("		<!--The list of automata-->&#13;");
            for(Vertice v: vertices){
                file.println("		<Machine" + v.getEstado().charAt(1) + "/>&#13;");
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Método que cria o cabeçalho comum a todos os arquivos de máquina de turing
    public void escreveCabecalho(PrintWriter file) {
        file.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<!--Created with Maquina_Turing--><structure>&#13;");

        file.println("	<type>turing</type>&#13;");
        file.println("	<automaton>&#13;");
    }

    public void escreveEstado(PrintWriter file, Vertice vertice) {
        // Escreve sua identificação e seu nome
        file.println("		<block id=" + vertice.getEstado().substring(1)
                + " name=" + vertice.getEstado() + ">&#13;");

        // Escreve sua tag
        file.println("			<tag>Machine" + vertice.getEstado().substring(1)
                + "</tag>&#13;");

        // Escreve suas posições
        file.println("			<x>" + vertice.getX() + "</x>&#13;");
        file.println("			<y>" + vertice.getY() + "</y>&#13;");

        // Se for inicial, deve ser salvo a tag
        if (vertice.isInicial()) {
            file.println("			<initial/>&#13;");
        }

        // Fechar o bloco do estado
        file.println("		</block>&#13;");
    }

    public void escreveTransicao(PrintWriter file, Aresta t) {
        ArrayList<String> trans = new ArrayList();
        Character ch;
        //trans = t.getTrans();
        for (String aux : trans) {
            // Escreve o começo da trasição
            file.println("		<transition>&#13;");

            // Escreve de qual estado para qual estado ela vai
            file.println("			<from>" + t.getOrigem().getEstado().substring(1) + "</from>&#13;");
            file.println("			<to>" + t.getDestino().getEstado().substring(1) + "</to>&#13;");

            // Símbolo de leitura
            ch = aux.charAt(0);
            if (ch.equals('\u25A1')) {
                file.println("			<read/>&#13;");
            } else {
                file.println("			<read>" + ch + "</read>&#13;");
            }

            // Símbolo de escrita
            ch = aux.charAt(2);
            if (ch.equals('\u25A1')) {
                file.println("			<write/>&#13;");
            } else {
                file.println("			<write>" + ch + "</write>&#13;");
            }

            // Direcionamento
            ch = aux.charAt(4);
            if (ch.equals('R')) {
                file.println("			<move>R</move>&#13;");
            } else {
                file.println("			<move>L</move>&#13;");
            }
            
            // Finaliza escrita da transição
            file.println("		</transition>&#13;");
        }

    }
}
