package persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ResultHandler<T> {
    T handler(PreparedStatement preparedStatement) throws SQLException;
}
