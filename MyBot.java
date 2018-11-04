import models.bots.SimpleBot;

/**
 * This is just the class we use to run and make halite happy.
 */
public class MyBot {
    public static void main(final String[] args) {
        SimpleBot.getInstance().run(args);
    }
}
