/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatos;

import AutomatoUI.Aresta;
import AutomatoUI.Automato;
import AutomatoUI.Vertice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 *
 * @author Gustavo
 */
public class FileManager {
    
    public void salvarMT(Automato automato) throws IOException {
        ArrayList<Vertice> vertices;
        ArrayList<Aresta> transicoes;
        JFileChooser escolhedor = new JFileChooser();
        String nomeArq;
        
        escolhedor.setMultiSelectionEnabled(false);
        escolhedor.setDialogTitle("Escolha o local do arquivo");
        escolhedor.setCurrentDirectory(new File("C:\\"));
        
        vertices = automato.getVertices();
        transicoes = automato.getArestas();
        int result = escolhedor.showDialog(null, "Salvar");
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                nomeArq = escolhedor.getSelectedFile().getAbsolutePath();
                nomeArq += ".jff";
                FileWriter aux = new FileWriter(nomeArq);
                PrintWriter file = new PrintWriter(aux);
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
                for (Vertice v : vertices) {
                    file.println("		<Machine" + v.getEstado().charAt(1) + "/>&#13;");
                }
                
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println(ex.getMessage());
            }
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
    
    public void carregaMT(Automato automato) throws ParserConfigurationException, SAXException {
        JFileChooser fc = new JFileChooser("C:\\");
        int result = fc.showOpenDialog(fc);
        
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getAbsolutePath();
            try {
                File file = new File(filename);
                // Abrir o documento 
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                
                // Pega todos os estados e transições, pelo nome da tag
                NodeList estados = doc.getElementsByTagName("block");
                NodeList transicoes = doc.getElementsByTagName("transition");
                
                // Leitura dos estados
                for (int i = 0; i < estados.getLength(); i++) {
                    Node no = estados.item(i);
                    
                    // Itera por todos os itens
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        this.lerEstado(automato, no);
                    }
                }
                
                // Leitura das transições
                for (int i = 0; i < transicoes.getLength(); i++) {
                    Node no = estados.item(i);
                    
                    // Itera por todos os itens
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        this.lerTransicao(automato, no);
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void lerEstado(Automato automato, Node no) {
        Element estado = (Element) no;
        String est;
        int x, y;
        float aux;
        
        // Acessa as tags do objeto
        est = estado.getAttribute("name");
        aux = Float.valueOf(estado.getElementsByTagName("x").item(0).getTextContent());
        x = (int)aux;
        aux = Float.valueOf(estado.getElementsByTagName("y").item(0).getTextContent());
        y = (int)aux;
        
        // Cria um novo vertice (estado) e o adiciona
        automato.addVertice(new Vertice(x, y, est));
    }
    
    private void lerTransicao(Automato automato, Node no) {
        Element transicao = (Element) no;
        Aresta aresta;
        int origem, destino;
        Vertice v1, v2;
        String tran = "";
        char le, escreve, move;
        
        // Lê os vértices de origem e estino
        origem = Integer.valueOf(transicao.getElementsByTagName("from").item(0).
                getTextContent());
        destino = Integer.valueOf(transicao.getElementsByTagName("to").item(0).
                getTextContent());
        
        // Lê o que compõe a transição
        if(transicao.getElementsByTagName("read").item(0).getTextContent().equals("")){
            le = '\u25A1';
        } else {
            le = transicao.getElementsByTagName("read").item(0).getTextContent().charAt(0);
        }
        
        if (transicao.getElementsByTagName("write").item(0).getTextContent().equals("")){
            escreve = '\u25A1';
        } else {
            escreve = transicao.getElementsByTagName("write").item(0).getTextContent().charAt(0);
        }
        
        move = transicao.getElementsByTagName("move").item(0).getTextContent().charAt(0);
        
        // Acessa o vetor de vértices do automato e pega os vértices que compõe a transição;
        v1 = automato.getVertices().get(origem);
        v2 = automato.getVertices().get(destino);

        // Cria a aresta no automato
        aresta = automato.addAresta(v1, v2);
        
        // Adiciona a transição em si
        aresta.addEstado(tran, null);
    }
}
