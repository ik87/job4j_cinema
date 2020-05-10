package persistence;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Executer {
    private final Connection connection;

    public Executer(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(update);
        statement.close();
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException {
        PreparedStatement psmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        T value = handler.handler(psmt);
        psmt.close();
        return value;
    }
}