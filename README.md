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

```feature
  @CreateUserAPI  @CreateUser01 @CreateUserAPIPass @All
  Scenario: Create new user
    #Adding request path
    #Given request '<API name>' have path '<request path>'
    Given request 'CreateUser' have path '/users'

    #Add query parameters for request
    Given with below query parameters for '<API name>' API
      | employeeID | UQS786980d7 |

    #Adding request body from json
    #Given request '<API name>' have request body '<request body path>'
    Given request 'CreateUser' have request body '/createUser/scenario1/input/requestBody.json'

    #Updating the body with random data like email
    Given request 'CreateUser' have random email

    #Adding request with headers
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |

    #Call post method for the request
    #When I call (POST||DELETE||GET...etc) 'CreateUser' request
    When I call POST 'CreateUser' request

    #Retrive from response and strore it in a variable
    #Given retrieve "<json path>" from "<API name>" response and store it in '<unique key where value to be stored>'
    Given retrieve "id" from "CreateUser" response and store it in 'idValue'

    #Put retrieved data in the request path
    #Given request '<API name>' have context '<unique key where value to be stored>' in request path '<req path>'
    Given request 'DeleteUser' have context 'idValue' in request path '/users'

    #Put retrieved data in the request body from value defined in the feature file
    #Given request '<API name>', put value "<value to be added in the request>" in path "<JSON path>"
    Given request 'UpdateUser', put value "male" in path "gender"

    #Put retrieved data in the request body, from value retrived and stored during the execution
    #Given request '<API name>', put context value "<context key which the value stored during the retierval>" in path "<JSON path>"
    Given request 'UpdateUser', put context value "female" in path "gender"

```
## API call chaining - Dynamic request creation
	* Add request value from step - Given request 'UpdateUser', put value "male" in path "gender"
		* Added API call chaining without context sharing - 
			* create request
			* update specific value in the request
	* Add request value from context - @UpdateUserAttribute03(Given request 'UpdateUser', put context value "female" in path "gender")
		* Added API call chaining with context sharing - 
			* call a request
			* extract id from the request
			* create another request using the retrieved id
			* Given request 'DeleteUser' have 'idValue' in request path '/users'
	* Add request url parameter from context - @GetUserAPI02 - Given request 'UpdateUser' have context 'idValue' in request path '/users'


## Assertions
```feature

    #validating response code
    #Then '<API name>' should have response code '<201||400||200 etc>'
    Then 'CreateUser' should have response code '201'

    #Validating response body with ignoring all extra fields
    And 'CreateUser' should have response body '/createUser/scenario1/output/responseBody.json' ignoring all extra fields

    #Validating response body with ignoring specified extra fields
    And 'CreateUser' should have response body '/createUser/scenario3/output/responseBody.json' ignoring specified fields
    | email | id |

    #Validating response body without ignoring all extra fields
    And 'CreateUser' should have response body '/createUser/scenario2/output/responseBody.json'

    #Validating response body for one field only
    And 'CreateUser' should have 'name' as 'Tenali Ramakrishna'

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

```

# Test reporting:

**Suit level configuration:**
Test report high level(suit level) configured will be done through `@BeforeClass` and `@AfterClass` annotations of TestNG inside TestRunner class. each scenario wise step will be added to report through `stepDef.Hooks`

**Scenario level configuration:**
We only have to create test in extend report in the scenario level configuration that is done using `@Before`

**Step level configuration:**
For test step status management are using listener class named `com.utils.TestListener` which implements cucumber plugin `ConcurrentEventListener`. Using this plugin we are managing the status of the test cases by monitoring test steps. We are handling three main status **FAILED, PASSED, SKIPPED**. Since we have all the steps in `stepDef` package, we added request response and other related details to report through the same classes in the package.

# Context sharing
All the features which are common in scenario level like **responseContext, reqBodyContext, contextValues, requestBuilder, configuration and wiremock server** are done through `com.utils.TestContext`. i.e. During the execution if we want to share the data between steps or scenarios, we need to use TestContext.
* TestContext is marked with `@ScenarioScoped`, so the class will have separate copy of instance for each scenario
* We are using google-guice for DI


# Other Features
* Added `google-juice` and `cucumber-juice` for managing the state of class object
* Added the `@ScenarioScoped`(the object of a class marked this annotation is only created for one scenario and destroyed after the use)
  * Added functionality of TestContext to accommodate all the common function in a scenario perspective eg: contextValues
* The output of the test execution like response body, status, DB values etc. can be logged in report for the later use, so
  didn't create any other mechanism for that.
  **RestAssuredLoggingFilter:** Adding rest assured logs to console including all the details of request and response.
  **Configuration:** Configurations for the test suits are done through `com.utils.ConfigUtil` interface which extends `org.aeonbits.owner.Config`. Suit level configuration are done in `TestContext` class.

# Why and why not
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
