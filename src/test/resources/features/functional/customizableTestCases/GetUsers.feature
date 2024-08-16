Feature: Get user list
  Get user list API scenarios

  Background: start scenario
    Given start new scenario

  @GetUserListAPI  @GetUserListAPI01 @Custom_All @All
  Scenario: GetUserListAPI01
  Get user list
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '200'