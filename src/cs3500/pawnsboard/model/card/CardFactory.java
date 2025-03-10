package cs3500.pawnsboard.model.card;

/**
 * Factory interface for creating Card objects.
 * Abstracts the card creation process from the reading logic.
 */
public interface CardFactory {
  
  /**
   * Creates a card with the specified parameters.
   *
   * @param name         the name of the card
   * @param cost         the cost of the card (1-3)
   * @param value        the value score of the card
   * @param influenceGrid the 5x5 influence grid for the card
   * @return a new Card instance
   */
  Card createCard(String name, int cost, int value, char[][] influenceGrid);
}
