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
        features = "classpath:features",
        dryRun = false,
        glue = "testSuit.stepDef",
        tags = "@AllAPIs or @AllUTs",
        plugin={"testSuit.utils.TestListener",
                "rerun:target/failedrerun.txt"
        }
)
public class TestSuitRunner extends AbstractTestNGCucumberTests {

    /**
     * Test can be executed parallel or sequentially;
     * Set the parallel = true to execute test parallel,
     * false to execute test sequentially
     */
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
