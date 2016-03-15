package com.royalkid.util;

import com.royalkid.db.DBHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Processing file creation and maintenance.
 *
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class OutputXMLFile implements FileInteraction {
    private DBHandler dbHandler = DBHandler.getInstance();
    private Connection connection;
    private BufferedWriter output;
    private String shopName = "Royalkid";
    private Map<String, String> vendorList;

    public void setOutputStream(BufferedWriter output) {
        this.output = output;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Deletes already existing output file.
     * Creates and initializes list of available vendors.
     *
     * @param pathToOutputFile A path to output file
     */
    public OutputXMLFile(Path pathToOutputFile) {
        deleteExistingOutputFile(pathToOutputFile);
        vendorList = new HashMap<>();
        initializeVendors(vendorList);
    }

    /**
     * Creates a initial markup in the output file. Adds e-shop name.
     */
    @Override
    public void addHeadings() {
        writeLineToFile(output, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeLineToFile(output, "");
        writeLineToFile(output, "<e-shop name=\"" + shopName + "\">");
    }

    /**
     * Creates a categories markup in the output file. Adds e-shop name.
     */
    @Override
    public void addCategories() {
        writeLineToFile(output, "");
        writeLineToFile(output, "<categories>");

        ResultSet resultSet = dbHandler.getCategoriesResultSet(connection);
        try {
            while (resultSet.next()) {
                addCategory(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        writeLineToFile(output, "</categories>");
    }

    /**
     * Writes categories data from remote database, prepares string (converts
     * to the right markup) and writes it in output file.
     *
     * @param resultSet A ResultSet. Used to get data from database
     * @throws SQLException
     */
    private void addCategory(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String parent = resultSet.getString("parent");
        String title = resultSet.getString("title");

        String XMLText;

        if (parent.equals("1")) {
            XMLText = "\t<category id=\"" + id
                    + "\">" + title + "</category>";
        } else {
            XMLText = "\t<category id=\"" + id + "\" parent=\""
                    + parent + "\">" + title + "</category>";
        }

        writeLineToFile(output, XMLText);
    }

    /**
     * Creates a item list markup in the output file.
     */
    @Override
    public void addItemlist() {
        writeLineToFile(output, "");
        writeLineToFile(output, "<itemlist>");

        ResultSet resultSet = dbHandler.getItemListResultSet(connection);
        try {
            while (resultSet.next()) {
                addItem(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        writeLineToFile(output, "");
        writeLineToFile(output, "</itemlist>");
    }

    /**
     * Writes item data from remote database, prepares string (converts to the
     * right markup) and writes it in output file.
     *
     * @param resultSet A ResultSet. Used to get data from database
     * @throws SQLException
     */
    private void addItem(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String category = resultSet.getString("category");
        String img = resultSet.getString("img");
        String click = resultSet.getString("click");
        String type = resultSet.getString("type");
        String vendor = resultSet.getString("vendor");
        String article = resultSet.getString("article");
        String name = resultSet.getString("name");
        String price = resultSet.getString("price");
        String description = resultSet.getString("description");

        ArrayList<String> lines = new ArrayList<>();

        String linkImg = "http://royalkid.com.ua" + img;
        String linkClick = "http://royalkid.com.ua/" + click;

        vendor = searchForVendor(name);

        type = checkForSpecialCharacters(type);
        name = checkForSpecialCharacters(name);
        description = checkForSpecialCharacters(description);

        lines.add("\t<item id=\"" + id + "\" category=\"" + category + "\">");
        lines.add("\t\t<link img=\"" + linkImg + "\" click=\"" + linkClick + "\" />");
        lines.add("\t\t<type>" + type + "</type>");
        lines.add("\t\t<vendor>" + vendor + "</vendor>");
        lines.add("\t\t<article>" + article + "</article>");
        lines.add("\t\t<name>" + name + "</name>");
        lines.add("\t\t<price>" + price + "</price>");
        lines.add("\t\t<description>" + description + "</description>");
        lines.add("\t</item>");

        writeLineToFile(output, "");
        for (String currentLine : lines) {
            writeLineToFile(output, currentLine);
        }
    }

    /**
     * Creates a closing markup in the output file.
     */
    @Override
    public void addEndings() {
        writeLineToFile(output, "");
        writeLineToFile(output, "</e-shop>");
    }

    /**
     * Writes string line to output file.
     *
     * @param output A BufferedWriter. Used to write data to output stream
     * @param line   A String. Used to pass string line to output stream
     */
    private void writeLineToFile(BufferedWriter output, String line) {
        try {
            output.write(line);
            // output.newLine();
            output.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes output file (if it exists).
     *
     * @param path A Path to output file
     */
    public void deleteExistingOutputFile(Path path) {
        File file = new File(path.toString());

        if (file.exists() && !file.isDirectory()) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes list of available vendors.
     *
     * @param vendorList A Map. Used to write vendors into HashMap
     */
    private void initializeVendors(Map<String, String> vendorList) {
        vendorList.put("Step-2", "step");
        vendorList.put("Fisher-Price", "fisher-price");
        vendorList.put("Fisher Price", "fisher price");
        vendorList.put("Smoby", "smoby");
        vendorList.put("Zapf Creation", "zapf creation");
        vendorList.put("Little Tikes", "little tikes");
        vendorList.put("KidKraft", "kidkraft");
        vendorList.put("Mondo", "mondo");
        vendorList.put("Rolly Toys", "rolly toys");
        vendorList.put("Maestro", "maestro");
        vendorList.put("Trunki", "trunki");
    }

    /**
     * Search for matching vendors.
     *
     * @param line A String. Used to write vendors into HashMap
     * @return the line contains fined vendor
     */
    private String searchForVendor(String line) {
        for (Map.Entry<String, String> entry : vendorList.entrySet()) {
            if (searchForTextMatching(line, entry.getValue())) {
                return entry.getKey();
            }
        }
        return "";
    }

    /**
     * Search for matching text lines.
     *
     * @param line    A String. Line for search
     * @param pattern A String. Searched pattern
     * @return boolean flag shows if find pattern in current line or not
     */
    private boolean searchForTextMatching(String line, String pattern) {
        String searchLine = line.toLowerCase();
        int searchLineLength = searchLine.length();
        int findMeLength = pattern.length();
        boolean foundIt = false;
        for (int i = 0; i <= (searchLineLength - findMeLength); i++) {
            if (searchLine.regionMatches(i, pattern, 0, findMeLength)) {
                foundIt = true;
                break;
            }
        }
        return foundIt;
    }

    /**
     * Rewrites regular characters to special XML symbols.
     *
     * @param line A String. Line for search
     * @return new updated line
     */
    private String checkForSpecialCharacters(String line) {
        if (searchForTextMatching(line, "&")) {
            line = line.replaceAll("&", "&amp;");
        }
        if (searchForTextMatching(line, "\"")) {
            line = line.replaceAll("\"", "&quot;");
        }
        if (searchForTextMatching(line, "\"")) {
            line = line.replaceAll("\'", "&apos;");
        }
        return line;
    }
}
