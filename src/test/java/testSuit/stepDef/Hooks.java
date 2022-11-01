package testSuit.stepDef;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import testSuit.runners.RunnerHelper;
import testSuit.utils.ReporterFactory;

@Slf4j
public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {

        ThreadContext.put("TC_Name", scenario.getName());
        log.info(Thread.currentThread().getName()+" --- "+scenario.getName() + " - execution started");

        //reporter scenario add
        ExtentTest test = RunnerHelper.extent.createTest(scenario.getName());
        ReporterFactory.getInstance().setExtentTestList(test);
    }

}
