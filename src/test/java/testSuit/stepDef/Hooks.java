package testSuit.stepDef;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import testSuit.runners.RunnerHelper;
import testSuit.utils.ReporterFactory;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {

        //reporter scenario add
        ExtentTest test = RunnerHelper.extent.createTest(scenario.getName());
        ReporterFactory.getInstance().setExtentTestList(test);
    }

}
