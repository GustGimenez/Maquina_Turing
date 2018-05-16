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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;

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
                file.write("      <!--The list of states.-->&#13;\n");
                for (Vertice v : vertices) {
                    this.escreveEstado(file, v);
                }

                // Escreve cada um das transições
                file.write("<!--The list of transitions.-->&#13;\n");
                for (Aresta t : transicoes) {
                    this.escreveTransicao(file, t);
                }

                // Escreve as máquinas
                file.write("		<!--The list of automata-->&#13;\n");
                for (Vertice v : vertices) {
                    file.write("		<Machine" + v.getEstado().charAt(1) + "/>&#13;\n");
                }
                file.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // Método que cria o cabeçalho comum a todos os arquivos de máquina de turing
    public void escreveCabecalho(PrintWriter file) {
        file.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<!--Created with Maquina_Turing--><structure>&#13;\n");

        file.write("	<type>turing</type>&#13;\n");
        file.write("	<automaton>&#13;\n");
    }

    public void escreveEstado(PrintWriter file, Vertice vertice) {
        // Escreve sua identificação e seu nome
        file.write("		<block id=\"" + vertice.getEstado().substring(1)
                + "\" name=\"" + vertice.getEstado() + "\">&#13;\n");

        // Escreve sua tag
        file.write("			<tag>Machine" + vertice.getEstado().substring(1)
                + "</tag>&#13;\n");

        // Escreve suas posições
        file.write("			<x>" + vertice.getX() + "</x>&#13;\n");
        file.write("			<y>" + vertice.getY() + "</y>&#13;\n");

        // Se for inicial, deve ser salvo a tag
        if (vertice.isInicial()) {
            file.write("			<initial/>&#13;\n");
        }

        // Fechar o bloco do estado
        file.write("		</block>&#13;\n");
    }

    public void escreveTransicao(PrintWriter file, Aresta t) {
        ArrayList<String> trans = new ArrayList();
        Character ch;
        //trans = t.getTrans();
        for (String aux : trans) {
            // Escreve o começo da trasição
            file.write("		<transition>&#13;\n");

            // Escreve de qual estado para qual estado ela vai
            file.write("			<from>" + t.getOrigem().getEstado().substring(1) + "</from>&#13;\n");
            file.write("			<to>" + t.getDestino().getEstado().substring(1) + "</to>&#13;\n");

            // Símbolo de leitura
            ch = aux.charAt(0);
            if (ch.equals('\u25A1')) {
                file.write("			<read/>&#13;\n");
            } else {
                file.write("			<read>" + ch + "</read>&#13;\n");
            }

            // Símbolo de escrita
            ch = aux.charAt(2);
            if (ch.equals('\u25A1')) {
                file.write("			<write/>&#13;\n");
            } else {
                file.write("			<write>" + ch + "</write>&#13;\n");
            }

            // Direcionamento
            ch = aux.charAt(4);
            if (ch.equals('R')) {
                file.write("			<move>R</move>&#13;\n");
            } else {
                file.write("			<move>L</move>&#13;\n");
            }

            // Finaliza escrita da transição
            file.write("		</transition>&#13;\n");
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
                    Node no = transicoes.item(i);

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
        x = (int) aux;
        aux = Float.valueOf(estado.getElementsByTagName("y").item(0).getTextContent());
        y = (int) aux;

        // Cria um novo vertice (estado) e o adiciona
        automato.addVertice(new Vertice(x, y, "q"));
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
        if (transicao.getElementsByTagName("read").item(0).getTextContent().equals("")) {
            le = '\u25A1';
        } else {
            le = transicao.getElementsByTagName("read").item(0).getTextContent().charAt(0);
        }

        if (transicao.getElementsByTagName("write").item(0).getTextContent().equals("")) {
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
        tran = le + ";" + escreve + ";" + move;
        aresta.addTransicao(tran, null);
    }

    public void saveMT(Automato mt) throws TransformerConfigurationException {
        try {
            JFileChooser escolhedor = new JFileChooser();
            String nomeArq;
            escolhedor.setMultiSelectionEnabled(false);
            escolhedor.setDialogTitle("Escolha o local do arquivo");
            escolhedor.setCurrentDirectory(new File("C:\\"));

            int result = escolhedor.showDialog(null, "Salvar");

            if (result == JFileChooser.APPROVE_OPTION) {

                nomeArq = escolhedor.getSelectedFile().getAbsolutePath();
                if (nomeArq.indexOf(".jff") == -1) {
                    nomeArq += ".jff";
                }
                DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFac.newDocumentBuilder();

                //Raiz
                Document doc = docBuilder.newDocument();
                Element raiz = doc.createElement("structure");
                doc.appendChild(raiz);

                //Type
                Element type = doc.createElement("type");
                type.appendChild(doc.createTextNode("turing"));
                raiz.appendChild(type);

                //Automaton block
                Element auto = doc.createElement("automaton");
                raiz.appendChild(auto);

                //Lista de estados
                Element block, tag, x, y, initial, fim;
                ArrayList<Vertice> vertices = mt.getVertices();
                ArrayList<Aresta> arestas = mt.getArestas();
                Comment com;
                com = doc.createComment("The list of states.");
                auto.appendChild(com);
                for (Vertice v : vertices) {
                    block = doc.createElement("block");
                    auto.appendChild(block);

                    block.setAttribute("id", Integer.toString(v.getPos()));
                    block.setAttribute("name", "q" + Integer.toString(v.getPos()));

                    tag = doc.createElement("tag");
                    tag.appendChild(doc.createTextNode("Machine" + v.getPos()));
                    block.appendChild(tag);

                    x = doc.createElement("x");
                    x.appendChild(doc.createTextNode(Float.toString((float) v.getX())));
                    block.appendChild(x);

                    y = doc.createElement("y");
                    y.appendChild(doc.createTextNode(Float.toString((float) v.getY())));
                    block.appendChild(y);

                    if (v.isInicial()) {
                        initial = doc.createElement("initial");
                        block.appendChild(initial);
                    }
                    if (v.isFim()) {
                        fim = doc.createElement("final");
                        block.appendChild(fim);
                    }
                }

                Element transition, from, to, read, write, move;
                ArrayList<String> valores;
                String[] chs;
                com = doc.createComment("The list of transitions.");
                auto.appendChild(com);
                for (Aresta a : arestas) {
                    valores = a.getTrans();
                    for (String s : valores) {
                        chs = s.split(";");
                        transition = doc.createElement("transition");
                        auto.appendChild(transition);

                        from = doc.createElement("from");
                        from.appendChild(doc.createTextNode(Integer.toString(a.getOrigem().getPos())));
                        transition.appendChild(from);

                        to = doc.createElement("to");
                        to.appendChild(doc.createTextNode(Integer.toString(a.getDestino().getPos())));
                        transition.appendChild(to);

                        read = doc.createElement("read");
                        if (!"\u25A1".equals(chs[0])) {
                            read.appendChild(doc.createTextNode(chs[0]));
                        }
                        transition.appendChild(read);

                        write = doc.createElement("write");
                        if (!"\u25A1".equals(chs[1])) {
                            write.appendChild(doc.createTextNode(chs[1]));
                        }
                        transition.appendChild(write);

                        move = doc.createElement("move");
                        move.appendChild(doc.createTextNode(chs[2]));
                        transition.appendChild(move);

                    }
                }
                Element machine;
                com = doc.createComment("The list of automata.");
                auto.appendChild(com);
                for (Vertice v : vertices) {
                    machine = doc.createElement("Machine" + v.getPos());
                    auto.appendChild(machine);
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult resultStream;
                resultStream = new StreamResult(new File(nomeArq));

                // Output to console for testing
                // StreamResult result = new StreamResult(System.out);
                transformer.transform(source, resultStream);
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
