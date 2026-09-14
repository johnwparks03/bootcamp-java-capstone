@catalog
Feature: Catalog management

  Background:
    Given the catalog contains at least one book

  Scenario: A user can browse the paginated catalog
    When I request the first page of the catalog
    Then the catalog response should be successful and paginated
    And the first catalog book should contain required fields

  Scenario: A user can search for a book by title
    When I search using the first catalog book title
    Then the catalog response should be successful and paginated
    And the results should include the first catalog book

  Scenario: A user can request pagination and sorting
    When I request the catalog sorted by "title" in "asc" order with page size 1
    Then the catalog response should be successful and paginated
    And the catalog page should be 0 with size 1

  Scenario: Searching for an unknown book returns no results
    When I search for a title that does not exist
    Then the catalog response should be successful and paginated
    And the catalog content should be empty

  Scenario: A user can view book details
    When I request details for the first catalog book
    Then the book details response should be 200
    And the book details should contain complete information

  Scenario: Requesting an unknown book returns not found
    When I request book details for ID "00000000-0000-0000-0000-000000000000"
    Then the book details response should be 404
    And the error field should be "NOT_FOUND"