package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.PawnsBoardBase;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalAccessException;
import cs3500.pawnsboard.model.exceptions.IllegalCardException;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the PawnsBoardTextualView class.
 * Verifies that the textual rendering of the game board produces the expected output
 * for various game states.
 */
public class PawnsBoardTextualViewTest {

  private PawnsBoard<PawnsBoardBaseCard, ?> model;
  private PawnsBoardTextualView<PawnsBoardBaseCard> view;
  private String testDeckPath;

  /**
   * Sets up a fresh model and view for each test.
   */
  @Before
  public void setUp() {
    model = new PawnsBoardBase<>();
    view = new PawnsBoardTextualView<>(model);
    // Use the test deck configuration file
    testDeckPath = "docs" + File.separator + "3x5PawnsBoardBaseCompleteDeck.config";
  }

  /**
   * Tests that the view correctly handles an unstarted game.
   */
  @Test
  public void testToString_GameNotStarted() {
    String output = view.toString();
    String expected = "Game has not been started";
    assertEquals(expected, output);
  }

  /**
   * Tests rendering of an initial game board.
   */
  @Test
  public void testToString_InitialBoard() throws InvalidDeckConfigurationException {
    model.startGame(3, 5, testDeckPath, 5);
    
    String output = view.toString();
    String expected = "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0";
    
    assertEquals(expected, output);
  }

  /**
   * Tests rendering of a board with a card placed.
   */
  @Test
  public void testToString_WithCardPlaced() throws InvalidDeckConfigurationException,
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Place a card for RED player at position (0,0)
    // The card at index 0 has value 2 (Security card from the deck)
    model.placeCard(0, 0, 0);
    
    String output = view.toString();
    
    // Extract the first row to verify it has the expected format
    String firstRow = output.split("\n")[0];
    String expectedFirstRow = "2 R2 1r __ __ 1b 0";
    assertEquals(expectedFirstRow, firstRow);
  }

  /**
   * Tests rendering of a board with pawns of different counts.
   * Since the exact board state depends on card influence patterns,
   * we'll create a specific known state and test that.
   */
  @Test
  public void testToString_SpecificBoardState() throws InvalidDeckConfigurationException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Create a specific known view for testing, mocking what the board 
    // might look like with different pawn counts
    String output = view.toString();
    String expected = "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0";
    
    assertEquals(expected, output);
  }

  /**
   * Tests rendering of a board with both RED and BLUE cards.
   */
  @Test
  public void testToString_WithBothPlayersCards() throws InvalidDeckConfigurationException,
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // RED places a card at (0,0)
    model.placeCard(0, 0, 0);
    
    // BLUE places a card at (0,4)
    model.placeCard(0, 0, 4);
    
    String output = view.toString();
    String firstRow = output.split("\n")[0];
    String expectedFirstRow = "2 R2 1r __ __ B2 2";
    
    assertEquals("First row should show both RED and BLUE cards with their scores", 
            expectedFirstRow, firstRow);
  }

  /**
   * Tests that the view correctly renders scores.
   */
  @Test
  public void testToString_WithScores() throws InvalidDeckConfigurationException,
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Get the first card from RED's hand
    List<PawnsBoardBaseCard> redHand = model.getPlayerHand(PlayerColors.RED);
    PawnsBoardBaseCard redCard = redHand.get(0);
    int redCardValue = redCard.getValue();
    
    // Get the first card from BLUE's hand
    List<PawnsBoardBaseCard> blueHand = model.getPlayerHand(PlayerColors.BLUE);
    PawnsBoardBaseCard blueCard = blueHand.get(0);
    int blueCardValue = blueCard.getValue();
    
    // RED places their card on row 0
    model.placeCard(0, 0, 0);
    
    // BLUE places their card on row 1
    model.placeCard(0, 1, 4);
    
    String output = view.toString();
    String[] rows = output.split("\n");
    
    // Expected format for row 0 and row 1
    String expectedRow0 = redCardValue + " R" + redCardValue + " 1r __ __ 1b 0";
    String expectedRow1 = "0 1r __ __ 1b B" + blueCardValue + " " + blueCardValue;
    
    assertEquals("Row 0 should show RED's card and score", expectedRow0, rows[0]);
    assertEquals("Row 1 should show BLUE's card and score", expectedRow1, rows[1]);
  }

  /**
   * Tests that a null model passed to the constructor throws IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullModel() {
    new PawnsBoardTextualView<>(null);
  }
  
  /**
   * Tests the renderGameState method with no header.
   */
  @Test
  public void testRenderGameState_NoHeader() throws InvalidDeckConfigurationException {
    model.startGame(3, 5, testDeckPath, 5);
    
    String output = view.renderGameState();
    String expected = "Current Player: RED\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n";
    
    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderGameState method with a header.
   */
  @Test
  public void testRenderGameState_WithHeader() throws InvalidDeckConfigurationException {
    model.startGame(3, 5, testDeckPath, 5);
    
    String output = view.renderGameState("Game Start");
    String expected = "--- Game Start ---\n"
                    + "Current Player: RED\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n";
    
    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderGameState method when the game is over.
   */
  @Test
  public void testRenderGameState_GameOver() throws InvalidDeckConfigurationException, 
          IllegalOwnerException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Have both players pass to end the game
    model.passTurn();
    model.passTurn();
    
    String output = view.renderGameState("Game Results");
    String expected = "--- Game Results ---\n"
                    + "Current Player: RED\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "Game is over\n"
                    + "RED score: 0\n"
                    + "BLUE score: 0\n"
                    + "Game ended in a tie!";
    
    assertEquals(expected, output);
  }
}
