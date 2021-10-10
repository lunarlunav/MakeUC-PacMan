package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private Dimension dimension; //Height and width of the playing field
    private boolean playing = false; //The game is running
    private boolean alive = false; //Pacman is alive

    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private final int BLOCK_SIZE = 24; // Block size
    private final int N_BLOCKS = 15; //Lines and columns
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int nGhosts = 6;
    private int lives;
    private int score;

    // position of the ghost - movement
    private int[] dX;
    private int[] dY;

    // ghost coordinates
    private int[] ghostX;
    private int[] ghostY;

    // ghost position on map
    private int[] ghostDX;
    private int[] ghostDY;

    // speed of the ghosts
    private int[] ghostSpeed;

    // images
    private Image heart;
    private Image ghost;
    private Image up;
    private Image down;
    private Image left;
    private Image right;

    // pacman coordinates
    private int pacmanX;
    private int pacmanY;

    // pacman positions on map
    private int pacmanDX;
    private int pacmanDY;

    // control of the cursor keys
    private int reqDX;
    private int reqDY;

    private final int[] VALID_SPEEDS = {1, 2, 3, 4, 6, 8};
    private final int MAX_SPEED = 6;
    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    private final short[] levelData = {
            19, 18, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 26, 18, 22,
            17, 28,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0, 25, 20,
            21,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,  0,  0, 21,
            17, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 18, 20,
            17, 16, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 20,
            17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,
            17, 16, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 24, 16, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 24, 20,
            21,  0, 17, 16, 16, 24, 28,  0, 25, 24, 16, 16, 20,  0, 21,
            21,  0, 17, 16, 20,  0,  0,  0,  0,  0, 17, 16, 20,  0, 21,
            21,  0, 17, 16, 16, 18, 18, 18, 18, 18, 16, 16, 20,  0, 21,
            21,  0, 25, 24, 24, 24, 16, 16, 16, 24, 24, 24, 28,  0, 21,
            21,  0,  0,  0,  0,  0, 17, 16, 20,  0,  0,  0,  0,  0, 21,
            25, 26, 26, 26, 26, 26, 24, 24, 24, 26, 26, 26, 26, 26, 28
    };

    public Board() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }

    private void loadImages() {
        up = new ImageIcon(getClass().getResource("../resources/up.gif")).getImage();
        down = new ImageIcon(getClass().getResource("../resources/down.gif")).getImage();
        left = new ImageIcon(getClass().getResource("../resources/left.gif")).getImage();
        right = new ImageIcon(getClass().getResource("../resources/right.gif")).getImage();
        ghost = new ImageIcon(getClass().getResource("../resources/ghost.gif")).getImage();
        heart = new ImageIcon(getClass().getResource("../resources/heart.png")).getImage();
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        dimension = new Dimension(400, 400);
        ghostX = new int[MAX_GHOSTS];
        ghostDX = new int[MAX_GHOSTS];
        ghostY = new int[MAX_GHOSTS];
        ghostDY = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dX = new int[4];
        dY = new int[4];

        timer = new Timer(50, this);
        timer.start();
    }

    private void initGame() {
        lives = 3;
        score = 0;
        initLevel();
        nGhosts = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        for (int i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        continueLevel();
    }

    public void showIntroScreen(Graphics2D g2d) {
        String start = "Press ENTER to start";
        g2d.setColor(Color.white);
        g2d.drawString(start, SCREEN_SIZE / 4 + 20, 135);
    }

    public void drawMaze(Graphics2D g2d) {
        int i = 0;

        for (int y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (int x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(109, 109, 109));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                }
                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }
                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(Color.blue);
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }
                i++;
            }
        }
    }

    public void drawScore(Graphics2D g2d) {
        g2d.setFont(smallFont);
        g2d.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g2d.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g2d.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    public void drawPacman(Graphics2D g2d) {
        if (reqDX == -1) {
            g2d.drawImage(left, pacmanX + 1, pacmanY + 1, this);
        } else if (reqDX == 1) {
            g2d.drawImage(right, pacmanX + 1, pacmanY + 1, this);
        } else if (reqDY == -1) {
            g2d.drawImage(up, pacmanX + 1, pacmanY + 1, this);
        } else {
            g2d.drawImage(down, pacmanX + 1, pacmanY + 1, this);
        }
    }

    public void drawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    private void checkMaze() {
        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {
            if ((screenData[i]) != 0) {
                finished = false;
            }
            i++;
        }

        if (finished) {
            score += 50;
            if (nGhosts < MAX_GHOSTS) {
                nGhosts++;
            }
            if (currentSpeed < MAX_SPEED) {
                currentSpeed++;
            }
            initLevel();
        }
    }

    public void movePacman() {
        int position;
        short ch;
        if (pacmanX % BLOCK_SIZE == 0 && pacmanY % BLOCK_SIZE == 0) {
            position = pacmanX / BLOCK_SIZE + N_BLOCKS * (pacmanY / BLOCK_SIZE);
            ch = screenData[position];

            if ((ch & 16) != 0) {
                screenData[position] = (short) (ch & 15);
                score++;
            }

            if (reqDX != 0 || reqDY != 0) {
                if (!((reqDX == -1 && reqDY == 0 && (ch & 1) != 0)
                        || (reqDX == 1 && reqDY == 0 && (ch & 4) != 0)
                        || (reqDX == 0 && reqDY == -1 && (ch & 2) != 0)
                        || (reqDX == 0 && reqDY == 1 && (ch & 8) != 0))) {
                    pacmanDX = reqDX;
                    pacmanDY = reqDY;
                }
            }

            if ((pacmanDX == -1 && pacmanDY == 0 && (ch & 1) != 0)
                    || (pacmanDX == 1 && pacmanDY == 0 && (ch & 4) != 0)
                    || (pacmanDX == 0 && pacmanDY == -1 && (ch & 2) != 0)
                    || (pacmanDX == 0 && pacmanDY == 1 && (ch & 8) != 0)) {
                pacmanDX = 0;
                pacmanDY = 0;
            }
        }
        pacmanX = pacmanX + PACMAN_SPEED * pacmanDX;
        pacmanY = pacmanY + PACMAN_SPEED * pacmanDY;
    }

    public void moveGhosts(Graphics2D g2d) {
        int position;
        int count;
        for (int i = 0; i < nGhosts; i++) {
            if (ghostX[i] % BLOCK_SIZE == 0 && ghostY[i] % BLOCK_SIZE == 0) {
                position = ghostX[i] / BLOCK_SIZE + N_BLOCKS * (ghostY[i] / BLOCK_SIZE);
                count = 0;

                if ((screenData[position] & 1) == 0 && ghostDX[i] != 1) {
                    dX[count] = -1;
                    dY[count] = 0;
                    count++;
                }
                if ((screenData[position] & 2) == 0 && ghostDY[i] != 1) {
                    dX[count] = 0;
                    dY[count] = -1;
                    count++;
                }
                if ((screenData[position] & 4) == 0 && ghostDX[i] != -1) {
                    dX[count] = 1;
                    dY[count] = 0;
                    count++;
                }
                if ((screenData[position] & 8) == 0 && ghostDY[i] != -1) {
                    dX[count] = 0;
                    dY[count] = 1;
                    count++;
                }

                if (count == 0) {
                    if ((screenData[position] & 15) == 15) {
                        ghostDX[i] = 0;
                        ghostDY[i] = 0;
                    } else {
                        ghostDX[i] = -ghostDX[i];
                        ghostDY[i] = -ghostDY[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    ghostDX[i] = dX[count];
                    ghostDY[i] = dY[count];
                }
            }

            ghostX[i] = ghostX[i] + (ghostDX[i] * ghostSpeed[i]);
            ghostY[i] = ghostY[i] + (ghostDY[i] * ghostSpeed[i]);
            drawGhost(g2d, ghostX[i] + 1, ghostY[i] + 1);

            if (pacmanX > (ghostX[i] - 12) && pacmanX < (ghostX[i] + 12)
                    && pacmanY > (ghostY[i] - 12) && pacmanY < (ghostY[i] + 12)
                    && playing) {
                alive = true;
            }
        }
    }

    private void playGame(Graphics2D g2d) {
        if (!alive) {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        } else {
            death();
        }
    }

    private void continueLevel() {
        int dx = 1;
        int random;

        for (int i = 0; i < nGhosts; i++) {
            ghostX[i] = 4 * BLOCK_SIZE;
            ghostY[i] = 4 * BLOCK_SIZE;
            ghostDX[i] = dx;
            ghostDY[i] = 0;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed)
                random = currentSpeed;

            ghostSpeed[i] = VALID_SPEEDS[random];
        }

        //start position
        pacmanX = 7 * BLOCK_SIZE;
        pacmanY = 11 * BLOCK_SIZE;
        //reset direction move
        pacmanDX = 0;
        pacmanDY = 0;
        // reset direction controls
        reqDX = 0;
        reqDY = 0;
        alive = false;
    }

    private void death() {
        lives--;
        if (lives == 0) {
            playing = false;
        }
        continueLevel();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, dimension.width, dimension.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (playing)
            playGame(g2d);
        else
            showIntroScreen(g2d);

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    //Keyboard controls
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            // To get each arrow code
            int key = e.getKeyCode();

            if (playing) {
                if (key == KeyEvent.VK_A) {
                    reqDX = -1;
                    reqDY = 0;
                } else if (key == KeyEvent.VK_D) {
                    reqDX = 1;
                    reqDY = 0;
                } else if (key == KeyEvent.VK_W) {
                    reqDX = 0;
                    reqDY = -1;
                } else if (key == KeyEvent.VK_S) {
                    reqDX = 0;
                    reqDY = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    playing = false;
                }
            } else {
                if (key == KeyEvent.VK_ENTER) {
                    playing = true;
                    initGame();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
