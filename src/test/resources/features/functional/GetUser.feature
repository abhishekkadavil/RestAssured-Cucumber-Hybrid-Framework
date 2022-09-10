Feature: Get user
  Get user API scenarios

  @GetUserAPI  @GetUserAPI01 @All
  Scenario: Get user
    #Api setup start
    Given request 'GetUser' have path '/users/3739'
    Given request 'GetUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET 'GetUser' request
    Then 'GetUser' should have response code '200'

  @GetUserAPI  @GetUserAPI02 @All
  Scenario: Create new user and retrieve the created user
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/getUser/scenario1/input/requestBody.json'
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'

    #retrieved data from response
    Given request 'CreateUser', retrieve value from path 'id' and store it in 'idValue'

    #get specific user using above extraction
    Given request 'GetUser' have context 'idValue' in request path '/users'
    Given request 'GetUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET 'GetUser' request
    Then 'GetUser' should have response code '200'