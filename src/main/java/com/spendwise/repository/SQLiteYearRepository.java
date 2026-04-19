package com.spendwise.repository;

import com.spendwise.domain.Year;
import org.sqlite.SQLiteDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteYearRepository implements InterfaceRepository<Year>, AutoCloseable {
    private static final String JDBC_URL = "jdbc:sqlite:spendwise.db";
    private Connection connection = null;

    public SQLiteYearRepository() {
        openConnection();
        createSchema();
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);

            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea cu baza de date", e);
        }
    }

    private void createSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS years(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "year_value INTEGER NOT NULL UNIQUE, " +
                "is_read_only INTEGER NOT NULL" +
                ");";
        try{
            try (final Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public void create(Year year) {
        String sql = "INSERT INTO years(year_value, is_read_only) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, year.getYear());
            statement.setInt(2, year.isReadOnly() ? 1 : 0);
            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Eroare la salvarea anului: " + year, e);
        }
    }

    @Override
    public Year read(int id) {
        String sql = "SELECT id, year_value, is_read_only FROM years WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Year(
                            rs.getInt("id"),
                            rs.getInt("year_value"),
                            rs.getInt("is_read_only") == 1
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la read pentru id=" + id, e);
        }
    }


    @Override
    public void update(Year year) {
        String sql = "UPDATE years SET year_value = ?, is_read_only = ? WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, year.getYear());
            statement.setInt(2, year.isReadOnly()  ? 1 : 0);
            statement.setInt(3, year.getId());

            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Eroare la actualizarea anului: " + year, e);
        }
    }

    @Override
    public void delete(int id){
        String sql = "DELETE FROM years WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Eroare la stergerea anului cu ID: " + id, e);
        }
    }

    @Override
    public List<Year> getAll() {
        List<Year> result = new ArrayList<>();
        String sql = "SELECT id, year_value, is_read_only FROM years";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Year year = new Year(
                        rs.getInt("id"),
                        rs.getInt("year_value"),
                        rs.getInt("is_read_only") == 1
                );

                result.add(year);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Eroare la getAll()", e);
        }

        return result;
    }

    @Override
    public void close() {
        if (connection != null) {
            try{
                connection.close();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
