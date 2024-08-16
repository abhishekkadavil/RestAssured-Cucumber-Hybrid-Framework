package testSuit.stepDef;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import testSuit.utils.ReporterFactory;
import testSuit.utils.ScenarioContext;

/**
 * @author Abhishek Kadavil
 */
public class CreateUserStepDef {

    @Inject
    ScenarioContext scenarioContext;

    @Given("request have random email")
    public void createUserHaveRandomEmail() {

        String randomEmail = ScenarioContext.faker.name().firstName() + "@gmail.com";

        scenarioContext.getReqBodyContext().put(scenarioContext.getReqId(),
                JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set("$.email", randomEmail).jsonString());
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).body(JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set("$.email", randomEmail).jsonString());

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Updated email in request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId()), CodeLanguage.JSON));

    }
}
