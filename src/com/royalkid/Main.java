package com.royalkid;

import com.royalkid.util.FileHandler;
import com.royalkid.util.HelpMenu;

/**
 * @author Vladimir Pantasenko
 * @since JDK1.8
 */
public class Main {
    public static void main(String[] args) {
        String defaultFileName = "price.xml";
        FileHandler fileHandler = FileHandler.getInstance();
        if (args.length == 1) {
            if (args[0].equals("help") || args[0].equals("h")) {
                HelpMenu.printReadme();
                System.exit(0);
            }
            fileHandler.createXMLFileFromDB(args[0]);
        } else
            fileHandler.createXMLFileFromDB(defaultFileName);
    }
}
