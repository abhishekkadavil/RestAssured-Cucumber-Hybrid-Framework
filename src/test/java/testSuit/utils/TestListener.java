package testSuit.utils;

import com.aventstack.extentreports.Status;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class TestListener implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
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
}
