package Knihy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {

	private static volatile Connection dbConnection;
    private static final String DB_PATH = "C:\\Users\\trnka\\Desktop\\aaa\\sqlite-tools-win-x64-3450300\\myDB.db";

    private dbconnect() {}

    public static Connection getDBConnection() {
        if (dbConnection == null) {
            synchronized (dbconnect.class) {
                if (dbConnection == null) {
                    try {
                        Class.forName("org.sqlite.JDBC");
                        dbConnection = DriverManager.getConnection("jdbc:sqlite:" + getDBPath());
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dbConnection;
    }

    public static void closeConnection() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getDBPath() {
        return DB_PATH;
    }
	}
