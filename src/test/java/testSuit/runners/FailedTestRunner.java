package testSuit.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.io.IOException;

/**
 * @author Abhishek Kadavil
 */
@CucumberOptions(
        features = "@target/failedrerun.txt",
        monochrome = true,
        dryRun = false,
        glue = "testSuit.stepDef",
        plugin = {"com.utils.TestListener",
                "rerun:target/failedrerun.txt"
        })
public class FailedTestRunner extends AbstractTestNGCucumberTests {


    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeClass
    public void beforeClass() throws IOException {
        RunnerHelper.beforeTestSuit();

    }

    @AfterClass
    public void afterClass() {
        RunnerHelper.afterTestSuit();
    }
}
