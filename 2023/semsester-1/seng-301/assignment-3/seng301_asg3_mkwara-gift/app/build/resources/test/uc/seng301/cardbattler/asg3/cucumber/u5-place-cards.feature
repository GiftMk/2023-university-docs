Feature: U5 As Alex, I want to place my 5 starting cards on the board so that I can start a fight.

  Scenario: AC1 - When the game starts, I can draw the first 5 cards of my battle deck.
    Given A player exists named "Jack"
    And I have a battle deck named "MyBattleDeck"
    When The game has started
    Then I draw 5 cards from my battle deck

  Scenario: AC2 - When I play each card in my deck, it is placed at its dedicated place on the board.
    Given A player exists named "Jack"
    And I have a battle deck named "MyBattleDeck"
    And The game has started
    When I play each card in my deck
    Then Each card is placed at its dedicated place on the board


  Scenario: AC3 - A monster card's starting life equals its attack or defense when placed in attack or defense mode
    Given A player exists named "Jack"
    And I have a battle deck named "MyBattleDeck"
    And The game has started
    When I play each card in my deck
    Then Each monster's life is equal to its attack when in attack mode or its defense when in defense mode
