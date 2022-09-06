package testSuit.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = "classpath:features",
        dryRun = false,
        glue = "testSuit.stepDef",
        tags = "@DbOpsAll",
        plugin={"testSuit.utils.TestListener",
                "rerun:target/failedrerun.txt"
        }
)
public class TestSuitRunner extends AbstractTestNGCucumberTests {

    @BeforeClass
    public void beforeClass()
    {
        RunnerHelper.beforeTestSuit();
    }

    @AfterClass
    public void afterClass() { RunnerHelper.afterTestSuit(); }

}
