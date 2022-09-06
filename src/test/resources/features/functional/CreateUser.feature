Feature: Create user
  Create user API scenarios

  @CreateUserAPI  @CreateUser01 @CreateUserAPIPass @All
  Scenario: Create new user
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/createUser/scenario1/input/requestBody.json'
    #Updating random data in email
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/createUser/scenario1/output/responseBody.json' ignoring all extra fields

  @CreateUserAPI  @CreateUser02 @CreateUserAPIPass @All
  Scenario: Error is thrown when creating a user with existing email id
    #Api setup start
    Given request '01-CreateUser' have path '/users'
    Given request '01-CreateUser' have request body '/createUser/scenario2/input/requestBody1.json'
    Given request '01-CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST '01-CreateUser' request
    #Api setup start
    Given request '02-CreateUser' have path '/users'
    Given request '02-CreateUser' have request body '/createUser/scenario2/input/requestBody2.json'
    Given request '02-CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST '02-CreateUser' request
    Then '02-CreateUser' should have response code '422'
    And '02-CreateUser' should have response body '/createUser/scenario2/output/responseBody.json'

  @CreateUserAPI  @CreateUser03 @CreateUserAPIPass @All
  Scenario: Create new user and validate response body by ignoring specified fields
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/createUser/scenario3/input/requestBody.json'
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/createUser/scenario3/output/responseBody.json' ignoring specified fields
    | email | id |

  @CreateUserAPI  @CreateUser04 @CreateUserAPIPass @All
  Scenario: Create new user and validate response body for one field only
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/createUser/scenario4/input/requestBody.json'
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have 'name' as 'Tenali Ramakrishna'

  @CreateUserAPI  @CreateUser05 @CreateUserAPIFail @All
  Scenario: Assertion failed when creating a user - Single assertion
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/createUser/scenario5/input/requestBody.json'
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/createUser/scenario1/output/responseBody.json'

  @CreateUserAPI  @CreateUser06 @CreateUserAPIFail @All
  Scenario: Assertion failed when creating a user - multiple assertion
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/createUser/scenario6/input/requestBody.json'
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have 'name' as 'Tenali'
    And 'CreateUser' should have 'gender' as 'female'