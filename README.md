# Pawns Board Game

## Overview

The Pawns Board game is a two-player card placement strategy game played on a rectangular grid. Players take turns placing cards from their hands onto cells with their pawns, with each card having unique influence patterns that affect the board state. The game implements a complete model of the game rules described in the assignment, including card placement mechanics, influence patterns, pawn ownership, and scoring.

### Key Assumptions and Design Goals

- The game supports different board sizes, though rows must be positive and columns must be odd
- Players can be human or AI-controlled (interface provided, implementation to come)
- The game is extensible to support different types of cards and influence mechanics
- Cards are loaded from configuration files, allowing for customizable decks

## Quick Start

Here is a guide on how to start a game and complete a few moves:

```java
// Create the model and load a deck
PawnsBoardBase<PawnsBoardBaseCard> model = new PawnsBoardBase<>();
String deckConfigPath = "docs/3x5TestingDeck.config";

// Start a game with 3 rows, 5 columns, and 5 cards per hand
model.startGame(3, 5, deckConfigPath, 5);

// Create a view to display the game
PawnsBoardTextualView<PawnsBoardBaseCard> view = new PawnsBoardTextualView<>(model);
System.out.println(view.toString());

// Place a card (RED player's turn)
model.placeCard(0, 0, 0);  // Card index 0 at position (0,0)

// Display updated board
System.out.println(view.toString());

// Blue player's turn
model.placeCard(0, 0, 4);  // Card index 0 at position (0,4)
}
```

## Full Game Visualization
To a completed game, use the PawnsBoard's main class to see a game fully played out. 

## Key Components

### Model
The model manages the game state and enforces the rules of Pawns Board. It's built as a hierarchical set of interfaces and implementations:

- **PawnsBoard Interface**: Defines all operations for the game, including setup, turn management, board queries, and scoring
- **AbstractPawnsBoard**: Provides common functionality for game state tracking and player turn management
- **PawnsBoardBase**: Concrete implementation with complete game logic for a rectangular grid board

### View
The view provides a text-based rendering of the game state:

- **PawnsBoardView Interface**: Defines methods for rendering the game state
- **PawnsBoardTextualView**: Implements a console-friendly text representation of the board, hands, and scores

### Player Framework
The player framework defines abstractions for different player types:

- **Player Interface**: Unified interface for all player types
- **HumanPlayer**: Implementation for human players, interacting via controller/UI
- **AIPlayer**: Stub implementation for computer players (to be completed)

## Key Subcomponents

### Card System
- **Card Interface**: Defines card properties (name, cost, value score, and influence grid)
- **PawnsBoardBaseCard**: Standard card implementation
- **DeckBuilder**: Creates and validates decks from configuration files
- **CardFactory**: Creates cards using the Builder pattern
- **CardReader**: Reads card definitions from files

### Board Representation
- **PawnsBoardCell Interface**: Represents a single cell on the board
- **PawnsBoardBaseCell**: Concrete cell implementation
- **CellContent Enum**: Defines possible cell contents (EMPTY, PAWNS, CARD)
- **PlayerColors Enum**: Represents player ownership (RED, BLUE)

### Exception Handling
Several custom exceptions handle different error cases:
- **IllegalAccessException**: For insufficient resources
- **IllegalOwnerException**: For ownership violations
- **IllegalCardException**: For invalid card operations
- **InvalidDeckConfigurationException**: For deck loading errors

## Source Organization

The codebase is organized into the following package structure:

- **cs3500.pawnsboard.model**: Core game model
  - **cards**: Card-related classes
    - **factory**: Card creation
    - **reader**: Card loading from files
    - **deckbuilder**: Deck creation and validation
  - **cell**: Cell representation
  - **enumerations**: Game enums
  - **exceptions**: Custom exception types
- **cs3500.pawnsboard.view**: View components
- **cs3500.pawnsboard.player**: Player abstractions
- **docs**: Configuration files and documentation
  - Card deck configurations
  - Player interface design documentation
- **PawnsBoard.java**: Main class demonstrating gameplay

## Testing

The project includes comprehensive tests:
- Unit tests for individual components
- Integration tests for the model-view interaction
- Mock model implementations for controlled testing

To run the tests, use JUnit4 through your IDE or build system.

## Changes for part 2

### Refactoring the Model Interfaces

In this iteration, we made several important changes to improve the model design and provide better support for the view and future AI players:

1. **Read-Only Interface**: 
   - The existing `PawnsBoard` interface now extends a new `ReadOnlyPawnsBoard` interface
   - `ReadOnlyPawnsBoard` contains all observation methods for viewing the game state
   - This separation ensures that views can only read the game state, not modify it

2. **Added Missing Functionality**:

   - **Move Legality Checking**:
     - Added `isLegalMove(int cardIndex, int row, int col)` method to check if a move is legal without actually making it
     - Previously, the only way to check move legality was to attempt the move and catch exceptions
     - This addition helps AI strategies evaluate possible moves without modifying the game state
     - Implemented in both `AbstractPawnsBoard` and `PawnsBoardMock` classes

   - **Board Copying**:
     - Added `copy()` method to create a deep copy of the board state
     - This allows AI players to simulate potential moves and evaluate their outcomes
     - The copy is completely independent and changes to it don't affect the original game
     - Implemented in both `PawnsBoardBase` and `PawnsBoardMock` classes

3. **Comprehensive Testing**:
   - Added thorough tests for both new methods
   - Tests verify correct behavior for various scenarios (valid moves, invalid moves, game not started, etc.)
   - Tests ensure that the copy maintains the expected behavior of being truly independent from the original game

These changes enhance the model's capabilities for strategy implementation in future assignments and maintain proper MVC architecture by preventing views from modifying the model directly.