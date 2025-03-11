package cs3500.pawnsboard.model.card.deckbuilder;

import cs3500.pawnsboard.model.card.Card;
import cs3500.pawnsboard.model.card.factory.CardFactory;
import cs3500.pawnsboard.model.card.factory.PawnsBoardRectangleCardFactory;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import cs3500.pawnsboard.model.card.reader.CardReader;
import cs3500.pawnsboard.model.card.reader.PawnsBoardRectangleCardReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Utility class for building decks from lists of PawnsBoardRectangleCard configuration files.
 * Provides methods to create, validate, and mirror decks.
 */
public class PawnsBoardRectangleCardDeckBuilder {

  /**
   * Creates original and mirrored decks from the specified file path with shuffling enabled.
   *
   * @param filePath path to the PawnsBoardRectangleCard configuration file
   * @return a list containing two lists of PawnsBoardRectangleCards:
   *         original (red) and mirrored (blue)
   * @throws InvalidDeckConfigurationException if the file cannot be read
   * @throws InvalidDeckConfigurationException if the file has and invalid format
   */
  public static List<List<Card>> createDecks(String filePath)
          throws InvalidDeckConfigurationException {
    return createDecks(filePath, true);
  }

  /**
   * Creates original and mirrored decks from the specified file path.
   *
   * @param filePath path to the PawnsBoardRectangleCard configuration file
   * @param shuffle whether to shuffle the decks
   * @return a list containing two lists of PawnsBoardRectangleCards:
   *         original (red) and mirrored (blue)
   * @throws InvalidDeckConfigurationException if the file cannot be read
   * @throws InvalidDeckConfigurationException if the file has and invalid format
   */
  public static List<List<Card>> createDecks(String filePath, boolean shuffle)
          throws InvalidDeckConfigurationException {
    CardReader reader = new PawnsBoardRectangleCardReader(new PawnsBoardRectangleCardFactory());
    List<Card> originalCards = reader.readCards(filePath);

    // Validate deck (check for duplicate PawnsBoardRectangleCard)
    validateDeck(originalCards);

    // Create mirrored PawnsBoardRectangleCards for blue player
    List<Card> mirroredCards = createMirroredCards(originalCards);

    // Shuffle the decks if requested
    if (shuffle) {
      Collections.shuffle(originalCards);
      Collections.shuffle(mirroredCards);
    }

    // Return both lists
    return Arrays.asList(originalCards, mirroredCards);
  }

  /**
   * Validates that the deck follows game rules (maximum two copies of any PawnsBoardRectangleCard).
   *
   * @param cards the list of PawnsBoardRectangleCard to validate
   * @throws InvalidDeckConfigurationException if the deck contains more than two copies of any
   *                                           PawnsBoardRectangleCard
   */
  private static void validateDeck(List<Card> cards) throws InvalidDeckConfigurationException {
    // Count occurrences of each PawnsBoardRectangleCard name
    Map<String, Integer> cardCounts = new HashMap<>();

    for (Card card : cards) {
      String name = card.getName();
      cardCounts.put(name, cardCounts.getOrDefault(name, 0) + 1);

      if (cardCounts.get(name) > 2) {
        throw new InvalidDeckConfigurationException(
                "Deck contains more than two copies of card: " + name);
      }
    }
  }

  /**
   * Creates mirrored versions of the provided PawnsBoardRectangleCard.
   *
   * @param originalCards the original PawnsBoardRectangleCards to mirror
   * @return a list of mirrored PawnsBoardRectangleCards
   */
  private static List<Card> createMirroredCards(List<Card> originalCards) {
    List<Card> mirroredCards = new ArrayList<>();
    CardFactory factory = new PawnsBoardRectangleCardFactory();

    for (Card originalCard : originalCards) {
      // Create a mirrored influence grid
      char[][] originalInfluence = originalCard.getInfluenceGridAsChars();
      char[][] mirroredInfluence = mirrorInfluenceGrid(originalInfluence);

      // Create a new PawnsBoardRectangleCards with the mirrored influence
      Card mirroredCard = factory.createPawnsBoardRectangleCard(
              originalCard.getName(),
              originalCard.getCost(),
              originalCard.getValue(),
              mirroredInfluence);

      mirroredCards.add(mirroredCard);
    }

    return mirroredCards;
  }

  /**
   * Mirrors an influence grid across the columns (y-axis).
   *
   * @param originalGrid the original influence grid
   * @return the mirrored grid
   */
  private static char[][] mirrorInfluenceGrid(char[][] originalGrid) {
    int rows = originalGrid.length;
    int cols = originalGrid[0].length;
    char[][] mirroredGrid = new char[rows][cols];

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        // Mirror across columns (swap left and right)
        mirroredGrid[row][col] = originalGrid[row][cols - 1 - col];
      }
    }

    return mirroredGrid;
  }
}