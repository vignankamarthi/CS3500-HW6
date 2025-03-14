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
    String expected = "Current Player: RED\n\n"
                    + "RED's hand is empty\n"
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
            + "Current Player: RED\n\n"
            + "RED's hand is empty\n"
            + "0 1r __ __ __ 1b 0\n"
            + "0 1r __ __ __ 1b 0\n"
            + "0 1r __ __ __ 1b 0\n"
            + "\n"
            + "--------------------------------------------------\n\n";

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
            + "Current Player: RED\n\n"
            + "RED's hand is empty\n"
            + "0 1r __ __ __ 1b 0\n"
            + "0 1r __ __ __ 1b 0\n"
            + "0 1r __ __ __ 1b 0\n"
            + "Game is over\n"
            + "RED score: 0\n"
            + "BLUE score: 0\n"
            + "Game ended in a tie!\n"
            + "--------------------------------------------------\n\n";

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
                    + "Current Player: RED\n\n"
                    + "RED's hand is empty\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "0 1r __ __ __ 1b 0\n"
                    + "Game is over\n"
                    + "RED score: 5\n"
                    + "BLUE score: 3\n"
                    + "Winner: RED\n"
            + "--------------------------------------------------\n\n";
    
    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderPlayerHand method for a player with cards.
   */
  @Test
  public void testRenderPlayerHand_WithCards() {
    mockModel.setupInitialBoard();

    // Create test cards
    PawnsBoardBaseCard card1 = new PawnsBoardBaseCard("Card1", 1, 2, emptyInfluence);
    PawnsBoardBaseCard card2 = new PawnsBoardBaseCard("Card2", 2, 3, emptyInfluence);

    // Create hand
    List<PawnsBoardBaseCard> redHand = new ArrayList<>();
    redHand.add(card1);
    redHand.add(card2);

    mockModel.setPlayerHand(PlayerColors.RED, redHand);

    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand:\n"
            + "1: Card1 (Cost: 1, Value: 2)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n"
            + "2: Card2 (Cost: 2, Value: 3)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n";

    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderPlayerHand method for a player with an empty hand.
   */
  @Test
  public void testRenderPlayerHand_EmptyHand() {
    mockModel.setupInitialBoard();
    mockModel.setPlayerHand(PlayerColors.RED, new ArrayList<>());
    
    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand is empty";
    
    assertEquals(expected, output);
  }
  
  /**
   * Tests the renderCurrentPlayerHand method.
   */
  @Test
  public void testRenderCurrentPlayerHand() {
    mockModel.setupInitialBoard();
    mockModel.setCurrentPlayer(PlayerColors.BLUE);

    // Create test cards
    PawnsBoardBaseCard card1 = new PawnsBoardBaseCard("Card1", 1, 2, emptyInfluence);
    PawnsBoardBaseCard card2 = new PawnsBoardBaseCard("Card2", 2, 3, emptyInfluence);

    // Create hand
    List<PawnsBoardBaseCard> blueHand = new ArrayList<>();
    blueHand.add(card1);
    blueHand.add(card2);

    mockModel.setPlayerHand(PlayerColors.BLUE, blueHand);

    String output = view.renderCurrentPlayerHand();
    String expected = "BLUE's hand:\n"
            + "1: Card1 (Cost: 1, Value: 2)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n"
            + "2: Card2 (Cost: 2, Value: 3)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n";

    assertEquals(expected, output);
  }


  /**
   * Tests rendering a player's hand with a single card that has a specific influence grid.
   * Verifies that:
   * - The card's basic information (name, cost, value) is correctly displayed
   * - The influence grid is accurately represented with 'X' for non-influenced cells
   * - Specific influence cells are marked with 'I'
   * - The grid is indented with 3 spaces
   * - There's a newline between cards
   */
  @Test
  public void testRenderPlayerHand_WithInfluenceGrid() {
    mockModel.setupInitialBoard();

    boolean[][] influenceGrid = new boolean[5][5];
    influenceGrid[1][2] = true;  // Influence at (1,2)
    influenceGrid[2][1] = true;  // Influence at (2,1)

    PawnsBoardBaseCard card = new PawnsBoardBaseCard("InfluenceCard", 1, 2, influenceGrid);

    List<PawnsBoardBaseCard> redHand = new ArrayList<>();
    redHand.add(card);

    mockModel.setPlayerHand(PlayerColors.RED, redHand);

    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand:\n"
            + "1: InfluenceCard (Cost: 1, Value: 2)\n"
            + "   XXXXX\n"
            + "   XXIXX\n"
            + "   XICXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n";

    assertEquals(expected, output);
  }


  /**
   * Tests rendering a player's hand with a single card that has a specific influence grid.
   * Verifies that:
   * - The card's basic information (name, cost, value) is correctly displayed
   * - The influence grid is accurately represented with 'X' for non-influenced cells
   * - Specific influence cells are marked with 'I'
   * - The grid is indented with 3 spaces
   * - There's a newline between cards
   */
  @Test
  public void testRenderPlayerHand_MultipleCardsWithInfluenceGrids() {
    mockModel.setupInitialBoard();

    boolean[][] influenceGrid1 = new boolean[5][5];
    influenceGrid1[1][2] = true;

    boolean[][] influenceGrid2 = new boolean[5][5];
    influenceGrid2[2][1] = true;
    influenceGrid2[2][3] = true;

    PawnsBoardBaseCard card1 = new PawnsBoardBaseCard("Card1", 1, 2, influenceGrid1);
    PawnsBoardBaseCard card2 = new PawnsBoardBaseCard("Card2", 2, 3, influenceGrid2);

    List<PawnsBoardBaseCard> redHand = new ArrayList<>();
    redHand.add(card1);
    redHand.add(card2);

    mockModel.setPlayerHand(PlayerColors.RED, redHand);

    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand:\n"
            + "1: Card1 (Cost: 1, Value: 2)\n"
            + "   XXXXX\n"
            + "   XXIXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n"
            + "2: Card2 (Cost: 2, Value: 3)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XICIX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n";

    assertEquals(expected, output);
  }


  /**
   * Tests rendering a card with almost all cells influenced (except the center cell).
   * Verifies that:
   * - A card with widespread influence is correctly represented
   * - The center cell (2,2) remains 'X' as per game rules
   * - All other cells are marked as influenced with 'I'
   * - The grid maintains its 5x5 structure
   * - The influence grid is correctly indented
   */
  @Test
  public void testRenderPlayerHand_AllInfluencedCard() {
    mockModel.setupInitialBoard();

    boolean[][] influenceGrid = new boolean[5][5];
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        if (row != 2 || col != 2) {
          influenceGrid[row][col] = true;
        }
      }
    }

    PawnsBoardBaseCard card = new PawnsBoardBaseCard("AllInfluenceCard", 1, 2, influenceGrid);

    List<PawnsBoardBaseCard> redHand = new ArrayList<>();
    redHand.add(card);

    mockModel.setPlayerHand(PlayerColors.RED, redHand);

    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand:\n"
            + "1: AllInfluenceCard (Cost: 1, Value: 2)\n"
            + "   IIIII\n"
            + "   IIIII\n"
            + "   IICII\n"
            + "   IIIII\n"
            + "   IIIII\n\n";

    assertEquals(expected, output);
  }


  /**
   * Tests rendering a card with no influenced cells.
   * Verifies that:
   * - A card with no influence is correctly represented
   * - All cells are marked with 'X'
   * - The grid maintains its 5x5 structure
   * - The influence grid is correctly indented
   * - The basic card information is still displayed correctly
   */
  @Test
  public void testRenderPlayerHand_NoInfluencedCard() {
    mockModel.setupInitialBoard();

    boolean[][] influenceGrid = new boolean[5][5];

    PawnsBoardBaseCard card = new PawnsBoardBaseCard("NoInfluenceCard", 1, 2, influenceGrid);

    List<PawnsBoardBaseCard> redHand = new ArrayList<>();
    redHand.add(card);

    mockModel.setPlayerHand(PlayerColors.RED, redHand);

    String output = view.renderPlayerHand(PlayerColors.RED);
    String expected = "RED's hand:\n"
            + "1: NoInfluenceCard (Cost: 1, Value: 2)\n"
            + "   XXXXX\n"
            + "   XXXXX\n"
            + "   XXCXX\n"
            + "   XXXXX\n"
            + "   XXXXX\n\n";

    assertEquals(expected, output);
  }

}