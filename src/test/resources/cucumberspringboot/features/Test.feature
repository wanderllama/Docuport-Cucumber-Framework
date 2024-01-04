Feature: test

  Scenario Outline: test one
    Given test runs
    And test <outcome>

    Examples:
      | outcome |
      | fail    |


  Scenario Outline: test two
    Given test runs
    And test <outcome>

    Examples:
      | outcome |
      | pass    |


  Scenario Outline: test three
    Given test runs
    And test <outcome>

    Examples:
      | outcome |
      | pass    |


  Scenario Outline: test four
    Given test runs
    And test <outcome>

    Examples:
      | outcome |
      | skip    |