import cs3500.pawnsboard.model.PawnsBoardBase;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

import java.io.File;

/**
 * The main class for the PawnsBoard game. This class contains a main method that demonstrates
 * the functionality of the PawnsBoard model by playing through several moves and showing
 * the textual output at each step.
 */
public class PawnsBoard {

  /**
   * Main method that demonstrates the functionality of the PawnsBoard model.
   * It initializes a game, plays a sequence of moves, and displays the game state after each action
   * using the textual view.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    try {
      // Create the model
      PawnsBoardBase<PawnsBoardBaseCard> model = new PawnsBoardBase<>();
      
      // Read deck configuration file
      String deckConfigPath = "docs" + File.separator + "3x5PawnsBoardBaseCompleteDeck.config";
      
      // Initialize game with 3 rows, 5 columns, and starting hand size of 5
      model.startGame(3, 5, deckConfigPath, 5);
      
      // Create the view
      PawnsBoardTextualView<PawnsBoardBaseCard> view = new PawnsBoardTextualView<>(model);
      
      // Display initial game state
      System.out.println(view.renderGameState("Game Start"));
      System.out.println();
      
      // RED places a card at position (0,0)
      model.placeCard(0, 0, 0); // Using the first card (Security, cost 1)
      System.out.println(view.renderGameState("RED places a card at (0,0)"));
      System.out.println();
      
      // BLUE places a card at position (0,4)
      model.placeCard(0, 0, 4); // Using the first card (Security, cost 1)
      System.out.println(view.renderGameState("BLUE places a card at (0,4)"));
      System.out.println();
      
      // RED places a card at position (1,0)
      model.placeCard(1, 1, 0); // Using the second card (Shield, cost 2)
      System.out.println(view.renderGameState("RED places a card at (1,0)"));
      System.out.println();
      
      // BLUE places a card at position (1,4)
      model.placeCard(1, 1, 4); // Using the second card (Shield, cost 2)
      System.out.println(view.renderGameState("BLUE places a card at (1,4)"));
      System.out.println();
      
      // RED places a card at position (2,0)
      model.placeCard(1, 2, 0); // Using the second card (avoiding Sword at index 2 which costs 3)
      System.out.println(view.renderGameState("RED places a card at (2,0)"));
      System.out.println();
      
      // BLUE places a card at position (2,4)
      model.placeCard(1, 2, 4); // Using the second card
      System.out.println(view.renderGameState("BLUE places a card at (2,4)"));
      System.out.println();
      
      // Try placing cards in middle positions or pass if not possible
      boolean success = false;
      
      // RED's turn - Try to place card in middle or pass
      try {
        model.placeCard(2, 0, 1); // Try to place at (0,1)
        success = true;
      } catch (Exception e) {
        model.passTurn();
      }
      
      if (success) {
        System.out.println(view.renderGameState("RED places a card at (0,1)"));
      } else {
        System.out.println(view.renderGameState("RED passes turn"));
      }
      System.out.println();
      
      // Reset success flag
      success = false;
      
      // BLUE's turn - Try to place card in middle or pass
      try {
        model.placeCard(2, 0, 3); // Try to place at (0,3)
        success = true;
      } catch (Exception e) {
        model.passTurn();
      }
      
      if (success) {
        System.out.println(view.renderGameState("BLUE places a card at (0,3)"));
      } else {
        System.out.println(view.renderGameState("BLUE passes turn"));
      }
      System.out.println();
      
      // RED passes turn
      model.passTurn();
      System.out.println(view.renderGameState("RED passes turn"));
      System.out.println();
      
      // BLUE passes turn to end the game
      model.passTurn();
      System.out.println(view.renderGameState("Game Results"));
      
    } catch (InvalidDeckConfigurationException | IllegalAccessException 
            | IllegalCardException | IllegalOwnerException e) {
      System.err.println("Error during game play: " + e.getMessage());
      e.printStackTrace();
    }
  }
}