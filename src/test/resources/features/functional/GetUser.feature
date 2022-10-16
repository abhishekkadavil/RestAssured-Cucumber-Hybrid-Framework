Feature: Get user
  Get user API scenarios

  @GetUserAPI  @GetUserAPI01 @All
  Scenario: Get user
    Given start new scenario
    #Api setup start
    Given request have path '/users/5153'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'

  @GetUserAPI  @GetUserAPI02 @All
  Scenario: Create new user and retrieve the created user
    Given start new scenario
    #Api setup start
    Given request have path '/users'
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
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'