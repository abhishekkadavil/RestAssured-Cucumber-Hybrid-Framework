package testSuit.utils;

import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import io.cucumber.guice.ScenarioScoped;
import lombok.SneakyThrows;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.TransactionOperation;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
public class DbOpsUtil {

    @SneakyThrows
    public IDataSet csvToDataSet(URL path) {
        return new CsvURLDataSet(path);
    }

    @SneakyThrows
    public void insertDataSetIntoDBByDeletingOtherData(DataSource dataSource, IDataSet idataSet) {
        IDatabaseConnection iDatabaseConnection = new DatabaseDataSourceConnection(dataSource);
        DatabaseOperation databaseOperation = new TransactionOperation(DatabaseOperation.CLEAN_INSERT);
        iDatabaseConnection
                .getConfig()
                .setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        databaseOperation.execute(iDatabaseConnection, idataSet);
        iDatabaseConnection.close();
    }

    @SneakyThrows
    public void insertDataSetIntoDB(DataSource dataSource, IDataSet idataSet) {
        IDatabaseConnection iDatabaseConnection = new DatabaseDataSourceConnection(dataSource);
        DatabaseOperation databaseOperation = new TransactionOperation(DatabaseOperation.INSERT);
        iDatabaseConnection
                .getConfig()
                .setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        databaseOperation.execute(iDatabaseConnection, idataSet);
        iDatabaseConnection.close();
    }

    @SneakyThrows
    public void compareDataSets(IDataSet expectDataSet, IDataSet actualDataSet, String[] cols) {

        List<IColumnFilter> columnFilters = new ArrayList<>();
        DefaultColumnFilter columnFilter = new DefaultColumnFilter();
        for(int i=0;i<cols.length;i++){
            columnFilter.excludeColumn(cols[i]);
        }
        columnFilters.add(columnFilter);

        DatabaseAssertionMode.NON_STRICT_UNORDERED
                .getDatabaseAssertion()
                .assertEquals(expectDataSet, actualDataSet, columnFilters);
    }

    @SneakyThrows
    public void compareDataSets(IDataSet expectDataSet, IDataSet actualDataSet) {
        List<IColumnFilter> columnFilters = new ArrayList<>();
        DatabaseAssertionMode.NON_STRICT_UNORDERED
                .getDatabaseAssertion()
                .assertEquals(expectDataSet, actualDataSet, columnFilters);
    }

    @SneakyThrows
    public void executeStatement(DataSource dataSource, String sqlStatement)
    {
        IDatabaseConnection dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
        Connection connection = dataSourceConnection.getConnection();
        Statement statement = connection.createStatement();

        statement.execute(sqlStatement);
        statement.close();
        connection.close();
        dataSourceConnection.close();
    }
}
