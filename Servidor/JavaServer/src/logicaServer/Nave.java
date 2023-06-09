package logicaServer;

import java.awt.*;

/*
 * Clase Nave:
 * Atributos
 * posX
 * posY
 * imagen
 * pulsadoIzquierda
 * pulsadoDerecha
 * */
public class Nave {
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;

    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;


    public Nave() {

    }

    public void mueve() {
        if (pulsadoIzquierda && posX > 0) {
            posX -= 3;
        }
        if (pulsadoDerecha && posX
                < Principal.ANCHOPANTALLA - imagen.getWidth(null)) {
            posX += 3;
        }
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {
        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoDerecha = false;
    }

    public boolean isPulsadoDerecha() {
        return pulsadoDerecha;
    }

    public void setPulsadoDerecha(boolean pulsadoDerecha) {
        this.pulsadoDerecha = pulsadoDerecha;
        this.pulsadoIzquierda = false;
    }
}




