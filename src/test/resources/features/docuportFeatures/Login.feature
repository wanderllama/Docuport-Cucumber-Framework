Feature: Docuport Login Logout Feature

  Background:

  @login
  Scenario Outline: Login and out as <user>
    Given the <user> is on Docuport login page
    When user <user> enters email in text field
    And user <user> enters password in text field
    And user clicks "login" button
#    And user clicks "continue" button
    Then the <user> is on Docuport home page
    When user clicks "settings" button
    And user clicks "log out" button
    Then the <user> is on Docuport login page

    Examples:
      | user       |
      | SUPERVISOR |
      | CLIENT     |
      | EMPLOYEE   |
      | ADVISOR    |

#
#  @smoke @regression
#  Scenario: Login and out as employee
#    When user enters username for <employee>
#    And user enters <password> for <employee>
#    And user clicks login button
#    And user closes left navigation window
#    Then user should see the home page for <employee>
#    When user clicks the user logo
#    And the user clicks Log out
#    Then the user return to the login page
#
#  @smoke @regression
#  Scenario: Login and out as supervisor
#    When user enters username for <supervisor>
#    And user enters <password> for <supervisor>
#    And user clicks login button
#    And user closes left navigation window
#    Then user should see the home page for <supervisor>
#    When user clicks the user logo
#    And the user clicks Log out
#    Then the user return to the login page
#
#  @smoke @regression
#  Scenario: Login and out as advisor
#    When user enters username for <advisor>
#    And user enters <password> for <advisor>
#    And user clicks login button
#    And user closes left navigation window
#    Then user should see the home page for <advisor>
#    When user clicks the user logo
#    And the user clicks Log out
#    Then the user return to the login page
#
#
