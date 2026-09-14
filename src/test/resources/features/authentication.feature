@authentication
Feature: Authentication and user profiles

  Scenario: A new user registers successfully
    Given I have valid details for a new user
    When I register the user
    Then the response status should be 201
    And the response field "email" should match the registered email
    And the response field "role" should be "LIBRARIAN"
    And the response field "membershipStatus" should be "ACTIVE"
    And the response field "message" should be "Registration Successful"

  Scenario: A user cannot register with an existing email
    Given a user has already registered
    When I register the same user again
    Then the response status should be 400
    And the response field "error" should be "VALIDATION_ERROR"

  Scenario: A user cannot register with a weak password
    Given I have valid details for a new user
    When I change the password to "weak"
    And I register the user
    Then the response status should be 400

  Scenario: A registered user can log in
    Given a registered user exists
    When I log in with the correct password
    Then the response status should be 200
    And the access token should be present
    And the response field "tokenType" should be "Bearer"
    And the response field "expiresIn" should be 86400
    And the response field "user.role" should be "LIBRARIAN"

  Scenario: Invalid credentials are rejected
    Given a registered user exists
    When I log in with an invalid password
    Then the response status should be 401
    And the response field "error" should be "AUTHENTICATION_FAILED"

  Scenario: An authenticated user can view their profile
    Given I am logged in
    When I request my profile
    Then the response status should be 200
    And the response field "email" should match the registered email
    And the response field "role" should be "LIBRARIAN"
    And the response field "membershipStatus" should be "ACTIVE"
    And the response field "activeReservations" should be present
    And the response field "borrowingHistory" should be present

  Scenario: An unauthenticated user cannot view a profile
    When I request my profile without a token
    Then the response status should be 401