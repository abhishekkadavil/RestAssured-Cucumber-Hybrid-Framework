# RestAssured-Cucumber-Hybrid-Framework
RestAssured cucumber hybrid framework template

# Test execution

* Test application: `https://gorest.co.in/`
* DB setup: `https://github.com/abhishekkadavil/localstack`

## We can execute the test in different ways
* Locally
  * Through maven
    * `mvn clean test` to execute test through maven.
    * To execute specific tags from command line we can use mvn test -> `-Dcucumber.filter.tags="@All"`
  * Through the `TestSuitRunner.java` class
* Rerun failed cases: Add `rerun:target/failedrerun.txt` in the plugin section.

In any point the execution in framework is start from `runners.TestSuitRunner.java` class. Test that need to execute are defined in the features files present in the Features folder, The set of test cases are identified by appropriate tag name using the tags section in TestRunner class.

## Test execution configurations

* Test API used from `https://gorest.co.in/`
* **Execution mode(parallel or sequentially):** In scenarios method in `runners.TestSuitRunner.java` class, we can set if the test need to execute parallel or sequentially. Set the parallel flag to true or false.
  * The default test case count can be set from `threadCount` section in `pom` file `maven-surefire-plugin`
* **Report configuration:** Test report high level(suit level) configured will be done through `@BeforeClass` and `@AfterClass` annotations of TestNG inside TestRunner class. Each scenario wise step will be added to report through `stepDef.Hooks`.

**RunnerHelper** class will be shared among `runners.TestRunner` and `runners.FailedTCRunner` class to implement code re-usability.

# Test case creation
Test cases are defined in the Features folder in the form of `.feature` file. We can create test case for any API with some predefined steps.
We can create test case in two-way
* Customisable API test cases - This is long steps and have very much customisable options.
* API specific test cases - This have shot steps and have to create classes for each API.
## Customisable API test cases
```feature
  @CreateUserAPI  @CreateUser01 @CreateUserAPIPass @All
  Scenario: Create new user
    #Adding request path
    #Given request have path '<request path>'
    Given request have path '/users'

    #Add query parameters for request
    Given request have below query parameters
      | employeeID | UQS786980d7 |

    #Adding request body from json
    #Given request have request body '<request body path>'
    Given request have request body '/createUser/scenario1/input/requestBody.json'

    #Updating the body with random data like email
    Given request have random email

    #Adding request with headers
    Given request have following headers
      | Content-Type        | application/json |

    #Call post method for the request
    #When I call (POST||DELETE||GET...etc) request
    When I call POST request

    #Retrive from response and strore it in a variable
    #Given retrieve "<json path>" from "<API name>" response and store it in '<unique key where value to be stored>'
    Given retrieve "id" from "CreateUser" response and store it in 'idValue'

    #Put retrieved data in the request path
    #Given request have context '<unique key where value to be stored>' in request path '<req path>'
    Given request have context 'idValue' in request path '/users'

    #Put retrieved data in the request body from value defined in the feature file
    #Given put value "<value to be added in the request>" in path "<JSON path>"
    Given put value "male" in path "gender"

    #Put retrieved data in the request body, from value retrived and stored during the execution
    #Given put context value "<context key which the value stored during the retierval>" in path "<JSON path>"
    Given put context value "female" in path "gender"
    
    # multi part data handling
    # request have following string multi part data
    * request have following string multi part data
      | projectId     | 2                        |
      | awardName     | Builder of the Year 2022 |
    # request have following multi part images
    * request have following multi part images
      | projectAwardPhoto | /Builder/Project/Award/Upload-Award/TC01/input/img1.png |
    # request have following multi part files
    * request have following multi part files
      | projectDocument | /Builder/Project/Documents/Upload-Project-Document/TC01/input/TestPDFfile.pdf |

```
## API specific test cases
Here we have to create specific class for each API eg: [createUserStepDef.java](src%2Ftest%2Fjava%2FtestSuit%2FstepDef%2FapiSpecificSteps%2FcreateUserStepDef.java)
```feature
  @E2E  @E2E01 @E2EPass @All_E2E @All
  Scenario: Create user - API specific method 1
    Given start new scenario
    * create user

  @E2E  @E2E02 @E2EPass @All_E2E @All
  Scenario: Create user - API specific method 2
    Given start new scenario
    #Api setup start
    * start create user api specification
    * request have request body '/createUser/scenario1/input/requestBody.json'
    When I call POST request
    Then response code should be '422'
```

## API call chaining - Dynamic request creation
	* Add request value from step - Given put value "male" in path "gender"
		* Added API call chaining without context sharing - 
			* create request
			* update specific value in the request
	* Add request value from context - @UpdateUserAttribute03(Given put context value "female" in path "gender")
		* Added API call chaining with context sharing - 
			* call a request
			* extract id from the request
			* create another request using the retrieved id
			* Given request have context 'idValue' in request path '/users'
	* Add request url parameter from context - @GetUserAPI02 - Given request have context 'idValue' in request path '/users'

## Request specification step building logic:
* Created reqId in ScenarioContext hence sharing is easy between steps. reqId will act as a scenario id key to build request specification map.
* Generate and put reqId in `@Given("start new scenario")` using `scenarioContext.setReqId(scenarioContext.generateReqId());`
* What will happen during chaining the request?
  * We are using `@Given("start new scenario")` when ever we start a scenario. When we are chaining two different APIs in a single scenario, We dont have to use `@Given("start new scenario")` twice i.e we can avoid generating new reqId.  Eg: CreateUser02, CreateUser07. If we have duplicate steps in same scenario the latest step will override the value.
    That is suppose of we have steps like below
      ```feature
        Given request have path '/users'
        Given request have path '/users/5153'
      ```
* During the first step execution request path should be '/users', When ever the second step execute the request path should be '/users/5153'

## Assertions
All the assertion common assertion steps are present in [CommonAssertions.java](src%2Ftest%2Fjava%2FtestSuit%2FstepDef%2FCommonAssertions.java)
```feature

    #validating response code
    #Then response code should be '<201||400||200 etc>'
    Then response code should be '201'

    #Validating response body with ignoring all extra fields
    And response body should be '/createUser/scenario1/output/responseBody.json' ignoring all extra fields

    #Validating response body with ignoring specified extra fields
    And response body should be '/createUser/scenario3/output/responseBody.json' ignoring specified fields
    | email | id |

    #Validating response body without ignoring all extra fields
    And response body should be '/createUser/scenario2/output/responseBody.json'

    #Validating response body for one field only
    And response should have 'name' as 'Tenali Ramakrishna'

```

## Mock creation
For creating mocks we are using wiremock.
```feature
  @MockAPI01 @MockAPIAll
  Scenario: Create new user
    #Mock setup start by adding url to mock
    #Given create (POST||GET||PATCH...etc) mock '<mock name>' to URL '<path>'
    Given create POST mock 'mock' to URL '/users'

    #Add JSON request body to mock
    #Given '<mock name>' external call expects json request body '<request body path>'
    Given 'mock' external call expects json request body '/wiremockFeatures/scenario1/mock/requestBody.json'

    #Add XML request body to mock
    #Given '<mock name>'external call expects xml request body '<request body path.txt>'

    #Add headers to request body
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |

    #Add JSON response body and status to mock
    #Given '<mock name>' external call with json response body '<response body path>' and status '<status>'
    Given 'mock' external call with json response body '/wiremockFeatures/scenario1/mock/responseBody.json' and status '201'

    #Add XML response body and status to mock
    Given '<mock name>' external call with xml response body '<response body path>' and status '<status>'

    #Add text response body and status to mock
    Given '<mock name>' external call with text response body '<response body path>' and status '<status>'

    #Add text response body and status to mock
    Given '<mock name>' external call with timeout

    #Add add mock to wiremock server
    #Given stub '<mock name>'
    Given stub 'mock'
    #mock setup end

    #We can call the corresponding API with above mock

    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/wiremockFeatures/scenario1/input/requestBody.json'
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    When I call POST 'CreateUser' request
    #Assertions
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/wiremockFeatures/scenario1/output/responseBody.json' ignoring all extra fields

    #Mock support: Added url pattern step to mock
    Given create GET mock 'mock' to URL pattern '/abc/def'

    #Mock support: Added response with no body
    Given 'mock' external call status '200'


```

## DB Operations
```feature

  @DbOps01 @DbOpsAll
  Scenario: update DB with CSV and validate - SUCCESS - with ignore columns

    #Execute statement
    #Given execute statement "<sql statement>"
    Given execute statement "delete from users"

    #Update DB with CSV
    #Given execute statement "<csv path(users.csv) and table name txt file(table-ordering.txt) path>"
    Given db is updated with "testData/DbOps/scenario1/input/"

```

## Authentication support
Auth token can be passed from maven command line as well as config file. If the maven token parameter is empty then the token will be fetched from config file.
* Cookie based
  * Passing token through `maven : mvn clean install -DCookieToken = authToken=saascsacsac`
  ```feature
    Given request have cookie token
  ```
* bearer token
  * Passing token through maven : `mvn clean install -Dtoken=68623a1c855aebc18cece732e35d920240db7deaeb49d74581729d57ad940987`
  ```feature
    Given request have bearer token in header
  ```

## DB Assertion
```feature

    #Assert DB with CSV
    #Then Expect '<csv path(users.csv) and table name txt file(table-ordering.txt) path>'
    Then Expect 'testData/DbOps/scenario1/output/'

    #Assert DB with CSV by ignoring specified columns
    #Then Expect '<csv path(users.csv) and table name txt file(table-ordering.txt) path>' with ignore columns
    #  | <column name> |
    Then Expect 'testData/DbOps/scenario1/output/' with ignore columns
      | user_id |
      
    #Execute select query to validate data exist in DB
    #Then validate data exist for select query "<sql query>"
    Then validate data exist for select query "select * from users where name = 'abhishek kadavil'"

    #Execute select query to validate data does not exist in DB
    #Then validate data does not exist for select query "<sql query>"
    Then validate data does not exist for select query "select * from users where name = 'abhishek kadavil1'"
    
    #Execute select query from file to validate data exist in DB
    #Then validate data exist for select query from file "<sql file>"
    Then validate data exist for select query from file "/DbOps/scenario9/SelQuery.sql"

    #Execute select query from file to validate data does not exist in DB
    #Then validate data does not exist for select query from file "<sql query>"
    Then validate data does not exist for select query from file "/DbOps/scenario11/SelQuery.sql"
    
    #Execute select query to validate data exist in DB with context
    Then validate data exist for select query '<sql query>' and where condition as a context value '<context value>'
    Then validate data exist for select query 'select * from users where name = ' and where condition as a context value 'name'
    
    #Execute select query to validate data exist in DB with context
    Then validate data not exist for select query '<sql query>' and where condition as a context value '<context value>'
    Then validate data not exist for select query 'select * from users where name = ' and where condition as a context value 'name'
    
    #Execute select query from file to validate data exist in DB
    Then validate data exist for select query from file "<sql query>" and context value '<context value>'
    Then validate data exist for select query from file "/DbOps/scenario18/SelQuery.sql" and context value 'name'

    #Execute select query from file to validate data does not exist in DB
    Then validate data not exist for select query from file "<sql query>" and context value '<context value>'
    Then validate data not exist for select query from file "/DbOps/scenario17/SelQuery.sql" and context value 'name'

```

# Test reporting:

**Suit level configuration:**
Test report high level(suit level) configured will be done through `@BeforeClass` and `@AfterClass` annotations of TestNG inside TestRunner class. each scenario wise step will be added to report through `stepDef.Hooks`

**Scenario level configuration:**
We only have to create test in extend report in the scenario level configuration that is done using `@Before`

**Step level configuration:**
For test step status management are using listener class named `com.utils.TestListener` which implements cucumber plugin `ConcurrentEventListener`. Using this plugin we are managing the status of the test cases by monitoring test steps. We are handling three main status **FAILED, PASSED, SKIPPED**. Since we have all the steps in `stepDef` package, we added request response and other related details to report through the same classes in the package.

# Context sharing
All the features which are common in scenario level like **responseContext, reqBodyContext, contextValues, requestBuilder, configuration and wiremock server** are done through `com.utils.ScenarioContext`. i.e. During the execution if we want to share the data between steps or scenarios, we need to use ScenarioContext.
* ScenarioContext is marked with `@ScenarioScoped`, so the class will have separate copy of instance for each scenario
* We are using google-guice for DI


# Other Features
* Added `google-juice` and `cucumber-juice` for managing the state of class object
* Added the `@ScenarioScoped`(the object of a class marked this annotation is only created for one scenario and destroyed after the use)
  * Added functionality of ScenarioContext to accommodate all the common function in a scenario perspective eg: contextValues
* The output of the test execution like response body, status, DB values etc. can be logged in report for the later use, so
  didn't create any other mechanism for that.
  **RestAssuredLoggingFilter:** Adding rest assured logs to console including all the details of request and response.
  **Configuration:** Configurations for the test suits are done through `com.utils.ConfigUtil` interface which extends `org.aeonbits.owner.Config`. Suit level configuration are done in `ScenarioContext` class.

# Why and why not
* RestAsssured
  * +Ve: Want to have more control on the framework
  * -Ve: Need good programming knowledge  
* OOPS, used in framework
  * `Runnerhelper` class
* Design pattern used
  * KISS
  * DI injection in Test context
* Why not use grass hopper extend report plugin - it's not support cucumber 7, It's not that much flexible as I wanted
* Why use Google guice instead of pico container or spring
  * google guice - can do DI(object creation) with annotations and have `@ScenarioScoped` annotation which will make the state management easy
  * pico container - Do not have `@ScenarioScoped` annotation
  * spring - this is complex
* Why not Cucumber+JUnit - cucumber junit will not allow us to execute scenarios in parallel only feature files in parallel, but TestNG can.
* Logging - Slf4J
  * For logging there are multiple loggers are available like log4J, JDK logger etc., so once we implement logging in 
    our framework it would be difficult to switch from one library to another. But if we use facade API like Slf4J we can do that easily because we are implementing Slf4J interfaces not logging library like log4J directly. ie we are using Slf4J conjunction with logging libraries(log4J etc.).


# Feature need to add
* dockerized the framework
  * https://codefresh.io/blog/not-ignore-dockerignore-2/
* Need to run test case from feature file instead of test runner file
* Integrate with slack - https://www.youtube.com/watch?v=BsLFhe_1By8&list=PLNky9jSTCKG3j0WwqMDFOrr3XMlaSsKOY&index=12
* Need to use builder in lombok to build request body
* need to add support for graphQL
* Have to generalise step - `Given request 'CreateUser' have random email`
* Test case with multiple types of data - Scenario outline mode
* Need to add step def for **Clear db and add csv file to DB** by using `insertDataSetIntoDBByDeletingOtherData` method in `DbOpsUtil.java`
* Need to add log support
* Need to handle OAuth, OAuth 2.0, Cookies based authentication and other authentications(https://www.youtube.com/watch?v=w-1EvCAszgE&list=PL6flErFppaj08LFolWioWyjfSGtCWcvVp&index=3)
