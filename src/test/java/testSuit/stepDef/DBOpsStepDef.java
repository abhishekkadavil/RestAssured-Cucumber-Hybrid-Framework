package testSuit.stepDef;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import testSuit.utils.DataSourceFactory;
import testSuit.utils.DbOpsUtil;
import testSuit.utils.ReporterFactory;
import testSuit.utils.TestContext;

import javax.sql.DataSource;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.List;

@Slf4j
public class DBOpsStepDef {

    private DataSource dataSource = DataSourceFactory.getDataSource();

    @Inject
    TestContext testContext;

    @Inject
    DbOpsUtil dbOpsUtil;

    @SneakyThrows
    @Given("db is updated with {string}")
    public void updateDbWith(String csvPath) {
        IDataSet setupData = dbOpsUtil.csvToDataSet(new URL(getClass().getResource("/") + csvPath));
        dbOpsUtil.insertDataSetIntoDB(dataSource, setupData);
    }

    @SneakyThrows
    @Given("execute statement {string}")
    public void executeStatement(String sqlStatement) {
        dbOpsUtil.executeStatement(dataSource, sqlStatement);
    }

    @SneakyThrows
    @Then("Expect {string} with ignore columns")
    public void expect(String filename, List<List<String>> cols) {

        IDataSet expectDataSet = dbOpsUtil.csvToDataSet(new URL(getClass().getResource("/") + filename));
        DatabaseDataSourceConnection databaseDataSourceConnection = new DatabaseDataSourceConnection(dataSource);
        IDataSet currentDataSet = databaseDataSourceConnection.createDataSet(expectDataSet.getTableNames());
        currentDataSet = new CachedDataSet(currentDataSet);

//          Add details to Reporting
        addDBValidationToReport(expectDataSet, currentDataSet);

//          DB validation
        dbOpsUtil.compareDataSets(expectDataSet, currentDataSet, cols.get(0).toArray(String[]::new));
        databaseDataSourceConnection.close();
    }

    @SneakyThrows
    @Then("Expect {string}")
    public void expect(String filename) {
        IDataSet expectDataSet = dbOpsUtil.csvToDataSet(new URL(getClass().getResource("/") + filename));
        DatabaseDataSourceConnection databaseDataSourceConnection = new DatabaseDataSourceConnection(dataSource);
        IDataSet currentDataSet = databaseDataSourceConnection.createDataSet(expectDataSet.getTableNames());
        currentDataSet = new CachedDataSet(currentDataSet);

//          Add details to Reporting
        addDBValidationToReport(expectDataSet, currentDataSet);

//          DB validation
        dbOpsUtil.compareDataSets(expectDataSet, currentDataSet);
        databaseDataSourceConnection.close();
    }

    @SneakyThrows
    public void addDBValidationToReport(IDataSet expectDataSet, IDataSet actualDataSet) {
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Validate DB");
        List<String> tableList = List.of(expectDataSet.getTableNames());
        ITable resultTable;

        for (String table : tableList) {
            resultTable = actualDataSet.getTable(table);
            ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Table value for " + resultTable.getTableMetaData().getTableName());

            StringBuilder sb = new StringBuilder();
            int columnCount = resultTable.getTableMetaData().getColumns().length;
            String[] columns = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                sb.append(resultTable.getTableMetaData().getColumns()[i].getColumnName());
                sb.append("\t");
                columns[i] = resultTable.getTableMetaData().getColumns()[i].getColumnName();
            }
            sb.append("\n");
            for (int i = 0; i < resultTable.getRowCount(); i++) {
                for (int j = 0; j < columns.length; j++) {
                    sb.append(resultTable.getValue(i, columns[j]));
                    sb.append("\t");
                }
                sb.append("\n");
            }
            ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(sb.toString()));
        }
    }

    @SneakyThrows
    @Then("validate data exist for select query {string}")
    public void validate_data_exist_for_select_query_string(String sqlStatement) {
        dbOpsUtil.executeSelQueryForDataExist(dataSource, sqlStatement);
    }

    @SneakyThrows
    @Then("validate data does not exist for select query {string}")
    public void validate_data_does_not_exist_for_select_query_string(String sqlStatement) {
        dbOpsUtil.executeSelQueryForDataNotExist(dataSource, sqlStatement);
    }

    @SneakyThrows
    @Then("validate data exist for select query from file {string}")
    public void validate_data_exist_for_select_query_from_file_string(String filePath) {
        String sqlStatement =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+filePath)));
        dbOpsUtil.executeSelQueryForDataExist(dataSource, sqlStatement);
    }

    @SneakyThrows
    @Then("validate data does not exist for select query from file {string}")
    public void validate_data_does_not_exist_for_select_query_from_file_string(String filePath) {
        String sqlStatement =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+filePath)));
        dbOpsUtil.executeSelQueryForDataNotExist(dataSource, sqlStatement);
    }

    @SneakyThrows
    @Then("validate data exist for select query {string} and where condition as a context value {string}")
    public void validate_data_exist_for_select_query_string_and_where_condition_as_a_context_value_string(String sqlStatement, String contextKey) {
        dbOpsUtil.executeSelQueryForDataExist(dataSource, sqlStatement + "'" + testContext.getContextValues().get(contextKey) + "'");
    }

    @SneakyThrows
    @Then("validate data not exist for select query {string} and where condition as a context value {string}")
    public void validate_data_not_exist_for_select_query_string_and_where_condition_as_a_context_value_string(String sqlStatement, String contextKey) {
        dbOpsUtil.executeSelQueryForDataNotExist(dataSource, sqlStatement + "'" + testContext.getContextValues().get(contextKey) + "'");
    }

    @SneakyThrows
    @Then("validate data exist for select query from file {string} and context value {string}")
    public void validate_data_exist_for_select_query_from_file_string_and_context_value_string(String filePath, String contextKey) {
        String sqlStatement =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+filePath)));
        dbOpsUtil.executeSelQueryForDataExist(dataSource, sqlStatement + "'" + testContext.getContextValues().get(contextKey) + "'");
    }

    @SneakyThrows
    @Then("validate data not exist for select query from file {string} and context value {string}")
    public void validate_data_not_exist_for_select_query_from_file_string_and_context_value_string(String filePath, String contextKey) {
        String sqlStatement =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+filePath)));
        dbOpsUtil.executeSelQueryForDataNotExist(dataSource, sqlStatement + "'" + testContext.getContextValues().get(contextKey) + "'");
    }
}