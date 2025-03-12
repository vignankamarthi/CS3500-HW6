package cs3500.pawnsboard.model.cards.deckbuilder;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cards.PawnsBoardBaseCard;
import cs3500.pawnsboard.model.cards.factory.CardFactory;
import cs3500.pawnsboard.model.cards.factory.PawnsBoardBaseCardFactory;
import cs3500.pawnsboard.model.exceptions.InvalidDeckConfigurationException;
import cs3500.pawnsboard.model.cards.reader.CardReader;
import cs3500.pawnsboard.model.cards.reader.PawnsBoardBaseCardReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation of {@link DeckBuilder} for {@link PawnsBoardBaseCard}.
 * Provides methods to create, validate, and mirror decks.
 */
public class PawnsBoardBaseCardDeckBuilder implements DeckBuilder<PawnsBoardBaseCard> {

  private final CardFactory<PawnsBoardBaseCard> cardFactory;
  private final CardReader<PawnsBoardBaseCard> cardReader;

  /**
   * Constructs a PawnsBoardBaseCardDeckBuilder with default factory and reader.
   */
  public PawnsBoardBaseCardDeckBuilder() {
    this.cardFactory = new PawnsBoardBaseCardFactory();
    this.cardReader = new PawnsBoardBaseCardReader(cardFactory);
  }

  /**
   * Creates decks for both players from a configuration file.
   *
   * @param filePath path to the {@link Card} configuration file
   * @return a list containing two decks (lists of {@link PawnsBoardBaseCard}s): one for each player
   * @throws {@link InvalidDeckConfigurationException} if the deck configuration is invalid
   */
  @Override
  public List<List<PawnsBoardBaseCard>> createDecks(String filePath)
          throws InvalidDeckConfigurationException {
    return createDecks(filePath, true);
  }

  /**
   * Creates decks for both players with optional shuffling.
   *
   * @param filePath path to the card configuration file
   * @param shuffle whether to shuffle the decks
   * @return a list containing two decks (lists of {@link PawnsBoardBaseCard}s): one for each player
   * @throws {@link InvalidDeckConfigurationException} if the deck configuration is invalid
   */
  @Override
  public List<List<PawnsBoardBaseCard>> createDecks(String filePath, boolean shuffle)
          throws InvalidDeckConfigurationException {
    // Read cards from file
    List<PawnsBoardBaseCard> originalCards = new ArrayList<>();
    originalCards.addAll(cardReader.readCards(filePath));

    // Validate deck
    validateDeck(originalCards);

    // Create mirrored cards for blue player
    List<PawnsBoardBaseCard> mirroredCards = createMirroredCards(originalCards);

    // Shuffle the decks if requested
    if (shuffle) {
      Collections.shuffle(originalCards);
      Collections.shuffle(mirroredCards);
    }

    // Return both lists
    return Arrays.asList(originalCards, mirroredCards);
  }

  /**
   * Validates that a deck follows the {@link cs3500.pawnsboard.model.PawnsBoard} rules.
   *
   * @param deck the deck to validate
   * @throws {@link InvalidDeckConfigurationException} if the deck doesn't follow game rules
   */
  @Override
  public void validateDeck(List<PawnsBoardBaseCard> deck)
          throws InvalidDeckConfigurationException {
    // Count occurrences of each card name
    Map<String, Integer> cardCounts = new HashMap<>();

    for (PawnsBoardBaseCard card : deck) {
      String name = card.getName();
      cardCounts.put(name, cardCounts.getOrDefault(name, 0) + 1);

      if (cardCounts.get(name) > 2) {
        throw new InvalidDeckConfigurationException(
                "Deck contains more than two copies of card: " + name);
      }
    }
  }

  /**
   * Creates mirrored versions of the provided cards.
   *
   * @param originalCards the original cards to mirror
   * @return a list of mirrored cards
   */
  private List<PawnsBoardBaseCard> createMirroredCards(
          List<PawnsBoardBaseCard> originalCards) {
    List<PawnsBoardBaseCard> mirroredCards = new ArrayList<>();

    for (PawnsBoardBaseCard originalCard : originalCards) {
      // Create a mirrored influence grid
      char[][] originalInfluence = originalCard.getInfluenceGridAsChars();
      char[][] mirroredInfluence = mirrorInfluenceGrid(originalInfluence);

      // Create a new card with the mirrored influence
      PawnsBoardBaseCard mirroredCard = cardFactory.createPawnsBoardBaseCard(
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
  private char[][] mirrorInfluenceGrid(char[][] originalGrid) {
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