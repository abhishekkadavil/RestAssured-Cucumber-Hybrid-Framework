Feature: E2E test scenarios
  E2E scenarios for create - write API specific steps

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