Feature: Update user attribute
  Update user attribute API scenarios

  @UpdateUserAttributeAPI  @UpdateUserAttribute01 @All
  Scenario: Update user attribute API
    Given start new scenario
    Given request have path '/users/4548'
    * request have bearer token in header
    Given request have request body '/updateUserAttribute/scenario1/input/requestBody.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '200'

  @UpdateUserAttributeAPI  @UpdateUserAttribute02 @All
  Scenario: Create user and update its status attribute - API call chaining and read context sharing
    Given start new scenario
    #create user
    #Api setup start
    Given request have path '/users'
    * request have bearer token in header
    Given request have request body '/updateUserAttribute/scenario2/input/createUserRequestBody.json'
    #Updating random data in email
    Given request have random email
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST request
    Then response code should be '201'

    #retrieved data from response
    Given retrieve value from path 'id' and store it in 'idValue'

    #update the status of specific user using above extraction
    #Api setup start
    Given request have context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have request body '/updateUserAttribute/scenario2/input/updateUserRequestBody.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '200'

  @UpdateUserAttributeAPI  @UpdateUserAttribute03 @All
  Scenario: Update only one attribute API
    Given start new scenario
    Given request have path '/users/4548'
    * request have bearer token in header
    * request have request body '/updateUserAttribute/scenario3/input/requestBody.json'
    * put value "male" in path "gender"
    #* request 'UpdateUser', put context value "female" in path "gender"
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '200'
