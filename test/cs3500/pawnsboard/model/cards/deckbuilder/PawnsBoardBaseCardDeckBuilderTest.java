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
  private String validDeckPath;
  private String smallDeckPath;
  private PawnsBoardBaseCardFactory cardFactory;

  /**
   * Set up testing environment before each test.
   */
  @Before
  public void setUp() {
    deckBuilder = new PawnsBoardBaseCardDeckBuilder();
    cardFactory = new PawnsBoardBaseCardFactory();

    // Set up paths to test files
    validDeckPath = "docs" + File.separator + "3x5TestingDeck.config";
    smallDeckPath = "docs" + File.separator + "1x3SmallPawnsBoardBaseCompleteDeck.config";
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
   * Test creating decks with a valid deck configuration file.
   */
  @Test
  public void testCreateDecks_ValidFile() throws InvalidDeckConfigurationException {
    List<List<PawnsBoardBaseCard>> decks = deckBuilder.createDecks(validDeckPath);

    // Verify we got two decks
    assertEquals(2, decks.size());

    // Verify decks have the same size
    assertEquals(decks.get(0).size(), decks.get(1).size());

    // Verify deck is not empty
    assertFalse(decks.get(0).isEmpty());
    assertFalse(decks.get(1).isEmpty());
  }

  /**
   * Test creating decks with the shuffle parameter set to false.
   */
  @Test
  public void testCreateDecks_NoShuffle() throws InvalidDeckConfigurationException {
    List<List<PawnsBoardBaseCard>> decks = deckBuilder.createDecks(validDeckPath, false);

    // With no shuffle, cards should be in the same order as in the file
    // Check first card in both decks
    PawnsBoardBaseCard redFirst = decks.get(0).get(0);
    PawnsBoardBaseCard blueFirst = decks.get(1).get(0);

    assertEquals("Security", redFirst.getName());
    assertEquals("Security", blueFirst.getName());
    assertEquals(1, redFirst.getCost());
    assertEquals(1, blueFirst.getCost());
    assertEquals(2, redFirst.getValue());
    assertEquals(2, blueFirst.getValue());
  }

  /**
   * Test creating decks with the shuffle parameter set to true.
   * Note: This is a non-deterministic test due to shuffling.
   * We can only check deck sizes and contents, not order.
   */
  @Test
  public void testCreateDecks_WithShuffle() throws InvalidDeckConfigurationException {
    List<List<PawnsBoardBaseCard>> decks = deckBuilder.createDecks(validDeckPath, true);

    // Verify we got two decks in total
    assertEquals(2, decks.size());

    // Both decks should have the same size
    assertEquals(decks.get(0).size(), decks.get(1).size());

    // Since we can't reliably test randomness, just verify cards exist
    assertFalse(decks.get(0).isEmpty());
    assertFalse(decks.get(1).isEmpty());
  }

  /**
   * Test that mirroring works correctly for the blue player's cards.
   */
  @Test
  public void testMirroringForBluePlayer() throws InvalidDeckConfigurationException {
    List<List<PawnsBoardBaseCard>> decks = deckBuilder.createDecks(smallDeckPath, false);

    // Get a card from both decks
    PawnsBoardBaseCard redCard = decks.get(0).get(0);
    PawnsBoardBaseCard blueCard = decks.get(1).get(0);

    // Cards should have the same name, cost, and value
    assertEquals(redCard.getName(), blueCard.getName());
    assertEquals(redCard.getCost(), blueCard.getCost());
    assertEquals(redCard.getValue(), blueCard.getValue());

    // Convert influence grids to char arrays for easier comparison
    char[][] redInfluence = redCard.getInfluenceGridAsChars();
    char[][] blueInfluence = blueCard.getInfluenceGridAsChars();

    // Center position should be the same in both
    assertEquals(redInfluence[2][2], blueInfluence[2][2]);

    // Check that blue influence is mirrored horizontally
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        assertEquals(redInfluence[row][col], blueInfluence[row][4 - col]);
      }
    }
  }

  /**
   * Test that an exception is thrown when the file path is invalid.
   */
  @Test
  public void testCreateDecks_InvalidFilePath() {
    try {
      deckBuilder.createDecks("nonexistent/path.config");
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
  public void testCreateDecks_InvalidFileFormat() {
    try {
      // Create a temporary invalid format file path
      String invalidFormatPath = "test/invalid_format.config";
      deckBuilder.createDecks(invalidFormatPath);
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
  }

  /**
   * Test creating decks with both shuffle parameters results in different deck orders.
   * Note: This is a probabilistic test, and could rarely fail due to coincidental identical
   * shuffling.
   */
  @Test
  public void testCreateDecks_ShuffleVsNoShuffle() throws InvalidDeckConfigurationException {
    // Get decks without shuffling
    List<List<PawnsBoardBaseCard>> decksNoShuffle = deckBuilder.createDecks(validDeckPath,
            false);

    // Get decks with shuffling
    List<List<PawnsBoardBaseCard>> decksWithShuffle = deckBuilder.createDecks(validDeckPath,
            true);

    // Check that decks have the same size
    assertEquals(decksNoShuffle.get(0).size(), decksWithShuffle.get(0).size());

    // Check that at least one card is in a different position
    // This is a probabilistic check - it could technically fail if the shuffle happens to
    // produce the exact same order, but this is extremely unlikely with a deck of any reasonable
    // size
    boolean atLeastOneDifferent = false;
    for (int i = 0; i < decksNoShuffle.get(0).size(); i++) {
      if
      (!decksNoShuffle.get(0).get(i).getName().equals(decksWithShuffle.get(0).get(i).getName())) {
        atLeastOneDifferent = true;
        break;
      }
    }

    // Assert that at least one card is in a different position
    assertTrue("Shuffled deck should have at least one card in a different position", atLeastOneDifferent);
  }
}