Feature: Update user attribute
  Update user attribute API scenarios

  @UpdateUserAttributeAPI  @UpdateUserAttribute01 @All
  Scenario: Update user attribute API
    Given request 'UpdateUser' have path '/users/3739'
    Given request 'UpdateUser' have request body '/updateUserAttribute/scenario1/input/requestBody.json'
    Given request 'UpdateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH 'UpdateUser' request
    Then 'UpdateUser' should have response code '200'

  @UpdateUserAttributeAPI  @UpdateUserAttribute02 @All
  Scenario: Create user and update its status attribute - API call chaining and read context sharing
    #create user
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/updateUserAttribute/scenario2/input/createUserRequestBody.json'
    #Updating random data in email
    Given request 'CreateUser' have random email
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'

    #retrieved data from response
    Given request 'CreateUser', retrieve value from path 'id' and store it in 'idValue'

    #update the status of specific user using above extraction
    #Api setup start
    Given request 'UpdateUser' have context 'idValue' in request path '/users'
    Given request 'UpdateUser' have request body '/updateUserAttribute/scenario2/input/updateUserRequestBody.json'
    Given request 'UpdateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH 'UpdateUser' request
    Then 'UpdateUser' should have response code '200'

  @UpdateUserAttributeAPI  @UpdateUserAttribute03 @All
  Scenario: Update only one attribute API
    Given request 'UpdateUser' have path '/users/3739'
    Given request 'UpdateUser' have request body '/updateUserAttribute/scenario3/input/requestBody.json'
    Given request 'UpdateUser', put value "male" in path "gender"
    #Given request 'UpdateUser', put context value "female" in path "gender"
    Given request 'UpdateUser' have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH 'UpdateUser' request
    Then 'UpdateUser' should have response code '200'
