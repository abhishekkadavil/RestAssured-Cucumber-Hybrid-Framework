package testSuit.stepDef;

import com.aventstack.extentreports.Status;
import com.google.inject.Inject;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.Assert;
import testSuit.utils.ReporterFactory;
import testSuit.utils.TestContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class CommonAssertions {

    @Inject
    TestContext testContext;

    @SneakyThrows
    @Then("should have response schema as {string}")
    public void should_have_response_schema(String expectedResSchema) {

        Response response = testContext.getResponseContext().get(testContext.getReqId());

        String responseSchema = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + expectedResSchema)));
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(responseSchema));

        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response schema validation passed");
    }

    @Then("response code should be {string}")
    public void should_have_response_code(String expectedResCode) {
        String actualValueResponseStatusCode =
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode());
        Assert.assertEquals(actualValueResponseStatusCode, expectedResCode);
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response code validation passed");
    }

    @SneakyThrows
    @Then("response body should be {string} ignoring all extra fields")
    public void should_have_response_body_with_ignoring_all_extra_fields(String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, false);

        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response body validation passed");
    }

    @SneakyThrows
    @Then("response body should be non extensible {string}")
    public void should_have_response_body_NON_EXTENSIBLE(String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, JSONCompareMode.NON_EXTENSIBLE);

        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response body should be same as in " + expectedValue);
    }

    @SneakyThrows
    @Then("response body should be {string}")
    public void should_have_response_body(String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, true);
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response body validation passed");
    }

    @SneakyThrows
    @Then("response body should be {string} ignoring specified fields")
    public void should_have_response_body_with_ignoring_specified_fields(String expectedValue, List<List<String>> cols) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();

        List<String> row = cols.get(0);
        Customization[] customizationArray = new Customization[row.size()];
        int i = 0;
        for (String element : row) {
            customizationArray[i] = new Customization(element, (o1, o2) -> true);
            i++;
        }

        JSONAssert.assertEquals(expectedRes, actualRes,
                new CustomComparator(JSONCompareMode.LENIENT, customizationArray));
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response body validation passed");
    }

    @Then("response should have {string} as {string}")
    public void should_have_value_in_path(String jsonpath, String expectedValue) {
        String actualValue = testContext.getResponseContext().get(testContext.getReqId()).then().extract().path(jsonpath).toString();
        Assert.assertEquals(actualValue, expectedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response field validation passed");
    }

    @Then("response should have context value {string} in path {string}")
    public void response_should_have_context_value_in_path(String contextKey, String jsonpath) {
        String extractedValue = testContext.getResponseContext().get(testContext.getReqId()).then().extract().path(jsonpath).toString();
        String valueToBeCompared = testContext.getContextValues().get(contextKey);
        Assert.assertEquals(extractedValue, valueToBeCompared);
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS, "Response field validation passed");
    }

    @Then("response should have context value {string} in array path {string}")
    public void response_should_have_context_value_in_array_path(String contextKey, String jsonpath) {
        String valueToBeCompared = testContext.getContextValues().get(contextKey);

        List<?> list =
                testContext.getResponseContext().get(testContext.getReqId()).then().extract().jsonPath().getList(jsonpath);

        List<String> stringsList = list.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        Assert.assertTrue(stringsList.contains(valueToBeCompared));
        ReporterFactory.getInstance().getExtentTest().log(Status.PASS,
                "Response have " + valueToBeCompared + " in json path " + jsonpath);
    }
}
