import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Properties;

/**
 * Class containing information about current level
 */
public class Level {
    private final int levelNumber;
    private final double gravitation;
    private int maxVelocity;
    private final int time;
    private final int stamina;
    private LinkedList<Line> lines;

    /**
     * Constuctor from hardcoded data.
     *
     * @param levelNumber Number of the level
     * @param gravitation Gravitational constant of the level
     * @param maxVelocity Maximum velocity for safe landing
     * @param time        Time limit to complete the level
     * @param stamina     Stamina amount for the level
     */
    public Level(int levelNumber, double gravitation, int maxVelocity, int time, int stamina) {
        this.levelNumber = levelNumber;
        this.gravitation = gravitation;
        this.maxVelocity = maxVelocity;
        this.time = time;
        this.stamina = stamina;
        this.lines = new LinkedList<>();
    }

    /**
     * Constructor from file.
     * @param filename File to load data from;
     */
    public Level(String filename) {
        Point tempPoint = new Point(0,0);

        this.lines = new LinkedList<>();
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.levelNumber = Integer.parseInt(prop.getProperty("levelNumber"));
        this.gravitation = Integer.parseInt(prop.getProperty("gravitation"));
        this.maxVelocity = Integer.parseInt(prop.getProperty("maxVelocity"));
        this.time = Integer.parseInt(prop.getProperty("time"));
        this.stamina = Integer.parseInt(prop.getProperty("stamina"));
        String dotstr = prop.getProperty("dots");
        String[] splited = dotstr.split("\\s+");
        for(int i=0; i<splited.length; i=i+3) {
            if(i!=0)
                this.lines.add(new Line(tempPoint.x, tempPoint.y, Integer.parseInt(splited[i]), 100 - Integer.parseInt(splited[i + 1]), Integer.parseInt(splited[i+2])));
            tempPoint = new Point(Integer.parseInt(splited[i]),100 - Integer.parseInt(splited[i + 1]));
        }
        this.lines.add(new Line(lines.get(lines.size()-1).x2,lines.get(lines.size()-1).y2,lines.get(0).x1,lines.get(0).y1,0));

    }

    /**
     * Returns number of the level.
     * @return Number of the level
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Returns gravitational constant of the level.
     * @return Gravitational constant
     */
    public double getGravitation() {
        return gravitation;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    /**
     * Returns maximum velocity for safe landing.
     * @return Maximum velocity for safe landing
     */
    public int getMaxVelocity() {
        return maxVelocity;
    }

    /**
     * Returns time limit to complete the level.
     * @return Time limit to complete the level
     */
    public int getTime() {
        return time;
    }

    /**
     * Returns stamina amount for the level.
     * @return Stamina amount for the level
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Returns list of lines in the level. Lines have values in percentage of the game area.
     * @return List of lines in the level
     */
    public LinkedList<Line> getLines() {
        return lines;
    }

}

