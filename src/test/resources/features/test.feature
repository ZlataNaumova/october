Feature: User registration

  Scenario Outline: User registration
    Given user is on home page
    And user opened registration page
    When User submitted registration form with data: "<firstName>", "<lastName>", "<password>"
    Then user will see success message which contains correct email
    Examples:
      | firstName | lastName      | password |
      | TestName  | TestLastName  | 1qaz!QAZ |
      | TestName1 | TestLastName1 | 1qaz!QAZ |
