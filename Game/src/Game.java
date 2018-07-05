import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;


/**
 * The main frame of the game.
 */
public class Game extends JFrame implements KeyListener {

    /**
     * Temp button to switch levels.
     */
    private JButton button;
    /**
     * Main area of the game.
     * Used for display of all elements.
     */
    private GameArea area;

    /**
     * Panel with informations.
     */
    private InfoPanel infoPanel;

    /**
     * Statistics of player
     */
    private Statistics stats;

    private DecimalFormat df = new DecimalFormat("#.##");

    private boolean pause;
    private boolean landed;
    private boolean twistedKeys;
    String[] highscores = new String[5];
    /**
     * @param title Title of the window showing in the bar
     */
    public Game(String title) {
        super(title);
        addKeyListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        pause = false;
        landed = false;

        //Menu section of the constructor
        JMenu gameMenu = new JMenu("Gra");
        gameMenu.add(new AbstractAction("Nowa gra") {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        gameMenu.add(new AbstractAction("Najlepsze wyniki") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showhigh();
            }
        });

        gameMenu.add(new AbstractAction("Koniec") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JMenu helpMenu = new JMenu("Pomoc");
        helpMenu.add(new AbstractAction("Zasady Gry") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showrules();
            }
        });
        JMenu netMenu = new JMenu("Sieć");
        netMenu.add(new AbstractAction("pobierz wyniki") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gethigh();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        menuBar.add(netMenu);
        setJMenuBar(menuBar);

        stats = new Statistics();
        area = new GameArea();

        infoPanel = new InfoPanel();
        button = new JButton(Integer.toString(area.getCurrentLevel().getLevelNumber()));
        button.setFocusable(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLevel("level"+Integer.toString(area.getCurrentLevel().getLevelNumber()+1)+".properties");
            }
        });

        infoPanel.add(button);
        add(infoPanel, BorderLayout.EAST);
        add(area, BorderLayout.CENTER);
        pack();

        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(250, 100));
    }

    /**
     * Calls a function to load a new level from file in GameArea.
     * @param dataFile File to load data from
     */
    public void changeLevel(String dataFile) {
        area.loadLevel(dataFile);

        area.stopMoving();

        area.setShipPosition(50, 1);

        button.setText(Integer.toString(area.getCurrentLevel().getLevelNumber()));

        stats.setStamina(area.getCurrentLevel().getStamina());
        stats.setTotalTime(stats.getTotalTime()+stats.getTime());
        stats.setTime(area.getCurrentLevel().getTime());

        infoPanel.levelLabel.setText("Aktualny poziom: " + Integer.toString(area.getCurrentLevel().getLevelNumber()));
        infoPanel.staminaLabel.setText("Pozostała wytrzymałość: " + Integer.toString(stats.getStamina()));
        infoPanel.timeLabel.setText("Pozostały czas: " + Integer.toString(stats.getTime()));
        infoPanel.maxVelocityLabel.setText("Maksymalna prędkość: " + Integer.toString(area.getCurrentLevel().getMaxVelocity()));
        infoPanel.gravityLabel.setText("Stała grawitacyjna: " + Double.toString(area.getCurrentLevel().getGravitation()));
        infoPanel.xVelocityLabel.setText("Aktualna prędkość X: " + Double.toString(area.getXSpeed()));
        infoPanel.yVelocityLabel.setText("Aktualna prędkość Y: " + Double.toString(area.getYSpeed()));
        area.repaint();

        landed = false;
        twistedKeys = false;
    }

    /**
     * Checks pressed key and calls correspodning function.
     * @param evt Key event
     */
    @Override
    public void keyPressed(KeyEvent evt){
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (!pause) {
                    if(!twistedKeys)
                        area.setMoveUp(true);
                    else area.setMoveDown(true);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!pause) {
                    if(!twistedKeys)
                        area.setMoveDown(true);
                    else area.setMoveUp(true);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (!pause) {
                    if(!twistedKeys)
                        area.setMoveLeft(true);
                    else area.setMoveRight(true);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!pause) {
                    if(!twistedKeys)
                        area.setMoveRight(true);
                    else area.setMoveLeft(true);
                }
                break;
            case KeyEvent.VK_P:
                pause = !pause;
                break;
            case KeyEvent.VK_SPACE:
                if (landed && area.getCurrentLevel().getLevelNumber() == 5)
                    giveScore(stats.getScore());
                else if (landed || area.getCurrentLevel().getLevelNumber() == 0)
                    changeLevel("level"+Integer.toString(area.getCurrentLevel().getLevelNumber()+1)+".properties");
            default:
                break;
        }
    }

    /**
     * Checks released key and calls correspodning function.
     * @param evt Key event
     */
    @Override
    public void keyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_UP:
                if(!twistedKeys)
                    area.setMoveUp(false);
                else area.setMoveDown(false);
                break;
            case KeyEvent.VK_DOWN:
                if(!twistedKeys)
                    area.setMoveDown(false);
                else area.setMoveUp(false);
                break;
            case KeyEvent.VK_LEFT:
                if(!twistedKeys)
                    area.setMoveLeft(false);
                else area.setMoveRight(false);
                break;
            case KeyEvent.VK_RIGHT:
                if(!twistedKeys)
                    area.setMoveRight(false);
                else area.setMoveLeft(false);
                break;
            default:
                break;
        }
    }

    /**
     * Checks released key and calls correspodning function.
     * Currently unused.
     * @param evt Key event
     */
    @Override
    public void keyTyped(KeyEvent evt) {

    }

    /**
     * Restarts the game.
     * Loads the level data from "level0.txt", resets the ship position and player statistics.
     */
    private void restartGame(){
        changeLevel("level0.properties");
        area.stopMoving();
        area.setShipPosition(50, 50);
        stats = new Statistics();
        stats.setScore(0);
    }

    private void giveScore(double score){
        String nick = JOptionPane.showInputDialog(area, "Brawo! Twój wynik to: " + df.format(score) + "\nPodaj swój nick:","pomoc",JOptionPane.PLAIN_MESSAGE);
        try {
            gethigh();
        }catch (Exception e) {};
        score *= 100;
        if (highscores[0] != "")
            for (int i = 0; i < 5; i++) {
                String[] splited = highscores[i].split("\\s+");
                if (score > Integer.parseInt(splited[1])){
                    try {
                        Network net = new Network();
                        net.send_highscores(nick, (int) score);
                    }catch (Exception e) {};
                }
            }
    }
    /**
     * Shows the highscore
     * Show the message dialog with top 5 results
     */
    private void showhigh(){
        JOptionPane.showMessageDialog(area, highscores,"najlepsze wyniki",JOptionPane.PLAIN_MESSAGE);
    }
    private void gethigh() throws IOException {
        Network net = new Network();
        highscores = net.get_highscores();
    }
    /**
     * Shows the simplified rules
     * Show the dialog box with little help how to play
     */
    private void showrules(){
        JOptionPane.showMessageDialog(area, "Celem gry jest wylądowanie na wyznaczonych polach, z prędkością Y mniejszą niż maksymalną.\n" +
                "Gracz porusza się za pomocą strzałek.\n" +
                "Pomóc mogą mu w tym bonusy, których działanie opisane jest w instrukcji","pomoc",JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Main function of the game.
     * @param args Command line arguments - unused
     */
    public static void main(String args[]) {
            Game newGame = new Game("Ayyyyyyy");
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                newGame.setVisible(true);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                long prevTime = System.nanoTime();
                double secTime = 0;
                double animTime = 0;
                while (true) {

                    double diffTime = ((double) (System.nanoTime() - prevTime)) / 1000000000;
                    secTime += diffTime;
                    animTime += diffTime;

                    //System.out.println(newGame.area.getCurrentLevel().getLevelNumber()+" ");
                    if (newGame.area.getCurrentLevel().getLevelNumber() != 0) {
                        if (!newGame.pause && !newGame.landed) {
                            if (animTime >= 0.3) {
                                newGame.area.animate();
                                animTime = 0;
                            }
                            if (secTime >= 1.0) {
                                secTime = 0;
                                newGame.stats.addTime(-1);
                                newGame.infoPanel.timeLabel.setText("Pozostały czas: " + Integer.toString(newGame.stats.getTime()));
                                if (new Random().nextInt(100) < 20)
                                    newGame.area.createBonus(new Random().nextInt(4));

                            }

                            int staminaLeft = newGame.stats.getStamina();

                            newGame.stats.setStamina(staminaLeft - newGame.area.move(diffTime, staminaLeft));

                            newGame.infoPanel.xVelocityLabel.setText("Aktualna prędkość X: " + newGame.df.format(newGame.area.getXSpeed()));
                            newGame.infoPanel.yVelocityLabel.setText("Aktualna prędkość Y: " + newGame.df.format(newGame.area.getYSpeed()));
                            newGame.infoPanel.staminaLabel.setText("Pozostała wytrzymałość: " + Integer.toString(newGame.stats.getStamina()));

                            switch (newGame.area.checkBonus()) {
                                case 0:
                                    newGame.stats.addTime(100);
                                    break;
                                case 1:
                                    newGame.area.getCurrentLevel().setMaxVelocity(newGame.area.getCurrentLevel().getMaxVelocity() + 15);
                                    newGame.infoPanel.maxVelocityLabel.setText("Maksymalna prędkość: " + Integer.toString(newGame.area.getCurrentLevel().getMaxVelocity()));
                                    break;
                                case 2:
                                    newGame.twistedKeys = true;
                                    break;
                                case 3:
                                    newGame.stats.setStamina(newGame.stats.getStamina() + 1000);
                                    break;
                                default:
                                    break;
                            }

                            newGame.landed = newGame.area.checkLanding();

                            if (newGame.landed) {
                                newGame.stats.setScore((newGame.stats.getScore()*(newGame.area.getCurrentLevel().getLevelNumber()-1) + newGame.area.landingPointValue()) / newGame.area.getCurrentLevel().getLevelNumber());
                                newGame.infoPanel.scoreLabel.setText("Aktualny wynik: " + newGame.df.format(newGame.stats.getScore()));
                            }
                        }
                        newGame.area.repaint();
                    }
                    prevTime = System.nanoTime();
                    try {
                        Thread.sleep((long) (1000 / 60));
                    } catch (
                            InterruptedException ie) {
                        System.out.println("Sleep interrupted exception");
                    }
                }
            }
        }).start();
    }
}
