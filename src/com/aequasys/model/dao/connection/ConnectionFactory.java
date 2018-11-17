package com.aequasys.model.dao.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.Preferences;

public class ConnectionFactory {
    static private Preferences preferences = Preferences.userRoot();
    private static String ip = preferences.get("ip","");
    private static String port = preferences.get("port","");
    private static String sid = preferences.get("sid","");
    private static String username = preferences.get("username","");
    private static String password = preferences.get("password","");
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":"+port+":"+sid+"",username,password);
    }
}
