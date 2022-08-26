package testSuit.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.google.inject.Inject;
import io.cucumber.datatable.DataTable;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@ScenarioScoped
public class MockerUtility {

    @Inject
    TestContext testContext;

    /**
     * Wiremock server
     */
    private WireMockServer wireMockServer;

    /**
     * Map of wiremock wireMockBuilderMap.
     */
    Map<String, MappingBuilder> wireMockBuilderMap = new HashMap<>();

    /**
     * Wiremock port
     */
    private String wiremockPort = testContext.getConfigUtil().getWiremockPort();

    @Before
    public void startWireMock() {
        log.info("Start wiremock");
        wireMockServer = new WireMockServer(options().port(Integer.valueOf(wiremockPort)));
        wireMockServer.start();
    }

    @After
    public void stopWireMock() {
        System.out.println("Wiremock server closed");
        wireMockServer.stop();
    }

    @Given("create POST mock {string} to URL {string}")
    public void createMockBuilderMapForPost(String mappingName, String url) {
        wireMockBuilderMap.putIfAbsent(mappingName, post(url));
    }

    @Given("create PATCH mock {string} to URL {string}")
    public void createMockBuilderMapForPatch(String mappingName, String url) { wireMockBuilderMap.putIfAbsent(mappingName, patch(urlEqualTo(url)));
    }

    @Given("create GET mock {string} to URL {string}")
    public void createMockBuilderMapForGet(String mappingName, String url) {
        wireMockBuilderMap.putIfAbsent(mappingName, get(url));
    }

    @Given("create DELETE mock {string} to URL {string}")
    public void createMockBuilderMapForDELETE(String mappingName, String url) { wireMockBuilderMap.putIfAbsent(mappingName, delete(url)); }

    @SneakyThrows
    @Given("{string} external call expects json request body {string}")
    public void addJsonRequestBody(String mappingName, String requestBodyPath) {

        String requestJson =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+requestBodyPath)));

        wireMockBuilderMap
                .get(mappingName)
                .withRequestBody(equalToJson(requestJson, true, true));

    }

    @SneakyThrows
    @Given("{string} external call expects xml request body {string}")
    public void addXMLRequestBody(String mappingName, String requestBodyPath) {

        String request =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/com" +
                        "/integration/step/definition/"+requestBodyPath)));
        wireMockBuilderMap
                .get(mappingName)
                .withRequestBody(equalToXml(request));

    }

    @Given("{string} external call expects following headers")
    public void addRequestHeaders(String mappingName, DataTable table) {

        List<List<String>> rows = table.asLists(String.class);

        for (List<String> columns : rows) {
            wireMockBuilderMap
                    .get(mappingName)
                    .withHeader(columns.get(0), equalTo(columns.get(1)));

        }
    }

    @SneakyThrows
    @Given("{string} external call with json response body {string} and status {string}")
    public void getJsonResponseBodyAndStatus(String mappingName, String responseBodyPath, String statusCode) {

        String responseJson =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testData"+responseBodyPath)));

        wireMockBuilderMap
                .get(mappingName)
                .willReturn(new ResponseDefinitionBuilder()
                        .withBody(responseJson)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withStatus(Integer.parseInt(statusCode)));

    }

    @SneakyThrows
    @Given("{string} external call with xml response body {string} and status {string}")
    public void getXMLResponseBodyAndStatus(String mappingName, String responseBodyPath, String statusCode) {

        String response =
                new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/com" +
                        "/integration/step/definition/"+responseBodyPath)));
        wireMockBuilderMap
                .get(mappingName)
                .willReturn(new ResponseDefinitionBuilder()
                        .withBody(response)
                        .withHeader("Content-Type", "text/xml; charset=UTF-8")
                        .withStatus(Integer.parseInt(statusCode)));

    }

    @SneakyThrows
    @Given("{string} external call with text response body {string} and status {string}")
    public void getTextResponseBodyAndStatus(String mappingName, String responseBodyPath,
                                             String statusCode) {

        Path filePath =
                Path.of(System.getProperty("user.dir") + "/src/test/resources/com/integration/step/definition/" + responseBodyPath);
        String responseJson = Files.readString(filePath);

        wireMockBuilderMap
                .get(mappingName)
                .willReturn(new ResponseDefinitionBuilder()
                        .withBody(responseJson)
                        .withHeader("Date", "Mon, 15 Jun 2022 04:33:38 GMT")
                        .withHeader("Server", "Apache")
                        .withHeader("Set-Cookie", "")
                        .withHeader("Connection", "close")
                        .withHeader("Content-Type", "text/html;charset=Shift_JIS")
                        .withStatus(Integer.parseInt(statusCode)));

    }

    @SneakyThrows
    @Given("{string} external call with timeout")
    public void getTimeOut(String mappingName) {

        String responseJson = "";

        wireMockBuilderMap
                .get(mappingName)
                .willReturn(new ResponseDefinitionBuilder()
                        .withBody(responseJson)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withFixedDelay(10000));

    }

    @Given("Stub {string}")
    public void stub(String mappingName) {
        stubFor(wireMockBuilderMap.get(mappingName));
    }

}
