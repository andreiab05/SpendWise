package com.spendwise.repository;

import com.spendwise.domain.MonthlyBudgetEntry;
import org.sqlite.SQLiteDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteMonthlyBudgetEntryRepository implements InterfaceRepository<MonthlyBudgetEntry>, AutoCloseable {
    private static final String JDBC_URL = "jdbc:sqlite:spendwise.db";
    private Connection connection = null;

    public SQLiteMonthlyBudgetEntryRepository() {
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
            throw new RuntimeException("An error came up while trying to connect to the data base", e);
        }
    }

    private void createSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS monthly_budget_entries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "year_value INTEGER NOT NULL, " +
                "month_value INTEGER NOT NULL, " +
                "category_name TEXT NOT NULL, " +
                "money_spent REAL NOT NULL, " +
                "monthly_budget REAL NOT NULL, " +
                "UNIQUE(year_value, month_value, category_name)" +
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
    public void create(MonthlyBudgetEntry entry) {
        String sql = "INSERT INTO monthly_budget_entries(year_value, month_value, category_name, money_spent, monthly_budget) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entry.getYear());
            statement.setInt(2, entry.getMonth());
            statement.setString(3, entry.getCategoryName());
            statement.setFloat(4, entry.getMoneySpent());
            statement.setFloat(5, entry.getMonthlyBudget());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    entry.setId(rs.getInt(1));
                }
            }
            
        } catch(SQLException e){
            throw new RuntimeException("Error at saving the entry: " + entry, e);
        }
    }

    @Override
    public MonthlyBudgetEntry read(int id) {
        String sql = "SELECT id, year_value, month_value, category_name, money_spent, monthly_budget FROM monthly_budget_entries WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MonthlyBudgetEntry(
                            rs.getInt("id"),
                            rs.getInt("year_value"),
                            rs.getInt("month_value"),
                            rs.getString("category_name"),
                            rs.getFloat("money_spent"),
                            rs.getFloat("monthly_budget")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error at reading id=" + id, e);
        }
    }


    @Override
    public void update(MonthlyBudgetEntry entry) {
        String sql = "UPDATE monthly_budget_entries SET year_value = ?, month_value = ?, category_name = ?, money_spent = ?, monthly_budget = ? WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entry.getYear());
            statement.setInt(2, entry.getMonth());
            statement.setString(3, entry.getCategoryName());
            statement.setFloat(4, entry.getMoneySpent());
            statement.setFloat(5, entry.getMonthlyBudget());
            statement.setInt(6, entry.getId());

            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Error at updating the entry: " + entry, e);
        }
    }

    @Override
    public void delete(int id){
        String sql = "DELETE FROM years WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Error at deleting entry with ID: " + id, e);
        }
    }

    @Override
    public List<MonthlyBudgetEntry> getAll() {
        List<MonthlyBudgetEntry> result = new ArrayList<>();
        String sql = "SELECT id, year_value, month_value, category_name, money_spent, monthly_budget FROM monthly_budget_entries;";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonthlyBudgetEntry entry = new MonthlyBudgetEntry(
                        rs.getInt("id"),
                        rs.getInt("year_value"),
                        rs.getInt("month_value"),
                        rs.getString("category_name"),
                        rs.getFloat("money_spent"),
                        rs.getFloat("monthly_budget")
                );

                result.add(entry);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error method getAll()", e);
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
