package testSuit.utils;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import io.cucumber.guice.ScenarioScoped;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.operation.TransactionOperation;
import org.testng.Assert;
import org.xmlunit.util.Convert;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
@ScenarioScoped
public class DbOpsUtil {

    IDatabaseConnection dataSourceConnection = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    IDatabaseConnection iDatabaseConnection;
    DatabaseOperation databaseOperation;

    @SneakyThrows
    public IDataSet csvToDataSet(URL path) {
        return new CsvURLDataSet(path);
    }

    @SneakyThrows
    public void insertDataSetIntoDBByDeletingOtherData(DataSource dataSource, IDataSet idataSet) {
        iDatabaseConnection = new DatabaseDataSourceConnection(dataSource);
        databaseOperation = new TransactionOperation(DatabaseOperation.CLEAN_INSERT);
        iDatabaseConnection
                .getConfig()
                .setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        databaseOperation.execute(iDatabaseConnection, idataSet);
        iDatabaseConnection.close();
    }

    @SneakyThrows
    public void insertDataSetIntoDB(DataSource dataSource, IDataSet idataSet) {
        iDatabaseConnection = new DatabaseDataSourceConnection(dataSource);
        databaseOperation = new TransactionOperation(DatabaseOperation.INSERT);
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
        for (int i = 0; i < cols.length; i++) {
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
    public void executeStatement(DataSource dataSource, String sqlStatement) {
        try {
            dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
            connection = dataSourceConnection.getConnection();
            statement = connection.createStatement();
            statement.execute(sqlStatement);
        } catch (Exception e) {
            log.info("An error occurred while processing results in executeStatement");
            log.info(e.toString());
            throw e;
        } finally {
            assert statement != null;
            statement.close();
            connection.close();
            dataSourceConnection.close();
        }
    }

    @SneakyThrows
    public void executeSelQueryForDataExist(DataSource dataSource, String sqlStatement) {
        try {
            dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
            connection = dataSourceConnection.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sqlStatement);

            if (resultSet.next()) {

                ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Query returned values");

                // Move the cursor back to the start
                resultSet.beforeFirst();

                // Retrieve column count
                int columnCount = resultSet.getMetaData().getColumnCount();
                StringBuilder columnValues = new StringBuilder();

                // Print column names
                for (int i = 1; i <= columnCount; i++) {
                    columnValues.append(resultSet.getMetaData().getColumnName(i)).append("\t | \t");
                }
                ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Column values:- | " + columnValues);

                // Print rows
                StringBuilder rowValues = new StringBuilder();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        rowValues.append(resultSet.getString(i) + "\t | \t");
                    }
                    ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Row values:- | " + rowValues + "\n");
                    rowValues.setLength(0);
                }

            } else {
                Assert.fail("Query does not return any values");
            }

        } catch (Exception e) {
            log.info("An error occurred while processing results in executeSelQuery");
            log.info(e.toString());
            throw e;
        } finally {
            assert statement != null;
            statement.close();
            connection.close();
            dataSourceConnection.close();
        }
    }

    @SneakyThrows
    public void executeSelQueryForDataNotExist(DataSource dataSource, String sqlStatement) {
        try {
            dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
            connection = dataSourceConnection.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sqlStatement);

            if (resultSet.next()) {

                // Move the cursor back to the start
                resultSet.beforeFirst();

                // Retrieve column count
                int columnCount = resultSet.getMetaData().getColumnCount();
                StringBuilder columnValues = new StringBuilder();

                // Print column names
                for (int i = 1; i <= columnCount; i++) {
                    columnValues.append(resultSet.getMetaData().getColumnName(i)).append("\t | \t");
                }
                ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Column values:- | " + columnValues);

                // Print rows
                StringBuilder rowValues = new StringBuilder();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        rowValues.append(resultSet.getString(i) + "\t | \t");
                    }
                    ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Row values:- | " + rowValues + "\n");
                    rowValues.setLength(0);
                }
                Assert.fail("Query returned values");

            } else {
                resultSet.last();
                ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Query returned " + resultSet.getRow() + " rows");
            }

        } catch (Exception e) {
            log.info("An error occurred while processing results in executeSelQueryForDataNotExist");
            log.info(e.toString());
            throw e;
        } finally {
            assert statement != null;
            statement.close();
            connection.close();
            dataSourceConnection.close();
        }
    }


}
