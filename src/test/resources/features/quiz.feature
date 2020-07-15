@quiz
Feature: Checking the home page of Quiz application
  It should allow to select a quiz
  Show the logo
  Show a header
  Show at least a question
  Scenario: Check the available elements in Quiz Home Page
    Given I visit the kavin school quiz url "https://quiz.kavinschool.com"
    When I Select "C Sharp" from the dropdown
    Then I should see "C# and .Net Framework"
    And I should see "Quiz - Kavin School" in the header
    And I should see a Kavin School logo
    And I should see "Question 1 of 10." present in that page