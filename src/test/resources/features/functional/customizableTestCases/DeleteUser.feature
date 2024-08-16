Feature: Delete user
  Delete user API scenarios

  Background: start scenario
    Given start new scenario

  @DeleteUserAPI  @DeleteUser01 @Custom_All @All
  Scenario: DeleteUser01:
  Delete user
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/deleteUser/scenario1/input/requestBody.json'
    #Updating random data in email
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'
    #retrieved data from response
    Given retrieve value from path 'id' and store it in 'idValue'
    #Api setup start
    #put retrieved data in the request path
    Given request have context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have request body '/createUser/scenario1/input/requestBody.json'
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call DELETE request
