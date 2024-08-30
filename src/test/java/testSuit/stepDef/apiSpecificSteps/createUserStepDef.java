package testSuit.stepDef.apiSpecificSteps;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import testSuit.utils.RALoggerUtil;
import testSuit.utils.ReporterFactory;
import testSuit.utils.ScenarioContext;
import testSuit.utils.TestContext;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class createUserStepDef {

    @Inject
    ScenarioContext scenarioContext;

    private Response response;

    @SneakyThrows
    @Given("start create user api specification")
    public void start_create_user_api_specification() {
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + "/users";


        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + scenarioContext.getToken());

        scenarioContext.getRequestBuilder().put(scenarioContext.getReqId(), requestSpecification);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request headers", "Content-Type : application/json"));

    }

    @Given("create user")
    public void create_user() {

//      Request creation
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + "/users";

        String value = TestContext.faker.animal().name()+TestContext.faker.random().hex();
        String content = "{\n" +
                "  \"name\": \"" + value + "\",\n" +
                "  \"gender\": \"male\",\n" +
                "  \"email\": \"" + value + "@gmail.com" + "\",\n" +
                "  \"status\": \"active\"\n" +
                "}";

        response = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + scenarioContext.getToken())
                .body(content)
                .post();

        Assert.assertEquals(response.getStatusCode(), 201);
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);


        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(content, CodeLanguage.JSON));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request headers", "Content-Type : application/json"));


        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(response.getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));


//      Retrieve status from response
        String extractedValue = response.then().extract().path("status").toString();
        scenarioContext.getContextValues().putIfAbsent("status", extractedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Extracted value from response body : " + extractedValue);
        log.info("Extracted value from response body : " + extractedValue);
    }

    @Given("request have random email")
    public void createUserHaveRandomEmail() {

        String randomEmail = TestContext.faker.name().firstName()+ TestContext.faker.random().hex() + "@gmail.com";

        scenarioContext.getReqBodyContext().put(scenarioContext.getReqId(),
                JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set("$.email", randomEmail).jsonString());
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).body(JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set("$.email", randomEmail).jsonString());

    }
}
