                              Royalkid XML Parser

What is it?
-----------

Royalkid XML Parser.jar is a Java command line application for exporting data
from MySQL database to XML file.

How to use?
-----------

You can run JAR packaged applications with the Java launcher (java command).
The basic command is:

java -jar jar-file.jar

To run the application from the JAR file that is in another directory, you
must specify the path of that directory:

java -jar path/app.jar

Description
-----------

* Royalkid XML Parser.jar - startup Java application.

* config.csv - file that contains parameters to connect to the remote database.

* README.txt - file that contains short application description and help.

* output - directory that contains exported files (by default output.xml).

Instructions
------------

Check to ensure that you have the recommended version of Java installed for
your operating system:

  https://java.com/en/download/installed8.jsp

To download Java for your operating system visit:

  http://www.java.com/en/download

If you have installed Java follow instructions below to run the application.

* MS Windows

  - Open a Command Prompt window.

  - Change working directory to directory with application (use 'cd' command)

    cd C:\Users\Admin\Desktop\Royalkid_XML_Parser

  - Run JAR packaged application using 'java' command

    java -jar .\Royalkid_XML_Parser.jar

  - Follow onscreen instructions

* Mac OS X or Linux

  - Open a Terminal.

  - Change working directory to directory with application (use 'cd' command)

    cd ~/Desktop/Royalkid_XML_Parser

  - Run JAR packaged application using 'java' command

    java -jar ./Royalkid_XML_Parser.jar

  - Follow onscreen instructions

A file that contains exported data from the database will have saved in
'output' directory (by default 'output.xml').

Version
-------

1.0.1 2015/11/07

Contacts
--------

waon@ukr.net

                     Copyright (C) 2015 Vladimir Pantasenko.