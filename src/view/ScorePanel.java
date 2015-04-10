/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Board;

/**
 * Represents a panel of the game that shows information about the user's score, number of 
 * lines the user has cleared, and the level that the user is on. 
 * @author Brandon Soto
 * @version Mar 4, 2014
 */
@SuppressWarnings("serial")
public class ScorePanel extends JPanel implements Observer, PropertyChangeListener {
    
    /** Default layout of this panel. */
    private static final GridLayout LAYOUT = new GridLayout(4, 1);
    
    /** Default number of lines to clear until the game advances to the next level. */
    private static final int LINES_FOR_NEXT_LEVEL = 1;

    /** The base score a user receives when a block freezes. */
    private static final int BASE_SCORE = 200;
    
    /** Label that displays the user's current score. */
    private final JLabel myScoreLabel;
    
    /** Label that displays the user's current level. */
    private final JLabel myLevelLabel;
    
    /** Label that displays the number of lines the user has cleared. */
    private final JLabel myLinesClearedLabel; 
    
    /** Panel that will show how many lines the user needs to clear until the next level. */
    private final JLabel myLinesLeftPanel; 
    
    /** The user's current score. */
    private Long myScore; 
    
    /** The level the user is currently on. */
    private Integer myLevel;
    
    /** The number of lines the user has cleared. */
    private Integer myLinesCleared; 
    
    /** The number of lines to clear until the game advances to the next level. */
    private Integer myLinesUntilNextLevel;
    
    /** 
     * Constructs a ScorePanel that shows information about the user's score, level, and 
     * level. 
     */
    public ScorePanel() {
        super(); 
        
        myScore = 0L;
        myLevel = 1; 
        myLinesCleared = 0;
        myLinesUntilNextLevel = LINES_FOR_NEXT_LEVEL; 
        
        myScoreLabel = new JLabel(myScore.toString());
        myLevelLabel = new JLabel(myLevel.toString()); 
        myLinesClearedLabel = new JLabel(myLinesCleared.toString());
        myLinesLeftPanel = new JLabel(myLinesUntilNextLevel.toString());
        
        setUpPanel();
    }
    
    /**
     * Sets up this panel by adding three panels to it. Specifically, it adds a score panel,
     * level panel, and lines cleared panel. 
     */
    private void setUpPanel() {
        setLayout(LAYOUT);
        
        final JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBorder(BorderFactory.createTitledBorder(
                                              BorderFactory.createEtchedBorder(), "Score"));
        scorePanel.add(myScoreLabel);
        
        final JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new FlowLayout());
        levelPanel.setBorder(BorderFactory.createTitledBorder(
                                              BorderFactory.createEtchedBorder(), "Level"));
        levelPanel.add(myLevelLabel);
        
        final JPanel linesPanel = new JPanel();
        linesPanel.setLayout(new FlowLayout());
        linesPanel.setBorder(BorderFactory.createTitledBorder(
                                              BorderFactory.createEtchedBorder(), "Lines"));
        linesPanel.add(myLinesClearedLabel);
        
        final JPanel linesLeftPanel = new JPanel();
        linesLeftPanel.setLayout(new FlowLayout());
        linesLeftPanel.setBorder(BorderFactory.createTitledBorder(
                                      BorderFactory.createEtchedBorder(), "Next Level In"));
        linesLeftPanel.add(myLinesLeftPanel);
        
        add(scorePanel);
        add(levelPanel);
        add(linesPanel);
        add(linesLeftPanel);
    }
    
    @Override 
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        myScoreLabel.setText(myScore.toString());
        myLevelLabel.setText(myLevel.toString());
        myLinesClearedLabel.setText(myLinesCleared.toString());
        myLinesLeftPanel.setText(myLinesUntilNextLevel.toString());
    }

    @Override
    public void update(final Observable theObservable, final Object theData) {
        if (theObservable instanceof Board) {
            final Board board = (Board) theObservable;
            final int linesRemoved = board.getLastLinesRemoved();
            
            myLinesUntilNextLevel -= linesRemoved; 
            myLinesCleared += linesRemoved; 
            myScore += BASE_SCORE * linesRemoved * myLevel;
            
            if (myLinesUntilNextLevel <= 0) {
                myLevel++;
                myLinesUntilNextLevel = LINES_FOR_NEXT_LEVEL;
                firePropertyChange("level up", null, null);
            }
            
            repaint();
            
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propName = theEvent.getPropertyName();
        
        if ("new game".equals(propName)) {
            myScore = 0L; 
            myLevel = 1; 
            myLinesCleared = 0; 
            myLinesUntilNextLevel = LINES_FOR_NEXT_LEVEL;
            
            repaint();
        }
    }
    
    
}
