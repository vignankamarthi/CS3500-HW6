package cs3500.pawnsboard.model.cell;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the PawnsBoardBaseCell class.
 * Tests all functionality and edge cases of the cell implementation,
 * including initialization, content management, ownership, and exception handling.
 */
public class PawnsBoardBaseCellTest {

  private PawnsBoardBaseCell<Card> cell;
  private Card mockCard;

  /**
   * Sets up a fresh cell and a mock card for each test.
   */
  @Before
  public void setUp() {
    cell = new PawnsBoardBaseCell<>();
    // Create a test influence grid
    boolean[][] influenceGrid = new boolean[5][5];
    influenceGrid[2][2] = true; // Center has influence
    mockCard = new PawnsBoardBaseCard("Test", 1, 1, influenceGrid);
  }

  /**
   * Tests that a new cell is properly initialized as empty.
   */
  @Test
  public void testInitialState() {
    assertEquals(CellContent.EMPTY, cell.getContent());
    assertNull(cell.getOwner());
    assertEquals(0, cell.getPawnCount());
    assertNull(cell.getCard());
  }

  /**
   * Tests adding a single pawn to an empty cell.
   */
  @Test
  public void testAddFirstPawn() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);

    assertEquals(CellContent.PAWNS, cell.getContent());
    assertEquals(PlayerColors.RED, cell.getOwner());
    assertEquals(1, cell.getPawnCount());
    assertNull(cell.getCard());
  }

  /**
   * Tests adding a second pawn to a cell that already has one pawn.
   */
  @Test
  public void testAddSecondPawn() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);

    assertEquals(CellContent.PAWNS, cell.getContent());
    assertEquals(PlayerColors.RED, cell.getOwner());
    assertEquals(2, cell.getPawnCount());
  }

  /**
   * Tests adding a third pawn to a cell that already has two pawns.
   */
  @Test
  public void testAddThirdPawn() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);

    assertEquals(CellContent.PAWNS, cell.getContent());
    assertEquals(PlayerColors.RED, cell.getOwner());
    assertEquals(3, cell.getPawnCount());
  }

  /**
   * Tests that adding a fourth pawn throws an IllegalStateException.
   */
  @Test
  public void testAddFourthPawn() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);

    String expectedMessage = "Cell already has maximum number of pawns";
    String actualMessage = "";

    try {
      cell.addPawn(PlayerColors.RED);
    } catch (IllegalStateException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);
  }

  /**
   * Tests that adding a pawn of a different owner throws an IllegalOwnerException.
   */
  @Test
  public void testAddPawnDifferentOwner() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);

    String expectedMessage = "Cannot add pawn of different owner";
    String actualMessage = "";

    try {
      cell.addPawn(PlayerColors.BLUE);
    } catch (IllegalOwnerException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);

    // Verify cell state is unchanged
    assertEquals(1, cell.getPawnCount());
    assertEquals(PlayerColors.RED, cell.getOwner());
  }

  /**
   * Tests that adding a pawn to a cell with a card throws an IllegalStateException.
   */
  @Test
  public void testAddPawnToCellWithCard() throws IllegalOwnerException {
    cell.setCard(mockCard, PlayerColors.RED);

    String expectedMessage = "Cannot add pawn to a cell containing a card";
    String actualMessage = "";

    try {
      cell.addPawn(PlayerColors.RED);
    } catch (IllegalStateException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);

    // Verify cell still has the card
    assertEquals(CellContent.CARD, cell.getContent());
    assertEquals(mockCard, cell.getCard());
  }

  /**
   * Tests changing ownership of pawns in a cell.
   */
  @Test
  public void testChangeOwnership() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);
    cell.changeOwnership(PlayerColors.BLUE);

    assertEquals(PlayerColors.BLUE, cell.getOwner());
    assertEquals(2, cell.getPawnCount());  // Count should remain the same
  }

  /**
   * Tests that changing ownership of an empty cell throws an IllegalStateException.
   */
  @Test
  public void testChangeOwnershipOfEmptyCell() {
    String expectedMessage = "Can only change ownership of pawns";
    String actualMessage = "";

    try {
      cell.changeOwnership(PlayerColors.RED);
    } catch (IllegalStateException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);
  }

  /**
   * Tests that changing ownership of a cell with a card throws an IllegalStateException.
   */
  @Test
  public void testChangeOwnershipOfCellWithCard() {
    cell.setCard(mockCard, PlayerColors.RED);

    String expectedMessage = "Can only change ownership of pawns";
    String actualMessage = "";

    try {
      cell.changeOwnership(PlayerColors.BLUE);
    } catch (IllegalStateException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);

    // Verify cell still has the card with original owner
    assertEquals(PlayerColors.RED, cell.getOwner());
  }

  /**
   * Tests setting a card in an empty cell.
   */
  @Test
  public void testSetCardInEmptyCell() {
    cell.setCard(mockCard, PlayerColors.RED);

    assertEquals(CellContent.CARD, cell.getContent());
    assertEquals(PlayerColors.RED, cell.getOwner());
    assertEquals(mockCard, cell.getCard());
    assertEquals(0, cell.getPawnCount());
  }

  /**
   * Tests setting a card in a cell that already has pawns.
   */
  @Test
  public void testSetCardInCellWithPawns() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    cell.addPawn(PlayerColors.RED);

    cell.setCard(mockCard, PlayerColors.RED);

    assertEquals(CellContent.CARD, cell.getContent());
    assertEquals(PlayerColors.RED, cell.getOwner());
    assertEquals(mockCard, cell.getCard());
    assertEquals(0, cell.getPawnCount());  // Pawns should be removed
  }

  /**
   * Tests setting a null card throws an appropriate exception.
   */
  @Test
  public void testSetNullCard() {
    String expectedMessage = "Card cannot be null";
    String actualMessage = "";

    try {
      cell.setCard(null, PlayerColors.RED);
    } catch (NullPointerException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);
  }

  /**
   * Tests that adding a null-owned pawn throws an IllegalArgumentException.
   */
  @Test
  public void testAddNullOwnedPawn() throws IllegalOwnerException {
    String expectedMessage = "Player colors cannot be null";
    String actualMessage = "";

    try {
      cell.addPawn(null);
    } catch (IllegalArgumentException e) {
      actualMessage = e.getMessage();
    }

    assertEquals(expectedMessage, actualMessage);
  }

  /**
   * Tests that getCard returns null when cell has pawns.
   */
  @Test
  public void testGetCardWhenCellHasPawns() throws IllegalOwnerException {
    cell.addPawn(PlayerColors.RED);
    assertNull(cell.getCard());
  }

  /**
   * Tests that getPawnCount returns 0 when cell has a card.
   */
  @Test
  public void testGetPawnCountWhenCellHasCard() {
    cell.setCard(mockCard, PlayerColors.RED);
    assertEquals(0, cell.getPawnCount());
  }
}