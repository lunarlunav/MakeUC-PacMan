package main;

import javax.swing.JFrame;
import java.io.IOException;

public class Pacman extends JFrame {

    public Pacman() {
        add(new Board());
    }

    public static void main(String[] args) {
        Pacman pacman = new Pacman();
        pacman.setVisible(true);
        pacman.setTitle("Retro Pacman");
        pacman.setSize(378, 425);
        pacman.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pacman.setLocationRelativeTo(null);
    }

}
