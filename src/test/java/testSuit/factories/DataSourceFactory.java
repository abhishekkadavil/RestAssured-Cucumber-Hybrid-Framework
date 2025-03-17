package testSuit.factories;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;
import testSuit.contexts.TestContext;

import javax.sql.DataSource;

/**
 * @author Abhishek Kadavil
 */
public class DataSourceFactory {

    @SneakyThrows
    public static DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(TestContext.configUtil.getPostgresDbURL());
        dataSource.setUser(TestContext.configUtil.getPostgresUsername());
        dataSource.setPassword(TestContext.configUtil.getPostgresPassword());
        return dataSource;
    }
}
