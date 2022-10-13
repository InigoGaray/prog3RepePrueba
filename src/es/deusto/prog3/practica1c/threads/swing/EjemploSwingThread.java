/**
 * This code has been adapted from this github repo:
 * https://github.com/unaguil/prog3-ejemplos-codigo/tree/master/tema1/tema1C
 * 
 * Thanks to: Unai Aguilera (https://github.com/unaguil) 
 */
package es.deusto.prog3.practica1c.threads.swing;

import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// En este ejemplo se lanza una tarea larga (contar hasta 1.000.000)
// dentro de un listener. Esto bloquea el thread de Swing y
// la UI deja de responder correctamente al usuario.

// En este caso se utiliza un nuevo hilo para realizar la tarea 
// y no bloquear la interfaz.

// ¡Cuidado!. Como el hilo creado para contar modifica la UI desde
// fuera del hilo de Swing para cambiar el valor de un JLabel,
// es necesario usar invokeLater para evitar problemas de concurrencia.

public class EjemploSwingThread extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    
    public EjemploSwingThread() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(320, 80);
        this.setLocationRelativeTo(null);
        this.setTitle("Swing + Hilo externo");

        JButton button = new JButton("Haz click!!!");
        JPanel panel = new JPanel();
        label = new JLabel();

        panel.add(label);
        panel.add(button);

        this.add(panel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // este método realiza una tarea muy larga
                // lo que bloquea la interfaz gráfica y deja
                // de responder a eventos.

                // Se crea un nuevo thread para realizar la tarea
                // y no bloquear Swing
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i <= 1000000; i++) {
                            updateLabel(i);
                        }
                    }
                });

                t.start(); // lanzar el hilo
            }
        });

        this.setVisible(true);
    }

    // El código de actualización del JLabel se ha sacado
    // a un método para que la variable value sea final.
    // Esto es requerido al utilizar una clase anónima
    // como la utilizada en el invokeLater.
    private void updateLabel(final int value) {
        // Como vamos a modificar la UI desde otro hilo
        // debemos usar SwingUtilities.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                label.setText(String.valueOf(value));
            }
        });
    }
    
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EjemploSwingThread();
            }
        }); 
    }
}
