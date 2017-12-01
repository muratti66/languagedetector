/**
 * Language Detector - Natural language detection application <br>
 * The class illustrates how to write comments used 
 * to generate JavaDoc documentation
 *
 * @url https://git.muratti66.com:8443/mbudak/languagedetector
 */
package com.muratti66.languagedetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import static com.muratti66.languagedetector.Main.clientDebug;
import static com.muratti66.languagedetector.Main.clientKeepAlive;
import static com.muratti66.languagedetector.Main.clientSoTimeout;
import static com.muratti66.languagedetector.Main.portNumber;

/** - Language Detector Socket Operation Class <p>
 * 
 * @author Murat B.
 * @version 1.00, 01 Dec 2017
 * @since 1.0
 */
public class SocketOperation {
    
    private final static Logger LOGGER = Logger.getLogger(SocketOperation.class);
    /**
     * Opening the tcp socket on the system with this method
     */
    public static void socketRun() {
        Boolean ExCheck = true;
        BufferedReader in;
        ServerSocket serverSocket;
        Socket clientSocket;
        String client, input, inputLine, result;
        PrintWriter out;
        
        SocketOperation.debugOps("Socket program started.");
        while (ExCheck) {
            try {
                serverSocket = new ServerSocket(portNumber);
                SocketOperation.debugOps(String.valueOf(portNumber) 
                         + " port socket is opened");
                clientSocket = serverSocket.accept();
                clientSocket.setKeepAlive(clientKeepAlive);
                clientSocket.setSoTimeout(clientSoTimeout);
                
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                client = clientSocket.getRemoteSocketAddress().toString();
                SocketOperation.debugOps(client + " connected to me..");
                while ((inputLine = in.readLine()) != null ) {
                    input = inputLine.trim().replaceAll("[\n|\r|\t]", " ");
                    SocketOperation.debugOps(input + " - query received from" + client);
                    if (input.equals("quit")) {
                        out.println("0;;Good Bye;;1");
                        SocketOperation.debugOps(client + " exited..");
                        break;
                    } else {
                        result = LearnOperation.checkData(input);
                        if (result == null) {
                            out.println("900;;NA;;0.0000000000000000");
                        } else {
                            out.println("100;;" + result);
                        }
                    }
                }
                SocketOperation.debugOps(client + " exited(with timeout)..");
                clientSocket.close();
                serverSocket.close();
            } catch (IOException ex) {
                ExCheck = false;
                LOGGER.error(ex);
            }
        }
        SocketOperation.debugOps("Socket program stopped.");
    }
    /**
     * Logging messages for this method, if enabled debugging operations
     * @param message Message String
     */
    private static void debugOps(String message) {
        if (clientDebug) {
            LOGGER.info(message);
        }
    }
}
