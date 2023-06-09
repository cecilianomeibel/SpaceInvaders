package logicaServer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;

public class Disparo {
    Image imagen = null;
    public int posX = 0;
    public int posY = 0;
    Clip sonidoDisparo;

    public Disparo() {
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
            sonidoDisparo = AudioSystem.getClip();
            sonidoDisparo.open(
                    AudioSystem.getAudioInputStream(
                            getClass().getResource("/sonidos/disparosXBOX.wav"))
            );
        } catch (Exception e) {
        }
    }

    public void mueve() {
        posY -= 5;

    }

    public void posicionaDisparo(Nave _nave) {
        posX = _nave.posX
                + _nave.imagen.getWidth(null) / 2
                - imagen.getWidth(null) / 2;
        posY = _nave.posY - _nave.imagen.getHeight(null) / 2;

    }


}




