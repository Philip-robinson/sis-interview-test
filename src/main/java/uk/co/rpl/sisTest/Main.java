/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.rpl.sisTest;

import com.sun.net.httpserver.HttpServer;
import static com.sun.net.httpserver.HttpServer.create;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author philip
 */
public class Main {
    private static Logger LOG = getLogger(Main.class);
    public static void main(String[] argv){

        try {
            TeamStore teamStore = new TeamStore();
            
            final HttpServer server = create(new InetSocketAddress(8021), 5);
            Runtime.getRuntime().addShutdownHook(new Thread(()->server.stop(1)));
            server.createContext("/team", new TeamHandler(teamStore));
            server.start();
            LOG.info ("Server Started");
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
    
}
