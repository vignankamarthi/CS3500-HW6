package cs3500.pawnsboard.model.cell;

import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

/**
 * Represents a single cell on the {@link PawnsBoard}.
 * A cell can contain pawns, a card, or be empty.
 * 
 * @param <C> the type of Card that can be placed in this cell
 */
public interface PawnsBoardCell<C extends Card> {

  /**
   * Gets the content type of this cell.
   *
   * @return the cell content type
   */
  CellContent getContent();

  /**
   * Gets the owner of this cell's contents.
   *
   * @return the player who owns the contents, or null if the cell is empty
   */
  PlayerColors getOwner();

  /**
   * Gets the number of pawns in this cell.
   *
   * @return the pawn count, or 0 if the cell is empty or contains a card
   */
  int getPawnCount();

  /**
   * Gets the card in this cell.
   *
   * @return the card, or null if the cell is empty or contains pawns
   */
  C getCard();

  /**
   * Adds a pawn to this cell. If the cell is empty, it becomes a pawn cell.
   *
   * @param playerColors the playerColors who owns the pawn
   * @throws IllegalStateException if trying to add a pawn to a cell with a card
   * @throws IllegalStateException if the cell already has the maximum number of pawns
   * @throws Exception actually throws an IllegalOwnerException when trying to add a pawn of a
   *                   different owner
   * @throws IllegalArgumentException if playerColors is null
   */
  void addPawn(PlayerColors playerColors) throws IllegalOwnerException;

  /**
   * Changes the ownership of pawns in this cell.
   * The pawn count remains the same, but the owner changes.
   *
   * @param newOwner the new owner of the pawns
   * @throws IllegalStateException if trying to change ownership of non-pawn content
   */
  void changeOwnership(PlayerColors newOwner);

  /**
   * Places a card in this cell, replacing any pawns.
   * The cell's content becomes a card, and pawns are removed.
   *
   * @param card the card to place
   * @param playerColors the playerColors who owns the card
   */
  void setCard(C card, PlayerColors playerColors);
}