package persistence;

import model.Account;
import model.Place;
import org.apache.commons.dbcp2.BasicDataSource;

import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DbStoreImpl implements DbStore {

    private final static BasicDataSource POOL_CONNECTIONS = new BasicDataSource();
    private final static DbStore INSTANCE = new DbStoreImpl();


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

    public static DbStore getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean setPlace(Collection<Place> places) {
        boolean success = true;
        String sql = "UPDATE hall set state = ?, account_id = ? WHERE place = ?";
        try (Connection conn = POOL_CONNECTIONS.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                for (Place place : places) {
                    int account_id = addAccount(conn, place.getAccount());
                    pstm.setString(3, place.getPlace());
                    pstm.setInt(1, place.getState());
                    pstm.setInt(2, account_id);
                    pstm.addBatch();
                }
                pstm.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                success = false;
                conn.rollback();
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
        String sql = "SELECT place, status FROM Hall";
        try (Connection conn = POOL_CONNECTIONS.getConnection()) {
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Place place = new Place();
                place.setPlace(rs.getString(1));
                place.setState(rs.getInt(2));
                places.add(place);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return places;
    }


    private Integer addAccount(Connection conn, Account account) throws SQLException {
        String sql = "INSERT INTO Account (Name, Phone) VALUES (?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, account.getName());
        pstm.setString(2, account.getPhone());

        pstm.executeUpdate();

        ResultSet generatedKeys = pstm.getGeneratedKeys();
        generatedKeys.next();
        account.setAccountId(generatedKeys.getInt(1));

        return account.getAccountId();
    }
}
