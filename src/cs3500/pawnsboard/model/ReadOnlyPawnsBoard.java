package cs3500.pawnsboard.model;

import java.util.List;

import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.cell.PawnsBoardCell;
import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.enumerations.PlayerColors;

/**
 * This interface represents the read-only behaviors for the Pawns Board game.
 * It provides observation methods to query the game state without modifying it.
 * Views should use this interface to maintain proper MVC design.
 *
 * <p>The Pawns Board game is a two-player card game played on a board
 * where players place {@link Card}s from their hand onto cells with their pawns.
 * Cards influence cells based on their influence pattern.</p>
 *
 * @param <C> the type of Card used in this game
 * @param <E> the type of Cell used in this game's board
 */
public interface ReadOnlyPawnsBoard<C extends Card, E extends PawnsBoardCell<C>> {

  /**
   * Checks if the game has ended.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Gets the current player whose turn it is.
   *
   * @return the current player
   * @throws IllegalStateException if the game hasn't been started
   */
  PlayerColors getCurrentPlayer() throws IllegalStateException;

  /**
   * Gets the dimensions of the board.
   *
   * @return an array where the elements represent some type of dimension depending on the shape of
   *         the board.
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getBoardDimensions() throws IllegalStateException;

  /**
   * Gets the content type of the given cell position on the board with the given dimensions.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a {@link CellContent} enum indicating whether the cell is empty, contains pawns, or a
   *         card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  CellContent getCellContent(int row, int col)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the owner of a cell's contents (pawns or card).
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the {@link PlayerColors} who owns the cell's contents, or null if the cell is empty
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  PlayerColors getCellOwner(int row, int col)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the number of pawns in a specified cell.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the number of pawns in the cell, or 0 if the cell is empty or contains a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  int getPawnCount(int row, int col)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the cards in the specified playerColors's hand.
   *
   * @param playerColors the playerColors whose hand to retrieve
   * @return a list of Card objects representing the playerColors's hand
   * @throws IllegalStateException if the game hasn't been started
   */
  List<C> getPlayerHand(PlayerColors playerColors) throws IllegalStateException;

  /**
   * Gets the number of cards remaining in the specified playerColors's deck.
   *
   * @param playerColors the playerColors whose deck size to retrieve
   * @return the number of cards left in the playerColors's deck
   * @throws IllegalStateException if the game hasn't been started
   */
  int getRemainingDeckSize(PlayerColors playerColors) throws IllegalStateException;

  /**
   * Gets the row scores for both players for a specific row.
   *
   * @param row the row index to calculate scores for
   * @return an array where the first element is Red's score for the row and the second is Blue's
   * @throws IllegalArgumentException if the row is out of bounds
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getRowScores(int row) throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets the total score for each player across all rows.
   *
   * @return an array where the first element is Red's total score and the second is Blue's
   * @throws IllegalStateException if the game hasn't been started
   */
  int[] getTotalScore() throws IllegalStateException;

  /**
   * Gets the winning player if the game is over.
   *
   * @return the winning PlayerColors (RED or BLUE), or null if the game is tied
   * @throws IllegalStateException if the game hasn't been started or is not over
   */
  PlayerColors getWinner() throws IllegalStateException;

  /**
   * Gets the card at the specified cell position.
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return the card at the specified position, or null if the cell doesn't contain a card
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started
   */
  C getCardAtCell(int row, int col) throws IllegalArgumentException, IllegalStateException;
  
  /**
   * Checks if it's legal for the current player to place a specific card at the given coordinates.
   * This method allows checking move legality without actually making the move.
   *
   * @param cardIndex the index of the card in the current player's hand
   * @param row the row index where the card would be placed
   * @param col the column index where the card would be placed
   * @return true if the move is legal, false otherwise
   * @throws IllegalArgumentException if the coordinates are invalid
   * @throws IllegalStateException if the game hasn't been started or is already over
   */
  boolean isLegalMove(int cardIndex, int row, int col) 
          throws IllegalArgumentException, IllegalStateException;
}