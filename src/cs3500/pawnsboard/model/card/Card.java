package cs3500.pawnsboard.model.card;

/**
 * Interface representing a card in the Pawns Board game.
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
}
