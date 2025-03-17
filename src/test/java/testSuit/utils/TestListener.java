package testSuit.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.ThreadContext;
import testSuit.factories.ReporterFactory;
import testSuit.runners.RunnerHelper;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class TestListener implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        ReporterFactory.getInstance().finisheExtentTest();
    }

    private void handleTestStepFinished(TestStepFinished event) {

        String stepName = "";

        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) event.getTestStep();
            stepName = pickleStepTestStep.getStep().getText();

            Result result = event.getResult();

            if (result.getStatus().name().equalsIgnoreCase("FAILED")) {
                //Failed step reporting
                ReporterFactory.getInstance().getExtentTest().log(Status.FAIL, stepName);
                ReporterFactory.getInstance().getExtentTest().log(Status.FAIL, result.getError().toString());
                log.error(stepName);
                log.error(result.getError().toString());
            }
        }
    }

    private void handleTestCaseStarted(TestCaseStarted event) {

        String featureName = FilenameUtils.getBaseName(event.getTestCase().getUri().toString());
        String scenarioName = event.getTestCase().getName();

        // start reporter with scenario name
        ExtentTest test = RunnerHelper.extent.createTest(featureName+" :: "+scenarioName);
        ReporterFactory.getInstance().setExtentTestList(test);

        // start log with scenario name
        ThreadContext.put("TC_Name", scenarioName);
        log.info(Thread.currentThread().getName() + " --- " + scenarioName + " - execution started");
    }
}
