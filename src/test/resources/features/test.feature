Feature: User registration

  Scenario Outline: User registration
    Given user is on home page
    And user opened registration page
    When User submitted registration form with data: "<firstName>", "<lastName>", "<email>","<password>"
    Then user will see success message which contains correct email "<email>"
    Examples:
      | firstName | lastName      | email           | password |
      | TestName  | TestLastName  | testf@gmail.com | 1qaz!QAZ |
      | TestName1 | TestLastName1 | testg@gmail.com | 1qaz!QAZ |



