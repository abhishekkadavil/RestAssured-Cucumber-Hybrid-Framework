@All @AllAPIs @Custom_All @CreateUserAPI
Feature: Create user
  Create user API scenarios

  Background: start scenario
    Given start new scenario

  @Author("JohnDoe")
  @Category("Smoke")
  @CreateUser01 @CreateUserAPIPass
  Scenario: CreateUser01
  Create new user
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario1/input/requestBody.json'
    #Updating random data in email
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response body should be '/createUser/scenario1/output/responseBody.json' ignoring all extra fields

  @Author("JohnDoe")
  @Category("Smoke")
  @CreateUser02 @CreateUserAPIPass
  Scenario: CreateUser02
  Error is thrown when creating a user with existing email id (Calling same API second time)
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario2/input/requestBody1.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    #Calling same API second time
    When I call POST request
    Then response code should be '422'
    And response body should be '/createUser/scenario2/output/responseBody.json'

  @Author("AbhishekKadavil")
  @Category("Regression")
  @CreateUser03 @CreateUserAPIPass
  Scenario: CreateUser03
  Create new user and validate response body by ignoring specified fields
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario3/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response body should be '/createUser/scenario3/output/responseBody.json' ignoring specified fields
    | email | id |

  @Author("AbhishekKadavil")
  @Category("Regression")
  @CreateUser04 @CreateUserAPIPass
  Scenario: CreateUser04
  Create new user and validate response body for one field only
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario4/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response should have 'name' as 'Tenali Ramakrishna'

  @CreateUser05 @CreateUserAPIFail
  Scenario: CreateUser05
  Assertion failed when creating a user - Single assertion
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario5/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response body should be '/createUser/scenario5/output/responseBody.json' ignoring all extra fields

  @CreateUser06 @CreateUserAPIFail
  Scenario: CreateUser06
  Assertion failed when creating a user - multiple assertion
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario6/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response should have 'name' as 'Tenali Ramakrishna'
    And response should have 'gender' as 'male'

  @CreateUser07 @CreateUserAPIPass
  Scenario: CreateUser07
  Calling request twice
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario2/input/requestBody1.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    #Calling same API second time
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario2/input/requestBody1.json'
    Given request have following headers
      | Content-Type        | application/json |
    When I call POST request
    Then response code should be '422'
    And response body should be '/createUser/scenario2/output/responseBody.json'

  @CreateUser08 @CreateUserAPIPass
  Scenario: CreateUser08
  Calling two different request
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario1/input/requestBody.json'
    #Updating random data in email
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    And response body should be '/createUser/scenario1/output/responseBody.json' ignoring all extra fields

    #retrieved data from response
    Given retrieve value from path 'id' and store it in 'idValue'

    #Api setup start
    Given request have scenario context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'