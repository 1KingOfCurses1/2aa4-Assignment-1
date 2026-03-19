package catandomainmodel;

/**
 * Strategy interface for machine decision-making.
 * Defines how an agent chooses an action based on the game state.
 */
public interface DecisionStrategy {
    /**
     * Chooses the best action for the given player.
     * 
     * @param player       the player making the decision
     * @param board        the current game board
     * @param resourceBank the shared resource bank
     * @param round        the current round number
     * @return the chosen Action, or null if no action is possible/chosen
     */
    Action chooseAction(Player player, Board board, ResourceBank resourceBank, int round);
}
