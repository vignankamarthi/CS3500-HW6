import cs3500.pawnsboard.model.PawnsBoardBase;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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
   * <p>This demonstration shows a complete game where players place cards until no cards
   * can be placed on the board (all valid positions are filled). The deck is not shuffled
   * to ensure predictable card order.</p>
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
      // The model uses the DeckBuilder with shuffle=false internally
      model.startGame(3, 5, deckConfigPath, 5);
      
      // Create the view
      PawnsBoardTextualView<PawnsBoardBaseCard> view = new PawnsBoardTextualView<>(model);
      
      // Display initial game state
      System.out.println(view.renderGameState("Game Start"));
      System.out.println();
      
      // ---------------------------- Turn 1 ----------------------------
      // RED places Security (cost 1) at position (0,0)
      model.placeCard(0, 0, 0); 
      System.out.println(view.renderGameState("RED places Security at (0,0)"));
      System.out.println();
      
      // BLUE places Security (cost 1) at position (0,4)
      model.placeCard(0, 0, 4); 
      System.out.println(view.renderGameState("BLUE places Security at (0,4)"));
      System.out.println();
      
      // ---------------------------- Turn 2 ----------------------------
      // RED places Mandragora (cost 1) at position (1,0)
      model.placeCard(3, 1, 0); // Mandragora should now be at index 3 after playing Security
      System.out.println(view.renderGameState("RED places Mandragora at (1,0)"));
      System.out.println();
      
      // BLUE places Mandragora (cost 1) at position (1,4)
      model.placeCard(3, 1, 4); // Same for BLUE
      System.out.println(view.renderGameState("BLUE places Mandragora at (1,4)"));
      System.out.println();
      
      // ---------------------------- Turn 3 ----------------------------
      // RED places Tempest (cost 1) at position (2,0)
      model.placeCard(3, 2, 0); // Tempest should now be at index 3
      System.out.println(view.renderGameState("RED places Tempest at (2,0)"));
      System.out.println();
      
      // BLUE places Tempest (cost 1) at position (2,4)
      model.placeCard(3, 2, 4);
      System.out.println(view.renderGameState("BLUE places Tempest at (2,4)"));
      System.out.println();
      
      // ---------------------------- Turn 4 ----------------------------
      // RED plays Valkyrie (cost 1) at position (0,1) if possible
      // Cards placed previously should have created pawns in adjacent cells
      try {
        model.placeCard(3, 0, 1); // Valkyrie should now be at index 3
        System.out.println(view.renderGameState("RED places Valkyrie at (0,1)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("RED passes - can't place card at (0,1)"));
      }
      System.out.println();
      
      // BLUE plays Valkyrie (cost 1) at position (0,3) if possible
      try {
        model.placeCard(3, 0, 3);
        System.out.println(view.renderGameState("BLUE places Valkyrie at (0,3)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("BLUE passes - can't place card at (0,3)"));
      }
      System.out.println();
      
      // ---------------------------- Turn 5 ----------------------------
      // RED attempts to place at (1,1) if possible
      try {
        model.placeCard(0, 1, 1);
        System.out.println(view.renderGameState("RED places card at (1,1)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("RED passes - can't place card at (1,1)"));
      }
      System.out.println();
      
      // BLUE attempts to place at (1,3) if possible
      try {
        model.placeCard(0, 1, 3);
        System.out.println(view.renderGameState("BLUE places card at (1,3)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("BLUE passes - can't place card at (1,3)"));
      }
      System.out.println();
      
      // ---------------------------- Turn 6 ----------------------------
      // RED attempts to place at (2,1) if possible
      try {
        model.placeCard(0, 2, 1);
        System.out.println(view.renderGameState("RED places card at (2,1)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("RED passes - can't place card at (2,1)"));
      }
      System.out.println();
      
      // BLUE attempts to place at (2,3) if possible
      try {
        model.placeCard(0, 2, 3);
        System.out.println(view.renderGameState("BLUE places card at (2,3)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("BLUE passes - can't place card at (2,3)"));
      }
      System.out.println();
      
      // ---------------------------- Turn 7 ----------------------------
      // RED attempts to place at (0,2) or middle column if possible
      try {
        model.placeCard(0, 0, 2);
        System.out.println(view.renderGameState("RED places card at (0,2)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("RED passes - can't place card at (0,2)"));
      }
      System.out.println();
      
      // BLUE attempts to place at middle column if possible
      try {
        model.placeCard(0, 1, 2);
        System.out.println(view.renderGameState("BLUE places card at (1,2)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("BLUE passes - can't place card at (1,2)"));
      }
      System.out.println();
      
      // ---------------------------- Turn 8 ----------------------------
      // Final attempts at remaining cells
      try {
        model.placeCard(0, 2, 2);
        System.out.println(view.renderGameState("RED places card at (2,2)"));
      } catch (Exception e) {
        model.passTurn();
        System.out.println(view.renderGameState("RED passes - no valid moves remaining"));
      }
      System.out.println();
      
      // If we reach this point and the game isn't over, one more pass will end it
      if (!model.isGameOver()) {
        model.passTurn();
        System.out.println(view.renderGameState("BLUE passes - no valid moves remaining"));
        System.out.println();
      }
      
      // Display the final game state
      System.out.println(view.renderGameState("Game Results"));
      
    } catch (InvalidDeckConfigurationException | IllegalAccessException 
            | IllegalCardException | IllegalOwnerException e) {
      System.err.println("Error during game play: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
