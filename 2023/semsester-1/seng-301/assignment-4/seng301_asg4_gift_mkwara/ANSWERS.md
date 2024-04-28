# SENG301 Assignment 4 (2023) - Student answers

**Gift Mkwara**

## Task 1 - Identify the patterns in the code

### EXAMPLE PATTERN (this pattern is given as an example)

#### What pattern is it?

Proxy

#### What is its goal in the code?

This proxy pattern is used in the Yu-Gi-Oh app to:

- obtain real cards from an external system (Yu-Gi-Oh API), i.e. access control to cards supplied by API;
- create cards on demand, pruning what is not needed from the retrieved cards before passing them.

#### Name of UML Class diagram attached

./diagrams/yugioh-domain.png

#### Mapping to GoF pattern elements

| GoF element | Code element        |
| ----------- | ------------------- |
| Client      | BattleDeckCreator   |
| Subject     | CardGenerator       |
| Proxy       | CardProxy           |
| RealSubject | CardService         |
| request()   | getRandomCard()     |
| request()   | getRandomCardOfType |

### Pattern 1

#### What pattern is it?

Observer

#### What is its goal in the code?

The observer pattern is used to:

- Allow cards in the game to subscribe and unsubscribe to game events
- Notify the subscribed cards when relevant events occur during the game
- Enable the cards to react to the game events and perform actions accordingly
- Remove played cards from the game after they have finished their actions

#### Name of UML Class diagram attached

./diagrams/observer_pattern_uml.png

#### Mapping to GoF pattern elements

| GoF element      | Code element                                                                                           |
|------------------|--------------------------------------------------------------------------------------------------------|
| Subject          | Game                                                                                                   |
| ConcreteSubject  | Board                                                                                                  |
| Observer         | Card                                                                                                   |
| ConcreteObserver | Monster                                                                                                |
| ConcreteObserver | Spell                                                                                                  |
| ConcreteObserver | Trap                                                                                                   |
| attach()         | listenForActions(Card card)                                                                            |
| detach()         | stopListeningForActions(Card card)                                                                     |
| notify()         | actionTrigger(PlayState playState, Card actor, Card target)                                            |
| update()         | reactToAction(PlayState playState, Card actor, Card target, boolean actorIsAlly, boolean targetIsAlly) |


### Pattern 2

#### What pattern is it?

Decorator

#### What is its goal in the code?

The decorator pattern is used to:

- Modify and enhance the behavior of card abilities dynamically
- Wrap abilities with different decorators to add specific functionality or conditions
- Customize and extend abilities without the need for extensive subclassing or modification of existing classes
- Enable the execution of abilities based on various criteria, such as the type of card, play state, or actor/target conditions

#### Name of UML Class diagram attached

./diagrams/decorator_pattern_uml.png

#### Mapping to GoF pattern elements

| GoF element       | Code element                                                                                                       |
|-------------------|--------------------------------------------------------------------------------------------------------------------|
| Component         | Ability                                                                                                            |
| ConcreteComponent | BasicAbility                                                                                                       |
| Decorator         | AbstractAbility                                                                                                    |
| ConcreteDecorator | ActorIsAllyOrEnemy                                                                                                 |
| ConcreteDecorator | CanTargetSelf                                                                                                      |
| ConcreteDecorator | OnlyIfPlayState                                                                                                    |
| ConcreteDecorator | OnlyOnType                                                                                                         |
| ConcreteDecorator | TargetActor                                                                                                        |
| ConcreteDecorator | TargetIsAllyOrEnemy                                                                                                |
| ConcreteDecorator | TotalTimes                                                                                                         |
| operation()       | execute(Card abilityCard, PlayState playState, Card actor, Card target, boolean actorIsAlly, boolean targetIsAlly) |

## Task 2 - Full UML Class diagram

### Name of file of full UML Class diagram attached

./diagrams/full_uml_diagram.png

## Task 3 - Implement new feature

### What pattern fulfils the need for the feature?

Builder pattern

### What is its goal and why is it needed here?

The goal of the builder pattern is to:
- provide a way to construct complex objects step by step
- It separates the construction of an object from its representation
- Allow the same construction process to create different representations

The builder pattern was needed for the `CardResponse` class to minimise the if-else block
present in the `toCard()` method.

By abstracting the construction of a type of card into a `CardBuilder` instance, we immediately make the code more:
- readable; each if statement only contains one line
- testable; the concrete card builder's can be tested separate from the `CardResponse` class
- extensible:
  - Concrete card builders can now extend and implement other classes for more complex functionality
  - Concrete card builders can freely implement other design patterns if needed 
  - It is much easier to add new concrete card builders
- consistent; building a card is always the same regardless of its underlying implementation

The builder pattern was also needed for the `PlayStyles` class to minimise each of the static methods by provide
a consistent build process for each of its AI actions.

The benefits are very similar to those provided to the `CardResponse` class, 
with readability, testability, extensibility and consistency all being improved by the `ActionBuilder`.

### Name of UML Class diagram attached

./diagrams/builder_pattern_uml.png

### Mapping to GoF pattern elements

| GoF element     | Code element                          |
|-----------------|---------------------------------------|
| Builder         | ActionBuilder                         |
| ConcreteBuilder | BasicAIBuilder                        |
| ConcreteBuilder | MonsterFavouringBuilder               |
| ConcreteBuilder | RecklessAIBuilder                     |
| ConcreteBuilder | SetupFavouringAIBuilder               |
| buildPart()     | setAllyBoard(Board allyBoard)         |
| buildPart()     | setEnemyBoard(Board enemyBoard)       |
| buildPart()     | setNumCardsPlayed(int numCardsPlayed) |
| Builder         | CardBuilder                           |
| ConcreteBuilder | MonsterBuilder                        |
| ConcreteBuilder | SpellBuilder                          |
| ConcreteBuilder | TrapBuilder                           |
| buildPart()     | setName(String name)                  |
| buildPart()     | setDescription(String description)    |
| buildPart()     | setAttack(int attack)                 |
| buildPart()     | setDefence(int defence)               |
| getResult()     | build()                               |


## Task 4 - BONUS - Acceptance tests for Task 4

### Feature file (Cucumber Scenarios)

**NAME OF FEATURE FILE**

### Java class implementing the acceptance tests

**NAME OF JAVA FILE**
