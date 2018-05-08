/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutomatoUI;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author fabio
 */
public class Vertice {

    private int x; //coordenadas do desenho
    private int y; //coordenadas do desenho
    private int raio; // dimensao do circulo
    private String estado; // oque será escrito no centro do circulo
    private boolean focus; // desenha com cor diferente
    private Color cor;  // cor padrão do contorno
    private boolean inicial; // apenas um inicial
    private boolean fim;
    private String label;   // Atributo que contém a descrição do label

    private int pos; // posição no ArrayList da maquina
    private boolean visitado; // auxiliar

    // Gets e Sets e construtor
    public Vertice(int x, int y, String estado) {
        this.raio = 20;
        this.x = x;
        this.y = y;
        this.estado = estado;
        this.focus = false;
        this.inicial = false;
        this.fim = false;
        this.label = null;
    }
    
    

    public boolean isVisitado() {
        return visitado;
    }

    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRaio() {
        return raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public void setColor(Color c) {
        this.cor = c;
    }

    public Color getColor() {
        return this.cor;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public void setFim(boolean fim) {
        this.fim = fim;
    }

    public boolean isFim() {
        return fim;
    }

    public boolean isInicial() {
        return this.inicial;
    }

        public void setLabel(String label) {
        this.label = label;
    }
    
    public String getLabel(){
        return this.label;
    }
    
    //Funções de desenho e manipulação
    //Desenho
    public void desenha(Graphics2D g) {
        if (this.focus) { // cor se está sendo focado
            g.setColor(Color.cyan);
        } else { // cor padrão do circulo interno
            g.setColor(Color.yellow);
        }
        g.fillOval(x - raio, y - raio, raio * 2, raio * 2);

        g.setColor(this.cor); // cor da borda
        if (this.fim) { // de é final, possui um circulo interno
            g.setStroke(new java.awt.BasicStroke(1.5f));
            this.desenhaCirculoBresenham(x, y, x, y + raio, g);
            this.desenhaCirculoBresenham(x, y, x, y + raio - 3, g);
        } else {// borda simples
            g.setStroke(new java.awt.BasicStroke(3f));
            g.drawOval(x - raio, y - raio, raio * 2, raio * 2);
        }

        if (this.label != null) {
            this.desenhaLabel(g);
        }

        if (this.inicial) { // Se é inicial, desenha um triangulo ao lado do vertice
            g.setStroke(new java.awt.BasicStroke(2f));
            g.drawLine(this.x - this.raio, this.y, this.x - 2 * this.raio, this.y + this.raio);
            g.drawLine(this.x - this.raio, this.y, this.x - 2 * this.raio, this.y - this.raio);
            g.drawLine(this.x - 2 * this.raio, this.y + this.raio, this.x - 2 * this.raio, this.y - this.raio);
        }
        g.drawString(this.estado, this.x - 4, this.y + 4);

    }
    
    
    // Funçao com parametros mais simples para desenhar circulos 
    public void desenhaCirculoBresenham(int x1, int y1, int x2, int y2, Graphics2D g) {
        int r, x, y, d, dE, dSE;
        r = (int) Math.round(Math.sqrt(Math.pow((double) (x2 - x1), 2) + Math.pow((double) (y2 - y1), 2)));
        x = 0;
        y = r;
        d = 1 - r;
        dE = 3;
        dSE = -2 * y + 5;

        simetria8(x, y, g, x1, y1);
        while (x <= y) {
            if (d > 0) {
                d += dSE;
                dSE += 4;
                y--;
            } else {
                d += dE;
                dSE += 2;
            }
            x++;
            dE += 2;
            simetria8(x, y, g, x1, y1);
        }

    }
    
    // Funcao auxiliar ao DesenhaCirculoBresenham
    private void simetria8(int x, int y, Graphics2D g, int xc, int yc) {
        float contorno = 2f;

        g.fillRect(x + xc, y + yc, (int) contorno, (int) contorno);
        g.fillRect(-x + xc, y + yc, (int) contorno, (int) contorno);
        g.fillRect(x + xc, -y + yc, (int) contorno, (int) contorno);
        g.fillRect(-x + xc, -y + yc, (int) contorno, (int) contorno);
        g.fillRect(y + xc, x + yc, (int) contorno, (int) contorno);
        g.fillRect(-y + xc, x + yc, (int) contorno, (int) contorno);
        g.fillRect(y + xc, -x + yc, (int) contorno, (int) contorno);
        g.fillRect(-y + xc, -x + yc, (int) contorno, (int) contorno);

    }

    //Desenha label abaixo do vertice
    public void desenhaLabel(Graphics2D g) {
        int largura = 15;
        largura += 5 * this.label.length();

        // Contornos do label
        g.setColor(Color.BLACK);
        g.drawLine(this.x - raio - largura / 2, this.y + raio, this.x + raio + largura / 2, this.y + raio);     // Linha de cima na horizontal
        g.drawLine(this.x - raio - largura / 2, this.y + raio + 15, this.x + raio + largura / 2, this.y + raio + 15); // Linha de baixo na horizontal
        g.drawLine(this.x - raio - largura / 2, this.y + raio, this.x - raio - largura / 2, this.y + raio + 15);    // Linha mais a esquerda na vertical
        g.drawLine(this.x + raio + largura / 2, this.y + raio, this.x + raio + largura / 2, this.y + raio + 15);  // Linha mais a direita na vertical

        // Pintando o label
        int larguraAux = (this.x + raio + largura / 2) - (this.x - raio - largura / 2);
        g.setColor(Color.yellow);
        g.fillRect(this.x - raio - largura / 2, this.y + raio, larguraAux + 1, 16);

        // Escrevendo o valor do label
        g.setColor(Color.black);
        g.drawString(this.label, this.x - (this.label.length() * 2), this.y + raio + 12);
        g.setColor(this.cor);
    }
}
