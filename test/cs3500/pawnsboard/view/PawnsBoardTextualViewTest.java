package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoardMock;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the PawnsBoardTextualView class.
 * Verifies that the textual rendering of the game board produces the expected output
 * for various game states.
 * Uses a mock model to ensure consistent and reliable testing of the view's rendering logic.
 */
public class PawnsBoardTextualViewTest {

  private PawnsBoardMock<PawnsBoardBaseCard, ?> mockModel;
  private PawnsBoardTextualView<PawnsBoardBaseCard> view;
  private boolean[][] emptyInfluence;

  /**
   * Sets up a fresh mock model and view for each test.
   */
  @Before
  public void setUp() {
    mockModel = new PawnsBoardMock<>();
    view = new PawnsBoardTextualView<>(mockModel);
    emptyInfluence = new boolean[5][5];
  }

  /**
   * Tests that the view correctly handles an unstarted game.
   */
  @Test
  public void testToString_GameNotStarted() {
    mockModel.setGameStarted(false);
    
    String output = view.toString();
    String expected = "Game has not been started";
    assertEquals(expected, output);
  }

  /**
   * Tests rendering of an initial game board.
   */
  @Test
  public void testToString_InitialBoard() {
    mockModel.setupInitialBoard();
    
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
  public void testToString_WithCardPlaced() {
    // Setup a board with a RED card (value 2) at position (0,0)
    mockModel.setupInitialBoard()
            .setCellContent(0, 0, CellContent.CARD)
            .setCellOwner(0, 0, PlayerColors.RED)
            .setRowScores(0, 2, 0);
    
    PawnsBoardBaseCard mockCard = new PawnsBoardBaseCard("TestCard", 1, 2, emptyInfluence);
    mockModel.setCardAtCell(0, 0, mockCard);
    
    String output = view.toString();
    
    // Extract the first row to verify it has the expected format
    String firstRow = output.split("\n")[0];
    String expectedFirstRow = "2 R2 __ __ __ 1b 0";
    assertEquals(expectedFirstRow, firstRow);
  }

  /**
   * Tests rendering of a board with pawns of different counts.
   */
  @Test
  public void testToString_DifferentPawnCounts() {
    mockModel.setupInitialBoard()
            .setPawnCount(0, 0, 2)  // RED with 2 pawns
            .setPawnCount(1, 0, 3)  // RED with 3 pawns
            .setPawnCount(0, 4, 2)  // BLUE with 2 pawns
            .setPawnCount(2, 4, 3); // BLUE with 3 pawns
    
    String output = view.toString();
    String expected = "0 2r __ __ __ 2b 0\n"
                    + "0 3r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 3b 0";
    
    assertEquals(expected, output);
  }

  /**
   * Tests rendering of a board with both RED and BLUE cards.
   */
  @Test
  public void testToString_WithBothPlayersCards() {
    // Setup a board with both RED and BLUE cards
    mockModel.setupInitialBoard()
            .setCellContent(0, 0, CellContent.CARD)
            .setCellOwner(0, 0, PlayerColors.RED)
            .setCellContent(0, 4, CellContent.CARD)
            .setCellOwner(0, 4, PlayerColors.BLUE)
            .setRowScores(0, 2, 2);
    
    PawnsBoardBaseCard redCard = new PawnsBoardBaseCard("RedCard", 1, 2, emptyInfluence);
    PawnsBoardBaseCard blueCard = new PawnsBoardBaseCard("BlueCard", 1, 2, emptyInfluence);
    
    mockModel.setCardAtCell(0, 0, redCard)
             .setCardAtCell(0, 4, blueCard);
    
    String output = view.toString();
    String firstRow = output.split("\n")[0];
    String expectedFirstRow = "2 R2 __ __ __ B2 2";
    
    assertEquals("First row should show both RED and BLUE cards with their scores", 
            expectedFirstRow, firstRow);
  }

  /**
   * Tests that the view correctly renders scores.
   */
  @Test
  public void testToString_WithScores() {
    // Setup a board with cards in different rows to test score rendering
    mockModel.setupInitialBoard()
            .setCellContent(0, 0, CellContent.CARD)
            .setCellOwner(0, 0, PlayerColors.RED)
            .setCellContent(1, 4, CellContent.CARD)
            .setCellOwner(1, 4, PlayerColors.BLUE)
            .setRowScores(0, 2, 0)
            .setRowScores(1, 0, 2);
    
    PawnsBoardBaseCard redCard = new PawnsBoardBaseCard("RedCard", 1, 2, emptyInfluence);
    PawnsBoardBaseCard blueCard = new PawnsBoardBaseCard("BlueCard", 1, 2, emptyInfluence);
    
    mockModel.setCardAtCell(0, 0, redCard)
             .setCardAtCell(1, 4, blueCard);
    
    String output = view.toString();
    String[] rows = output.split("\n");
    
    // Expected format for row 0 and row 1
    String expectedRow0 = "2 R2 __ __ __ 1b 0";
    String expectedRow1 = "0 1r __ __ __ B2 2";
    
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
  public void testRenderGameState_NoHeader() {
    mockModel.setupInitialBoard()
             .setCurrentPlayer(PlayerColors.RED);
    
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
  public void testRenderGameState_WithHeader() {
    mockModel.setupInitialBoard()
             .setCurrentPlayer(PlayerColors.RED);
    
    String output = view.renderGameState("Game Start");
    String expected = "--- Game Start ---\n"
                    + "Current Player: RED\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n";
    
    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderGameState method when the game is over with a tie.
   */
  @Test
  public void testRenderGameState_GameOverTie() {
    mockModel.setupInitialBoard()
             .setGameOver(true)
             .setTotalScore(0, 0)
             .setWinner(null); // Tie game
    
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
  
  /**
   * Tests renderGameState when there's a winner.
   */
  @Test
  public void testRenderGameState_WithWinner() {
    mockModel.setupInitialBoard()
             .setGameOver(true)
             .setTotalScore(5, 3)
             .setWinner(PlayerColors.RED);
    
    String output = view.renderGameState("Game Results");
    String expected = "--- Game Results ---\n"
                    + "Current Player: RED\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "Game is over\n"
                    + "RED score: 5\n"
                    + "BLUE score: 3\n"
                    + "Winner: RED";
    
    assertEquals(expected, output);
  }
}
