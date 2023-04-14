/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicaServer;

import java.awt.*;

/**
 * @author Angel Esquinas
 */
public class Invasor {
    //Se crea imagenes
    public Image imagen1 = null;
    public Image imagen2 = null;

    public int posX = 0;
    public int posY = 0;

    private int anchoPantalla;

    public int vida = 50;

    public Invasor(int _anchoPantalla) {
        anchoPantalla = _anchoPantalla;
    }

    //método para mover a la nave 
    public void mueve(boolean direccion) {
        if (direccion) {
            posX++;
        } else {
            posX--;
        }
    }
}
