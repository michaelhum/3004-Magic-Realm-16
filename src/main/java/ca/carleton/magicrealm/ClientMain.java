package ca.carleton.magicrealm;

import ca.carleton.magicrealm.Networking.AppClient;
import ca.carleton.magicrealm.Networking.Config;
import ca.carleton.magicrealm.control.GameController;
import com.sun.deploy.util.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * Date: 03/02/15
 * Time: 4:45 PM
 */
public class ClientMain {

    private static final Logger LOG = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {

        GameController game = new GameController();
        AppClient clnt = new AppClient(Config.DEFAULT_HOST, Config.DEFAULT_HOST_PORT,game);
        
    }
}
