import models.bots.SimpleBot;

import java.io.File;
import java.util.logging.Logger;

/**
 * This is just the class we use to run and make halite happy.
 */
public class MyBot {
    private static final Logger LOGGER = Logger.getLogger(MyBot.class.getName());

    public static void main(final String[] args) {
        File replayFolder = new File("replays");

        if (!replayFolder.exists()) {
            LOGGER.info("Replay folder doesn't exist");
            replayFolder.mkdir();
            LOGGER.info("Replay folder created");
        }

        SimpleBot.getInstance().run(args);
    }
}
