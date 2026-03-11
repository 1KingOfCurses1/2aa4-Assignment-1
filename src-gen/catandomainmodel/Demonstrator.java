// --------------------------------------------------------
// Main: minimal entry point for the Catan simulator demonstrator.
//
// Design rationale:
// - Main contains NO simulation or setup logic (SRP).
// - It delegates to Simulator, which owns object creation.
// - Simulator delegates to Game, which owns round execution.
// - This two-level delegation keeps Main trivially simple and
//   means new features never require changes to this file (OCP).
// --------------------------------------------------------

package catandomainmodel;

import java.util.logging.Logger;

/**
 * Demonstrator entry point for the Catan simulator.
 */
public class Demonstrator {

    private static final Logger LOGGER = Logger.getLogger(Demonstrator.class.getName());

    public static void main(String[] args) {
        LOGGER.info("========================================");
        LOGGER.info("   Settlers of Catan - Simulator Demo   ");
        LOGGER.info("========================================");

        // Setup + execution fully encapsulated in Simulator
        Simulator simulator = new Simulator();
        simulator.run();

        LOGGER.info("========================================");
        LOGGER.info("          Simulation Complete            ");
        LOGGER.info("========================================");
    }
}
