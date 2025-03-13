import cs3500.pawnsboard.model.PawnsBoardBase;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
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
   * <p>This demonstration shows a complete game where players place cards until all 15 positions
   * on the board are filled. The deck is not shuffled to ensure the exact sequence of cards.</p>
   *
   * @param args command line arguments (not used)
   */
  /**
   * Helper method to execute a move in the game.
   * Attempts to place a card at the specified position and prints the result.
   * If the move fails, the turn is passed.
   *
   * @param model the game model
   * @param view the game view
   * @param cardIndex the index of the card in the current player's hand
   * @param row the row to place the card
   * @param col the column to place the card
   * @param description a description of the move for the output
   * @return true if the move was successful, false otherwise
   */
  private static boolean executeMove(PawnsBoardBase<PawnsBoardBaseCard> model,
                              PawnsBoardTextualView<PawnsBoardBaseCard> view,
                              int cardIndex, int row, int col, String description) {
    try {
      // Place the card - the view will automatically show the current player's hand
      model.placeCard(cardIndex, row, col);
      System.out.println(view.renderGameState(description));
      System.out.println();
      return true;
    } catch (Exception e) {
      System.out.println("Move failed: " + e.getMessage());
      try {
        model.passTurn();
        System.out.println(view.renderGameState("PASS - " + description
                + " failed"));
        System.out.println();
      } catch (Exception ex) {
        System.err.println("Error passing turn: " + ex.getMessage());
      }
      return false;
    }
  }

  public static void main(String[] args) {
    try {
      // Create the model
      PawnsBoardBase<PawnsBoardBaseCard> model = new PawnsBoardBase<>();
      
      // Read our sequential deck configuration file
      String deckConfigPath = "docs" + File.separator + "3x5PawnsBoardBaseCompleteDeck.config";
      
      // Initialize game with 3 rows, 5 columns, and starting hand size of 5
      model.startGame(3, 5, deckConfigPath, 5);
      
      // Create the view
      PawnsBoardTextualView<PawnsBoardBaseCard> view = new PawnsBoardTextualView<>(model);
      
      // Display initial game state
      System.out.println(view.renderGameState("Game Start"));
      System.out.println();
      
      // ----- ROUND 1: Fill the Outer Columns -----
      
      // RED's turn - Place Security (index 0) at top-left (0,0)
      executeMove(model, view, 0, 0, 0, "RED places Security at (0,0)");
      
      // BLUE's turn - Place Security (index 0) at top-right (0,4)
      executeMove(model, view, 0, 0, 4, "BLUE places Security at (0,4)");
      
      // RED's turn - Place Mandragora (index 0) at middle-left (1,0)
      executeMove(model, view, 0, 1, 0, "RED places Mandragora at (1,0)");
      
      // BLUE's turn - Place Mandragora (index A0) at middle-right (1,4)
      executeMove(model, view, 0, 1, 4, "BLUE places Mandragora at (1,4)");
      
      // RED's turn - Place Tempest (index 0) at bottom-left (2,0)
      executeMove(model, view, 0, 2, 0, "RED places Tempest at (2,0)");
      
      // BLUE's turn - Place Tempest (index 0) at bottom-right (2,4)
      executeMove(model, view, 0, 2, 4, "BLUE places Tempest at (2,4)");
      
      // ----- ROUND 2: Fill the Second and Fourth Columns -----
      // By now, the Security cards should have influenced adjacent cells
      
      // RED's turn - Place Valkyrie (index 0) at top-second (0,1)
      executeMove(model, view, 0, 0, 1, "RED places Valkyrie at (0,1)");
      
      // BLUE's turn - Place Valkyrie (index 0) at top-fourth (0,3)
      executeMove(model, view, 0, 0, 3, "BLUE places Valkyrie at (0,3)");
      
      // RED's turn - Place BasePawn (index 0) at middle-second (1,1)
      executeMove(model, view, 0, 1, 1, "RED places BasePawn at (1,1)");
      
      // BLUE's turn - Place BasePawn (index 0) at middle-fourth (1,3)
      executeMove(model, view, 0, 1, 3, "BLUE places BasePawn at (1,3)");
      
      // RED's turn - Place CenterPawn (index 0) at bottom-second (2,1)
      executeMove(model, view, 0, 2, 1, "RED places CenterPawn at (2,1)");
      
      // BLUE's turn - Place CenterPawn (index 0) at bottom-fourth (2,3)
      executeMove(model, view, 0, 2, 3, "BLUE places CenterPawn at (2,3)");
      
      // ----- ROUND 3: Fill the Middle Column -----
      // By now, cards should have influenced the middle column cells
      
      // RED's turn - Place CrossPawn (index 0) at top-middle (0,2)
      executeMove(model, view, 0, 0, 2, "RED places CrossPawn at (0,2)");
      
      // BLUE's turn - Place DiagonalPawn (index 0) at middle-middle (1,2)
      executeMove(model, view, 0, 1, 2, "BLUE places DiagonalPawn at (1,2)");
      
      // RED's turn - Place CornerPawn (index 0) at bottom-middle (2,2)
      executeMove(model, view, 0, 2, 2, "RED places CornerPawn at (2,2)");
      
      // At this point all 15 positions are filled
      // Display final game state with results
      System.out.println(view.renderGameState("Game Complete - All 15 Positions Filled"));

    } catch (InvalidDeckConfigurationException e) {
      System.err.println("Error during game setup: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
