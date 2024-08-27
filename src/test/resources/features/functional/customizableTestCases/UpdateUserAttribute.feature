Feature: Update user attribute
  Update user attribute API scenarios

  Background: start scenario
    Given start new scenario

  @UpdateUserAttributeAPI  @UpdateUserAttribute01 @Custom_All @All
  Scenario: UpdateUserAttribute01
  Update user attribute API
    Given request have path '/users/4519'
    * request have bearer token in header
    Given request have request body '/updateUserAttribute/scenario1/input/requestBody.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '404'

  @UpdateUserAttributeAPI  @UpdateUserAttribute02 @All
  Scenario: UpdateUserAttribute02
  Create user and update its status attribute - API call chaining and read context sharing
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
    Given request have scenario context 'idValue' in request path '/users'
    * request have bearer token in header
    Given request have request body '/updateUserAttribute/scenario2/input/updateUserRequestBody.json'
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '200'

  @UpdateUserAttributeAPI  @UpdateUserAttribute03 @All
  Scenario: UpdateUserAttribute03
  Update only one attribute API
    Given request have path '/users/4519'
    * request have bearer token in header
    * request have request body '/updateUserAttribute/scenario3/input/requestBody.json'
    * put value "male" in path "gender"
    #* request 'UpdateUser', put context value "female" in path "gender"
    Given request have following headers
      | Content-Type        | application/json |
    #Api setup end
    When I call PATCH request
    Then response code should be '404'
