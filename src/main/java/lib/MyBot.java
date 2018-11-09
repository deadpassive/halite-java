package lib;

import lib.models.bots.GeneticBot;

/**
 * This is just the class we use to run and make halite happy.
 */
public class MyBot {
    public static void main(final String[] args) {
        GeneticBot.getInstance().run(args);
    }
}
