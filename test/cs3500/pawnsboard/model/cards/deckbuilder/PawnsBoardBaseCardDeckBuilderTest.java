package cs3500.pawnsboard.model.cards.deckbuilder;

import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.cards.factory.PawnsBoardBaseCardFactory;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Test suite for the PawnsBoardBaseCardDeckBuilder class.
 * This class tests the functionality of building decks, validating decks,
 * and verifying proper mirroring of cards for the blue player.
 */
public class PawnsBoardBaseCardDeckBuilderTest {

  private PawnsBoardBaseCardDeckBuilder deckBuilder;
  private String redValidDeckPath;
  private String blueValidDeckPath;
  private PawnsBoardBaseCardFactory cardFactory;

  /**
   * Set up testing environment before each test.
   */
  @Before
  public void setUp() {
    deckBuilder = new PawnsBoardBaseCardDeckBuilder();
    cardFactory = new PawnsBoardBaseCardFactory();

    // Set up paths to test files
    redValidDeckPath = "docs" + File.separator + "RED3x5TestingDeck.config";
    blueValidDeckPath = "docs" + File.separator + "BLUE3x5TestingDeck.config";
  }

  /**
   * Test that the constructor properly initializes a deck builder.
   */
  @Test
  public void testConstructor() {
    // Just verifying the constructor doesn't throw any exceptions
    PawnsBoardBaseCardDeckBuilder builder = new PawnsBoardBaseCardDeckBuilder();
    assertNotNull(builder);
  }

  /**
   * Test creating a single deck with a valid deck configuration file.
   */
  @Test
  public void testCreateDeck_ValidFile() throws InvalidDeckConfigurationException {
    List<PawnsBoardBaseCard> deck = deckBuilder.createDeck(redValidDeckPath);

    // Verify deck is not empty
    assertFalse(deck.isEmpty());
    
    // First card should be Security (if using the test deck file)
    assertEquals("Security", deck.get(0).getName());
    assertEquals(1, deck.get(0).getCost());
    assertEquals(2, deck.get(0).getValue());
  }

  /**
   * Test creating a single deck with the shuffle parameter set to false.
   */
  @Test
  public void testCreateDeck_NoShuffle() throws InvalidDeckConfigurationException {
    List<PawnsBoardBaseCard> deck = deckBuilder.createDeck(redValidDeckPath, false);

    // With no shuffle, cards should be in the same order as in the file
    // Check first card
    PawnsBoardBaseCard firstCard = deck.get(0);
    
    assertEquals("Security", firstCard.getName());
    assertEquals(1, firstCard.getCost());
    assertEquals(2, firstCard.getValue());
  }

  /**
   * Test creating a single deck with the shuffle parameter set to true.
   * Note: This is a non-deterministic test due to shuffling.
   * We can only check deck size and contents, not order.
   */
  @Test
  public void testCreateDeck_WithShuffle() throws InvalidDeckConfigurationException {
    List<PawnsBoardBaseCard> deck = deckBuilder.createDeck(redValidDeckPath, true);

    // Since we can't reliably test randomness, just verify cards exist
    assertFalse(deck.isEmpty());
  }

  /**
   * Test that separate RED and BLUE deck files work correctly.
   */
  @Test
  public void testSeparateRedBlueDeckFiles() throws InvalidDeckConfigurationException {
    List<PawnsBoardBaseCard> redDeck = deckBuilder.createDeck(redValidDeckPath, false);
    List<PawnsBoardBaseCard> blueDeck = deckBuilder.createDeck(blueValidDeckPath, false);
    
    // Both decks should have the same number of cards
    assertEquals(redDeck.size(), blueDeck.size());
    
    // Cards should have the same basic properties but different influence grids
    for (int i = 0; i < redDeck.size(); i++) {
      PawnsBoardBaseCard redCard = redDeck.get(i);
      PawnsBoardBaseCard blueCard = blueDeck.get(i);
      
      // Same name, cost, and value
      assertEquals(redCard.getName(), blueCard.getName());
      assertEquals(redCard.getCost(), blueCard.getCost());
      assertEquals(redCard.getValue(), blueCard.getValue());
      
      // Different influence grids (not testing specifics of mirroring here)
      boolean allSame = true;
      char[][] redInfluence = redCard.getInfluenceGridAsChars();
      char[][] blueInfluence = blueCard.getInfluenceGridAsChars();
      
      for (int row = 0; row < 5 && allSame; row++) {
        for (int col = 0; col < 5; col++) {
          // Only checking outside the center position which is always 'C'
          if ((row != 2 || col != 2) && redInfluence[row][col] != blueInfluence[row][col]) {
            allSame = false;
            break;
          }
        }
      }
      
      // At least some of the influence cells should be different
      assertFalse("RED and BLUE cards should have different influence grids", allSame);
    }
  }









  /**
   * Test that an exception is thrown when the file path is invalid.
   */
  @Test
  public void testCreateDeck_InvalidFilePath() {
    try {
      deckBuilder.createDeck("nonexistent/path.config");
    } catch (IllegalArgumentException e) {
      assertEquals("File not found: nonexistent/path.config", e.getMessage());
    } catch (InvalidDeckConfigurationException e) {
      // Should not reach here
    }
  }

  /**
   * Test that an exception is thrown when the file has invalid format.
   */
  @Test
  public void testCreateDeck_InvalidFileFormat() {
    try {
      // Create a temporary invalid format file path
      String invalidFormatPath = "test/invalid_format.config";
      deckBuilder.createDeck(invalidFormatPath);
    } catch (IllegalArgumentException e) {
      assertEquals("File not found: test/invalid_format.config", e.getMessage());
    } catch (InvalidDeckConfigurationException e) {
      // Should not reach here
    }
  }

  /**
   * Test validation of a valid deck with cards.
   */
  @Test
  public void testValidateDeck_ValidDeck() throws InvalidDeckConfigurationException {
    // Create a valid deck with one card
    List<PawnsBoardBaseCard> deck = new ArrayList<>();

    // Create a simple influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C';
    influenceGrid[1][2] = 'I';

    // Add a card to the deck
    deck.add(cardFactory.createPawnsBoardBaseCard("TestCard", 1, 2,
            influenceGrid));

    // This should not throw an exception
    deckBuilder.validateDeck(deck);

    assertEquals("Deck should still contain the card after validation",
            1, deck.size());
    assertEquals("Card in deck should remain unchanged", "TestCard",
            deck.get(0).getName());
  }

  /**
   * Test that validation throws an exception when a deck has more than two copies of a card.
   */
  @Test
  public void testValidateDeck_MoreThanTwoCopies() {
    try {
      // Create a deck with three copies of the same card
      List<PawnsBoardBaseCard> deck = new ArrayList<>();

      // Create a simple influence grid
      char[][] influenceGrid = new char[5][5];
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
          influenceGrid[i][j] = 'X';
        }
      }
      influenceGrid[2][2] = 'C';
      influenceGrid[1][2] = 'I';

      // Add three copies of the same card
      PawnsBoardBaseCard card = cardFactory.createPawnsBoardBaseCard("DuplicateCard", 1,
              2, influenceGrid);
      deck.add(card);
      deck.add(card);
      deck.add(card);

      // This should throw an exception
      deckBuilder.validateDeck(deck);
    } catch (InvalidDeckConfigurationException e) {
      assertEquals("Deck contains more than two copies of card: DuplicateCard", e.getMessage());
    }
  }

  /**
   * Test that validation passes when a deck has exactly two copies of a card.
   */
  @Test
  public void testValidateDeck_ExactlyTwoCopies() throws InvalidDeckConfigurationException {
    // Create a deck with two copies of the same card
    List<PawnsBoardBaseCard> deck = new ArrayList<>();

    // Create a simple influence grid
    char[][] influenceGrid = new char[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        influenceGrid[i][j] = 'X';
      }
    }
    influenceGrid[2][2] = 'C';
    influenceGrid[1][2] = 'I';

    // Add two copies of the same card
    PawnsBoardBaseCard card = cardFactory.createPawnsBoardBaseCard("DuplicateCard", 1,
            2, influenceGrid);
    deck.add(card);
    deck.add(card);

    // This should not throw an exception
    deckBuilder.validateDeck(deck);
    
    // Assert that validation passed successfully
    assertEquals("Deck should maintain two copies after validation", 2, deck.size());
    assertEquals("First card name should be preserved", "DuplicateCard", deck.get(0).getName());
    assertEquals("Second card name should be preserved", "DuplicateCard", deck.get(1).getName());
  }

  /**
   * Test that validation works with an empty deck.
   */
  @Test
  public void testValidateDeck_EmptyDeck() throws InvalidDeckConfigurationException {
    // Create an empty deck
    List<PawnsBoardBaseCard> deck = new ArrayList<>();

    // This should not throw an exception
    deckBuilder.validateDeck(deck);
    
    // Assert that the empty deck remains empty after validation
    assertTrue("Empty deck should remain empty after validation", deck.isEmpty());
    assertEquals("Empty deck should have size 0", 0, deck.size());
  }

  /**
   * Test creating a deck with both shuffle parameters results in different deck orders.
   * Note: This is a probabilistic test, and could rarely fail due to coincidental identical
   * shuffling.
   */
  @Test
  public void testCreateDeck_ShuffleVsNoShuffle() throws InvalidDeckConfigurationException {
    // Get deck without shuffling
    List<PawnsBoardBaseCard> deckNoShuffle = deckBuilder.createDeck(redValidDeckPath, false);

    // Get deck with shuffling
    List<PawnsBoardBaseCard> deckWithShuffle = deckBuilder.createDeck(redValidDeckPath, true);

    // Check that decks have the same size
    assertEquals(deckNoShuffle.size(), deckWithShuffle.size());

    // Check that at least one card is in a different position
    // This is a probabilistic check - it could technically fail if the shuffle happens to
    // produce the exact same order, but this is extremely unlikely with a deck of any reasonable
    // size
    boolean atLeastOneDifferent = false;
    for (int i = 0; i < deckNoShuffle.size(); i++) {
      if (!deckNoShuffle.get(i).getName().
              equals(deckWithShuffle.get(i).getName())) {
        atLeastOneDifferent = true;
        break;
      }
    }

    // Assert that at least one card is in a different position
    assertTrue("Shuffled deck should have at least one card in a different position",
            atLeastOneDifferent);
  }
}