/**
 * Language Detector - Natural language detection application <br>
 * The class illustrates how to write comments used 
 * to generate JavaDoc documentation
 *
 * @url https://git.muratti66.com:8443/mbudak/languagedetector
 */
package com.muratti66.languagedetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/** - Language Detector First Class <p>
 * 
 * @author Murat B.
 * @version 1.00, 01 Dec 2017
 * @since 1.0
 */
public class Main {
    // First of all
    private final static String globalPath = "/opt/LanguageDetector";
    
    // for Socket Operations
    public static int portNumber = 8181;
    public static int clientSoTimeout = 15000;
    public final static Boolean clientKeepAlive = true;
    public static Boolean clientDebug = false;
    
    // for Learning Operations
    public static String langFilesPath = null;
    public static int maxNumberOfThreadsPerTask = 1;
    public final static Boolean setParallelized = true;
    public final static Long setGlobalSeed = 42L;
    
    // for Others
    private final static String configFile = globalPath + "/config.cfg";
    private final static Logger LOGGER = Logger.getLogger(Main.class);
    /**
     * First run Main method
     * @param args String[] Object / Not Used..
     */
    public static void main(String[] args) {
        Main.log4jPrep();
        Main.configPrep();
        LearnOperation.initialization();
        SocketOperation.socketRun();
    }
    /**
     * Log4j Preperation
     */
    private static void log4jPrep() {
        InputStream inputFile = ClassLoader
                .getSystemResourceAsStream("log4j.properties");
        Properties props = new Properties();
        try {
            props.load(inputFile);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        PropertyConfigurator.configure(props);
    }
    /**
     * Configuration Overwritten Method
     */
    private static void configPrep() { 
        int chckDebug = 1;
        Properties prop = Main.propReturn();
        langFilesPath = prop.getProperty("ld.language_files_path").trim();
        maxNumberOfThreadsPerTask = Integer.valueOf(
                prop.getProperty("ld.max_number_of_threads").trim());
        portNumber = Integer.valueOf(
                prop.getProperty("sck.port_number").trim());
        clientSoTimeout = Integer.valueOf(
                prop.getProperty("sck.client_so_timeout_milsec").trim());
        chckDebug = Integer.valueOf(
                prop.getProperty("sck.client_debug").trim());
        if (chckDebug != 0) {
            clientDebug = true;
        }
    }
    /**
     * Configuration File Preperation
     * @return Properties Object
     */
    private static Properties propReturn() {
        File cfile = new File(configFile);
        Properties propReturn = new Properties();
        try {
            InputStream configFileLoad = new FileInputStream(cfile);
            propReturn.load(configFileLoad);
        } catch (IOException ex) {
            LOGGER.error(ex);
            System.exit(-1);
        }
        return propReturn;
    }
}