Feature: Create user
  Create user API scenarios

  @CreateUserAPI  @CreateUser01 @CreateUserAPIPass @All
  Scenario: CreateUser01
  Create new user
    Given start new scenario
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

  @CreateUserAPI  @CreateUser02 @CreateUserAPIPass @All
  Scenario: CreateUser02
  Error is thrown when creating a user with existing email id (Calling same API second time)
    Given start new scenario
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

  @CreateUserAPI  @CreateUser03 @CreateUserAPIPass @All
  Scenario: CreateUser03
  Create new user and validate response body by ignoring specified fields
    Given start new scenario
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

  @CreateUserAPI  @CreateUser04 @CreateUserAPIPass @All
  Scenario: CreateUser04
  Create new user and validate response body for one field only
    Given start new scenario
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

  @CreateUserAPI  @CreateUser05 @CreateUserAPIFail @All
  Scenario: CreateUser05
  Assertion failed when creating a user - Single assertion
    Given start new scenario
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
    And response body should be '/createUser/scenario1/output/responseBody.json'

  @CreateUserAPI  @CreateUser06 @CreateUserAPIFail @All
  Scenario: CreateUser06
  Assertion failed when creating a user - multiple assertion
    Given start new scenario
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
    And response should have 'name' as 'Tenali'
    And response should have 'gender' as 'female'

  @CreateUserAPI  @CreateUser07 @CreateUserAPIPass @All
  Scenario: CreateUser07
  Calling request twice
    Given start new scenario
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

  @CreateUserAPI  @CreateUser08 @CreateUserAPIPass @All
  Scenario: CreateUser08
  Calling two different request
    Given start new scenario
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
    #Api setup start
    Given request have path '/users/4705'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'