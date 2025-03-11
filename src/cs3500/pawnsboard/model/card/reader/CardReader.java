package cs3500.pawnsboard.model.card.reader;

import cs3500.pawnsboard.model.card.Card;
import java.util.List;

/**
 * Interface for reading cards from a configuration file.
 * Abstracts the process of reading and parsing card data.
 */
public interface CardReader {
  
  /**
   * Reads cards from a file and returns them as a list.
   *
   * @param filePath path to the card configuration file
   * @return a list of cards read from the file
   * @throws IllegalArgumentException if the file cannot be read or has invalid format
   */
  List<Card> readCards(String filePath) throws IllegalArgumentException;
}
