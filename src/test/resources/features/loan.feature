Feature: request a loan

  Background:
    Given User on submit amount page

  Scenario: Bank member can't submit form with invalid data
    Given user clicked on submit offer button
    When User changed the amount to "0"
    Then Continue button is disabled

  Scenario: Bank member can't submit form with invalid data
    Given user clicked on submit offer button
    When User changed the amount to "?'1"
    Then Continue button is disabled

  Scenario: Information is saved on "amount page"
    Given User submitted an offer on amount page
    And Clicked Continue button
    When User clicked back button in browser
    Then Information preserved on amount page

