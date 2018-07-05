/**
 * Current statistics of the player
 */
public class Statistics {
    /**
     * Current score
     */
    private float score;

    /**
     * Stamina left in level
     */
    private int stamina;

    /**
     * Time left in level
     */
    private int time;

    /**
     * Total unused time
     */
    private int totalTime;

    /**
     * Default constructor
     */
    public Statistics(){
        this.score = 0;
        this.stamina = 0;
        this.time = 0;
        this.totalTime = 0;
    }

    public Statistics(float score, int stamina, int time, int totalTime) {
        this.score = score;
        this.stamina = stamina;
        this.time = time;
        this.totalTime = totalTime;
    }

    /**
     * Returns current score.
     * @return Current score
     */
    public float getScore() {
        return score;
    }

    /**
     * Returns stamina left in level.
     * @return Stamina left in level
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Returns time left in level.
     * @return Time left in level
     */
    public int getTime() {
        return time;
    }

    /**
     * Returns total unused time.
     * @return Total unused time
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Sets current score
     * @param score Score to set
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Sets stamina left
     * @param stamina Stamina to set
     */
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    /**
     * Sets time left
     * @param time Time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Adds amount of time
     * @param amount Amount of time to add
     */
    public void addTime(int amount){ this.time += amount; }

    /**
     * Sets total unused time
     * @param totalTime Time to set
     */
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
