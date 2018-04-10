/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Desenho.Color;

import java.awt.Color;

/**
 *
 * @author Danilo Medeiros Eler
 */
public abstract class ColorScale {
     public abstract String getName();
     
     public Color getColor(int index){
         if (index > 255) index = 255;
         if (index <= 0) index = 0;
         return colors[index];
     }

    protected java.awt.Color[] colors;
}
