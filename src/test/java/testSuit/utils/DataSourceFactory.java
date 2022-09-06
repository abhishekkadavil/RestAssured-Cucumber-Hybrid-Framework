package testSuit.utils;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    @SneakyThrows
    public static DataSource getDataSource() {
        TestContext testContext = new TestContext();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(testContext.getConfigUtil().getPostgresDbURL());
        dataSource.setUser(testContext.getConfigUtil().getPostgresUsername());
        dataSource.setPassword(testContext.getConfigUtil().getPostgresPassword());
        return dataSource;
    }
}
