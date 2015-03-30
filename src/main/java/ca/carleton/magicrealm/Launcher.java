package ca.carleton.magicrealm;

import ca.carleton.magicrealm.Networking.AppClient;
import ca.carleton.magicrealm.Networking.AppServer;
import ca.carleton.magicrealm.control.GameController;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * Date: 03/02/15
 * Time: 4:45 PM
 */
public class Launcher {

    public static boolean CHEAT_MODE = false;

    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    private static final String IP_ADDRESS_ARG = "ip";

    private static final String PORT_ARG = "port";

    private static final String CHEAT_ARG = "cheat";

    private static final String HOST_ARG = "host";

    private static final String LAUNCH_COMMAND = "java -jar Magic_Realm.jar";

    public static void main(String[] args) {

        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();

        options.addOption(HOST_ARG, false, "Whether or not to start as a server host.");
        options.addOption(IP_ADDRESS_ARG, true, "The ip address to connect to.");
        options.addOption(PORT_ARG, true, "The port to use.");
        options.addOption(CHEAT_ARG, false, "Optional. Whether or not to use cheat mode.");

        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(CHEAT_ARG)) {
                CHEAT_MODE = true;
                LOG.info("Cheat mode enabled for this session.");
            }
            if (cmd.hasOption(HOST_ARG)) {
                LOG.info("Attempting to start as host server...");
                if (!cmd.hasOption(PORT_ARG)) {
                    LOG.error("No port specified for host.");
                    throw new Exception("Attempted to start as host with no port specified.");
                }
               new AppServer(Integer.parseInt(cmd.getOptionValue(PORT_ARG))).start();
            } else {
                if (!cmd.hasOption(IP_ADDRESS_ARG) || !cmd.hasOption(PORT_ARG)) {
                    formatter.printHelp(LAUNCH_COMMAND, options);
                } else {
                    GameController game = new GameController();
                    LOG.info("Connecting to {}:{}.", cmd.getOptionValue(IP_ADDRESS_ARG), cmd.getOptionValue(PORT_ARG));
                    AppClient client = new AppClient(cmd.getOptionValue(IP_ADDRESS_ARG), Integer.parseInt(cmd.getOptionValue(PORT_ARG)), game);
                    game.setNetworkConnection(client);
                }
            }
        } catch (final Exception exception) {
            LOG.error("Error with options parse. {}", exception);
            formatter.printHelp(LAUNCH_COMMAND, options);
        }
    }
}
