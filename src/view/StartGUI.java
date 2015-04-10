/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.EventQueue;

/**
 * Calls a TetrisGUI to run the program. 
 * @author Brandon Soto
 * @version Mar 5, 2014
 */
public final class StartGUI {
    
    /** Prevents making instances of this class. */
    private StartGUI() {
        // do nothing
    };
    
    /**
     * Calls a TetrisGUI to run the Tetris program. 
     * @param theArgs arguments from command line. 
     */
    public static void main(final String ...theArgs) {
        EventQueue.invokeLater(() -> new TetrisGUI());
    }
}
