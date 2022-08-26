Feature: Get user list
  Get user list API scenarios

  @GetUserListAPI  @GetUserListAPI01 @All
  Scenario: Get user list
    #Api setup start
    Given request 'GetUserList' have path '/users'
    Given request 'GetUserList' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call GET 'GetUserList' request
    Then 'GetUserList' should have response code '200'