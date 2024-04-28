Feature: U2 As Alex I want to create a deck so that I can keep track of my cards and use them to battle.
  Background:
    Given I create a player named "Gift Mkwara"
    And I create a card named "My Card" with attack: 100, defence: 100, life: 100

  Scenario: AC1 - A deck has a unique, non-empty alphanumeric name.
    When I create a deck named "My Deck" with player: "Gift Mkwara", cards: [card]
    Then The deck is created with the correct name, player, and cards.