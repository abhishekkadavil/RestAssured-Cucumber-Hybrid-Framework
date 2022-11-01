package testSuit.stepDef;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.inject.Inject;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.Assert;
import testSuit.utils.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class CommonStepDef {

    @Inject
    TestContext testContext;

    private Response response;

    @Given("start new scenario")
    public void newReq() {
        testContext.setReqId(testContext.generateReqId());
    }

    @Given("request have path {string}")
    public void request_have_path(String apiPath) {
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + apiPath;
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"URL: "+Url);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url);

        testContext.getRequestBuilder().put(testContext.getReqId(), requestSpecification);
    }

    @Given("request have cookie token")
    public void request_have_path() {
        testContext.getRequestBuilder().get(testContext.getReqId()).cookie(testContext.getCookieToken());
    }

    @Given("request have bearer token in header")
    public void request_have_bearer_token_in_header() {
        testContext.getRequestBuilder()
                .get(testContext.getReqId())
                .header("Authorization","Bearer " + testContext.getToken());
    }

    @SneakyThrows
    @Given("request have request body {string}")
    public void request_have_request_body(String apiBodyPath) {

        String content =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+apiBodyPath)));

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(content, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(testContext.getReqId(),content);
        testContext.getRequestBuilder().get(testContext.getReqId()).body(content);
    }

    @Given("request have following headers")
    public void request_have_following_headers(DataTable requestHeaders) {
        List<List<String>> rows = requestHeaders.asLists(String.class);
        String ReqHearders="";

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(testContext.getReqId())
                    .header(columns.get(0), columns.get(1));
            ReqHearders = ReqHearders + columns.get(0) +" : " + columns.get(1) + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request hearders", ReqHearders));
    }

    @When("I call POST request")
    public void callPOST() {
        response = testContext.getRequestBuilder().get(testContext.getReqId()).post();
        testContext.getResponseContext().put(testContext.getReqId(),response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call DELETE request")
    public void callDELETE() {
        response = testContext.getRequestBuilder().get(testContext.getReqId()).delete();
        testContext.getResponseContext().put(testContext.getReqId(),response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call GET request")
    public void callGET() {
        response = testContext.getRequestBuilder().get(testContext.getReqId()).get();
        testContext.getResponseContext().put(testContext.getReqId(),response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PUT request")
    public void callPUT() {
        response = testContext.getRequestBuilder().get(testContext.getReqId()).put();
        testContext.getResponseContext().put(testContext.getReqId(),response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PATCH request")
    public void callPATCH() {
        response = testContext.getRequestBuilder().get(testContext.getReqId()).patch();
        testContext.getResponseContext().put(testContext.getReqId(),response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @Then("response code should be {string}")
    public void should_have_response_code(String expectedResCode) {
        String actualValueResponseStatusCode =
                String.valueOf(testContext.getResponseContext().get(testContext.getReqId()).getStatusCode());
        Assert.assertEquals(actualValueResponseStatusCode,expectedResCode);
    }

    @SneakyThrows
    @Then("response body should be {string} ignoring all extra fields")
    public void should_have_response_body_with_ignoring_all_extra_fields(String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, false);
    }

    @SneakyThrows
    @Then("response body should be non extensible {string}")
    public void should_have_response_body_NON_EXTENSIBLE(String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, JSONCompareMode.NON_EXTENSIBLE);
    }

    @SneakyThrows
    @Then("response body should be {string}")
    public void should_have_response_body(String expectedValue) {
            String expectedRes =
                    new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
            String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();
            JSONAssert.assertEquals(expectedRes, actualRes, true);
    }

    @SneakyThrows
    @Then("response body should be {string} ignoring specified fields")
    public void should_have_response_body_with_ignoring_specified_fields(String expectedValue, List<List<String>> cols) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
        String actualRes = testContext.getResponseContext().get(testContext.getReqId()).body().asString();

        List<String> row = cols.get(0);
        Customization[] customizationArray = new Customization[row.size()];
        int i = 0;
        for (String element : row) {
            customizationArray[i] = new Customization(element, (o1, o2) -> true);
            i++;
        }

        JSONAssert.assertEquals(expectedRes, actualRes,
                new CustomComparator(JSONCompareMode.LENIENT,customizationArray));
    }

    @Then("response should have {string} as {string}")
    public void should_have_value_in_path(String jsonpath, String expectedValue) {
        String actualValue = testContext.getResponseContext().get(testContext.getReqId()).then().extract().path(jsonpath).toString();
        Assert.assertEquals(actualValue,expectedValue);
    }

    @Given("request have context {string} in request path {string}")
    public void requestAddedDeleteUserHaveIdValueInRequestPath(String retrievedValue, String apiPath) {
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + apiPath + "/"+ testContext.getContextValues().get(retrievedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"URL: "+Url);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url);

        testContext.getRequestBuilder().put(testContext.getReqId(), requestSpecification);

    }

    @Given("retrieve value from path {string} and store it in {string}")
    public void requestCreateUserRetrieveValueFromPathIdAndStoreItInIdValue(String jsonpath, String contextKey) {
        String extractedValue = testContext.getResponseContext().get(testContext.getReqId()).then().extract().path(jsonpath).toString();
        testContext.getContextValues().putIfAbsent(contextKey,extractedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Extracted value from response body : " + extractedValue);
    }

    @Given("put value {string} in path {string}")
    public void requestUpdateUserPutValueInPath(String value, String path) {
        String newRequestBody = JsonPath.parse(testContext.getReqBodyContext().get(testContext.getReqId())).set(path,value).jsonString();

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(newRequestBody, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(testContext.getReqId(),newRequestBody);
        testContext.getRequestBuilder().get(testContext.getReqId()).body(newRequestBody);
    }

    @Given("put context value {string} in path {string}")
    public void requestUpdateUserPutContextValueInPath(String contextKey, String jsonpath) {

        String newRequestBody =
                JsonPath.parse(testContext.getReqBodyContext().get(testContext.getReqId())).set(jsonpath,
                        testContext.getContextValues().get(contextKey)).jsonString();

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(newRequestBody, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(testContext.getReqId(),newRequestBody);
        testContext.getRequestBuilder().get(testContext.getReqId()).body(newRequestBody);
    }

    @Given("request have below query parameters")
    public void addQueryParamInReq(DataTable table) {

        List<List<String>> rows = table.asLists(String.class);
        String queryParam="";

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(testContext.getReqId())
                    .queryParam(columns.get(0), columns.get(1));
            queryParam = queryParam + columns.get(0) +" : " + columns.get(1) + "\n";

        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,queryParam);
    }

    @SneakyThrows
    @Then("should have response schema as {string}")
    public void should_have_response_schema(String expectedResSchema) {

        Response response = testContext.getResponseContext().get(testContext.getReqId());

        String responseSchema = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedResSchema)));

        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(responseSchema));
    }

    @Given("request have following string multi part data")
    public void request_have_following_form_data(DataTable multiPartValues) {
        List<List<String>> rows = multiPartValues.asLists(String.class);
        String ReqFormdata="";

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(testContext.getReqId())
                    .multiPart(columns.get(0), columns.get(1));
            ReqFormdata = ReqFormdata + columns.get(0) +" : " + columns.get(1) + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request form data", ReqFormdata));
    }

    @Given("request have following multi part images")
    public void request_have_following_images_form_data(DataTable imagePath) {
        List<List<String>> rows = imagePath.asLists(String.class);

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(testContext.getReqId())
                    .multiPart(columns.get(0),
                            new File(System.getProperty("user.dir")+"/src/test/resources/testData"+columns.get(1)),
                            "image/gif");
            ReporterFactory
                    .getInstance()
                    .getExtentTest()
                    .log(Status.INFO,
                            MarkupHelper
                                    .createCodeBlock("Request form data :" + columns.get(0),
                                            System.getProperty("user.dir")+"/src/test/resources/testData"+columns.get(1)));
        }
    }

    @Given("request have following multi part files")
    public void request_have_following_file_form_data(DataTable imagePath) {
        List<List<String>> rows = imagePath.asLists(String.class);

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(testContext.getReqId())
                    .multiPart(columns.get(0),
                            new File(System.getProperty("user.dir")+"/src/test/resources/testData/Square-feet-area-1"+columns.get(1)));
            ReporterFactory
                    .getInstance()
                    .getExtentTest()
                    .log(Status.INFO,
                            MarkupHelper
                                    .createCodeBlock("Request form data :" + columns.get(0),
                                            System.getProperty("user.dir")+"/src/test/resources/testData/Square-feet-area-1"+columns.get(1)));
        }
    }
}
