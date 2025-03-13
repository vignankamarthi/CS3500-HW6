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
   * It initializes a game, places cards, and shows the textual output at each step.
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
      
      // Print initial game state
      System.out.println("=== Game Start ===");
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // RED places a card at position (0,0)
      System.out.println("=== RED places a card at (0,0) ===");
      model.placeCard(0, 0, 0);
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // BLUE places a card at position (0,4)
      System.out.println("=== BLUE places a card at (0,4) ===");
      model.placeCard(0, 0, 4);
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // RED places a card at position (1,0)
      System.out.println("=== RED places a card at (1,0) ===");
      model.placeCard(0, 1, 0);
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // BLUE places a card at position (1,4)
      System.out.println("=== BLUE places a card at (1,4) ===");
      model.placeCard(0, 1, 4);
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // RED places a card at position (2,0)
      System.out.println("=== RED places a card at (2,0) ===");
      model.placeCard(0, 2, 0);
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // BLUE passes turn
      System.out.println("=== BLUE passes turn ===");
      model.passTurn();
      System.out.println("Current Player: " + model.getCurrentPlayer());
      System.out.println(view.toString());
      System.out.println();
      
      // RED passes turn, which should end the game
      System.out.println("=== RED passes turn ===");
      model.passTurn();
      System.out.println("Game Over: " + model.isGameOver());
      System.out.println(view.toString());
      System.out.println();
      
      // Print game results
      System.out.println("=== Game Results ===");
      int[] totalScores = model.getTotalScore();
      System.out.println("RED score: " + totalScores[0]);
      System.out.println("BLUE score: " + totalScores[1]);
      
      PlayerColors winner = model.getWinner();
      if (winner != null) {
        System.out.println("Winner: " + winner);
      } else {
        System.out.println("Game ended in a tie!");
      }
      
    } catch (InvalidDeckConfigurationException | IllegalAccessException 
            | IllegalCardException | IllegalOwnerException e) {
      System.err.println("Error during game play: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
