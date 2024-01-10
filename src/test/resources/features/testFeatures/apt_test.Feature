Feature:

  Scenario Outline:
    Given the <user> logs in to docuport
    And get cookie test
    And set token test
    Then token as jsonobject in test context
    Examples:
