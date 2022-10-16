Feature: Get user list
  Get user list API scenarios

  @GetUserListAPI  @GetUserListAPI01 @All
  Scenario: Get user list
    Given start new scenario
    #Api setup start
    Given request have path '/users'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'