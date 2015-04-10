/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

/**
 * Represents a panel that contains "New Game", "Quit", "Pause", and "Controls" buttons. 
 * @author Brandon Soto
 * @version Mar 4, 2014
 */
@SuppressWarnings("serial")
public class ButtonToolbar extends JToolBar implements PropertyChangeListener {
    
    /** Shows all of the game's controls. */
    private static final String CONTROLS = "CONTROLS: "
                    + "\n------------------- "
                    + "\nLeft : Left Arrow"
                    + "\nRight : Right Arrow "
                    + "\nDrop : Spacebar"
                    + "\nDown : Down Arrow"
                    + "\nRotate Clockwise : Up Arrow"
                    + "\nRotate Counterclockwise : d"
                    + "\nPause Game : p";
    
    /** Name of the pause game property change. */
    private static final String PAUSE_GAME = "pause game";
    
    /** Name of the end game property change. */
    private static final String END_GAME = "end game";
    
    /** Name of the new game property change. */
    private static final String NEW_GAME = "new game";
    
    /** Represents a "Pause" button. */
    private final JButton myPause;
    
    /** Represents a "New Game" button. This will start a new game. */
    private final JButton myNewGameButton;
    
    /** Represents an "End Game" button. This will end the current game. */
    private final JButton myEndGameButton; 
    
    /** True if the game is currently paused. Otherwise false. */
    private boolean myGameIsPaused;
    
    /** Constructs a ButtonToolbar with "New Game", "Quit", and "Pause" buttons. */
    public ButtonToolbar() {
        super();
        
        myPause = getPauseButton();
        myNewGameButton = getNewGameButton();
        myEndGameButton = getEndGameButton();
        myGameIsPaused = false; 
        
        setUpPanel();
    }
    
    /** Creates the panel with "New Game", "Quit", and "Pause" buttons. */
    private void setUpPanel() {
        // allows the new game and end game buttons to enable and disable at proper times 
        addPropertyChangeListener(this);
        
        addSeparator();
        add(myNewGameButton);
        add(myEndGameButton); 
        addSeparator();
        add(myPause);
        addSeparator();
        add(getControlsButton());
        addSeparator();
        add(getGridCheckbox());
        
        setFloatable(false); 
        addKeyListener(new Controls());
    }
    
    /**
     * Returns a check box that can enable and disable the grid. 
     * @return check box to turn on and turn off the grid. 
     */
    private JCheckBox getGridCheckbox() {
        final JCheckBox grid = new JCheckBox(new ButtonAction("Grid", KeyEvent.VK_G));
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                ButtonToolbar.this.firePropertyChange("enable grid", null, grid.isSelected());
            }
        });
        
        return grid;
    }
    
    /**
     * Returns an "New Game" button. This button will start a new game. However, the new game
     * button is only enabled once the user has ended or finished a game. 
     * @return "New Game" button that starts a new game. 
     */
    private JButton getNewGameButton() {
        final JButton newGame = new JButton(new ButtonAction("New Game", KeyEvent.VK_N));
        newGame.setEnabled(false);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                ButtonToolbar.this.firePropertyChange(NEW_GAME, null, null);
            }
        });
        
        return newGame; 
    }
    
    /**
     * Returns an "End Game" button. This button will end the current game a user is playing. 
     * @return button that ends the currently played game. 
     */
    private JButton getEndGameButton() {
        final JButton end = new JButton(new ButtonAction("End Game", KeyEvent.VK_E));
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                ButtonToolbar.this.firePropertyChange(END_GAME, null, null);
            }
        });
        
        return end;
    }
    
    /**
     * Returns a "Pause" button. This button can stop the GUI's timer so that pieces do not
     * drop and it can also start the GUI's timer so that pieces do drop. 
     * @return button that can pause the game. 
     */
    private JButton getPauseButton() {
        final JButton pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myGameIsPaused ^= true; // inverts pause status
                
                ButtonToolbar.this.firePropertyChange(PAUSE_GAME, null, myGameIsPaused);
            }
        });
        
        return pause;
    }
    
    /**
     * Returns a button that displays a dialog that shows the game's controls. 
     * @return button that shows the game's controls. 
     */
    private JButton getControlsButton() {
        final JButton controls = new JButton(new ButtonAction("Controls", KeyEvent.VK_C));
        controls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final boolean pauseEnabled = myPause.isEnabled();
                
                // Ensures that the pause screen doesn't pop up if the pause button is disabled
                if (pauseEnabled) {
                    ButtonToolbar.this.firePropertyChange(PAUSE_GAME, null, true);
                }           
                
                JOptionPane.showMessageDialog(null, CONTROLS, "Game Controls", 
                                                              JOptionPane.INFORMATION_MESSAGE);
                // Ensures that the "Game over" dialog doesn't appear if pause is disabled
                if (pauseEnabled) {
                    ButtonToolbar.this.firePropertyChange(PAUSE_GAME, null, false);

                }
            }
        });
        
        return controls; 
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propName = theEvent.getPropertyName();
        
        if ("enable pause".equals(propName)) {
            myPause.setEnabled((boolean) theEvent.getNewValue());
        } else if (END_GAME.equals(propName)) {
            myNewGameButton.setEnabled(true);
            myEndGameButton.setEnabled(false);
        } else if (NEW_GAME.equals(theEvent.getPropertyName())) {
            myNewGameButton.setEnabled(false);
            myEndGameButton.setEnabled(true);
        } else if (PAUSE_GAME.equals(propName)) {
            myGameIsPaused = (boolean) theEvent.getNewValue();
        }
    }
    
    /**
     * Convenience class that quickly creates an action with a given name, mnemonic, and an
     * optional status. 
     * @author Brandon Soto
     * @version Mar 5, 2014
     */
    private class ButtonAction extends AbstractAction {
        
        /**
         * Constructs a ButtonAction with given name and mnemonic. 
         * @param aName desired name of action.
         * @param aMnem desired mnemonic of action. 
         */
        public ButtonAction(final String aName, final int aMnem) {
            super();
            
            putValue(Action.NAME, aName);
            putValue(Action.MNEMONIC_KEY, aMnem);
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            // will be overwritten by the individual components that use this action
        }
    }
    
    /**
     * Represents a KeyAdapter that listens for the 'p' key. If 'p' is pressed, the game 
     * pauses. 
     * @author Brandon Soto
     * @version Mar 4, 2014
     */
    private class Controls extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final int code = theEvent.getKeyCode();
            
            // the pause button should be enabled when the pause event is fired
            if (myPause.isEnabled() && code == KeyEvent.VK_P) {
                ButtonToolbar.this.firePropertyChange(PAUSE_GAME, null, !myGameIsPaused);
            }
        }
    }
}
