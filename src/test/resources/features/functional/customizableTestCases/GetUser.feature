@All @AllAPIs @GetUserAPI @Custom_All
Feature: Get user
  Get user API scenarios

  Background: start scenario
    Given start new scenario

  @GetUserAPI01
  Scenario: GetUserAPI01
  Get user
    #Api setup start
    Given request have path '/users/4519'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '404'

  @GetUserAPI02
  Scenario: GetUserAPI02
  Create new user and retrieve the created user
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/getUser/scenario1/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'

    #retrieved data from response
    Given retrieve value from path 'id' and store it in 'idValue'

    #get specific user using above extraction
    Given request have scenario context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'