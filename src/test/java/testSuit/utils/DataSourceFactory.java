package testSuit.utils;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

/**
 * @author Abhishek Kadavil
 */
public class DataSourceFactory {

    @SneakyThrows
    public static DataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(ScenarioContext.configUtil.getPostgresDbURL());
        dataSource.setUser(ScenarioContext.configUtil.getPostgresUsername());
        dataSource.setPassword(ScenarioContext.configUtil.getPostgresPassword());
        return dataSource;
    }
}
