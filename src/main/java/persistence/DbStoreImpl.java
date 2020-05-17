package persistence;

import model.Account;
import model.Place;
import org.apache.commons.dbcp2.BasicDataSource;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class DbStoreImpl implements Store {

    private final static BasicDataSource POOL_CONNECTIONS = new BasicDataSource();
    private final static Store INSTANCE = new DbStoreImpl();


    private DbStoreImpl() {
        try {
            URI dbUri = new URI(System.getenv("DB_JOB4j_CINEMA"));
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
            if (dbUri.getUserInfo() != null) {
                POOL_CONNECTIONS.setUsername(dbUri.getUserInfo().split(":")[0]);
                POOL_CONNECTIONS.setPassword(dbUri.getUserInfo().split(":")[1]);
            }
            POOL_CONNECTIONS.setDriverClassName("org.postgresql.Driver");
            POOL_CONNECTIONS.setUrl(dbUrl);
            POOL_CONNECTIONS.setInitialSize(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Store getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean setPlace(Collection<Place> places, Account account) {
        boolean success = true;
        String sql = "UPDATE hall set state = ?, account_id = ? WHERE place = ?";
        try (Connection conn = POOL_CONNECTIONS.getConnection()) {
            conn.setAutoCommit(false);
            Integer account_id = addAccount(conn, account);
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                for (Place place : places) {
                    pstm.setInt(1, place.getState());
                    pstm.setInt(2, account_id);
                    pstm.setString(3, place.getPlace());
                    pstm.executeUpdate();
                }
                conn.commit();
            } catch (Exception e) {
                success = false;
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    @Override
    public Collection<Place> getPlaces() {
        Collection<Place> places = new ArrayList<>();
        String sql = "SELECT place, state, price FROM Hall";
        try (Connection conn = POOL_CONNECTIONS.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Place place = new Place();
                place.setPlace(rs.getString(1));
                place.setState(rs.getInt(2));
                place.setPrice(rs.getFloat(3));
                places.add(place);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return places;
    }


    private Integer addAccount(Connection conn, Account account) throws SQLException {
        String sql = "INSERT INTO Account (Name, Phone) VALUES (?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstm.setString(1, account.getName());
        pstm.setString(2, account.getPhone());

        pstm.executeUpdate();
        try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                account.setAccountId(generatedKeys.getInt(1));
            }
        }

        return account.getAccountId();
    }

    @Override
    public void clear() {
        String sql = createNewTableSql();
        try (Connection connection = POOL_CONNECTIONS.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement st = POOL_CONNECTIONS.getConnection().createStatement()) {
                st.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

   private String createNewTableSql() {
        return "DROP TABLE hall;"
                + "DROP TABLE Account;"

                + "CREATE TABLE Account ("
                + "        Account_id SERIAL PRIMARY KEY,"
                + "        Name varchar(100) NOT NULL,"
                + "        Phone varchar(100) NOT NULL"
                + ");"

                + "CREATE TABLE Hall ("
                + "       Place varchar(10) NOT NULL  PRIMARY KEY,"
                + "       State int,"
                + "       Price float,"
                + "       Account_id INT REFERENCES Account(Account_id)"
                + ");"

                + "INSERT INTO Hall(Place, State, Price, Account_id)"
                + "VALUES"
                + "       ('1.1', 1, 500, null ),"
                + "       ('1.2', 1, 500, null ),"
                + "       ('1.3', 1, 500, null ),"
                + "       ('2.1', 1, 500, null ),"
                + "       ('2.2', 1, 500, null ),"
                + "       ('2.3', 1, 500, null ),"
                + "       ('3.1', 1, 500, null ),"
                + "       ('3.2', 1, 500, null ),"
                + "       ('3.3', 1, 500, null );";
    }

}
