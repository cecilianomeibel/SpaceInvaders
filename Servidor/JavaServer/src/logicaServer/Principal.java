/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicaServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @class Principal
 */
public class Principal extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 500;

    int filasMarcianos = 3;
    int columnasMarcianos = 5;
    int contador = 0;

    BufferedImage buffer = null;
    //
    BufferedImage plantilla = null;

    Image[] imagenes = new Image[30];


    Timer temporizador = new Timer(10, new ActionListener() {//bucle de animacion del juego. refresca el contenido de la pantalla
        @Override
        public void actionPerformed(ActionEvent ae) {
            //TODO: codigo de animacion
            bucleJuego();
        }
    });

    Invasor invasor = new Invasor(ANCHOPANTALLA);//inicializo el invasor
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    ArrayList<Disparo> listaDisparos = new ArrayList();
    ArrayList<Colision> listaExplosiones = new ArrayList();

    //el array de dos dimensiones que guarda la lista de marcianos
    Invasor[][] listaInvasors = new Invasor[filasMarcianos][columnasMarcianos];
    //dirección en la que se mueve el grupo de marcianos
    boolean direccionMarcianos = true;

    /**
     * Creates new form Principal
     */
    public Principal() {

        initComponents();

        try {
            plantilla = ImageIO.read(getClass().getResource("/imagenes/invaders2.png"));
        } catch (IOException ex) {
        }
        //cargo las 30 imágenes del spritesheet en el array de bufferedimages
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                imagenes[i * 4 + j] = plantilla
                        .getSubimage(j * 64, i * 64, 64, 64)
                        .getScaledInstance(32, 32, Image.SCALE_SMOOTH);

            }
        }
        imagenes[20] = plantilla.getSubimage(0, 320, 66, 32); //sprite de la nave
        imagenes[21] = plantilla.getSubimage(66, 320, 64, 32);
        imagenes[23] = plantilla.getSubimage(255, 320, 32, 32);//explosion parteB
        imagenes[22] = plantilla.getSubimage(255, 289, 32, 32);//explosion parteA


        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        jPanel1.setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);//inicializo el buffer
        buffer.createGraphics();

        temporizador.start();//arranco el temporizador
        miNave.imagen = imagenes[21];
        miNave.posX = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.posY = ALTOPANTALLA - 100;
        //creamos el array de marcianos
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaInvasors[i][j] = new Invasor(ANCHOPANTALLA);
                listaInvasors[i][j].imagen1 = imagenes[2 * i];
                listaInvasors[i][j].imagen2 = imagenes[2 * i + 1];
                listaInvasors[i][j].posX = j * (15 + listaInvasors[i][j].imagen1.getWidth(null));
                listaInvasors[i][j].posY = i * (10 + listaInvasors[i][j].imagen1.getHeight(null));
            }
        }
        miDisparo.posY = -2000;
    }

    private void pintaMarcianos(Graphics2D _g2) {
        for (int i = 0; i < filasMarcianos; i++) {
            for (int j = 0; j < columnasMarcianos; j++) {
                listaInvasors[i][j].mueve(direccionMarcianos);
                if (contador < 50) {
                    _g2.drawImage(listaInvasors[i][j].imagen1, listaInvasors[i][j].posX, listaInvasors[i][j].posY, null);
                } else if (contador < 100) {
                    _g2.drawImage(listaInvasors[i][j].imagen2, listaInvasors[i][j].posX, listaInvasors[i][j].posY, null);
                } else {
                    contador = 0;
                }

                if (listaInvasors[i][j].posX == ANCHOPANTALLA - listaInvasors[i][j].imagen1.getWidth(null) || listaInvasors[i][j].posX == 0) {
                    direccionMarcianos = !direccionMarcianos;
                    for (int k = 0; k < filasMarcianos; k++) {
                        for (int m = 0; m < columnasMarcianos; m++) {
                            listaInvasors[k][m].posY += listaInvasors[k][m].imagen1.getHeight(null);
                        }
                    }
                }
            }
        }
    }

    private void pintaDisparos(Graphics2D g2) {
        //pinta todos los disparos 
        Disparo disparoAux;
        for (int i = 0; i < listaDisparos.size(); i++) {
            disparoAux = listaDisparos.get(i);
            disparoAux.mueve();
            if (disparoAux.posY < 0) {
                listaDisparos.remove(i);
            } else {
                g2.drawImage(disparoAux.imagen, disparoAux.posX, disparoAux.posY, null);
            }
        }
    }


    private void pintaExplosiones(Graphics2D g2) {
        //pinta todas las explosiones 
        Colision explosionAux;
        for (int i = 0; i < listaExplosiones.size(); i++) {
            explosionAux = listaExplosiones.get(i);
            explosionAux.tiempoDeVida--;
            if (explosionAux.tiempoDeVida > 25) {
                g2.drawImage(explosionAux.imagen1,
                        explosionAux.posX,
                        explosionAux.posY, null);
            } else {
                g2.drawImage(explosionAux.imagen2,
                        explosionAux.posX,
                        explosionAux.posY, null);
            }
            //si el tiempo de vida de la explosión es menor o igual a 0 la elimino
            if (explosionAux.tiempoDeVida <= 0) {
                listaExplosiones.remove(i);
            }
        }
    }


    private void bucleJuego() {//redibuja los objetos en el jPanel1

        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        //borro todo lo que hay en el buffer

        g2.setColor(Color.BLACK);//Background Color: color negro a la pantalla
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        ///////////////////////////////////////////////////

        contador++;
        pintaMarcianos(g2);
        //dibujo la nave
        g2.drawImage(miNave.imagen, miNave.posX, miNave.posY, null);
        pintaDisparos(g2);
        pintaExplosiones(g2);
        miNave.mueve();
        chequeaColision();
        ///////////////////////////////////////////////////
        g2 = (Graphics2D) jPanel1.getGraphics();//dibujo de golpe el buffer sobre el jPanel
        g2.drawImage(buffer, 0, 0, null);
    }


    // chequeaColision
    // Permite verificar si el disparo colisiono con el invasor
    // chequea si un disparo y un invasor colisionan
    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        for (int k = 0; k < listaDisparos.size(); k++) {
            //calculo el rectangulo que contiene al disparo correspondiente
            rectanguloDisparo.setFrame(listaDisparos.get(k).posX,
                    listaDisparos.get(k).posY,
                    listaDisparos.get(k).imagen.getWidth(null),
                    listaDisparos.get(k).imagen.getHeight(null));

            for (int i = 0; i < filasMarcianos; i++) {
                for (int j = 0; j < columnasMarcianos; j++) {
                    //calculo el rectángulo correspondiente al invasor que estoy comprobando

                    rectanguloMarciano.setFrame(listaInvasors[i][j].posX,
                            listaInvasors[i][j].posY,
                            listaInvasors[i][j].imagen1.getWidth(null),
                            listaInvasors[i][j].imagen1.getHeight(null)
                    );
                    if (rectanguloDisparo.intersects(rectanguloMarciano)) {

                        //si entra aquí es porque han chocado un invasor y el disparo
                        // En caso de que la bala haya impactado contra el invasor
                        Colision e = new Colision();
                        e.posX = listaInvasors[i][j].posX;
                        e.posY = listaInvasors[i][j].posY;
                        e.imagen1 = imagenes[23];
                        e.imagen2 = imagenes[22];
                        listaExplosiones.add(e);
                        e.sonidoExplosion.start(); //permite que suene el sonido
                        listaInvasors[i][j].posY = 2000;
                        listaDisparos.remove(k);
                    }
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 646, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 514, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                Disparo d = new Disparo();
                d.sonidoDisparo.start();
                d.posicionaDisparo(miNave);
                //agregamos el disparo a la lista de disparos
                listaDisparos.add(d);
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;

        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
