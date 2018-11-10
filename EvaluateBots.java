import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Map<String, Integer> winCounts = new HashMap<>();

        for (String bot1 : bots){
            for (String bot2 : bots) {
                if (!bot1.equals(bot2)){
                    for (int i = 0; i < 10; i++) {
                        LOGGER.info("Running match (" + bot2 + " vs " + bot1 + ")");

                        // Build the bots
                        buildBot(bot1);
                        buildBot(bot2);

                        LOGGER.info("Running match");
                        ProcessBuilder processBuilder = new ProcessBuilder("./halite",  "--replay-directory replays/", "java models.bots." + bot1, "java models.bots." + bot2);
                        processBuilder.redirectErrorStream(true);
                        Process process = processBuilder.start();

                        String winner = null;

                        try (BufferedReader processOutputReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()))) {
                            String readLine;

                            while ((readLine = processOutputReader.readLine()) != null) {
                                System.out.println(readLine);
                                if (readLine.contains(" was rank 1")) {
                                    winner = readLine.split("'")[1];

                                }
                            }

                            process.waitFor();
                        }

                        System.out.println("AND THE WINNER IS " + winner);
                        if (winCounts.containsKey(winner)) {
                            winCounts.put(winner, winCounts.get(winner) + 1);
                        } else {
                            winCounts.put(winner, 1);
                        }
                    }
                }
            }
        }

        System.out.println(winCounts);
    }

    private void buildBot(String botName) throws InterruptedException, IOException {
        ProcessBuilder buildBotBuilder2 = new ProcessBuilder("javac",  "models/bots/" + botName + ".java");
        buildBotBuilder2.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        buildBotBuilder2.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process buildBotProcess2 = buildBotBuilder2.start();
        buildBotProcess2.waitFor();
    }

    private static List<String> getAllBots(){
        List<String> bots = new ArrayList<String>();

        File[] files = new File("./models/bots/").listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java") && !file.getName().equals("AbstractBot.java")) {
                bots.add(file.getName().split(".java")[0]);
            }
        }

        return bots;
    }
}
