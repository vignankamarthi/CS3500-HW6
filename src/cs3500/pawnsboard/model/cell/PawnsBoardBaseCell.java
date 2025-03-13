package cs3500.pawnsboard.model.cell;

import cs3500.pawnsboard.model.enumerations.CellContent;
import cs3500.pawnsboard.model.PawnsBoard;
import cs3500.pawnsboard.model.cards.Card;
import cs3500.pawnsboard.model.enumerations.PlayerColors;
import cs3500.pawnsboard.model.exceptions.IllegalOwnerException;

/**
 * Implementation of {@link PawnsBoardCell} for the {@link PawnsBoard} game.
 * Represents a single cell on the board that can contain pawns, a card, or be empty.
 *
 * <p>This implementation maintains the following invariants:</p>
 * <ul>
 *   <li>A cell can only have one type of content at a time (empty, pawns, or a card)</li>
 *   <li>Pawns can only be added if there is space (max 3 pawns in this implementation)</li>
 *   <li>Pawns in a cell all belong to a single player</li>
 * </ul>
 *
 * @param <C> the type of Card that can be placed in this cell
 */
public class PawnsBoardBaseCell<C extends Card> implements PawnsBoardCell<C> {
  private CellContent content;
  private PlayerColors owner;
  private int pawnCount;
  private C card;
  
  /**
   * Creates an empty cell.
   */
  public PawnsBoardBaseCell() {
    this.content = CellContent.EMPTY;
    this.owner = null;
    this.pawnCount = 0;
    this.card = null;
  }

  /**
   * Gets the content type of this cell.
   *
   * @return the cell content type
   */
  @Override
  public CellContent getContent() {
    return content;
  }

  /**
   * Gets the owner of this cell's contents.
   *
   * @return the player who owns the contents, or null if the cell is empty
   */
  @Override
  public PlayerColors getOwner() {
    return owner;
  }

  /**
   * Gets the number of pawns in this cell.
   *
   * @return the pawn count, or 0 if the cell is empty or contains a card
   */
  @Override
  public int getPawnCount() {
    return (content == CellContent.PAWNS) ? pawnCount : 0;
  }

  /**
   * Gets the card in this cell.
   *
   * @return the card, or null if the cell is empty or contains pawns
   */
  @Override
  public C getCard() {
    return (content == CellContent.CARD) ? card : null;
  }

  /**
   * Adds a pawn to this cell. If the cell is empty, it becomes a pawn cell.
   * The maximum number of pawns in a cell is 3 in this implementation.
   *
   * @param playerColors the playerColors who owns the pawn
   * @throws IllegalStateException if trying to add a pawn to a cell with a card
   * @throws IllegalStateException if the cell already has the maximum number of pawns
   * @throws IllegalOwnerException if trying to add a pawn of a different owner
   * @throws IllegalArgumentException if playerColors is null
   */
  @Override
  public void addPawn(PlayerColors playerColors) throws IllegalOwnerException {
    if (playerColors == null) {
      throw new IllegalArgumentException("Player colors cannot be null");
    }

    if (content == CellContent.CARD) {
      throw new IllegalStateException("Cannot add pawn to a cell containing a card");
    }

    if (content == CellContent.EMPTY) {
      content = CellContent.PAWNS;
      owner = playerColors;
      pawnCount = 1;
    } else {
      // Cell already has pawns
      if (pawnCount >= 3) {
        throw new IllegalStateException("Cell already has maximum number of pawns");
      }

      if (owner != playerColors) {
        throw new IllegalOwnerException("Cannot add pawn of different owner");
      }

      pawnCount++;
    }
  }

  /**
   * Changes the ownership of pawns in this cell.
   * The pawn count remains the same, but the owner changes.
   *
   * @param newOwner the new owner of the pawns
   * @throws IllegalStateException if trying to change ownership of non-pawn content
   */
  @Override
  public void changeOwnership(PlayerColors newOwner) {
    if (content != CellContent.PAWNS) {
      throw new IllegalStateException("Can only change ownership of pawns");
    }

    owner = newOwner;
  }

  /**
   * Places a card in this cell, replacing any pawns.
   * The cell's content becomes a card, and pawns are removed.
   *
   * @param card the card to place
   * @param playerColors the playerColors who owns the card
   * @throws NullPointerException if card is null
   */
  @Override
  public void setCard(C card, PlayerColors playerColors) {
    if (card == null) {
      throw new NullPointerException("Card cannot be null");
    }

    this.content = CellContent.CARD;
    this.owner = playerColors;
    this.card = card;
    this.pawnCount = 0;
  }
}