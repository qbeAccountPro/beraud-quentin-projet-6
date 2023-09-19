package com.paymybuddy.web.configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class DataBaseSetUp {

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public void setUpTables() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataBaseConfig.getConnection();
            statement = connection.createStatement();

            InputStream inputStream = DataBaseSetUp.class.getResourceAsStream("/scriptTable.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }

            statement.executeUpdate(script.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseConfig.closeStatement(statement);
            dataBaseConfig.closeConnection(connection);
        }
    }

    public void setUpData() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataBaseConfig.getConnection();
            statement = connection.createStatement();

            InputStream inputStream = DataBaseSetUp.class.getResourceAsStream("/scriptData.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }

            statement.executeUpdate(script.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseConfig.closeStatement(statement);
            dataBaseConfig.closeConnection(connection);
        }
    }
}