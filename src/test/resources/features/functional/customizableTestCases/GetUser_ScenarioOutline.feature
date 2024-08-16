Feature: Get user
  Get user API scenarios

  Background: start scenario
    Given start new scenario

  @GetUserAPISceOut @GetUserAPISceOut01 @Custom_All @All
  Scenario Outline: GetUser API test for '<userID>'
  Get user
    #Api setup start
    Given request have path '/users/<userID>'
    * request have bearer token in header
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET request
    Then response code should be '404'
    Examples:
      | userID |
      | 4519   |
      | 4520   |
      | 4521   |
      | 4522   |