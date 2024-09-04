@All @AllAPIs @All_E2E
Feature: E2E test scenarios
  E2E scenarios for create - write API specific steps

  Background: start scenario
    Given start new scenario

  @E2E01 @E2EPass
  Scenario: Create user - API specific method 1
    * create user

  @E2E02 @E2EPass
  Scenario: Create user - API specific method 2
    #Api setup start
    * start create user api specification
    * request have request body '/createUser/scenario1/input/requestBody.json'
    When I call POST request
    Then response code should be '422'