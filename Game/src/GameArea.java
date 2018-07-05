import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Class containing the game area.
 * Contains level variables and displays level on the screen.
 */
public class GameArea extends JPanel implements ComponentListener{
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 800;

    private LinkedList polyx = new LinkedList<Integer>();
    private LinkedList polyy = new LinkedList<Integer>();
    private Polygon polyg = new Polygon();

    private LinkedList<Line> pointedLines;

    private boolean moveRight, moveLeft, moveUp, moveDown;

    private double xPerSec, yPerSec;
    private Level currentLevel;

    private Player player;
    private Image playerImg;

    private LinkedList<Bonus> bonuses;
    private LinkedList<Image> bonusImages;

    private JLabel winInfo;

    /**
     * Constructor of the game area.
     */
    public GameArea() {
        super();


        player = new Player(50, 0);

        xPerSec = 0;
        yPerSec = 0;

        moveRight = false;
        moveLeft = false;
        moveUp = false;
        moveDown = false;

        bonuses = new LinkedList<>();
        bonusImages = new LinkedList<>();
        pointedLines = new LinkedList<>();

        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setDoubleBuffered(true);
        setBackground(Color.CYAN);
        currentLevel = new Level(0, 0, 0, 0, 0);

        playerImg = player.playerImage.getScaledInstance(Player.SHIP_SIZE*DEFAULT_WIDTH/100, Player.SHIP_SIZE*DEFAULT_HEIGHT/100, Image.SCALE_DEFAULT);


        //creating initial terrain
        for (Line line : currentLevel.getLines()) {
            polyx.add(line.x1);
            polyy.add(line.y1);
        }
        polyg.reset();
        for(int i =0; i < polyx.size(); i++){
            polyg.addPoint((int)polyx.get(i)*this.getWidth()/100, (int)polyy.get(i)*this.getHeight()/100);
        }
        polyx.clear();
        polyy.clear();

        addComponentListener(this);

        winInfo = new JLabel("");
        winInfo.setFont(new Font(this.getFont().getName(), Font.PLAIN, 40));
        winInfo.setForeground(Color.RED);

        this.setLayout(new BorderLayout());
        add(winInfo);

        setMinimumSize(new Dimension(50, 50));

    }

    public void componentResized(ComponentEvent e){
        //adjusting terrain
        for (Line line : currentLevel.getLines()) {
            polyx.add(line.x1);
            polyy.add(line.y1);
        }
        polyg.reset();
        for(int i =0; i < polyx.size(); i++){
            polyg.addPoint((int)polyx.get(i)*this.getWidth()/100, (int)polyy.get(i)*this.getHeight()/100);
        }
        polyx.clear();
        polyy.clear();

        //scaling image
        playerImg = player.playerImage.getScaledInstance(Player.SHIP_SIZE*this.getWidth()/100, Player.SHIP_SIZE*this.getHeight()/100, Image.SCALE_DEFAULT);

        //scaling text
        if(winInfo.getText() != "") {
            double stringWitdh = winInfo.getFontMetrics(winInfo.getFont()).stringWidth(winInfo.getText());
            double scaling = this.getWidth() / stringWitdh;

            winInfo.setFont(new Font(this.getFont().getName(), Font.PLAIN, (int) (winInfo.getFont().getSize() * scaling)));

            Rectangle bounds = new Rectangle(this.getWidth() / 2 - winInfo.getPreferredSize().width / 2, this.getHeight() / 2 - winInfo.getPreferredSize().height / 2, winInfo.getPreferredSize().width, winInfo.getPreferredSize().height);
            winInfo.setBounds(bounds);
        }
    };

    public void componentHidden(ComponentEvent e){};
    public void componentShown(ComponentEvent e){};
    public void componentMoved(ComponentEvent e){};

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            /*g2.setColor(Color.white);

            g2.drawPolygon(polyg);*/
            g2.setColor(new Color(60,150,60));
            g2.fillPolygon(polyg);

            g2.setPaint(Color.BLUE);

            for (Line line : pointedLines) {
                switch (line.points){
                    case 3: g2.setColor(Color.RED); break;
                    case 4: g2.setColor(Color.YELLOW); break;
                    case 5: g2.setColor(Color.black); break;
                    default: break;
                }
                g2.drawLine(line.x1*this.getWidth()/100, line.y1*this.getHeight()/100, line.x2*this.getWidth()/100, line.y2*this.getHeight()/100);
            }

            AffineTransform t = new AffineTransform();
            t.translate(player.getPosition().x*this.getWidth()/100, player.getPosition().y*this.getHeight()/100);
            t.scale(1, 1);
            g2.drawImage(playerImg, t, null);

            //bonuses display
                for (Bonus bon : bonuses){
                Image bonusImg = bon.bonusImage.getScaledInstance(Bonus.BONUS_SIZE*this.getWidth()/100, Bonus.BONUS_SIZE*this.getHeight()/100, Image.SCALE_DEFAULT);
                AffineTransform t2 = new AffineTransform();
                t2.translate(bon.getPosition().x*this.getWidth()/100, bon.getPosition().y*this.getHeight()/100);
                t2.scale(1, 1);
                g2.drawImage(bonusImg, t2, null);
            }
    }

    /**
     * Get method for default game area size
     * @return info about default game area size
     */
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Returns info about current level.
     * @return info about current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Creates new level using file.
     * @param dataFile File to load data from
     */
    public void loadLevel(String dataFile) {
        currentLevel = new Level(dataFile);

        pointedLines.clear();

        //create terrain
        for (Line line : currentLevel.getLines()) {
            polyx.add(line.x1);
            polyy.add(line.y1);
            if (line.points > 2) pointedLines.add(line);
        }
        polyg.reset();
        for(int i =0; i < polyx.size(); i++){
            polyg.addPoint((int)polyx.get(i)*this.getWidth()/100, (int)polyy.get(i)*this.getHeight()/100);
        }
        polyx.clear();
        polyy.clear();

        bonuses.clear();



        winInfo.setText("");
    }

    /**
     * Sets speed gain in right direction.
     * @param set Acceleration to right
     */
    public void setMoveRight(boolean set) {
        moveRight = set;
    }

    /**
     * Sets speed gain in left direction.
     * @param set Acceleration to left
     */
    public void setMoveLeft(boolean set) {
        moveLeft = set;
    }

    /**
     * Sets speed gain in up direction.
     * @param set Acceleration to up
     */
    public void setMoveUp(boolean set) {
        moveUp = set;
    }

    /**
     * Sets speed gain in down direction.
     * @param set Acceleration to down
     */
    public void setMoveDown(boolean set) {
        moveDown = set;
    }

    /**
     * Returns vertical speed in pixels per second.
     * @return Vertical speed (px/sec)
     */
    public double getYSpeed(){
        return yPerSec;
    }

    /**
     * Returns horizontal speed in pixels per second.
     * @return Horizontal speed (px/sec)
     */
    public double getXSpeed(){
        return xPerSec;
    }

    /**
     * Stops the movement of the ship
     */
    public void stopMoving(){
        xPerSec = 0;
        yPerSec = 0;
    }

    /**
     * Sets ship's position.
     * @param x Horizontal position
     * @param y Vertical position
     */
    public void setShipPosition(double x, double y){
        player.setPosition(x, y);
    }

    /**
     * Moves the object. Main animation function.
     * @param dt Time difference since last frame
     * @return Stamina used in movement
     */
    public int move(double dt, int staminaLeft) {

        double x = player.getPosition().x;
        double y = player.getPosition().y;

        Rectangle2D hitbox = new Rectangle2D.Double (x*this.getWidth()/100, y*this.getHeight()/100, Player.SHIP_SIZE*this.getWidth()/100, Player.SHIP_SIZE*this.getHeight()/100);

        int staminaUsed = 0;

        if(polyg.intersects(hitbox))
            return 0;

        //set movement direction
        if(staminaLeft >= Player.STAMINA_USAGE) {
            if (moveRight && moveLeft)
                xPerSec = xPerSec + 0;
            else if (moveLeft) {
                xPerSec = xPerSec - Player.X_SPEED_GAIN*dt;
                staminaUsed += Player.STAMINA_USAGE;
            }
            else if (moveRight) {
                xPerSec = xPerSec + Player.X_SPEED_GAIN*dt;
                staminaUsed += Player.STAMINA_USAGE;
            }
            else xPerSec = xPerSec + 0;
        }

        yPerSec = yPerSec + currentLevel.getGravitation()*dt;

        if(staminaLeft >= Player.STAMINA_USAGE){
            if (moveUp && moveDown && !polyg.intersects(hitbox))
                yPerSec = yPerSec + 0;
            else if (moveUp) {
                yPerSec = yPerSec - Player.Y_SPEED_GAIN*dt;
                staminaUsed += Player.STAMINA_USAGE;
            }
            else if (moveDown) {
                yPerSec = yPerSec + Player.Y_SPEED_GAIN*dt;
                staminaUsed += Player.STAMINA_USAGE;
            }
            else yPerSec = yPerSec + 0;
        }

        double dx = xPerSec * dt;
        double dy = yPerSec * dt;

        //moving
        if (!(x + Player.SHIP_SIZE <= 100 && x >= 0))
            stopMoving();
        x = x + dx;

        if (!(y + Player.SHIP_SIZE <= 100 && y>=0))
            stopMoving();
        y = y + dy;

        player.setPosition(x, y);

        return staminaUsed;
    }

    public void animate(){
        player.changeImage();

        playerImg = player.playerImage.getScaledInstance(Player.SHIP_SIZE * DEFAULT_WIDTH / 100, Player.SHIP_SIZE * DEFAULT_HEIGHT / 100, Image.SCALE_DEFAULT);
            }
    /**
     *  Checks, if the player landed on the polygon
     * @return true if landed
     */
    public boolean checkLanding(){
        Rectangle2D hitbox = new Rectangle2D.Double (player.getPosition().x*this.getWidth()/100, player.getPosition().y*this.getHeight()/100, Player.SHIP_SIZE*this.getWidth()/100, Player.SHIP_SIZE*this.getHeight()/100);

        if (polyg.intersects(hitbox) || player.getPosition().x + Player.SHIP_SIZE > 100 || player.getPosition().x < 0 || player.getPosition().y + Player.SHIP_SIZE > 100 || player.getPosition().y < 0 )
        return true;
        else return false;
    }
    /**
     * Check the points value of the point (line)
     * @return point value
     */
    public int landingPointValue() {
        Rectangle2D hitbox = new Rectangle2D.Double(player.getPosition().x, player.getPosition().y, Player.SHIP_SIZE, Player.SHIP_SIZE);
        Line2D line2;
        int points = 2;
        for (Line line : currentLevel.getLines()){
            line2 = new Line2D.Double(line.x1, line.y1, line.x2, line.y2);
            if (line2.intersects(hitbox))
                points = line.points;
            if (points > 2) break;
            }

        if((points > 2) && Math.abs(xPerSec)<currentLevel.getMaxVelocity() && (Math.abs(yPerSec)<currentLevel.getMaxVelocity()))
            winInfo.setText("Gratulacje! Twój wynik w tym poziomie to: " + points);
        else {
            winInfo.setText("Niestety, nie udało ci się poprawnie wylądować...");
            points=2;
        }
        double stringWitdh = winInfo.getFontMetrics(winInfo.getFont()).stringWidth(winInfo.getText());
        double scaling = this.getWidth() / stringWitdh;


        winInfo.setFont(new Font(this.getFont().getName(),  Font.PLAIN, (int) (winInfo.getFont().getSize() * scaling)));

        Rectangle bounds = new Rectangle(this.getWidth()/2 - winInfo.getPreferredSize().width/2, this.getHeight()/2 - winInfo.getPreferredSize().height/2, winInfo.getPreferredSize().width, winInfo.getPreferredSize().height);
        winInfo.setBounds(bounds);
        return points;
    }
    /**
     *  Creates the new bonus
     */
    public void createBonus(int type){
        Rectangle2D bonusArea;
        double x,y;
        do {
            x = new Random().nextDouble()*100;
            y = new Random().nextDouble()*100;
            bonusArea = new Rectangle2D.Double(x*this.getWidth() / 100, y*this.getHeight() / 100, Bonus.BONUS_SIZE* this.getWidth() / 100, Bonus.BONUS_SIZE* this.getHeight() / 100);
        } while (polyg.intersects(bonusArea));

        bonuses.add(new Bonus(type, new Point2D.Double(x,y)));
    }
    /**
     *  Checks, if the player gets the bonus
     * @return type of bonus
     */
    public int checkBonus(){
        Rectangle2D.Double hitbox =  new Rectangle2D.Double(player.getPosition().x, player.getPosition().y, Player.SHIP_SIZE, Player.SHIP_SIZE);

        for(int i = 0; i < bonuses.size(); i++){
            Bonus bonus = bonuses.get(i);
            Rectangle2D bonusHitbox = new Rectangle2D.Double(bonus.getPosition().x, bonus.getPosition().y, Bonus.BONUS_SIZE, Bonus.BONUS_SIZE);
            if (hitbox.intersects(bonusHitbox)){
                bonuses.remove(i);
                return bonus.getType();
            }
        }
        return -1;
    }
}