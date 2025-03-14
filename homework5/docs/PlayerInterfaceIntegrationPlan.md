# User-Player Interface Design Document

## Overview

This document outlines the design for the player interfaces in the Pawns Board game, focusing specifically on how these interfaces facilitate interaction between different player types (human and AI) and the game controller. The design follows the Model-View-Controller architectural pattern, allowing for flexible, extensible gameplay while maintaining clear separation of concerns.

## Player Interface Design

The `Player` interface serves as an abstraction that allows both human and computer players to interact with the game model in a consistent way. Both player types implement the same interface but handle decision-making differently - human players receive input from a user through the controller, while AI players make decisions algorithmically.

```
Player<C extends Card>
 ├── HumanPlayer<C extends Card>
 └── AIPlayer<C extends Card>
```

### Core Interface Methods

The interface defines several key methods that enable player-game interaction:

1. **takeTurn()**: The main entry point for player actions
2. **placeCard()**: Places a card on the board at specified coordinates
3. **passTurn()**: Allows a player to skip their turn
4. **isMyTurn()**: Checks if it's the player's turn
5. **receiveInvalidMoveMessage()**: Handles invalid move feedback
6. **notifyGameEnd()**: Processes game-over information

This design enables the controller to treat human and AI players uniformly while allowing for their implementation differences.

## Controller-Player Interaction

### General Flow

1. The controller initializes the game model and creates player objects
2. For each turn, the controller:
   - Determines the current player
   - Renders the game state through the view
   - Facilitates player decision-making 
   - Executes the player's move on the model
   - Handles any exceptions or invalid moves

### Human Player Interaction

For human players, the controller will:

1. Display the current game state to the user
2. Present available actions (place card or pass)
3. If placing a card:
   - Show the player's hand
   - Prompt for card selection and placement coordinates
   - Call `humanPlayer.placeCard()` with the selected parameters
4. If passing:
   - Call `humanPlayer.passTurn()`
5. If an invalid move occurs:
   - Retrieve the error message via `getLastErrorMessage()`
   - Display it to the user
   - Allow another attempt

### AI Player Interaction

For AI players, the controller will:

1. Display the current game state
2. Call `aiPlayer.takeTurn()`, which internally:
   - Evaluates the game state
   - Decides whether to place a card or pass
   - Executes the chosen action
3. The controller displays the AI's chosen action

## Player Implementation Differences

### Human Player Specifics

The `HumanPlayer` implementation:
- Acts as a thin wrapper around model methods
- Tracks invalid move state and error messages
- Relies on the controller to collect user input and handle UI
- Provides methods like `hasReceivedInvalidMove()` and `getLastErrorMessage()` for the controller to use

### AI Player Specifics

The `AIPlayer` implementation:
- Contains decision-making logic to evaluate game state
- Autonomously chooses moves based on algorithms
- Handles its own move validation internally
- May implement learning capabilities based on game outcomes