import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class EvaluateBots {

    private static final Logger LOGGER = Logger.getLogger(EvaluateBots.class.getName());

    /**
     * Evaluate bots by playing each bot in a round robin against the other bots
     * @param args list of bots to evaluate
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        EvaluateBots evaluateBots = new EvaluateBots();

        if(args[0].equals("all")) {
           evaluateBots.evaluate(getAllBots());
        } else if (args.length > 1) {
            evaluateBots.evaluate(Arrays.asList(args));
        } else {
            LOGGER.info("No arguments provided, expected either: 'all' or <bot1> <bot2> <botn>");
        }
    }

    private void evaluate(List<String> bots) throws IOException, InterruptedException {
        LOGGER.info("Evaluating bots, cmd line arguments: " + bots);
        // TODO: keep track of the results of the matches and report them somehow

        for (String bot1 : bots){
            for (String bot2 : bots) {
                if (!bot1.equals(bot2)){
                    LOGGER.info("Running match (" + bot2 + " vs " + bot1 + ")");

                    // TODO: javac on the two bots
                    //LOGGER.info("Building " + bot1);
                    //Process pr1 = runtime.exec("#!/bin/sh\njavac models/bots/" + bot1 + ".java");
                    //pr1.waitFor();

                    //LOGGER.info("Building " + bot2);
                    //Process pr2 = runtime.exec("#!/bin/sh\njavac models/bots/" + bot2 + ".java");
                    //pr2.waitFor();

                    LOGGER.info("Running match");
                    //LOGGER.info("#!/bin/sh\n./halite --replay-directory replays/ \"java models.bots." + bot1 + "\" \"java models.bots." + bot2+ "\"");
                    ProcessBuilder processBuilder = new ProcessBuilder("./halite",  "--replay-directory replays/", "java models.bots." + bot1, "java models.bots." + bot2);
                    Process process = processBuilder.start();
                    process.waitFor();

                }
            }
        }
    }

    private static List<String> getAllBots(){
        List<String> bots = new ArrayList<String>();

        File[] files = new File("./models/bots/").listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java") && !file.getName().equals("AbstractBot")) {
                bots.add(file.getName());
            }
        }

        return bots;
    }
}
