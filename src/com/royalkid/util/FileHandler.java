package com.royalkid.util;

import com.royalkid.db.DBConfiguration;
import com.royalkid.db.DBConnector;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

/**
 * Maintains output's file operations.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class FileHandler {
    private static FileHandler thisInstance = new FileHandler();
    private DBConnector dbConnector = DBConnector.getInstance();

    public static FileHandler getInstance() {
        return thisInstance;
    }

    /**
     * Don't let anyone instantiate this class
     */
    private FileHandler() {
    }

    /**
     * Creates a new buffered line-output stream that uses to create and write
     * output file (by default 'output.xml') contains data from database.
     *
     * @param fileName A String contains custom name of output file
     */
    public void createXMLFileFromDB(String fileName) {
        Connection connection = dbConnector.openConnection();
        Path pathToOutputFile = Paths.get("./output/" + fileName);
        OutputXMLFile outputXMLFile = new OutputXMLFile(pathToOutputFile);

        try (BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(pathToOutputFile.toString()), Charset.forName("UTF-8").newEncoder()))) {

            outputXMLFile.setConnection(connection);
            outputXMLFile.setOutputStream(outputStream);

            DBConfiguration conf = DBConfiguration.getInstance();
            System.out.println("Exporting data from " + conf.getHost() +
                    ":" + conf.getPort() + "/" + conf.getSchema());

            outputXMLFile.addHeadings();
            outputXMLFile.addCategories();
            outputXMLFile.addItemlist();
            outputXMLFile.addEndings();

            System.out.println("Data successfully exported to " + pathToOutputFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not write data to " + pathToOutputFile.toString());
            outputXMLFile.deleteExistingOutputFile(pathToOutputFile);
        } finally {
            dbConnector.closeConnection();
        }
    }
}
