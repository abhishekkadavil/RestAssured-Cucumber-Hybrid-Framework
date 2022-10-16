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

import javax.annotation.Nullable;

public class CreateUserStepDef {

    @Inject
    TestContext testContext;

    @Given("request have random email")
    public void createUserHaveRandomEmail() {

        String randomEmail = TestContext.faker.name().firstName() + "@gmail.com";

        testContext.getReqBodyContext().put(testContext.getReqId(),
                JsonPath.parse(testContext.getReqBodyContext().get(testContext.getReqId())).set("$.email",randomEmail).jsonString());
        testContext.getRequestBuilder().get(testContext.getReqId()).body(JsonPath.parse(testContext.getReqBodyContext().get(testContext.getReqId())).set("$.email",randomEmail).jsonString());

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated email in request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(testContext.getReqBodyContext().get(testContext.getReqId()), CodeLanguage.JSON));

    }
}
