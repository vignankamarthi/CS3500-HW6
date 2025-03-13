package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.PawnsBoardBase;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.CellContent;
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
import static org.junit.Assert.assertTrue;

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
    assertEquals("Game has not been started", output);
  }

  /**
   * Tests rendering of an initial game board.
   */
  @Test
  public void testToString_InitialBoard() throws InvalidDeckConfigurationException {
    model.startGame(3, 5, testDeckPath, 5);
    
    String output = view.toString();
    
    // Check that the output has the correct format
    String[] rows = output.split("\n");
    
    // There should be 3 rows
    assertEquals("There should be 3 rows", 3, rows.length);
    
    // Each row should have the same format since the board starts in the same state for each row
    String expectedRowFormat = "0 1r __ __ __ 1b 0";
    
    for (int i = 0; i < rows.length; i++) {
      assertEquals("Row " + i + " should match the expected format",
              expectedRowFormat, rows[i]);
    }
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
    
    // The first row should now have "R2" for RED's card with value 2 instead of "1r"
    // The score should be 2 (from the card's value)
    String expectedFirstRow = "2 R2 1r __ __ 1b 0";
    String[] outputRows = output.split("\n");
    assertEquals(expectedFirstRow, outputRows[0]);
  }

  /**
   * Tests rendering of a board with pawns of different counts.
   */
  @Test
  public void testToString_WithDifferentPawnCounts() throws InvalidDeckConfigurationException, 
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Use card influence to create cells with different pawn counts
    // We'll place a card that influences cells to get different pawn counts
    
    // Place the first card to influence nearby cells
    model.placeCard(0, 0, 0);
    
    // Verify that at least some cell now has pawns after influence
    boolean foundPawns = false;
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 5; c++) {
        if (model.getCellContent(r, c) == CellContent.PAWNS && model.getPawnCount(r, c) > 1) {
          foundPawns = true;
          break;
        }
      }
    }
    
    String output = view.toString();
    
    // For this test, we can't predict exactly what the board will look like because
    // the card influences are variable, but we can verify that the board structure is correct
    // and that it contains expected elements
    
    // Check that all rows begin with a score, contain cells, and end with a score
    String[] rows = output.split("\n");
    assertEquals("There should be 3 rows", 3, rows.length);
    
    for (String row : rows) {
      // Each row starts with a number (score)
      char firstChar = row.charAt(0);
      assertTrue("Row should start with a digit", firstChar >= '0'
              && firstChar <= '9');
      
      // Each row ends with a number (score)
      char lastChar = row.charAt(row.length() - 1);
      assertTrue("Row should end with a digit", lastChar >= '0'
              && lastChar <= '9');
      
      // Each row should have spaces separating cells
      String[] parts = row.split(" ");
      assertTrue("Row should have at least 7 parts (score + 5 cells + score)",
              parts.length >= 7);
    }
    
    // If we found cells with multiple pawns, the view should include them
    if (foundPawns) {
      assertTrue("Output should contain cells with multiple pawns (2r or 3r)",
              output.contains("2r") || output.contains("3r"));
    }
  }

  /**
   * Tests rendering of a board with both RED and BLUE cards.
   */
  @Test
  public void testToString_WithBothPlayersCards() throws InvalidDeckConfigurationException,
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // RED places a card
    model.placeCard(0, 0, 0);
    
    // BLUE places a card
    model.placeCard(0, 0, 4);
    
    String output = view.toString();
    
    // The output should contain both a RED card and a BLUE card
    String[] rows = output.split("\n");
    
    boolean foundRedCard = false;
    boolean foundBlueCard = false;
    
    for (String row : rows) {
      if (row.contains("R")) {
        foundRedCard = true;
      }
      if (row.contains("B")) {
        foundBlueCard = true;
      }
    }
    
    assertTrue("Output should contain a RED card", foundRedCard);
    assertTrue("Output should contain a BLUE card", foundBlueCard);
    
    // We expect the first row to have RED's card (R2) and the format should be proper
    assertTrue("First row should contain R2", rows[0].contains("R2"));
    // We expect the second row to have BLUE's card
    assertTrue("A row should contain BLUE's card", output.contains("B"));
  }

  /**
   * Tests that the view correctly renders scores.
   */
  @Test
  public void testToString_WithScores() throws InvalidDeckConfigurationException,
          IllegalAccessException, IllegalOwnerException, IllegalCardException {
    model.startGame(3, 5, testDeckPath, 5);
    
    // Get the first two cards from RED's hand to ensure we know their values
    List<PawnsBoardBaseCard> redHand = model.getPlayerHand(PlayerColors.RED);
    PawnsBoardBaseCard firstCard = redHand.get(0);
    int firstCardValue = firstCard.getValue();
    
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
    
    // Check that there are 3 rows
    assertEquals("There should be 3 rows in the output", 3, rows.length);
    
    // First row should have RED's score (first card value) and BLUE's score (0)
    assertTrue("First row should start with RED's score: " + firstCardValue, 
            rows[0].startsWith(firstCardValue + " "));
    assertTrue("First row should end with BLUE's score: 0", rows[0].endsWith(" 0"));
    
    // Second row should have RED's score (0) and BLUE's score (blueCardValue)
    assertTrue("Second row should start with RED's score: 0", rows[1].startsWith("0 "));
    assertTrue("Second row should end with BLUE's score: " + blueCardValue, 
            rows[1].endsWith(" " + blueCardValue));
  }

  /**
   * Tests that a null model passed to the constructor throws IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullModel() {
    new PawnsBoardTextualView<>(null);
  }
}
