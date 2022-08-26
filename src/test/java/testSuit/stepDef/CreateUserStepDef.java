package testSuit.stepDef;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import net.datafaker.Faker;
import testSuit.utils.ReporterFactory;
import testSuit.utils.TestContext;

public class CreateUserStepDef {

    @Inject
    TestContext testContext;
    Faker faker = new Faker();

    @Given("request {string} have random email")
    public void createuserHaveRandomUserid(String apiName) {

        String randomEmail = faker.name().firstName() + "@gmail.com";

        testContext.getReqBodyContext().put(apiName,
                JsonPath.parse(testContext.getReqBodyContext().get(apiName)).set("$.email",randomEmail).jsonString());
        testContext.getRequestBuilder().get(apiName).body(JsonPath.parse(testContext.getReqBodyContext().get(apiName)).set("$.email",randomEmail).jsonString());

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated email in request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(testContext.getReqBodyContext().get(apiName), CodeLanguage.JSON));

    }
}
