package cs3500.pawnsboard.model;

import cs3500.pawnsboard.model.card.Card;
import cs3500.pawnsboard.model.card.CardFactory;
import cs3500.pawnsboard.model.card.PawnsBoardCardFactory;
import cs3500.pawnsboard.model.reader.CardReader;
import cs3500.pawnsboard.model.reader.PawnsBoardCardReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Utility class for building decks from lists of cards.
 * Provides methods to create, validate, and mirror decks.
 */
public class DeckBuilder {
  
  /**
   * Creates original and mirrored decks from the specified file path.
   *
   * @param filePath path to the card configuration file
   * @return a list containing two lists of cards: original (red) and mirrored (blue)
   * @throws IllegalArgumentException if the file cannot be read or has invalid format
   */
  public static List<List<Card>> createDecks(String filePath) 
      throws IllegalArgumentException {
    CardReader reader = new PawnsBoardCardReader(new PawnsBoardCardFactory());
    List<Card> originalCards = reader.readCards(filePath);
    
    // Validate deck (check for duplicate cards)
    validateDeck(originalCards);
    
    // Create mirrored cards for blue player
    List<Card> mirroredCards = createMirroredCards(originalCards);
    
    // Return both lists
    return Arrays.asList(originalCards, mirroredCards);
  }
  
  /**
   * Validates that the deck follows game rules (maximum two copies of any card).
   *
   * @param cards the list of cards to validate
   * @throws IllegalArgumentException if the deck contains more than two copies of any card
   */
  private static void validateDeck(List<Card> cards) throws IllegalArgumentException {
    // Count occurrences of each card name
    Map<String, Integer> cardCounts = new HashMap<>();
    
    for (Card card : cards) {
      String name = card.getName();
      cardCounts.put(name, cardCounts.getOrDefault(name, 0) + 1);
      
      if (cardCounts.get(name) > 2) {
        throw new IllegalArgumentException(
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
  private static List<Card> createMirroredCards(List<Card> originalCards) {
    List<Card> mirroredCards = new ArrayList<>();
    CardFactory factory = new PawnsBoardCardFactory();
    
    for (Card originalCard : originalCards) {
      // Create a mirrored influence grid
      char[][] originalInfluence = originalCard.getInfluenceGridAsChars();
      char[][] mirroredInfluence = mirrorInfluenceGrid(originalInfluence);
      
      // Create a new card with the mirrored influence
      Card mirroredCard = factory.createCard(
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
