package cs3500.pawnsboard.model;

/**
 * Represents a player and certain behaviors involving ties and ownership in the Pawns Board game.
 * There are two players: RED and BLUE.
 * TIE is used to represent a tied game when no player has won.
 * NONE is used to represent a cell with no Ownership.
 */
public enum Player {
  RED, BLUE, TIE, NONE
}
