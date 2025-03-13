package cs3500.pawnsboard.model.cards.reader;

import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.cards.factory.CardFactory;
import cs3500.pawnsboard.model.cards.factory.PawnsBoardBaseCardFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for PawnsBoardBaseCardReader.
 * Tests functionality for reading card configurations from files.
 */
public class PawnsBoardBaseCardReaderTest {

  private CardFactory<PawnsBoardBaseCard> cardFactory;
  private PawnsBoardBaseCardReader cardReader;
  private String validCardConfig;
  private String invalidCostCardConfig;
  private String invalidValueCardConfig;
  private String invalidGridSizeCardConfig;
  private String invalidGridCharConfig;
  private String multipleCardCConfig;
  private String noCardCConfig;
  private String wrongCardCPositionConfig;

  // Keep track of created temporary files to clean up after tests
  private final java.util.List<File> tempFiles = new java.util.ArrayList<>();

  @Before
  public void setUp() {
    cardFactory = new PawnsBoardBaseCardFactory();
    cardReader = new PawnsBoardBaseCardReader(cardFactory);

    // Valid card configuration
    validCardConfig = "TestCard 2 3\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";

    // Invalid cost card configuration
    invalidCostCardConfig = "TestCard 4 3\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";

    // Invalid value card configuration
    invalidValueCardConfig = "TestCard 2 0\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";

    // Invalid grid size card configuration
    invalidGridSizeCardConfig = "TestCard 2 3\nXXXXX\nXXIXX\nXICIX\nXXIXX";

    // Invalid grid character card configuration
    invalidGridCharConfig = "TestCard 2 3\nXXXXX\nXXIXX\nXICAX\nXXIXX\nXXXXX";

    // Multiple C in grid configuration
    multipleCardCConfig = "TestCard 2 3\nXXXXX\nXXIXX\nXICCX\nXXIXX\nXXXXX";

    // No C in grid configuration
    noCardCConfig = "TestCard 2 3\nXXXXX\nXXIXX\nXIXIX\nXXIXX\nXXXXX";

    // Wrong position C in grid configuration
    wrongCardCPositionConfig = "TestCard 2 3\nXXXXX\nXXICX\nXIXIX\nXXIXX\nXXXXX";
  }

  @After
  public void tearDown() {
    // Delete all temporary files created during tests
    for (File file : tempFiles) {
      if (file.exists()) {
        file.delete();
      }
    }
  }

  /**
   * Tests that the constructor properly creates a PawnsBoardBaseCardReader object.
   */
  @Test
  public void testConstructor() {
    assertNotNull("CardReader should be created", cardReader);
  }

  /**
   * Tests that passing null to the constructor throws the appropriate exception.
   */
  @Test
  public void testConstructorWithNullFactory() {
    try {
      new PawnsBoardBaseCardReader(null);
    } catch (NullPointerException e) {
      assertEquals("Expected appropriate exception message",
              "Cannot invoke \"cs3500.pawnsboard.model.cards.factory.CardFactory.createPawnsBoardBaseCard"
                      + "(String, int, int, char[][])\" because \"this.cardFactory\" is null", e.getMessage());
    }
  }

  /**
   * Tests reading a valid card configuration from a file.
   */
  @Test
  public void testReadValidCard() throws IOException {
    File cardFile = createTempFile("validCard.config", validCardConfig);

    List<PawnsBoardBaseCard> cards = cardReader.readCards(cardFile.getAbsolutePath());

    assertEquals("Should read exactly one card", 1, cards.size());
    PawnsBoardBaseCard card = cards.get(0);
    assertEquals("Card name should match", "TestCard", card.getName());
    assertEquals("Card cost should match", 2, card.getCost());
    assertEquals("Card value should match", 3, card.getValue());

    // Check influence grid
    boolean[][] influenceGrid = card.getInfluenceGrid();
    assertFalse("Top-center cell should not have influence", influenceGrid[0][2]);
    assertTrue("Middle row should have influence", influenceGrid[2][1]);
    assertTrue("Middle row should have influence", influenceGrid[2][3]);
  }

  /**
   * Tests reading multiple valid cards from a file.
   */
  @Test
  public void testReadMultipleValidCards() throws IOException {
    String multiCardConfig = validCardConfig + "\nCard2 1 2\nXXXXX\nXXXXX\nXXCXX\nXXXXX\nXXXXX";
    File cardFile = createTempFile("multiCard.config", multiCardConfig);

    List<PawnsBoardBaseCard> cards = cardReader.readCards(cardFile.getAbsolutePath());

    assertEquals("Should read exactly two cards", 2, cards.size());

    // Check first card
    PawnsBoardBaseCard card1 = cards.get(0);
    assertEquals("First card name should match", "TestCard", card1.getName());

    // Check second card
    PawnsBoardBaseCard card2 = cards.get(1);
    assertEquals("Second card name should match", "Card2", card2.getName());
  }

  /**
   * Tests that trying to read from a non-existent file throws the appropriate exception.
   */
  @Test
  public void testReadFromNonExistentFile() {
    try {
      cardReader.readCards("non_existent_file.config");
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "File not found: non_existent_file.config", e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with invalid cost.
   */
  @Test
  public void testReadCardWithInvalidCost() throws IOException {
    File cardFile = createTempFile("invalidCost.config", invalidCostCardConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Card cost must be between 1 and 3, got: 4", e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with invalid value.
   */
  @Test
  public void testReadCardWithInvalidValue() throws IOException {
    File cardFile = createTempFile("invalidValue.config", invalidValueCardConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Card value must be positive, got: 0", e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with invalid grid size.
   */
  @Test
  public void testReadCardWithInvalidGridSize() throws IOException {
    File cardFile = createTempFile("invalidGridSize.config", invalidGridSizeCardConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Unexpected end of file while reading influence grid", e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with invalid grid characters.
   */
  @Test
  public void testReadCardWithInvalidGridChars() throws IOException {
    File cardFile = createTempFile("invalidGridChars.config", invalidGridCharConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Invalid character in influence grid: A, expected X, I, or C",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with multiple C positions.
   */
  @Test
  public void testReadCardWithMultipleCPositions() throws IOException {
    File cardFile = createTempFile("multipleC.config", multipleCardCConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Multiple card positions (C) found in influence grid",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with no C position.
   */
  @Test
  public void testReadCardWithNoCardPosition() throws IOException {
    File cardFile = createTempFile("noC.config", noCardCConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: No card position (C) found in influence grid",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown when reading a card with C in wrong position.
   */
  @Test
  public void testReadCardWithWrongCPosition() throws IOException {
    File cardFile = createTempFile("wrongCPosition.config", wrongCardCPositionConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Card position (C) must be in the center at (2,2), found at (1,3)",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown for malformed card header.
   */
  @Test
  public void testMalformedCardHeader() throws IOException {
    String malformedHeaderConfig = "TestCard 2\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";
    File cardFile = createTempFile("malformedHeader.config", malformedHeaderConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Invalid card header format: TestCard 2",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown for unexpected end of file.
   */
  @Test
  public void testUnexpectedEndOfFile() throws IOException {
    String truncatedConfig = "TestCard 2 3\nXXXXX\nXXIXX";
    File cardFile = createTempFile("truncated.config", truncatedConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Unexpected end of file while reading influence grid",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown for non-numeric cost.
   */
  @Test
  public void testNonNumericCost() throws IOException {
    String nonNumericCostConfig = "TestCard ABC 3\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";
    File cardFile = createTempFile("nonNumericCost.config", nonNumericCostConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Invalid cost object type in card header: TestCard ABC 3",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown for non-numeric value.
   */
  @Test
  public void testNonNumericValue() throws IOException {
    String nonNumericValueConfig = "TestCard 2 ABC\nXXXXX\nXXIXX\nXICIX\nXXIXX\nXXXXX";
    File cardFile = createTempFile("nonNumericValue.config", nonNumericValueConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Invalid value object type in card header: TestCard 2 ABC",
              e.getMessage());
    }
  }

  /**
   * Tests that an exception is thrown for wrong grid line length.
   */
  @Test
  public void testWrongGridLineLength() throws IOException {
    String wrongLineLengthConfig = "TestCard 2 3\nXXXXX\nXXIXXX\nXICIX\nXXIXX\nXXXXX";
    File cardFile = createTempFile("wrongLineLength.config", wrongLineLengthConfig);

    try {
      cardReader.readCards(cardFile.getAbsolutePath());
    } catch (IllegalArgumentException e) {
      assertEquals("Expected appropriate exception message",
              "Error reading card file: Influence grid line must have exactly 5 characters, got: XXIXXX",
              e.getMessage());
    }
  }

  /**
   * Tests that an empty file results in an empty list and not an exception.
   */
  @Test
  public void testEmptyFile() throws IOException {
    File cardFile = createTempFile("empty.config", "");

    List<PawnsBoardBaseCard> cards = cardReader.readCards(cardFile.getAbsolutePath());

    assertTrue("Should return empty list for empty file", cards.isEmpty());
  }

  /**
   * Helper method to create a temporary file with the given content.
   *
   * @param fileName the name of the file to create
   * @param content the content to write to the file
   * @return the created file
   * @throws IOException if there's an error creating or writing to the file
   */
  private File createTempFile(String fileName, String content) throws IOException {
    // Create temp file in the system's temp directory
    File file = File.createTempFile(fileName, null);
    file.deleteOnExit(); // Request deletion when JVM exits
    tempFiles.add(file); // Add to list for cleanup

    // Write content to the file
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(content);
    }

    return file;
  }
}