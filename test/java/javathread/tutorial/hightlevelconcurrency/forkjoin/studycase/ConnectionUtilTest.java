package javathread.tutorial.hightlevelconcurrency.forkjoin.studycase;

import javathread.tutorial.ConnectionUtil;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtilTest {
    @Test
    void testConnection() {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection()){
            System.out.println("success connect");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
