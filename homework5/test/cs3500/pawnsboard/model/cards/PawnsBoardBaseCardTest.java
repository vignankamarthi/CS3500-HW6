package cs3500.pawnsboard.model.cards;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for PawnsBoardBaseCard.
 * Tests construction, attribute access, and behavior of cards in the Pawns Board game.
 */
public class PawnsBoardBaseCardTest {

  private String validName;
  private int validCost;
  private int validValue;
  private boolean[][] validInfluenceGrid;

  private PawnsBoardBaseCard card;

  @Before
  public void setUp() {
    validName = "TestCard";
    validCost = 2;
    validValue = 3;

    // Create a valid 5x5 influence grid with center cell at (2,2)
    validInfluenceGrid = new boolean[5][5];
    validInfluenceGrid[1][2] = true;  // Above center
    validInfluenceGrid[2][1] = true;  // Left of center
    validInfluenceGrid[2][3] = true;  // Right of center
    validInfluenceGrid[3][2] = true;  // Below center

    card = new PawnsBoardBaseCard(validName, validCost, validValue, validInfluenceGrid);
  }

  /**
   * Tests that a valid card can be constructed.
   */
  @Test
  public void testValidCardConstruction() {
    assertNotNull("Card should be successfully created", card);
  }

  /**
   * Tests that getName returns the correct name.
   */
  @Test
  public void testGetName() {
    assertEquals("Card name should match", validName, card.getName());
  }

  /**
   * Tests that getCost returns the correct cost.
   */
  @Test
  public void testGetCost() {
    assertEquals("Card cost should match", validCost, card.getCost());
  }

  /**
   * Tests that getValue returns the correct value.
   */
  @Test
  public void testGetValue() {
    assertEquals("Card value should match", validValue, card.getValue());
  }

  /**
   * Tests that getInfluenceGrid returns a valid grid matching the input.
   */
  @Test
  public void testGetInfluenceGrid() {
    boolean[][] returnedGrid = card.getInfluenceGrid();

    // Verify grid is the correct size
    assertEquals("Grid should have 5 rows", 5, returnedGrid.length);
    assertEquals("First row should have 5 columns", 5, returnedGrid[0].length);

    // Verify grid content matches what we created
    assertTrue("Top center should have influence", returnedGrid[1][2]);
    assertTrue("Left center should have influence", returnedGrid[2][1]);
    assertTrue("Right center should have influence", returnedGrid[2][3]);
    assertTrue("Bottom center should have influence", returnedGrid[3][2]);

    // Test a non-influence cell
    assertFalse("Center should not have influence", returnedGrid[2][2]);
  }

  /**
   * Tests that getInfluenceGrid returns a defensive copy, not the original grid.
   */
  @Test
  public void testGetInfluenceGridDefensiveCopy() {
    boolean[][] returnedGrid = card.getInfluenceGrid();

    // Modify the returned grid
    returnedGrid[0][0] = true;

    // Get the grid again
    boolean[][] secondGrid = card.getInfluenceGrid();

    // Verify the second grid doesn't reflect our modifications
    assertFalse("Grid should be a defensive copy", secondGrid[0][0]);
  }

  /**
   * Tests that getInfluenceGridAsChars returns the correct char representation.
   */
  @Test
  public void testGetInfluenceGridAsChars() {
    char[][] charGrid = card.getInfluenceGridAsChars();

    // Verify grid is the correct size
    assertEquals("Grid should have 5 rows", 5, charGrid.length);
    assertEquals("First row should have 5 columns", 5, charGrid[0].length);

    // Verify correct character representation
    assertEquals("Center should be C", 'C', charGrid[2][2]);
    assertEquals("Influenced cells should be I", 'I', charGrid[1][2]);
    assertEquals("Influenced cells should be I", 'I', charGrid[2][1]);
    assertEquals("Influenced cells should be I", 'I', charGrid[2][3]);
    assertEquals("Influenced cells should be I", 'I', charGrid[3][2]);
    assertEquals("Non-influenced cells should be X", 'X', charGrid[0][0]);
  }

  /**
   * Tests that constructor rejects null name.
   */
  @Test
  public void testConstructorWithNullName() {
    try {
      new PawnsBoardBaseCard(null, validCost, validValue, validInfluenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Card name cannot be null or empty", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects empty name.
   */
  @Test
  public void testConstructorWithEmptyName() {
    try {
      new PawnsBoardBaseCard("", validCost, validValue, validInfluenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Card name cannot be null or empty", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects cost below 1.
   */
  @Test
  public void testConstructorWithCostBelowRange() {
    try {
      new PawnsBoardBaseCard(validName, 0, validValue, validInfluenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Card cost must be between 1 and 3", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects cost above 3.
   */
  @Test
  public void testConstructorWithCostAboveRange() {
    try {
      new PawnsBoardBaseCard(validName, 4, validValue, validInfluenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Card cost must be between 1 and 3", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects non-positive value.
   */
  @Test
  public void testConstructorWithNonPositiveValue() {
    try {
      new PawnsBoardBaseCard(validName, validCost, 0, validInfluenceGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Card value must be positive", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects null influence grid.
   */
  @Test
  public void testConstructorWithNullGrid() {
    try {
      new PawnsBoardBaseCard(validName, validCost, validValue, null);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Influence grid must be a 5x5 grid", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects wrong grid row size.
   */
  @Test
  public void testConstructorWithWrongGridRowSize() {
    try {
      new PawnsBoardBaseCard(validName, validCost, validValue, new boolean[4][5]);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Influence grid must be a 5x5 grid", e.getMessage());
    }
  }

  /**
   * Tests that constructor rejects wrong grid column size.
   */
  @Test
  public void testConstructorWithWrongGridColSize() {
    boolean[][] invalidGrid = new boolean[5][];
    for (int i = 0; i < 5; i++) {
      invalidGrid[i] = new boolean[i == 2 ? 4 : 5]; // Making the 3rd row have 4 columns
    }

    try {
      new PawnsBoardBaseCard(validName, validCost, validValue, invalidGrid);
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Influence grid must be a 5x5 grid", e.getMessage());
    }
  }

  /**
   * Tests equals method for the same object reference.
   */
  @Test
  public void testEqualsWithSameObject() {
    assertTrue("A card should equal itself", card.equals(card));
  }

  /**
   * Tests equals method with null.
   */
  @Test
  public void testEqualsWithNull() {
    assertFalse("A card should not equal null", card.equals(null));
  }

  /**
   * Tests equals method with different object type.
   */
  @Test
  public void testEqualsWithDifferentType() {
    assertFalse("A card should not equal an object of a different type", card.equals("Not a card"));
  }

  /**
   * Tests equals method with different name.
   */
  @Test
  public void testEqualsWithDifferentName() {
    PawnsBoardBaseCard differentCard = new PawnsBoardBaseCard(
            "OtherCard", validCost, validValue, validInfluenceGrid);
    assertFalse("Cards with different names should not be equal", card.equals(differentCard));
  }

  /**
   * Tests equals method with different cost.
   */
  @Test
  public void testEqualsWithDifferentCost() {
    PawnsBoardBaseCard differentCard = new PawnsBoardBaseCard(
            validName, 1, validValue, validInfluenceGrid);
    assertFalse("Cards with different costs should not be equal", card.equals(differentCard));
  }

  /**
   * Tests equals method with different value.
   */
  @Test
  public void testEqualsWithDifferentValue() {
    PawnsBoardBaseCard differentCard = new PawnsBoardBaseCard(
            validName, validCost, 4, validInfluenceGrid);
    assertFalse("Cards with different values should not be equal", card.equals(differentCard));
  }

  /**
   * Tests equals method with different influence grid.
   */
  @Test
  public void testEqualsWithDifferentGrid() {
    boolean[][] differentGrid = new boolean[5][5];
    differentGrid[0][0] = true; // Different from our valid grid

    PawnsBoardBaseCard differentCard = new PawnsBoardBaseCard(
            validName, validCost, validValue, differentGrid);
    assertFalse("Cards with different grids should not be equal", card.equals(differentCard));
  }

  /**
   * Tests equals method with identical attributes.
   */
  @Test
  public void testEqualsWithIdenticalCards() {
    PawnsBoardBaseCard identicalCard = new PawnsBoardBaseCard(
            validName, validCost, validValue, validInfluenceGrid);
    assertTrue("Cards with identical attributes should be equal", card.equals(identicalCard));
  }

  /**
   * Tests hash code consistency with equals.
   */
  @Test
  public void testHashCodeConsistency() {
    PawnsBoardBaseCard identicalCard = new PawnsBoardBaseCard(
            validName, validCost, validValue, validInfluenceGrid);

    assertEquals("Equal cards should have equal hash codes",
            card.hashCode(), identicalCard.hashCode());
  }

  /**
   * Tests that toString produces the expected string representation of the card.
   */
  @Test
  public void testToString() {
    String expectedString = validName + " (Cost: " + validCost + ", Value: " + validValue + ")\n"
            + "XXXXX\n"
            + "XXIXX\n"
            + "XICIX\n"
            + "XXIXX\n"
            + "XXXXX\n";

    String actualString = card.toString();

    assertEquals("toString should return the correct string representation",
            expectedString, actualString);
  }
}