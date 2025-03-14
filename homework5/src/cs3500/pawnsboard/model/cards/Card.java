package cs3500.pawnsboard.model.cards;

import cs3500.pawnsboard.model.PawnsBoard;

/**
 * Interface representing a card in the {@link PawnsBoard} game.
 * A card has a name, cost, value score, and influence grid.
 */
public interface Card {
  
  /**
   * Gets the name of the card.
   *
   * @return the card name
   */
  String getName();
  
  /**
   * Gets the cost of the card (1-3 pawns).
   *
   * @return the card cost
   */
  int getCost();
  
  /**
   * Gets the value score of the card.
   *
   * @return the value score
   */
  int getValue();

  /**
   * Gets the influence grid as a 2D boolean array.
   * True indicates a cell has influence, false indicates no influence.
   * This is used to determine which cells are affected when a card is played in
   * {@link PawnsBoard#placeCard}.
   *
   * @return the influence grid
   */
  boolean[][] getInfluenceGrid();
  
  /**
   * Gets the influence grid as a 2D char array.
   * 'I' indicates a cell has influence, 'X' indicates no influence,
   * 'C' indicates the card position.
   *
   * @return the influence grid as chars
   */
  char[][] getInfluenceGridAsChars();
  
  /**
   * Determines if this card is equal to another object.
   * Cards are equal if they have the same name, cost, value score, and influence grid.
   *
   * @param o the object to compare with
   * @return true if equal, false otherwise
   */
  @Override
  boolean equals(Object o);
  
  /**
   * Returns a hash code value for the card.
   * Consistent with equals: equal cards must have equal hash codes.
   * Robust implementation of hashing is necessary to ensure that cards follows
   * the specified equality in the assignment instructions.
   *
   * @return a hash code value for this card
   */
  @Override
  int hashCode();

  /**
   * Returns a String representation of the card with useful information
   * about the card and for better readability.
   * @return a String representation of the card with useful information
   */
  @Override
  public String toString();
}
