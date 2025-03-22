package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the PawnsBoardMock model.
 * Validates that the mock correctly manages game state and returns expected values.
 */
public class PawnsBoardMockTest {

  private PawnsBoardMock<PawnsBoardBaseCard, ?> mockModel;
  private boolean[][] emptyInfluence;
  private PawnsBoardBaseCard testCard;

  /**
   * Sets up a fresh mock model and test card for each test.
   */
  @Before
  public void setUp() {
    mockModel = new PawnsBoardMock<>();
    emptyInfluence = new boolean[5][5];
    testCard = new PawnsBoardBaseCard("TestCard", 1, 2, emptyInfluence);
  }

  /**
   * Tests the initial state of the mock model.
   * Verifies that the game is not over and trying to access the current player
   * throws an IllegalStateException when the game is not started.
   */
  @Test
  public void testInitialState() {
    assertFalse(mockModel.isGameOver());
    
    // Should throw exception when not started
    try {
      mockModel.getCurrentPlayer();
    } catch (IllegalStateException e) {
      assertEquals("Game has not been started", e.getMessage());
    }
  }

  /**
   * Tests the setGameStarted method.
   * Verifies that after setting the game to started, the current player can be accessed.
   */
  @Test
  public void testSetGameStarted() {
    mockModel.setGameStarted(true);
    mockModel.setCurrentPlayer(PlayerColors.RED);
    
    assertEquals(PlayerColors.RED, mockModel.getCurrentPlayer());
  }

  /**
   * Tests the setGameOver method.
   * Verifies that after setting the game to over, isGameOver returns true
   * and the winner can be accessed.
   */
  @Test
  public void testSetGameOver() {
    mockModel.setGameStarted(true)
             .setGameOver(true)
             .setWinner(PlayerColors.BLUE);
    
    assertTrue(mockModel.isGameOver());
    assertEquals(PlayerColors.BLUE, mockModel.getWinner());
  }

  /**
   * Tests the setBoardDimensions method.
   * Verifies that the board dimensions are correctly set and can be retrieved.
   */
  @Test
  public void testSetBoardDimensions() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(4, 7);
    
    int[] dimensions = mockModel.getBoardDimensions();
    assertEquals(4, dimensions[0]);
    assertEquals(7, dimensions[1]);
  }

  /**
   * Tests the setCellContent method.
   * Verifies that cell content can be set for a specific cell and retrieved correctly,
   * and that unset cells default to EMPTY.
   */
  @Test
  public void testSetCellContent() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setCellContent(1, 2, CellContent.PAWNS);
    
    assertEquals(CellContent.PAWNS, mockModel.getCellContent(1, 2));
    assertEquals(CellContent.EMPTY, mockModel.getCellContent(0, 0));
  }

  /**
   * Tests the setCellOwner method.
   * Verifies that cell ownership can be set for a specific cell and retrieved correctly,
   * and that unset cells have no owner (null).
   */
  @Test
  public void testSetCellOwner() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setCellContent(1, 2, CellContent.PAWNS)
             .setCellOwner(1, 2, PlayerColors.BLUE);
    
    assertEquals(PlayerColors.BLUE, mockModel.getCellOwner(1, 2));
    assertNull(mockModel.getCellOwner(0, 0));
  }

  /**
   * Tests the setPawnCount method.
   * Verifies that pawn count can be set for a specific cell and retrieved correctly,
   * and that unset cells have a pawn count of 0.
   */
  @Test
  public void testSetPawnCount() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setCellContent(1, 2, CellContent.PAWNS)
             .setCellOwner(1, 2, PlayerColors.BLUE)
             .setPawnCount(1, 2, 3);
    
    assertEquals(3, mockModel.getPawnCount(1, 2));
    assertEquals(0, mockModel.getPawnCount(0, 0));
  }

  /**
   * Tests the setCardAtCell method.
   * Verifies that a card can be set for a specific cell and retrieved correctly,
   * and that unset cells have no card (null).
   */
  @Test
  public void testSetCardAtCell() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setCellContent(1, 2, CellContent.CARD)
             .setCellOwner(1, 2, PlayerColors.RED)
             .setCardAtCell(1, 2, testCard);
    
    assertEquals(testCard, mockModel.getCardAtCell(1, 2));
    assertNull(mockModel.getCardAtCell(0, 0));
  }

  /**
   * Tests the setRowScores method.
   * Verifies that row scores can be set for multiple rows and retrieved correctly.
   */
  @Test
  public void testSetRowScores() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setRowScores(0, 3, 2)
             .setRowScores(1, 0, 5)
             .setRowScores(2, 4, 1);
    
    assertArrayEquals(new int[]{3, 2}, mockModel.getRowScores(0));
    assertArrayEquals(new int[]{0, 5}, mockModel.getRowScores(1));
    assertArrayEquals(new int[]{4, 1}, mockModel.getRowScores(2));
  }

  /**
   * Tests the setTotalScore method.
   * Verifies that total scores can be set for both players and retrieved correctly.
   */
  @Test
  public void testSetTotalScore() {
    mockModel.setGameStarted(true)
             .setTotalScore(7, 4);
    
    assertArrayEquals(new int[]{7, 4}, mockModel.getTotalScore());
  }

  /**
   * Tests the setPlayerHand method.
   * Verifies that cards can be added to each player's hand and retrieved correctly.
   */
  @Test
  public void testSetPlayerHand() {
    List<PawnsBoardBaseCard> redCards = new ArrayList<>();
    redCards.add(testCard);
    
    List<PawnsBoardBaseCard> blueCards = new ArrayList<>();
    blueCards.add(new PawnsBoardBaseCard("BlueCard", 2, 3, emptyInfluence));
    
    mockModel.setGameStarted(true)
             .setPlayerHand(PlayerColors.RED, redCards)
             .setPlayerHand(PlayerColors.BLUE, blueCards);
    
    assertEquals(1, mockModel.getPlayerHand(PlayerColors.RED).size());
    assertEquals(1, mockModel.getPlayerHand(PlayerColors.BLUE).size());
    assertEquals(testCard, mockModel.getPlayerHand(PlayerColors.RED).get(0));
  }

  /**
   * Tests the setRemainingDeckSize method.
   * Verifies that deck sizes can be set for both players and retrieved correctly.
   */
  @Test
  public void testSetRemainingDeckSize() {
    mockModel.setGameStarted(true)
             .setRemainingDeckSize(PlayerColors.RED, 10)
             .setRemainingDeckSize(PlayerColors.BLUE, 8);
    
    assertEquals(10, mockModel.getRemainingDeckSize(PlayerColors.RED));
    assertEquals(8, mockModel.getRemainingDeckSize(PlayerColors.BLUE));
  }

  /**
   * Tests the setupInitialBoard method.
   * Verifies that the method correctly initializes a standard 3x5 game board with:
   * - Game started but not over
   * - RED as the current player
   * - RED pawns in the first column
   * - BLUE pawns in the last column
   * - Row scores and total score initialized to 0
   */
  @Test
  public void testSetupInitialBoard() {
    mockModel.setupInitialBoard();
    
    assertTrue(mockModel.isGameStarted());
    assertFalse(mockModel.isGameOver());
    assertEquals(PlayerColors.RED, mockModel.getCurrentPlayer());
    
    // Check dimensions
    int[] dimensions = mockModel.getBoardDimensions();
    assertEquals(3, dimensions[0]);
    assertEquals(5, dimensions[1]);
    
    // Check RED pawns in first column
    for (int r = 0; r < 3; r++) {
      assertEquals(CellContent.PAWNS, mockModel.getCellContent(r, 0));
      assertEquals(PlayerColors.RED, mockModel.getCellOwner(r, 0));
      assertEquals(1, mockModel.getPawnCount(r, 0));
    }
    
    // Check BLUE pawns in last column
    for (int r = 0; r < 3; r++) {
      assertEquals(CellContent.PAWNS, mockModel.getCellContent(r, 4));
      assertEquals(PlayerColors.BLUE, mockModel.getCellOwner(r, 4));
      assertEquals(1, mockModel.getPawnCount(r, 4));
    }
    
    // Check row scores
    for (int r = 0; r < 3; r++) {
      assertArrayEquals(new int[]{0, 0}, mockModel.getRowScores(r));
    }
    
    // Check total score
    assertArrayEquals(new int[]{0, 0}, mockModel.getTotalScore());
  }

  /**
   * Tests that getWinner throws an exception when the game is not over.
   * Verifies that the getWinner method correctly throws an IllegalStateException
   * when trying to access the winner before the game is over.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetWinner_GameNotOver() {
    mockModel.setGameStarted(true)
             .setGameOver(false);
    
    mockModel.getWinner(); // Should throw IllegalStateException
  }

  /**
   * Tests that getCellContent throws an exception for invalid coordinates.
   * Verifies that the getCellContent method correctly throws an IllegalArgumentException
   * when trying to access a cell outside the board boundaries.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetCellContent_InvalidCoordinates() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5);
    
    mockModel.getCellContent(5, 5); // Should throw IllegalArgumentException
  }

  /**
   * Tests the method chaining functionality.
   * Verifies that multiple setter methods can be chained together,
   * and that the state is correctly updated after all operations.
   */
  @Test
  public void testMethodChaining() {
    // Test that method chaining works correctly
    mockModel.setGameStarted(true)
             .setCurrentPlayer(PlayerColors.BLUE)
             .setBoardDimensions(4, 5)
             .setCellContent(1, 2, CellContent.PAWNS)
             .setCellOwner(1, 2, PlayerColors.BLUE)
             .setPawnCount(1, 2, 2);
    
    assertEquals(PlayerColors.BLUE, mockModel.getCurrentPlayer());
    assertEquals(CellContent.PAWNS, mockModel.getCellContent(1, 2));
    assertEquals(PlayerColors.BLUE, mockModel.getCellOwner(1, 2));
    assertEquals(2, mockModel.getPawnCount(1, 2));
  }

  /**
   * Tests that getPlayerHand returns a defensive copy.
   * Verifies that modifying the list returned by getPlayerHand does not
   * affect the internal state of the mock model.
   */
  @Test
  public void testGetPlayerHand_DefensiveCopy() {
    List<PawnsBoardBaseCard> redCards = new ArrayList<>();
    redCards.add(testCard);
    
    mockModel.setGameStarted(true)
             .setPlayerHand(PlayerColors.RED, redCards);
    
    // Get the hand and modify it
    List<PawnsBoardBaseCard> hand = mockModel.getPlayerHand(PlayerColors.RED);
    hand.clear();
    
    // The original hand should still have the card
    assertEquals(1, mockModel.getPlayerHand(PlayerColors.RED).size());
  }

  /**
   * Tests that getPlayerHand throws an exception for null player.
   * Verifies that the getPlayerHand method correctly throws an IllegalArgumentException
   * when null is passed as the player parameter.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPlayerHand_NullPlayer() {
    mockModel.setGameStarted(true);
    mockModel.getPlayerHand(null);
  }

  /**
   * Tests that getRemainingDeckSize throws an exception for null player.
   * Verifies that the getRemainingDeckSize method correctly throws an IllegalArgumentException
   * when null is passed as the player parameter.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetRemainingDeckSize_NullPlayer() {
    mockModel.setGameStarted(true);
    mockModel.getRemainingDeckSize(null);
  }

  /**
   * Tests the isLegalMove method returns true for valid parameters.
   * Verifies that the mock implementation correctly validates move parameters.
   * For the mock, we consider a move legal if the card index is within range
   * and the coordinates are valid.
   */
  @Test
  public void testIsLegalMove_ValidParameters() {
    // Create a non-empty hand with a dummy card
    List<PawnsBoardBaseCard> sampleHand = new ArrayList<>();
    sampleHand.add(testCard);
    
    mockModel.setGameStarted(true)
            .setGameOver(false)
            .setBoardDimensions(3, 5)
            .setPlayerHand(PlayerColors.RED, sampleHand)
            .setCurrentPlayer(PlayerColors.RED);

    // Should return true for valid coordinates and card index
    assertTrue(mockModel.isLegalMove(0, 1, 1));
  }

  /**
   * Tests the isLegalMove method returns false for invalid card index.
   * Verifies that the mock implementation correctly identifies illegal moves
   * due to invalid card index. The mock should return false when the card index
   * is outside the range of the player's hand.
   */
  @Test
  public void testIsLegalMove_InvalidCardIndex() {
    // Create a non-empty hand with a dummy card
    List<PawnsBoardBaseCard> sampleHand = new ArrayList<>();
    sampleHand.add(testCard);
    
    mockModel.setGameStarted(true)
            .setGameOver(false)
            .setBoardDimensions(3, 5)
            .setPlayerHand(PlayerColors.RED, sampleHand)
            .setCurrentPlayer(PlayerColors.RED);

    // Should return false for invalid card index
    assertFalse(mockModel.isLegalMove(10, 1, 1));
  }

  /**
   * Tests that isLegalMove throws exception when game is not started.
   * Verifies that the mock implementation correctly validates game state
   * before checking move legality. An IllegalStateException should be thrown
   * when the game has not been started.
   */
  @Test(expected = IllegalStateException.class)
  public void testIsLegalMove_GameNotStarted() {
    mockModel.isLegalMove(0, 0, 0);
  }

  /**
   * Tests that isLegalMove throws exception when game is over.
   * Verifies that the mock implementation correctly validates game state
   * before checking move legality. An IllegalStateException should be thrown
   * when the game is already over, as no moves are legal at that point.
   */
  @Test(expected = IllegalStateException.class)
  public void testIsLegalMove_GameOver() {
    mockModel.setGameStarted(true)
             .setGameOver(true);
    mockModel.isLegalMove(0, 0, 0);
  }

  /**
   * Tests that isLegalMove throws exception for invalid coordinates.
   * Verifies that the mock implementation correctly validates the coordinates
   * before checking move legality. An IllegalArgumentException should be thrown
   * when the coordinates are outside the board boundaries.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testIsLegalMove_InvalidCoordinates() {
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5);
    mockModel.isLegalMove(0, 10, 10);
  }

  /**
   * Tests that copy method creates an independent copy with the same state.
   * Verifies that the mock implementation correctly creates a deep copy of the model.
   * This test sets up a mock with a specific state, creates a copy, and then
   * checks that all state attributes are correctly copied. It also tests that
   * changes to the original don't affect the copy, demonstrating independence.
   */
  @Test
  public void testCopy_IndependentCopy() {
    // Setup a mock with some state
    mockModel.setGameStarted(true)
             .setBoardDimensions(3, 5)
             .setCurrentPlayer(PlayerColors.RED)
             .setCellContent(0, 0, CellContent.PAWNS)
             .setCellOwner(0, 0, PlayerColors.RED)
             .setPawnCount(0, 0, 1);

    // Create a copy
    PawnsBoard<?, ?> copy = mockModel.copy();

    // Verify the copy has the same state
    assertFalse(copy.isGameOver());
    assertEquals(PlayerColors.RED, copy.getCurrentPlayer());
    assertEquals(CellContent.PAWNS, copy.getCellContent(0, 0));
    assertEquals(PlayerColors.RED, copy.getCellOwner(0, 0));
    assertEquals(1, copy.getPawnCount(0, 0));

    // Verify the copy is independent by modifying the original
    mockModel.setCurrentPlayer(PlayerColors.BLUE);
    assertEquals(PlayerColors.RED, copy.getCurrentPlayer());
  }

  /**
   * Tests that copy throws exception when game is not started.
   * Verifies that the mock implementation correctly validates the game state
   * before attempting to create a copy. An IllegalStateException should be thrown
   * when trying to copy a game that hasn't been started yet.
   */
  @Test(expected = IllegalStateException.class)
  public void testCopy_GameNotStarted() {
    mockModel.copy();
  }
}
