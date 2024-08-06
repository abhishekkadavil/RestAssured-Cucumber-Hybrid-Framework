package testSuit.stepDef.apiSpecificSteps;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.testng.Assert;
import testSuit.utils.RALoggerUtil;
import testSuit.utils.ReporterFactory;
import testSuit.utils.TestContext;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class createUserStepDef {

    @Inject
    TestContext testContext;

    private Response response;

    Faker faker = new Faker();

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
                .header("Authorization", "Bearer " + testContext.getToken());

        testContext.getRequestBuilder().put(testContext.getReqId(), requestSpecification);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request headers", "Content-Type : application/json"));

    }

    @Given("create user")
    public void create_user() {

//      Request creation
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + "/users";

        String value = this.faker.animal().name();
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
                .header("Authorization", "Bearer " + testContext.getToken())
                .body(content)
                .post();

        Assert.assertEquals(response.getStatusCode(), 201);
        testContext.getResponseContext().put(testContext.getReqId(), response);


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
        testContext.getContextValues().putIfAbsent("status", extractedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Extracted value from response body : " + extractedValue);
        log.info("Extracted value from response body : " + extractedValue);
    }
}
