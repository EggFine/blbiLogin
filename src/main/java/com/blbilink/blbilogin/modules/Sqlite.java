package com.blbilink.blbilogin.modules;


import com.blbilink.blbilogin.load.LoadFunction;
import com.blbilink.blbilogin.utils.PasswordUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sqlite {

    private Connection connection;

    public static Sqlite getSqlite() {
        return LoadFunction.sqlite;
    }
    public Sqlite() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/blbiLogin/players.db");
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, username TEXT, password TEXT)")) {
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(String uuid) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerPlayer(String uuid, String username, String password) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO players (uuid, username, password) VALUES (?, ?, ?)")) {
            statement.setString(1, uuid);
            statement.setString(2, username);
            statement.setString(3, PasswordUtil.hashPassword(password));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPassword(String uuid, String password) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT password FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                String storedPassword = results.getString("password");
                return PasswordUtil.checkPassword(password, storedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(String uuid, String newPassword) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE players SET password = ? WHERE uuid = ?")) {
            statement.setString(1, PasswordUtil.hashPassword(newPassword));
            statement.setString(2, uuid);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}