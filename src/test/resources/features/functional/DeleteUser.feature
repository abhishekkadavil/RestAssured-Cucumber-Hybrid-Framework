Feature: Delete user
  Delete user API scenarios

  @DeleteUserAPI  @DeleteUser01 @All
  Scenario: Delete user
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/deleteUser/scenario1/input/requestBody.json'
    #Updating random data in email
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    #retrieved data from response
    Given request 'CreateUser', retrieve value from path 'id' and store it in 'idValue'
    #Api setup start
    #put retrieved data in the request path
    Given request 'DeleteUser' have context 'idValue' in request path '/users'
    Given request 'DeleteUser' have request body '/createUser/scenario1/input/requestBody.json'
    Given request 'DeleteUser' have random email
    Given request 'DeleteUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call DELETE 'DeleteUser' request
