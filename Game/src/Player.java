import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Player class
 */
public class Player {
    //speed gain per second in percent value of gamearea size

    public static final double Y_SPEED_GAIN = 20;
    public static final double X_SPEED_GAIN = 20;

    /**
     * ship size in percent value of gamearea size
     */
    public static final int SHIP_SIZE = 8;

    /**
     * stamina used to move in one direction per tick
     */
    public static final int STAMINA_USAGE = 1;

    private BufferedImage img1, img2;
    private boolean img;

    BufferedImage playerImage;
    private Point2D.Double position;

    Player(){
        position = new Point2D.Double(0, 0);

        playerImage = null;
        img1 = null;
        img2 = null;
        img = true;
        try {
            img1 = ImageIO.read(new File("player1.png"));
            img2 = ImageIO.read(new File("player2.png"));

            playerImage = img2;
        }catch (IOException e){
            System.out.println("Błąd odczytu obrazu");
            System.exit(4);
        }
    }

    Player(int x, int y){
        position = new Point2D.Double(x, y);

        img1 = null;
        img2 = null;
        img = false;
        try {
            img1 = ImageIO.read(new File("player1.png"));
            img2 = ImageIO.read(new File("player2.png"));

            playerImage = img1;
        }catch (IOException e){
            System.out.println("Błąd odczytu obrazu");
            System.exit(4);
        }
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

    public void setPosition(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void changeImage(){
        try {
            if (img)
                playerImage = img1;
            else
                playerImage = img2;
            img = !img;
        }catch (Exception e) { };
    }
}
