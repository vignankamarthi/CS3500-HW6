package cs3500.pawnsboard.model.cards.factory;

import cs3500.pawnsboard.model.cards.Card;

/**
 * Factory interface for creating {@link Card} objects.
 * For every new type of card, add additional methods for every new type of card and create
 * the corresponding Implementation.
 *
 * @param <C> the specific type of {@link Card} this factory produces
 */
public interface CardFactory<C extends Card> {
  
  /**
   * Creates a card with the specified parameters.
   *
   * @param name         the name of the card
   * @param cost         the cost of the card (1-3)
   * @param value        the value score of the card
   * @param influenceGrid the 5x5 influence grid for the card
   * @return a new card instance of type C
   */
  C createPawnsBoardBaseCard(String name, int cost, int value, char[][] influenceGrid);

  // Add new Card Type creation methods here:
}