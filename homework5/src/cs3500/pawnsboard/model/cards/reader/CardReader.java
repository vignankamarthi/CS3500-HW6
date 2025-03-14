package cs3500.pawnsboard.model.cards.reader;

import cs3500.pawnsboard.model.cards.Card;
import java.util.List;

/**
 * Interface for reading {@link Card}s from a configuration file.
 * Abstracts the process of reading and parsing card data.
 * 
 * @param <C> the specific type of {@link Card} this reader processes
 */
public interface CardReader<C extends Card> {
  
  /**
   * Reads cards from a file and returns them as a list.
   *
   * @param filePath path to the card configuration file
   * @return a list of cards of type C read from the file
   * @throws IllegalArgumentException if the file cannot be read or has invalid format
   */
  List<C> readCards(String filePath) throws IllegalArgumentException;
}
