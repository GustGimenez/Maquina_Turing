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
                int numFitas = Integer.valueOf(doc.getElementsByTagName("tapes").item(0).getTextContent());
                automato.setNumFitas(numFitas);

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
        Element ini, fim;
        int x, y;
        float aux;

        // Acessa as tags do objeto
        aux = Float.valueOf(estado.getElementsByTagName("x").item(0).getTextContent());
        x = (int) aux;
        aux = Float.valueOf(estado.getElementsByTagName("y").item(0).getTextContent());
        y = (int) aux;

        // Cria um novo vertice (estado) e o adiciona
        ini = (Element) estado.getElementsByTagName("initial").item(0);
        fim = (Element) estado.getElementsByTagName("final").item(0);
        Vertice vert = new Vertice(x, y, "q");

        vert.setInicial(ini != null);
        if (ini != null) {
            automato.setInicial(vert);
        }
        vert.setFim(fim != null);
        automato.addVertice(vert);
    }

    private void lerTransicao(Automato automato, Node no) {
        Element transicao = (Element) no;
        Aresta aresta;
        int origem, destino;
        Vertice v1, v2;
        int numFitas = 5;
        int fita;
        numFitas = automato.getNumFitas();
        String[] tran = new String[numFitas];

        // Lê os vértices de origem e destino
        origem = Integer.valueOf(transicao.getElementsByTagName("from").item(0).
                getTextContent());
        destino = Integer.valueOf(transicao.getElementsByTagName("to").item(0).
                getTextContent());

        // Lê o que compõe a transição
        for (int i = 0; i < numFitas; i++) {

            fita = Integer.valueOf(transicao.getElementsByTagName("read").
                    item(i).getAttributes().getNamedItem("tape").getTextContent()) - 1;
            tran[fita] = "";
            if (transicao.getElementsByTagName("read").item(i).getTextContent().equals("")) {
                tran[fita] += '\u25A1';
            } else if (transicao.getElementsByTagName("read").item(i).getTextContent().equals(";")) {
                tran[fita] += "&pv";
            } else if (transicao.getElementsByTagName("read").item(i).getTextContent().equals("|")) {
                tran[fita] += "$bv";
            } else {
                tran[fita] += transicao.getElementsByTagName("read").item(i).
                        getTextContent().charAt(0);
            }

            tran[fita] += ";";

            if (transicao.getElementsByTagName("write").item(i).getTextContent().equals("")) {
                tran[fita] += '\u25A1';
            } else if (transicao.getElementsByTagName("write").item(i).getTextContent().equals(";")) {
                tran[fita] += "&pv";
            } else if (transicao.getElementsByTagName("write").item(i).getTextContent().equals("|")) {
                tran[fita] += "$bv";
            } else {
                tran[fita] += transicao.getElementsByTagName("write").item(i).
                        getTextContent().charAt(0);
            }

            tran[fita] += ";";

            tran[fita] += transicao.getElementsByTagName("move").item(0).
                    getTextContent().charAt(0);

            tran[fita] += "|";
        }
        tran[numFitas - 1] = tran[numFitas - 1].substring(0, 5);

        // Acessa o vetor de vértices do automato e pega os vértices que compõe a transição;
        v1 = automato.getVertices().get(origem);
        v2 = automato.getVertices().get(destino);

        // Cria a aresta no automato
        aresta = automato.addAresta(v1, v2);
        String tranFinal = "";
        for (int i = 0; i < numFitas; i++) {
            tranFinal += tran[i];
        }

        aresta.addTransicao(tranFinal, null);
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

                //Type
                Element tapes = doc.createElement("tapes");
//                    tapes.appendChild(doc.createTextNode(mt.getNumFitas()));
                raiz.appendChild(tapes);

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
                ArrayList<String> transicoes;
                String[] chs;
                String aux;
//                int numFitas = mt.getNumFitas();
                int numFitas = 5;

                com = doc.createComment("The list of transitions.");
                auto.appendChild(com);
                for (Aresta a : arestas) {
                    transicoes = a.getTrans();

                    for (String s : transicoes) {
                        // Split não funciona com '|', então, troca pra '/' e depois volta pra '|'
                        s = s.replace('|', '/');
                        chs = s.split("/");
                        s = s.replace('/', '|');

                        transition = doc.createElement("transition");
                        auto.appendChild(transition);

                        from = doc.createElement("from");
                        from.appendChild(doc.createTextNode(Integer.toString(a.getOrigem().getPos())));
                        transition.appendChild(from);

                        to = doc.createElement("to");
                        to.appendChild(doc.createTextNode(Integer.toString(a.getDestino().getPos())));
                        transition.appendChild(to);

                        for (int i = 0; i < numFitas; i++) {
                            aux = chs[i];

                            read = doc.createElement("read");
                            read.setAttribute("tape", String.valueOf(i + 1));
                            if (aux.charAt(0) != '\u25A1') {
                                read.appendChild(doc.createTextNode(String.valueOf(aux.charAt(0))));
                            }
                            transition.appendChild(read);

                            write = doc.createElement("write");
                            write.setAttribute("tape", String.valueOf(i + 1));
                            if (aux.charAt(2) != '\u25A1') {
                                write.appendChild(doc.createTextNode(String.valueOf(aux.charAt(2))));
                            }
                            transition.appendChild(write);

                            move = doc.createElement("move");
                            move.setAttribute("tape", String.valueOf(i + 1));
                            move.appendChild(doc.createTextNode(String.valueOf(aux.charAt(4))));
                            transition.appendChild(move);
                            
                        }
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
