Feature: U1 As Alex, I want to create a card so that I can use it in a deck.
  Scenario: AC1 - A card has a unique non-empty name, a strictly positive attack, a strictly positive defence, and positive
  life stats.
    Given There is no card with name "Minotaur"
    When I create a card named "Minotaur" with attack: 60, defence: 50, life: 110
    Then The card is created with the correct name, attack, defence, and life
  Scenario Outline: AC2 - A card name cannot contain non-alphanumeric or numeric-only values.
    Given There is no card with name <name>
    When I create an invalid card named <name> with attack: 60, defence: 50, life: 110
    Then An exception is thrown
    Examples:
      | name      |
      | "673975"  |
      | "$*&ynsl" |
  Scenario Outline: AC3 - A card cannot have a negative values for the attack, the defence, or life stats.
    Given There is no card with name "Minotaur"
    When I create an invalid card named "Minotaur" with attack: <attack>, defence: <defence>, life: <life>
    Then An exception is thrown
    Examples:
      | attack | defence | life |
      | 100    | 0       | 100  |
      | -100   | 10     | 100  |
      | 100    | 10      | -100 |