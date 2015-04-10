/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

import model.Board;
import sound.SoundEffect;

/**
 * Represents the main frame of the Tetris GUI. 
 * @author Brandon Soto
 * @version Mar 4, 2014
 */
@SuppressWarnings("serial")
public class TetrisGUI extends JFrame implements PropertyChangeListener, FocusListener {
    
    /** Initial delay for the GUI's timer. */
    private static final int DEFAULT_DELAY = 800;
    
    /** Default width for the board. (note: the board's height is twice the board's width.) */
    private static final int DEFAULT_BOARD_WIDTH = 10;
    
    /** Default strut height. */
    private static final int STRUT_HEIGHT = 80;
    
    /** 
     * Number to be subtracted from the timer's current delay when the user advances to the 
     * next level. 
     */
    private static final int LEVEL_DECREMENT = 100;
    
    /** Name of pause game property change. */
    private static final String PAUSE_GAME = "pause game";
    
    /** Name of enable pause property change. This enables and disables the pause button. */
    private static final String ENABLE_PAUSE = "enable pause";
    
    /** Name of the end game property change. */
    private static final String END_GAME = "end game";
    
    /** Name of the new game property change. */
    private static final String NEW_GAME = "new game";
    
    /** 
     * Message to be displayed before the game starts that tells the user of this game's 
     * scoring algorithm.
     */
    private static final String SCORING_INFO = "Earn points by clearing lines!\n"
                    + "-----------------------------------------\n"
                    + "1 Line    :  200  *  Level Number\n"
                    + "2 Lines  :  400  *  Level Number\n"
                    + "3 Lines  :  600  *  Level Number\n"
                    + "4 Lines  :  800  *  Level Number";
    
    /** Timer that refreshes the GUI. */
    private final Timer myTimer;
    
    /** Board that the GUI is based on. This Board contains information about the frozen, 
     * current, and next pieces. 
     */
    private Board myBoard;
    
    /** True if the game should be paused. Otherwise false. */
    private Boolean myGameIsPaused;
    
    /** A panel that the tetris pieces are drawn onto. */
    private final JPanel myGamePanel;
    
    /** A panel that shows the next Tetris piece. */
    private final JPanel myNextPiecePanel;
    
    /** A panel that displays info about the user's score, level, and lines cleared. */
    private final JPanel myScorePanel;
    
    /** A Toolbar that contains all of the GUI's buttons. */
    private final JToolBar mySouthToolbar;
    
    /** True if the current game is over. Otherwise false. */
    private boolean myGameIsOver; 
    
    /**
     * Constructs a TetrisGUI by initializing all fields and setting up the GUI's panels. 
     */
    public TetrisGUI() {
        super();
        
        myGameIsPaused = false; 
        myGameIsOver = false; 
        
        myTimer = new Timer(DEFAULT_DELAY, new TimerAction());
        
        myBoard = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_WIDTH * 2);
        
        myNextPiecePanel = new NextPiecePanel(myBoard.getNextPiece());
        myGamePanel = new GameboardPanel(myBoard.getCurrentPiece(), myBoard.getFrozenBlocks());
        myScorePanel = new ScorePanel();
        mySouthToolbar = new ButtonToolbar();
        
        setUpGUI();
    }
    
    /**
     * Sets up the GUI. It adds property listeners to certain components and adds the game 
     * board, controls, next piece, score, and button panels. 
     */
    private void setUpGUI() {
        setUpObservers();
        
        setUpPropertyChangeListeners();
        
        add(getEastPanel(), BorderLayout.EAST);
        add(myGamePanel, BorderLayout.WEST);
        add(mySouthToolbar, BorderLayout.SOUTH);

        setTitle("Tetris");
        addFocusListener(this);
        addKeyListener(new Controls());
        addKeyListener(mySouthToolbar.getKeyListeners()[0]);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        
        // show scoring dialog before GUI is visible
        //JOptionPane.showMessageDialog(null, SCORING_INFO, "Scoring", JOptionPane.INFORMATION_MESSAGE);
        setVisible(true);
        setFocusable(true);
        
        SoundEffect.THEME.play(); 
                myTimer.start();
    }
    
    /** 
     * Adds the necessary observers to the board. 
     */
    private void setUpObservers() {
        myBoard.addObserver((Observer) myGamePanel);
        myBoard.addObserver((Observer) myNextPiecePanel);
        myBoard.addObserver((Observer) myScorePanel);
    }
    
    /**
     * Adds all of the necessary property change listeners to all of the components. 
     */
    private void setUpPropertyChangeListeners() {
        // allows the timer to stop when the board is full 
        addPropertyChangeListener(this);
        
        // allows blocks to be grayed out when there's a game over
        addPropertyChangeListener((PropertyChangeListener) myGamePanel);
        
        // allows the pause button to be disabled when a game is over
        addPropertyChangeListener((PropertyChangeListener) mySouthToolbar);
        
        // allows the timer to be changed so that the levels get harder
        myScorePanel.addPropertyChangeListener(this);
        
        // allows grid to be displayed when the "Grid" checkbox is pressed
        myGamePanel.addPropertyChangeListener((PropertyChangeListener) mySouthToolbar);
        
        // allows score, level, and lines to be updated when user starts a new game
        mySouthToolbar.addPropertyChangeListener((PropertyChangeListener) myScorePanel);
        
        // allows the pause button to stop and start the timer
        mySouthToolbar.addPropertyChangeListener(this);
        
        // allows pause screen to be displayed
        mySouthToolbar.addPropertyChangeListener((PropertyChangeListener) myGamePanel);
    }
    
    /**
     * Returns the GUI's east panel. The east panel contains the next piece panel and score
     * panel. 
     * @return east panel of GUI. 
     */
    private JPanel getEastPanel() {
        final JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new FlowLayout()); 
        
        final Box eastBox = new Box(BoxLayout.Y_AXIS);
        eastBox.add(myNextPiecePanel);
        eastBox.add(Box.createVerticalStrut(STRUT_HEIGHT));
        eastBox.add(myScorePanel);
        eastBox.add(Box.createVerticalGlue());
       
        eastPanel.add(eastBox);
        
        return eastPanel;
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propName = theEvent.getPropertyName();
        
        if (PAUSE_GAME.equals(propName)) {
            myGameIsPaused = (boolean) theEvent.getNewValue();
            
            if (myGameIsPaused) {
                myTimer.stop(); 
                SoundEffect.THEME.stop();
            } else {
                myTimer.start();
                SoundEffect.THEME.play();
            } 
            
            myGamePanel.repaint(); 
            
        } else if (END_GAME.equals(propName)) {
            myGameIsOver = true; 
            firePropertyChange(ENABLE_PAUSE, null, false);
            firePropertyChange(PAUSE_GAME, null, false);
            SoundEffect.THEME.stop(); 
            myTimer.stop();
            
        } else if (NEW_GAME.equals(propName)) {
            startNewGame();
            SoundEffect.THEME.restart();
        } else if ("level up".equals(propName)) {
            levelUp();
        }
    }
    
    /**
     * Adjusts the GUI's timer to reflect the level that the user is currently on. 
     */
    private void levelUp() {
        final int newDelay = myTimer.getDelay() - LEVEL_DECREMENT;
        
        if (newDelay >= 0) {
            myTimer.setDelay(newDelay);
        } else {
            myTimer.setDelay(0); // craziness
        }
    }
    
    /**
     * Starts a new game by reinitializing the board. 
     */
    private void startNewGame() {
        myGameIsPaused = false; 
        myGameIsOver = false; 
        
        myBoard = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_WIDTH * 2);
        setUpObservers();
        
        // update myGamePanel - needs current piece and frozen blocks to be reset
        ((GameboardPanel) myGamePanel).setFrozenBlocks(myBoard.getFrozenBlocks());
        ((GameboardPanel) myGamePanel).setCurrentPiece(myBoard.getCurrentPiece());
        
        // update the next piece panel's shown piece
        ((NextPiecePanel) myNextPiecePanel).setPiece(myBoard.getNextPiece());
        
        firePropertyChange(ENABLE_PAUSE, null, true);
        
        myNextPiecePanel.repaint();
        
        myTimer.setDelay(DEFAULT_DELAY);
        myTimer.start();
    }
    
    @Override
    public void focusLost(final FocusEvent theEvent) {
        requestFocusInWindow();
    }
    
    @Override
    public void focusGained(final FocusEvent theEvent) {
        // do nothing
    }
    
    /**
     * Represents the action listener that the GUI's timer will call. This action moves 
     * the game pieces down. If the board is full, it will dispay a "Game Over" dialog. 
     * @author Brandon Soto
     * @version Mar 4, 2014
     */
    private class TimerAction extends AbstractAction {
        
        /** Message to display when the board is full. */
        private static final String GAME_OVER = "Game Over!";
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (!myGameIsPaused) {
                if (myBoard.isFull()) {
                    JOptionPane.showMessageDialog(TetrisGUI.this, GAME_OVER, GAME_OVER, 
                                                              JOptionPane.INFORMATION_MESSAGE);
                    TetrisGUI.this.firePropertyChange(END_GAME, null, null);
                } else {
                    myBoard.moveDown();
                }
            } 
        }
        
    }
    
    /**
     * Represents a KeyAdapter that listens for certain key presses so that the user can 
     * control the game. 
     * @author Brandon Soto
     * @version Mar 4, 2014
     */
    private class Controls extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (!myGameIsPaused && !myGameIsOver) {
                movePiece(theEvent.getKeyCode());
            } 
        }
        
        /**
         * If the passed key code matches one of the controls of the game, the board moves
         * the current piece appropriately. 
         * @param aKeyCode code of the key that was pressed. 
         */
        private void movePiece(final int aKeyCode) {
            if (aKeyCode == KeyEvent.VK_RIGHT) {
                myBoard.moveRight();
            } else if (aKeyCode == KeyEvent.VK_LEFT) {
                myBoard.moveLeft();
            } else if (aKeyCode == KeyEvent.VK_UP) {
                myBoard.rotateClockwise();
            } else if (aKeyCode == KeyEvent.VK_DOWN) {
                myBoard.moveDown();
            } else if (aKeyCode == KeyEvent.VK_D) {
                myBoard.rotateCounterclockwise();
            } else if (aKeyCode == KeyEvent.VK_SPACE) {
                myBoard.drop();
            } 
        }
    }
}
