package cs3500.pawnsboard.model.cards.factory;

import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Test suite for the PawnsBoardBaseCardFactory class.
 * Tests the card creation functionality and validates that created cards have
 * the correct properties and influence grids.
 */
public class PawnsBoardBaseCardFactoryTest {

  private PawnsBoardBaseCardFactory factory;

  /**
   * Set up testing environment before each test.
   */
  @Before
  public void setUp() {
    factory = new PawnsBoardBaseCardFactory();
  }

  /**
   * Test creating a card with valid parameters.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_ValidParameters() {
    // Create a valid influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C';
    influenceGrid[1][2] = 'I';

    // Create a card with the influence grid
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("TestCard", 2, 3, influenceGrid);

    // Verify card properties
    assertEquals("TestCard", card.getName());
    assertEquals(2, card.getCost());
    assertEquals(3, card.getValue());

    // Verify influence grid was properly converted
    boolean[][] expected = new boolean[5][5];
    expected[1][2] = true;

    boolean[][] actual = card.getInfluenceGrid();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        assertEquals("Mismatch at position [" + i + "][" + j + "]",
                expected[i][j], actual[i][j]);
      }
    }
  }

  /**
   * Test creating a card with minimum valid values (cost = 1, value = 1).
   */
  @Test
  public void testCreatePawnsBoardBaseCard_MinimumValues() {
    // Create a valid influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C';

    // Create a card with minimum valid values
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("MinCard", 1, 1, influenceGrid);

    // Verify card properties
    assertEquals("MinCard", card.getName());
    assertEquals(1, card.getCost());
    assertEquals(1, card.getValue());
  }

  /**
   * Test creating a card with maximum valid cost (cost = 3).
   */
  @Test
  public void testCreatePawnsBoardBaseCard_MaximumCost() {
    // Create a valid influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C';

    // Create a card with maximum valid cost
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("MaxCostCard", 3, 5, influenceGrid);

    // Verify card properties
    assertEquals("MaxCostCard", card.getName());
    assertEquals(3, card.getCost());
    assertEquals(5, card.getValue());
  }

  /**
   * Test creating a card with a complex influence grid pattern.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_ComplexInfluenceGrid() {
    // Create a complex influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C'; // Card center
    influenceGrid[1][1] = 'I'; // Top-left diagonal
    influenceGrid[1][3] = 'I'; // Top-right diagonal
    influenceGrid[3][1] = 'I'; // Bottom-left diagonal
    influenceGrid[3][3] = 'I'; // Bottom-right diagonal

    // Create a card with the complex influence grid
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("ComplexCard", 2, 4, influenceGrid);

    // Verify influence grid was properly converted
    boolean[][] expected = new boolean[5][5];
    expected[1][1] = true;
    expected[1][3] = true;
    expected[3][1] = true;
    expected[3][3] = true;

    boolean[][] actual = card.getInfluenceGrid();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        assertEquals("Mismatch at position [" + i + "][" + j + "]",
                expected[i][j], actual[i][j]);
      }
    }
  }

  /**
   * Test creating a card with no influence cells (only the center marker).
   */
  @Test
  public void testCreatePawnsBoardBaseCard_NoInfluenceCells() {
    // Create an influence grid with no 'I' cells
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C'; // Only the center marker

    // Create a card with no influence cells
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("NoInfluenceCard", 1, 2, influenceGrid);

    // Verify all influence grid cells are false
    boolean[][] actual = card.getInfluenceGrid();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        assertFalse("Expected no influence at position [" + i + "][" + j + "]", actual[i][j]);
      }
    }
  }

  /**
   * Test creating a card with all cells having influence (except the center).
   */
  @Test
  public void testCreatePawnsBoardBaseCard_AllInfluenceCells() {
    // Create an influence grid with all 'I' cells except center
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'I';
      }
    }
    influenceGrid[2][2] = 'C'; // Center marker

    // Create a card with maximum influence
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("MaxInfluenceCard", 3, 5, influenceGrid);

    // Verify all cells except center have influence
    boolean[][] actual = card.getInfluenceGrid();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (i == 2 && j == 2) {
          // Center should not have influence
          assertFalse("Center position [2][2] should not have influence", actual[i][j]);
        } else {
          // All other cells should have influence
          assertTrue("Expected influence at position [" + i + "][" + j + "]", actual[i][j]);
        }
      }
    }
  }

  /**
   * Test that creating a card with an invalid name throws the correct exception.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_InvalidName() {
    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with null name
      factory.createPawnsBoardBaseCard(null, 1, 2, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card name cannot be null or empty", e.getMessage());
    }

    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with empty name
      factory.createPawnsBoardBaseCard("", 1, 2, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card name cannot be null or empty", e.getMessage());
    }
  }

  /**
   * Test that creating a card with an invalid cost throws the correct exception.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_InvalidCost() {
    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with cost = 0
      factory.createPawnsBoardBaseCard("TestCard", 0, 2, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card cost must be between 1 and 3", e.getMessage());
    }

    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with cost = 4
      factory.createPawnsBoardBaseCard("TestCard", 4, 2, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card cost must be between 1 and 3", e.getMessage());
    }
  }

  /**
   * Test that creating a card with a negative value throws the correct exception.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_InvalidValue() {
    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with value = 0
      factory.createPawnsBoardBaseCard("TestCard", 1, 0, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card value must be positive", e.getMessage());
    }

    try {
      // Create a valid influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';

      // Try to create a card with negative value
      factory.createPawnsBoardBaseCard("TestCard", 1, -5, influenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Card value must be positive", e.getMessage());
    }
  }

  /**
   * Test that creating a card with a null influence grid throws the correct exception.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_NullInfluenceGrid() {
    try {
      // Try to create a card with null influence grid
      factory.createPawnsBoardBaseCard("TestCard", 1, 2, null);
    } catch (IllegalArgumentException e) {
      assertEquals("Influence grid must be a 5x5 grid", e.getMessage());
    }
  }

  /**
   * Test that creating a card with an incorrectly sized influence grid throws the correct
   * exception.
   */
  @Test
  public void testCreatePawnsBoardBaseCard_WrongSizeInfluenceGrid() {
    try {
      // Create an influence grid that's too small
      char[][] smallGrid = new char[4][5];
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 5; j++) {
          smallGrid[i][j] = 'X';
        }
      }
      smallGrid[2][2] = 'C';

      // Try to create a card with wrong-sized influence grid
      factory.createPawnsBoardBaseCard("TestCard", 1, 2, smallGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Influence grid must be a 5x5 grid", e.getMessage());
    }

    try {
      // Create an influence grid with uneven rows
      char[][] unevenGrid = new char[5][];
      for (int i = 0; i < 5; i++) {
        // Make row i have length i+1
        unevenGrid[i] = new char[i+1];
        for (int j = 0; j < i+1; j++) {
          unevenGrid[i][j] = 'X';
        }
      }

      // Try to create a card with uneven influence grid
      factory.createPawnsBoardBaseCard("TestCard", 1, 2, unevenGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Influence grid must be a 5x5 grid", e.getMessage());
    }
  }

  /**
   * Test that the factory properly handles the center position when converting influence grid.
   */
  @Test
  public void testCenterPositionInCharRepresentation() {
    // Create a valid influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C'; // Center marked with 'C'

    // Create a card
    PawnsBoardBaseCard card = factory.createPawnsBoardBaseCard("CenterCard", 2, 3, influenceGrid);

    // Verify the center is marked as 'C' in the char representation
    char[][] charGrid = card.getInfluenceGridAsChars();
    assertEquals('C', charGrid[2][2]);
  }
}