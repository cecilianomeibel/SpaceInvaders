package logicaServer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;

public class Colision {
    Image imagen1 = null;
    Image imagen2 = null;

    public int posX = 0;
    public int posY = 0;

    public int tiempoDeVida = 50;

    Clip sonidoExplosion;

    public Colision() {
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(
                    AudioSystem.getAudioInputStream(
                            getClass().getResource("/sonidos/explosion.wav"))
            );
        } catch (Exception ex) {

        }

    }

}
