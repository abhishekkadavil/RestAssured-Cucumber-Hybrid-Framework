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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.Assert;
import testSuit.utils.ReporterFactory;
import testSuit.utils.RestAssuredLoggingFilter;
import testSuit.utils.TestContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CommonStepDef {

    @Inject
    TestContext testContext;

    private Response response;

    @Given("request {string} have path {string}")
    public void request_have_path(String reqName, String apiPath) {
        String Url =
                testContext.getConfigUtil().getProtocol() + "://" + testContext.getConfigUtil().getHost() + apiPath;
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"URL: "+Url);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(RestAssuredLoggingFilter.getLoggingFilters())
                .baseUri(Url);

        testContext.getRequestBuilder().put(reqName, requestSpecification);
    }

    @SneakyThrows
    @Given("request {string} have request body {string}")
    public void request_have_request_body(String reqName, String apiBodyPath) {

        String content =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+apiBodyPath)));

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(content, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(reqName,content);
        testContext.getRequestBuilder().get(reqName).body(content);
    }

    @Given("request {string} have following headers")
    public void request_have_following_headers(String apiName, DataTable requestHeaders) {
        List<List<String>> rows = requestHeaders.asLists(String.class);
        String ReqHearders="";

        testContext.getRequestBuilder()
                .get(apiName)
                .header("Authorization",testContext.getConfigUtil().getToken());

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(apiName)
                    .header(columns.get(0), columns.get(1));
            ReqHearders = ReqHearders + columns.get(0) +" : " + columns.get(1) + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request hearders", ReqHearders));
    }

    @When("I call POST {string} request")
    public void callPOST(String apiName) {
        response = testContext.getRequestBuilder().get(apiName).post();
        testContext.getResponseContext().put(apiName,response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call DELETE {string} request")
    public void callDELETE(String apiName) {
        response = testContext.getRequestBuilder().get(apiName).delete();
        response.then().log().all();
        testContext.getResponseContext().put(apiName,response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call GET {string} request")
    public void callGET(String apiName) {
        response = testContext.getRequestBuilder().get(apiName).get();
        response.then().log().all();
        testContext.getResponseContext().put(apiName,response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PUT {string} request")
    public void callPUT(String apiName) {
        response = testContext.getRequestBuilder().get(apiName).put();
        response.then().log().all();
        testContext.getResponseContext().put(apiName,response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PATCH {string} request")
    public void callPATCH(String apiName) {
        response = testContext.getRequestBuilder().get(apiName).patch();
        response.then().log().all();
        testContext.getResponseContext().put(apiName,response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @Then("{string} should have response code {string}")
    public void should_have_response_code(String apiName, String expectedResCode) {
        String actualValueResponseStatusCode =
                String.valueOf(testContext.getResponseContext().get(apiName).getStatusCode());
        Assert.assertEquals(actualValueResponseStatusCode,expectedResCode);
    }

    @SneakyThrows
    @Then("{string} should have response body {string} ignoring all extra fields")
    public void should_have_response_body_with_ignoring_all_extra_fields(String apiName, String expectedValue) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
        String actualRes = testContext.getResponseContext().get(apiName).body().asString();
        JSONAssert.assertEquals(expectedRes, actualRes, false);
    }

    @SneakyThrows
    @Then("{string} should have response body {string}")
    public void should_have_response_body(String apiName, String expectedValue) {
            String expectedRes =
                    new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
            String actualRes = testContext.getResponseContext().get(apiName).body().asString();
            JSONAssert.assertEquals(expectedRes, actualRes, true);
    }

    @SneakyThrows
    @Then("{string} should have response body {string} ignoring specified fields")
    public void should_have_response_body_with_ignoring_specified_fields(String apiName, String expectedValue, List<List<String>> cols) {
        String expectedRes =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+expectedValue)));
        String actualRes = testContext.getResponseContext().get(apiName).body().asString();

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

    @Then("{string} should have {string} as {string}")
    public void should_have_value_in_path(String apiName, String jsonpath, String expectedValue) {
        String actualValue = testContext.getResponseContext().get(apiName).then().extract().path(jsonpath).toString();
        Assert.assertEquals(actualValue,expectedValue);
    }

    @Given("request {string} have context {string} in request path {string}")
    public void requestAddedDeleteUserHaveIdValueInRequestPath(String reqName, String retrievedValue, String apiPath) {
        String Url =
                testContext.getConfigUtil().getProtocol() + "://" + testContext.getConfigUtil().getHost() + apiPath + "/"+ testContext.getContextValues().get(retrievedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"URL: "+Url);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(RestAssuredLoggingFilter.getLoggingFilters())
                .baseUri(Url);

        testContext.getRequestBuilder().put(reqName, requestSpecification);

    }

    @Given("request {string}, retrieve value from path {string} and store it in {string}")
    public void requestCreateUserRetrieveValueFromPathIdAndStoreItInIdValue(String apiName, String jsonpath, String contextKey) {
        String extractedValue = testContext.getResponseContext().get(apiName).then().extract().path(jsonpath).toString();
        testContext.getContextValues().putIfAbsent(contextKey,extractedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Extracted value from response body : " + extractedValue);
    }

    @Given("request {string}, put value {string} in path {string}")
    public void requestUpdateUserPutValueInPath(String apiName, String value, String path) {
        String newRequestBody = JsonPath.parse(testContext.getReqBodyContext().get(apiName)).set(path,value).jsonString();

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(newRequestBody, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(apiName,newRequestBody);
        testContext.getRequestBuilder().get(apiName).body(newRequestBody);
    }

    @Given("request {string}, put context value {string} in path {string}")
    public void requestUpdateUserPutContextValueInPath(String apiName, String contextKey, String jsonpath) {

        String newRequestBody =
                JsonPath.parse(testContext.getReqBodyContext().get(apiName)).set(jsonpath,
                        testContext.getContextValues().get(contextKey)).jsonString();

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,"Updated Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,MarkupHelper.createCodeBlock(newRequestBody, CodeLanguage.JSON));

        testContext.getReqBodyContext().put(apiName,newRequestBody);
        testContext.getRequestBuilder().get(apiName).body(newRequestBody);
    }

    @Given("below query parameters for {string}")
    public void addQueryParamInReq(String apiName, DataTable table) {

        List<List<String>> rows = table.asLists(String.class);
        String queryParam="";

        for (List<String> columns : rows) {
            testContext.getRequestBuilder()
                    .get(apiName)
                    .queryParam(columns.get(0), columns.get(1));
            queryParam = queryParam + columns.get(0) +" : " + columns.get(1) + "\n";

        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,queryParam);
    }
}
