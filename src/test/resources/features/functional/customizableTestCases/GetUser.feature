Feature: Get user
  Get user API scenarios

  @GetUserAPI  @GetUserAPI01 @Custom_All @All
  Scenario: GetUserAPI01
  Get user
    Given start new scenario
    #Api setup start
    Given request have path '/users/4519'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'

  @GetUserAPI  @GetUserAPI02 @Custom_All @All
  Scenario: GetUserAPI02
  Create new user and retrieve the created user
    Given start new scenario
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
    Given request have context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'