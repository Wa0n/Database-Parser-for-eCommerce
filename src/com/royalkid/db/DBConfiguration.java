package com.royalkid.db;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Gets database configuration parameters from existing file ./config/config.csv
 * and stores it in private fields.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class DBConfiguration {
    private String host;
    private String port;
    private String loginName;
    private String password;
    private String schema;
    private String dbms;

    private static DBConfiguration thisInstance = new DBConfiguration();

    public static DBConfiguration getInstance() {
        return thisInstance;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
        DBHandler.getInstance().setSchema(schema);
    }

    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    /**
     * Don't let anyone instantiate this class
     */
    private DBConfiguration() {
    }

    public void setupDBConfiguration() {
        readConfigurationFromFile();
    }

    /**
     * Reads data from file (config.csv) and writes it in private fields.
     */
    private void readConfigurationFromFile() {
        String pathToConfigurationFile = "./config/config.csv";

        try (
                FileReader fileReader = new FileReader(pathToConfigurationFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            String[] lines = new String[2];

            for (int i = 0; i < lines.length; i++) {
                lines[i] = bufferedReader.readLine();
            }

            String targetString = lines[1];
            String separatorPattern = ",";
            StringTokenizer csvTokenizer = new StringTokenizer(targetString, separatorPattern);

            setHost(csvTokenizer.nextToken());
            setPort(csvTokenizer.nextToken());
            setLoginName(csvTokenizer.nextToken());
            setPassword(csvTokenizer.nextToken());
            setSchema(csvTokenizer.nextToken());
            setDbms(csvTokenizer.nextToken());
        } catch (FileNotFoundException e) {
            // if config file is not exist read configuration manually
            System.out.println("Directory and/or file './config/config.csv' is missing\n" +
                    "Enter database configuration manually:");
            readConfigurationFromConsole();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Input/Output is not working\n" +
                    "Shutdown application");
            System.exit(1);
        }
    }

    /**
     * Reads data from user using console and writes it in private fields.
     */
    private void readConfigurationFromConsole() {
        Scanner consoleInputScanner = new Scanner(System.in);

        System.out.print("Enter hostname: ");
        setHost(consoleInputScanner.nextLine());
        System.out.print("Enter port: ");
        setPort(consoleInputScanner.nextLine());
        System.out.print("Enter login: ");
        setLoginName(consoleInputScanner.nextLine());
        getPasswordFromConsole(consoleInputScanner);
        System.out.print("Enter database (schema): ");
        setSchema(consoleInputScanner.nextLine());
        System.out.print("Enter dbms (by default is mysql, press enter to use it): ");
        String dbms = consoleInputScanner.nextLine();
        if ("".equals(dbms))
            setDbms("mysql");
        else
            setDbms(dbms);
    }

    /**
     * Retrieves user's input like password input.
     * Provides input character hiding (if available).
     *
     * @param consoleInputScanner A Scanner
     */
    private void getPasswordFromConsole(Scanner consoleInputScanner) {
        Console console = System.console();
        String password;

        if (console == null) {
            System.out.print("Enter password: ");
            password = consoleInputScanner.nextLine();
        } else {
            char[] passwordArray = console.readPassword("Enter password: ");
            password = new String(passwordArray);
        }

        setPassword(password);
    }
}
