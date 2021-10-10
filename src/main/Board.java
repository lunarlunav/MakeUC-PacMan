package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private boolean inGame = false;
    private boolean dying = false;

    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int N_GHOSTS = 6;
    private int lives;
    private int score;
    private int[] dx;
    private int[] dy;
    private int[] ghost_x;
    private int[] ghost_y;
    private int[] ghost_dx;
    private int[] ghost_dy;
    private int[] ghostSpeed;

    private Image heart;
    private Image ghost;
    private Image up;
    private Image down;
    private Image left;
    private Image right;

    private int pacman_x;
    private int pacman_y ;
    private int pacmand_x;
    private int pacmand_y;

    private int req_dx;
    private int req_dy;

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;
    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    private final short levelData[] = {
            19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21,  0,  0,  0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28,  0, 25, 24, 24, 16, 20,  0, 21,
            1,  17, 16, 20,  0,  0,  0,  0,  0,  0,  0, 17, 20,  0, 21,
            1,  17, 16, 16, 18, 18, 22,  0, 19, 18, 18, 16, 20,  0, 21,
            1,  17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
            1,  17, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 20,  0, 21,
            1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20,  0, 21,
            1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,  0, 21,
            1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9,   8,  8,  8,  8,  8,  8,  8,  8,  8, 25, 24, 24, 24, 28
    };

    public Board() {
        loadImages();
        initVariables();
        //addKeyListener(new TAdapter());
        setFocusable(true);
        //initGame();
    }

    private void loadImages() {
        up = new ImageIcon("../resources/up.gif").getImage();
        down = new ImageIcon("../resources/down.gif").getImage();
        left = new ImageIcon("../resources/left.gif").getImage();
        right = new ImageIcon("../resources/right.gif").getImage();
        ghost = new ImageIcon("../resources/ghost.gif").getImage();
        heart = new ImageIcon("../resources/heart.gif").getImage();
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(600, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
