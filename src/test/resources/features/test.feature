Feature: Adding ad to memo

  Background:
    Given user is on home page
    Then user opened registration page


  Scenario: User registration with random data
    Given user is on registration page
    When user filled all fields
#      | fieldId       | value           |
#      | firstname     | TestName        |
#      | lastname      | TestLastName    |
#      | email         | test@gmail.com  |
#      | password      | 1qaz!QAZ       |
    And submit form
    Then user will see success message which contains correct email



