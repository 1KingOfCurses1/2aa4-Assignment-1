package catandomainmodel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * R3.2 Rule-based machine intelligence.
 * Evaluates legal actions based on immediate value and chooses the best.
 */
public class RuleBasedDecisionStrategy implements DecisionStrategy {
    private Random random = new SecureRandom();

    private static final Map<ResourceType, Integer> SETTLEMENT_COST = new EnumMap<>(ResourceType.class);
    private static final Map<ResourceType, Integer> CITY_COST = new EnumMap<>(ResourceType.class);
    private static final Map<ResourceType, Integer> ROAD_COST = new EnumMap<>(ResourceType.class);

    static {
        SETTLEMENT_COST.put(ResourceType.BRICK, 1);
        SETTLEMENT_COST.put(ResourceType.LUMBER, 1);
        SETTLEMENT_COST.put(ResourceType.WOOL, 1);
        SETTLEMENT_COST.put(ResourceType.GRAIN, 1);

        CITY_COST.put(ResourceType.ORE, 3);
        CITY_COST.put(ResourceType.GRAIN, 2);

        ROAD_COST.put(ResourceType.BRICK, 1);
        ROAD_COST.put(ResourceType.LUMBER, 1);
    }

    @Override
    public Action chooseAction(Player player, Board board, ResourceBank resourceBank, int round) {
        List<ScoredAction> candidates = new ArrayList<>();
        ResourceHand hand = player.getResourceHand();

        // 1. Evaluate legal settlements (VP Gain = 1.0)
        if (hand.canAfford(SETTLEMENT_COST)) {
            for (Node n : board.getNodes()) {
                if (board.isValidSettlementPlacement(n, player)) {
                    Action action = new Action(round, player.getId(), "BUILD_SETTLEMENT " + n.getId(), ActionType.BUILD_SETTLEMENT);
                    candidates.add(new ScoredAction(action, 1.0));
                }
            }
        }

        // 2. Evaluate legal cities (VP Gain = 1.0)
        if (hand.canAfford(CITY_COST)) {
            for (Node n : board.getNodes()) {
                if (board.isValidCityPlacement(n, player)) {
                    Action action = new Action(round, player.getId(), "BUILD_CITY " + n.getId(), ActionType.BUILD_CITY);
                    candidates.add(new ScoredAction(action, 1.0));
                }
            }
        }

        // 3. Evaluate legal roads (Build without VP = 0.8)
        if (hand.canAfford(ROAD_COST)) {
            for (Edge e : board.getEdges()) {
                if (board.isValidRoadPlacement(e, player)) {
                    if (e.getNodes().size() == 2) {
                        int n1 = e.getNodes().get(0).getId();
                        int n2 = e.getNodes().get(1).getId();
                        Action action = new Action(round, player.getId(), "BUILD_ROAD " + n1 + " " + n2, ActionType.BUILD_ROAD);
                        candidates.add(new ScoredAction(action, 0.8));
                    }
                }
            }
        }

        // Apply "Economy" rule: If an action leaves fewer than 5 cards, its score is adjusted to 0.5
        // This overrides the initial score to 0.5 if triggered.
        for (ScoredAction sa : candidates) {
            if (leavesFewerThanFiveCards(player, sa.action)) {
                sa.score = 0.5;
            }
        }

        if (candidates.isEmpty()) {
            return new Action(round, player.getId(), "PASS", ActionType.PASS);
        }

        // Find highest score
        double maxScore = -1.0;
        for (ScoredAction sa : candidates) {
            if (sa.score > maxScore) {
                maxScore = sa.score;
            }
        }

        // Collect all actions with the highest score for tie-breaking
        List<Action> bestActions = new ArrayList<>();
        for (ScoredAction sa : candidates) {
            if (Math.abs(sa.score - maxScore) < 0.0001) {
                bestActions.add(sa.action);
            }
        }

        // Random tie-break
        Collections.shuffle(bestActions, random);
        return bestActions.get(0);
    }

    private boolean leavesFewerThanFiveCards(Player player, Action action) {
        int cost = 0;
        switch (action.getActionType()) {
            case BUILD_SETTLEMENT: cost = 4; break;
            case BUILD_CITY:       cost = 5; break;
            case BUILD_ROAD:       cost = 2; break;
            default:               break;
        }
        int remaining = player.getResourceHand().getTotalCards() - cost;
        return remaining < 5;
    }

    private static class ScoredAction {
        Action action;
        double score;

        ScoredAction(Action action, double score) {
            this.action = action;
            this.score = score;
        }
    }
}
