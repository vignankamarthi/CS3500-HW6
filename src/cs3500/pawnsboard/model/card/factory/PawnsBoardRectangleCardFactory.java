package cs3500.pawnsboard.model.card.factory;

import cs3500.pawnsboard.model.card.Card;
import cs3500.pawnsboard.model.card.PawnsBoardRectangleCard;

/**
 * Default implementation of PawnsBoardRectangleCard for the Pawns Board game.
 * Creates Card objects based on provided parameters using a method chaining approach.
 */
//TODO: Test this implementation
public class PawnsBoardRectangleCardFactory implements CardFactory {
  
  /**
   * Creates a card with the specified parameters.
   *
   * @param name          the name of the card
   * @param cost          the cost of the card (1-3)
   * @param value         the value score of the card
   * @param influenceGrid the 5x5 influence grid for the card
   * @return a new Card instance
   */
  @Override
  public Card createPawnsBoardRectangleCard(String name, int cost, int value,
                                            char[][] influenceGrid) {
    return new CardBuilder()
        .withName(name)
        .withCost(cost)
        .withValue(value)
        .withInfluenceGrid(convertInfluenceGrid(influenceGrid))
        .build();
  }
  
  /**
   * Converts the char grid to a boolean grid where true indicates influence.
   *
   * @param charGrid the char grid to convert
   * @return the boolean influence grid
   */
  private boolean[][] convertInfluenceGrid(char[][] charGrid) {
    boolean[][] influence = new boolean[5][5];
    
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        // Mark cells with influence (I)
        influence[row][col] = charGrid[row][col] == 'I';
      }
    }
    
    return influence;
  }
  
  /**
   * Builder class for creating PawnsBoardRectangleCard instances.
   * Allows for method chaining for better readability.
   */
  private static class CardBuilder {
    private String name;
    private int cost;
    private int value;
    private boolean[][] influenceGrid;
    
    /**
     * Sets the PawnsBoardRectangleCard name.
     *
     * @param name the name of the card
     * @return this builder for method chaining
     */
    public CardBuilder withName(String name) {
      this.name = name;
      return this;
    }
    
    /**
     * Sets the PawnsBoardRectangleCard cost.
     *
     * @param cost the cost of the card
     * @return this builder for method chaining
     */
    public CardBuilder withCost(int cost) {
      this.cost = cost;
      return this;
    }
    
    /**
     * Sets the PawnsBoardRectangleCard value.
     *
     * @param value the value of the card
     * @return this builder for method chaining
     */
    public CardBuilder withValue(int value) {
      this.value = value;
      return this;
    }
    
    /**
     * Sets the PawnsBoardRectangleCard influence grid.
     *
     * @param influenceGrid the influence grid
     * @return this builder for method chaining
     */
    public CardBuilder withInfluenceGrid(boolean[][] influenceGrid) {
      this.influenceGrid = influenceGrid;
      return this;
    }
    
    /**
     * Builds a new PawnsBoardRectangleCard instance.
     *
     * @return the new Card
     */
    public Card build() {
      return new PawnsBoardRectangleCard(name, cost, value, influenceGrid);
    }
  }
}
