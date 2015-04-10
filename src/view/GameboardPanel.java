/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;
import model.ImmutablePiece;
import model.Point;

/**
 * Represents the panel that the tetris pieces will be drawn onto. 
 * @author Brandon Soto
 * @version Mar 4, 2014
 */
@SuppressWarnings("serial")
public class GameboardPanel extends JPanel implements Observer, PropertyChangeListener {
    
    /** Width and height of the a rectangle representing a single block of a piece.  */
    private static final double BLOCK_SIZE = 40.0;
    
    /** The number of blocks in each tetris piece. */
    private static final int BLOCKS_IN_A_PIECE = 4;
    
    /** Default dimensions of a GameboardPanel. */
    private static final Dimension BOARD_DIMENSIONS = new Dimension(400, 800);
    
    /** Default size of the pause screen's font. */
    private static final int PAUSE_FONT_SIZE = 20;
    
    /** Name of the end game property change. */
    private static final String END_GAME = "end game";
    
    /** Name of the pause game property change. */
    private static final String PAUSE_GAME = "pause game";
        
    /** A list of rows of colors which represents the frozen blocks. */
    private List<Color[]> myFrozenBlocks;
    
    /** The current piece in play on the Board. */
    private ImmutablePiece myCurrentPiece;
    
    /** True if the game is paused. Otherwise false. */
    private boolean myGameisPaused;
    
    /** True if the grid should be displayed. Otherwise false. */
    private boolean myGridIsVisible;
    
    /** True if the game is over. Otherwise false. */
    private boolean myGameIsOver; 
   
    /**
     * Constructs a GamboardPanel with the given board. The Board reference allows this 
     * panel to draw the Board's pieces. 
     * @param aCurrentPiece board's current piece.
     * @param theFrozenBlocks board's frozen pieces. 
     */
    public GameboardPanel(final ImmutablePiece aCurrentPiece, 
                                                      final List<Color[]> theFrozenBlocks) {
        super();
        
        myGameisPaused = false; 
        myGridIsVisible = false; 
        myGameIsOver = false; 
        myFrozenBlocks = theFrozenBlocks;
        myCurrentPiece = aCurrentPiece;
        
        setBackground(Color.WHITE);
        setPreferredSize(BOARD_DIMENSIONS); 
        addPropertyChangeListener(this);
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        final Rectangle2D.Double rec = new Rectangle2D.Double(0, 0, BLOCK_SIZE, 
                                                                          BLOCK_SIZE);
        if (myGameisPaused) {
            drawPauseScreen(g2d, rec);
        } else {
            
            if (myGridIsVisible) {
                drawGrid(g2d);
            }
            
            drawFrozenPieces(g2d, rec);
            drawCurrentPiece(g2d, rec);
        }
      
    }
    
    /** 
     * Sets this panel's frozen blocks to the passed blocks. 
     * @param theBlocks desired frozen blocks. 
     */
    public void setFrozenBlocks(final List<Color[]> theBlocks) {
        myFrozenBlocks = theBlocks;
    }
    
    /**
     * Sets this panel's current piece to the passed piece.
     * @param thePiece desired current piece. 
     */
    public void setCurrentPiece(final ImmutablePiece thePiece) {
        myCurrentPiece = thePiece;
    }
    
    /**
     * Draws the grid to the board panel. 
     * @param theGraphics graphic context of this panel. 
     */
    private void drawGrid(final Graphics2D theGraphics) {
        int x = 0;
        final Line2D.Double gridLine = new Line2D.Double();
        
        theGraphics.setPaint(Color.GRAY.brighter());
        
        // creates vertical lines
        while (x <= BOARD_DIMENSIONS.width) {
            theGraphics.draw(gridLine);
            gridLine.setLine(new Point2D.Double(x, 0), new Point2D.Double(x, 
                                                              (int) BOARD_DIMENSIONS.height));
            x += BLOCK_SIZE;
        }
        
        x = 0;
        
        // creates horizontal lines
        while (x <= BOARD_DIMENSIONS.height) {
            theGraphics.draw(gridLine);
            gridLine.setLine(new Point2D.Double(0, x), 
                                         new Point2D.Double((int) BOARD_DIMENSIONS.width, x));
            x += BLOCK_SIZE;
        }
    }
    
    /**
     * Draws the board's currently frozen pieces to this panel.
     * @param theGraphics graphic context of this panel.
     * @param theRec rectangle representing a block of a piece.
     */
    private void drawFrozenPieces(final Graphics2D theGraphics, 
                                                          final Rectangle2D.Double theRec) {
        
        final int height = (int) (BOARD_DIMENSIONS.getHeight() / BLOCK_SIZE - 1);
        for (int yCoord = height; yCoord >= 0; yCoord--) {
            for (final Color color : myFrozenBlocks.get(yCoord)) {
                // if the color is not null draw a 25 x 25 colored rectangle 
                if (color != null) {    
                    
                    if (myGameIsOver) {
                        theGraphics.setPaint(Color.GRAY.brighter());
                    } else {
                        theGraphics.setPaint(color.darker());
                    }
                    
                    theGraphics.fillRect((int) theRec.getX(), (int) theRec.getY(), 
                                         (int) theRec.getWidth(), (int) theRec.getHeight()); 

                    if (myGameIsOver) {
                        theGraphics.setPaint(Color.GRAY.darker());
                    } else {
                        theGraphics.setPaint(color.brighter().brighter());
                    }
                    
                    theGraphics.drawRect((int) theRec.getX(), (int) theRec.getY(), 
                                         (int) theRec.getWidth(), (int) theRec.getHeight()); 
               
                }
                
                // going horizontally - only need to change theRectangle's x coordinate
                theRec.x = theRec.getX() + BLOCK_SIZE;
            }
            
            // about to change vertically - need to reset x and change y 
            theRec.x = 0;
            theRec.y = theRec.getY() + BLOCK_SIZE;
        }
    }
    
    /**
     * Draws the board's current piece to this panel.
     * @param theGraphics graphic context of this panel.
     * @param theRec rectangle representing a block of the current piece.
     */
    private void drawCurrentPiece(final Graphics2D theGraphics, 
                                                          final Rectangle2D.Double theRec) {
        
        final Color color = ((AbstractPiece) myCurrentPiece).getColor();
        Point point; 
        for (int blockNum = 0; blockNum < BLOCKS_IN_A_PIECE; blockNum++) {
            point = ((AbstractPiece) myCurrentPiece).getAbsolutePosition(blockNum);
            
            final double x = point.getX() * BLOCK_SIZE;
            final double y = (BOARD_DIMENSIONS.getHeight() - BLOCK_SIZE) 
                                                                - point.getY() * BLOCK_SIZE;
            
            theRec.setFrame(x, y, BLOCK_SIZE, BLOCK_SIZE); 
            
            if (myGameIsOver) {
                theGraphics.setPaint(Color.GRAY.brighter());
            } else {
                theGraphics.setPaint(
                                 color.brighter().brighter().brighter().brighter().brighter());
            }
            
            theGraphics.fillRect((int) theRec.getX(), (int) theRec.getY(), 
                                 (int) theRec.getWidth(), (int) theRec.getHeight()); 
            

            if (myGameIsOver) {
                theGraphics.setPaint(Color.GRAY.darker());
            } else {
                theGraphics.setPaint(color.darker().darker());
            }
            
            theGraphics.drawRect((int) theRec.getX(), (int) theRec.getY(), 
                                 (int) theRec.getWidth(), (int) theRec.getHeight()); 
            
        }
    }
    
    /**
     * This method will fill the panel with a gray "Pause" screen. 
     * @param theGraphics graphic context of this panel.
     * @param theRec rectangle to be drawn on the panel. 
     */
    private void drawPauseScreen(final Graphics2D theGraphics,
                                                         final Rectangle2D.Double theRec) {
        // draws gray pause screen to panel
        theRec.setFrame(0, 0, BOARD_DIMENSIONS.getWidth(), BOARD_DIMENSIONS.getHeight());
        theGraphics.setPaint(Color.GRAY.brighter());
        theGraphics.fill(theRec);
        
        final StringBuilder paused = new StringBuilder("PAUSED");
        final int strLength =  paused.length();
        final int x = (int) (BOARD_DIMENSIONS.getWidth() / 2) - strLength * strLength;
        final int y = (int) BOARD_DIMENSIONS.getHeight() / 2;
        
        // draws "PAUSED" to panel. 
        theGraphics.setPaint(Color.WHITE);
        theGraphics.setFont(new Font("SansSerif", Font.BOLD, PAUSE_FONT_SIZE));
        theGraphics.drawString(paused.toString(), x, y);
    }

    @Override
    public void update(final Observable theObject, final Object theData) {
        if (theObject instanceof Board) {
            myFrozenBlocks = ((Board) theObject).getFrozenBlocks();
            myCurrentPiece = ((Board) theObject).getCurrentPiece();
            repaint();
        }
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propName = theEvent.getPropertyName();
        
        if (PAUSE_GAME.equals(propName)) {
            myGameisPaused = (boolean) theEvent.getNewValue();
        } else if ("new game".equals(propName)) {
            myGameIsOver = false; 
            setBackground(Color.WHITE);
        } else if (END_GAME.equals(propName)) {
            myGameisPaused = false; 
            myGameIsOver = true; 
            setBackground(Color.GRAY.darker());
        } else if ("enable grid".equals(propName)) {
            myGridIsVisible = (boolean) theEvent.getNewValue();
        }
        
        repaint();

    }
}