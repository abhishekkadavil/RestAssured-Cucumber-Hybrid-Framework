Feature: Wiremock actions
  Wiremock actions scenarios

  @MockAPI01 @MockAPIAll
  Scenario: Create new user
    #mock setup start
    Given create POST mock 'mock' to URL '/users'
    Given 'mock' external call expects json request body '/wiremockFeatures/scenario1/mock/requestBody.json'
    Given 'mock' external call with json response body '/wiremockFeatures/scenario1/mock/responseBody.json' and status '201'
    Given stub 'mock'
    #mock setup end
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/wiremockFeatures/scenario1/input/requestBody.json'
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/wiremockFeatures/scenario1/output/responseBody.json' ignoring all extra fields

  @MockAPI02 @MockAPIAll
  Scenario: Create new user2
    #mock setup start
    Given create POST mock 'mock' to URL '/users'
    Given 'mock' external call expects json request body '/wiremockFeatures/scenario2/mock/requestBody.json'
    Given 'mock' external call with json response body '/wiremockFeatures/scenario2/mock/responseBody.json' and status '201'
    Given stub 'mock'
    #mock setup end
    #Api setup start
    Given request 'CreateUser' have path '/users'
    Given request 'CreateUser' have request body '/wiremockFeatures/scenario2/input/requestBody.json'
    Given request 'CreateUser' have following headers
      | Content-Type        | application/json |
    When I call POST 'CreateUser' request
    Then 'CreateUser' should have response code '201'
    And 'CreateUser' should have response body '/wiremockFeatures/scenario2/output/responseBody.json' ignoring all extra fields