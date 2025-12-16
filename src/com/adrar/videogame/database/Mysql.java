package com.adrar.videogame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.adrar.videogame.Env.*;

public class Mysql {

    private static Connection connexion;

    static {
        try {
            connexion = DriverManager.getConnection(DB_URL,DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnect(){
        return connexion;
    }
}
