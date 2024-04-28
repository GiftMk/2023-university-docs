Feature: U1 - As Alex, I want to create a user within the application so I can use other functionality.

  Scenario Outline: AC1 - A player has a unique non-empty name and a specific AI play style
    Given I want to be named <name> with play style <play_style>
    When I create a player
    Then My player is registered correctly
    And I can interact with other functionality
    Examples:
      | name      | play_style |
      | "Jess"    | "basic"    |
      | "James"   | "monster"  |
      | "Jolene"  | "setup"    |
      | "Jacques" | "reckless" |

  Scenario Outline: AC2 - A player name cannot contain non-alphanumeric or numeric-only values
    Given I want to be named <name> with play style "basic"
    When I create a player
    Then The player is not registered
    And I am shown an error message telling me the name is invalid
    Examples:
      | name        |
      | "123"       |
      | "{&James&}" |
      | "."         |
      | "#Jacques"  |

  Scenario Outline: AC3 - A player’s play style must be selected from one of the existing types
    Given I want to be named "Josh" with play style <play_style>
    When I create a player
    Then The player is not registered
    And  I am shown an error message telling me the play style is invalid
    Examples:
      | play_style         |
      | "not_a_play_style" |
      | "."                |
      | "#&^"              |