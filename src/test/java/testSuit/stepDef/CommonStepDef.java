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
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testSuit.utils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ParamConfig.UpdateStrategy.REPLACE;
import static io.restassured.config.ParamConfig.paramConfig;

/**
 * @author Abhishek Kadavil
 */
@Slf4j
public class CommonStepDef {

    @Inject
    ScenarioContext scenarioContext;

    @Inject
    TestContext testContext;

    private Response response;

    @Given("start new scenario")
    public void newReq() {
        scenarioContext.setReqId(scenarioContext.generateReqId());
    }

    @Given("request have path {string}")
    public void request_have_path(String apiPath) {
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + apiPath;
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);

        //timeout for
        config = config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5000)
                        .setParam("http.socket.timeout", 5000)
                        .setParam("http.connection-manager.timeout", 5000));

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url);

        scenarioContext.getContextValues().put(ConstUtils.PATH, apiPath);

        scenarioContext.getRequestBuilder().put(scenarioContext.getReqId(), requestSpecification);
    }

    @Given("request have cookie token")
    public void request_have_path() {
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).cookie(scenarioContext.getCookieToken());
    }

    @Given("request have bearer token in header")
    public void request_have_bearer_token_in_header() {
        scenarioContext.getRequestBuilder()
                .get(scenarioContext.getReqId())
                .header("Authorization", "Bearer " + scenarioContext.getToken());
    }

    @SneakyThrows
    @Given("request have request body {string}")
    public void request_have_request_body(String apiBodyPath) {

        String content =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/test/resources/testData" + apiBodyPath)));

        scenarioContext.getContextValues().put(ConstUtils.BODY, content);

        scenarioContext.getReqBodyContext().put(scenarioContext.getReqId(), content);
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).body(content);
    }

    @Given("request have following headers")
    public void request_have_following_headers(DataTable requestHeaders) {
        List<List<String>> rows = requestHeaders.asLists(String.class);
        String ReqHearders = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .header(columns.get(0), columns.get(1));
            ReqHearders = ReqHearders + columns.get(0) + " : " + columns.get(1) + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request hearders", ReqHearders));
    }

    @When("I call POST request")
    public void callPOST() {
        response = scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).post();
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Request body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(scenarioContext.getContextValues().get(ConstUtils.BODY), CodeLanguage.JSON));

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(scenarioContext.getResponseContext().get(scenarioContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call DELETE request")
    public void callDELETE() {
        response = scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).delete();
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(scenarioContext.getResponseContext().get(scenarioContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call GET request")
    public void callGET() {
        response = scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).get();
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(scenarioContext.getResponseContext().get(scenarioContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PUT request")
    public void callPUT() {
        response = scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).put();
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(scenarioContext.getResponseContext().get(scenarioContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @When("I call PATCH request")
    public void callPATCH() {
        response = scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).patch();
        scenarioContext.getResponseContext().put(scenarioContext.getReqId(), response);

        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response code",
                String.valueOf(scenarioContext.getResponseContext().get(scenarioContext.getReqId()).getStatusCode())));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Response hearders", response.getHeaders().toString()));
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Response body");
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO,
                MarkupHelper.createCodeBlock(response.getBody().asString(), CodeLanguage.JSON));
    }

    @Given("request have scenario context {string} in request path {string}")
    public void requestAddedDeleteUserHaveIdValueInRequestPath(String retrievedValue, String apiPath) {
        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + apiPath + "/" + scenarioContext.getContextValues().get(retrievedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .filters(new RALoggerUtil())
                .baseUri(Url);

        scenarioContext.getRequestBuilder().put(scenarioContext.getReqId(), requestSpecification);

    }

    @Given("retrieve value from path {string} and store it in {string}")
    public void requestCreateUserRetrieveValueFromPathIdAndStoreItInIdValue(String jsonpath, String contextKey) {
        String extractedValue = scenarioContext.getResponseContext().get(scenarioContext.getReqId()).then().extract().path(jsonpath).toString();
        scenarioContext.getContextValues().putIfAbsent(contextKey, extractedValue);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "Extracted value from response body : " + extractedValue);
    }

    @Given("put value {string} in path {string}")
    public void requestUpdateUserPutValueInPath(String value, String path) {
        String newRequestBody = JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set(path, value).jsonString();

        scenarioContext.getReqBodyContext().put(scenarioContext.getReqId(), newRequestBody);
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).body(newRequestBody);
    }

    @Given("put scenario context value {string} in path {string}")
    public void requestUpdateUserPutContextValueInPath(String contextKey, String jsonpath) {

        String newRequestBody =
                JsonPath.parse(scenarioContext.getReqBodyContext().get(scenarioContext.getReqId())).set(jsonpath,
                        scenarioContext.getContextValues().get(contextKey)).jsonString();

        scenarioContext.getReqBodyContext().put(scenarioContext.getReqId(), newRequestBody);
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).body(newRequestBody);
    }

    @Given("request have below query parameters")
    public void addQueryParamInReq(DataTable table) {

        List<List<String>> rows = table.asLists(String.class);
        String queryParam = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .queryParam(columns.get(0), columns.get(1));
            queryParam = queryParam + columns.get(0) + " : " + columns.get(1) + "\n";

        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, queryParam);
    }

    @Given("request have following string multi part data")
    public void request_have_following_form_data(DataTable multiPartValues) {
        List<List<String>> rows = multiPartValues.asLists(String.class);
        String ReqFormdata = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .multiPart(columns.get(0), columns.get(1));
            ReqFormdata = ReqFormdata + columns.get(0) + " : " + columns.get(1) + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request form data", ReqFormdata));
    }

    @Given("request have following multi part images")
    public void request_have_following_images_form_data(DataTable imagePath) {
        List<List<String>> rows = imagePath.asLists(String.class);

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .multiPart(columns.get(0),
                            new File(System.getProperty("user.dir") + "/src/test/resources/testData" + columns.get(1)),
                            "image/gif");
            ReporterFactory
                    .getInstance()
                    .getExtentTest()
                    .log(Status.INFO,
                            MarkupHelper
                                    .createCodeBlock("Request form data :" + columns.get(0),
                                            System.getProperty("user.dir") + "/src/test/resources/testData" + columns.get(1)));
        }
    }

    @Given("request have following multi part files")
    public void request_have_following_file_form_data(DataTable imagePath) {
        List<List<String>> rows = imagePath.asLists(String.class);

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .multiPart(columns.get(0),
                            new File(System.getProperty("user.dir") + "/src/test/resources/testData/Square-feet-area-1" + columns.get(1)));
            ReporterFactory
                    .getInstance()
                    .getExtentTest()
                    .log(Status.INFO,
                            MarkupHelper
                                    .createCodeBlock("Request form data :" + columns.get(0),
                                            System.getProperty("user.dir") + "/src/test/resources/testData/Square-feet-area-1" + columns.get(1)));
        }
    }

    @Given("request get following multi parameter from scenario context")
    public void request_get_following_multi_parameter_from_context(DataTable contextDataTable) {

        String formKey = "";
        String formValue = "";

        String formdata = "";

        List<List<String>> rows = contextDataTable.asLists(String.class);

        for (List<String> columns : rows) {

            formKey = columns.get(0);
            formValue = scenarioContext.getContextValues().get(columns.get(1));

            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .multiPart(formKey, formValue);
            formdata = formdata + formKey + " : " + formValue + "\n";
        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock("Request formdata", formdata));
    }

    @Given("put {string} in scenario context value {string}")
    public void put_value_in_context_value_token_in_token(String value, String contextKey) {
        scenarioContext.getContextValues().put(contextKey, value);
        log.info("put value " + value + " in " + contextKey);
    }

    @Given("request replace below query parameters")
    public void replaceQueryParamInReq(DataTable table) {

        List<List<String>> rows = table.asLists(String.class);
        String queryParam = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .config(config().paramConfig(paramConfig().queryParamsUpdateStrategy(REPLACE)))
                    .queryParam(columns.get(0), columns.get(1));
            queryParam = queryParam + columns.get(0) + " : " + columns.get(1) + "\n";

        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, queryParam);
    }

    @Given("put scenario context value {string} in cookie token")
    public void put_context_value_in_token(String contextKey) {
        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).cookie(scenarioContext.getContextValues().get(contextKey));
    }

    @Given("request have scenario context {string} in request path pattern {string}")
    public void requestAddedDeleteUserHaveIdValueInRequestPathPattern(String retrievedValue, String patternToReplace) {

        String newAPIPath = scenarioContext.getContextValues().get(ConstUtils.PATH).replaceAll(patternToReplace, scenarioContext.getContextValues().get(retrievedValue));
        scenarioContext.getContextValues().put(ConstUtils.PATH, newAPIPath);

        String Url =
                TestContext.configUtil.getProtocol() + "://" + TestContext.configUtil.getHost() + newAPIPath;
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, "URL: " + Url);

        scenarioContext.getRequestBuilder().get(scenarioContext.getReqId()).baseUri(Url);

    }

    @Then("request have below scenario context query parameters")
    public void request_have_below_context_query_parameters(DataTable table) {
        String currentPath = scenarioContext.getContextValues().get(ConstUtils.PATH) + "?";
        List<List<String>> rows = table.asLists(String.class);
        String queryParam = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .queryParam(columns.get(0), scenarioContext.getContextValues().get(columns.get(1)));
            queryParam = queryParam + columns.get(0) + " : " + scenarioContext.getContextValues().get(columns.get(1)) + "\n";
            currentPath = currentPath + columns.get(0) + "=" + scenarioContext.getContextValues().get(columns.get(1)) + "&";

        }
        scenarioContext.getContextValues().put(ConstUtils.PATH, currentPath);
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, queryParam);
    }

    @Then("add below scenario context value to query parameters")
    public void add_below_context_value_to_query_parameters(DataTable table) {
        List<List<String>> rows = table.asLists(String.class);
        String queryParam = "";

        for (List<String> columns : rows) {
            scenarioContext.getRequestBuilder()
                    .get(scenarioContext.getReqId())
                    .queryParam(columns.get(0), scenarioContext.getContextValues().get(columns.get(1)));
            queryParam = queryParam + columns.get(0) + " : " + scenarioContext.getContextValues().get(columns.get(1)) + "\n";

        }
        ReporterFactory.getInstance().getExtentTest().log(Status.INFO, queryParam);
    }

    @Given("add image from path{string} in aws request")
    public void add_image_from_path_string_in_aws_request(String imagePath) {

        File imagefile = new File(System.getProperty("user.dir") + "/src/test/resources/testData" + imagePath);

        // Convert the image file to a byte array
        byte[] imageBytes = null;
        try (FileInputStream fis = new FileInputStream(imagefile)) {
            imageBytes = fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scenarioContext.getRequestBuilder()
                .get(scenarioContext.getReqId())
                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .urlEncodingEnabled(false)
//                .filters(new RALoggerUtil())
                .header("Content-Type","image/jpeg")
                .queryParam("X-Amz-Algorithm", "AWS4-HMAC-SHA256")
                .queryParam("X-Amz-Content-Sha256", "UNSIGNED-PAYLOAD")
                .queryParam("X-Amz-Credential", "xamzcredential")
                .queryParam("X-Amz-Date", "xamzdate")
                .queryParam("X-Amz-Expires", "3600")
                .queryParam("X-Amz-Signature", "xamzsignature")
                .queryParam("X-Amz-SignedHeaders", "host")
                .queryParam("x-amz-acl", "public-read")
                .queryParam("x-id", "PutObject")
                .body(imageBytes);

        ReporterFactory
                .getInstance()
                .getExtentTest()
                .log(Status.INFO,
                        MarkupHelper
                                .createCodeBlock("Request image :",
                                        System.getProperty("user.dir") + "/src/test/resources/testData" + imagePath));


    }

    @Given("add video from path{string} in aws request")
    public void add_video_from_path_string_in_aws_request(String videoPath) {

        File imagefile = new File(System.getProperty("user.dir") + "/src/test/resources/testData" + videoPath);

        // Convert the image file to a byte array
        byte[] videoBytes = null;
        try (FileInputStream fis = new FileInputStream(imagefile)) {
            videoBytes = fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scenarioContext.getRequestBuilder()
                .get(scenarioContext.getReqId())
                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .urlEncodingEnabled(false)
//                .filters(new RALoggerUtil())
                .header("Content-Type","video/mp4")
                .queryParam("X-Amz-Algorithm", "AWS4-HMAC-SHA256")
                .queryParam("X-Amz-Content-Sha256", "UNSIGNED-PAYLOAD")
                .queryParam("X-Amz-Credential", "xamzcredential")
                .queryParam("X-Amz-Date", "xamzdate")
                .queryParam("X-Amz-Expires", "3600")
                .queryParam("X-Amz-Signature", "xamzsignature")
                .queryParam("X-Amz-SignedHeaders", "host")
                .queryParam("x-amz-acl", "public-read")
                .queryParam("x-id", "PutObject")
                .body(videoBytes);

        ReporterFactory
                .getInstance()
                .getExtentTest()
                .log(Status.INFO,
                        MarkupHelper
                                .createCodeBlock("Request image :",
                                        System.getProperty("user.dir") + "/src/test/resources/testData" + videoPath));


    }
}
