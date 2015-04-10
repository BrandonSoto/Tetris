/*
 * TCSS 305 Winter 2014 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;
import model.ImmutablePiece;
import model.Point;

/**
 * A panel that displays the next piece that will be played. 
 * @author Brandon Soto
 * @version Mar 5, 2014
 */
@SuppressWarnings("serial")
public class NextPiecePanel extends JPanel implements Observer {
    
    /** Default width and height of a single block. */
    private static final int BLOCK_SIZE = 20;
    
    /** Default size of this panel. */
    private static final Dimension DEFAULT_SIZE = new Dimension(120, 120);
    
    /** The piece that this panel will draw. */
    private ImmutablePiece myPiece;
    
    /**
     * Constructs a NextPiecePanel with the given game piece. 
     * @param thePiece game piece that will be drawn on this panel. 
     */
    public NextPiecePanel(final ImmutablePiece thePiece) {
        super();
        
        myPiece = thePiece;
        
        setUpPanel();
    }
    
    /**
     * Sets this panel's currently displayed piece to the passed piece.
     * @param thePiece desired piece to be displayed. 
     */
    public void setPiece(final ImmutablePiece thePiece) {
        myPiece = thePiece;
    }
    
    /** Gives this panel a default size, background color, and border. */
    private void setUpPanel() {
        setPreferredSize(DEFAULT_SIZE);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Next Piece"));
    }
    
    @Override 
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final Rectangle2D.Double rec = new Rectangle2D.Double(0, 0, BLOCK_SIZE, BLOCK_SIZE);
        final Color color = ((AbstractPiece) myPiece).getColor();
        
        double x = 0.0;
        double y = 0.0;
        
        g2d.setPaint(color);
        
        // draw a block for each of the piece's points. (each piece will have four blocks)
        for (final Point point : ((AbstractPiece) myPiece).getPoints()) {
            g2d.setPaint(color);
            x = point.getX() * BLOCK_SIZE + BLOCK_SIZE;
            y = (DEFAULT_SIZE.getHeight() - BLOCK_SIZE * 2) - (point.getY() * BLOCK_SIZE);
            
            // draws a filled block
            rec.setFrame(x, y, BLOCK_SIZE, BLOCK_SIZE);
            g2d.fill(rec);
            
            // draws the outline of the block
            g2d.setPaint(Color.BLACK);
            g2d.draw(rec);
        }
        
        
    }

    @Override
    public void update(final Observable theObservable, final Object theData) {
        // the next piece panel should not draw any new pieces when the game is over
        if (theObservable instanceof Board) {
            myPiece = ((Board) theObservable).getNextPiece();
            repaint();
        }
    }

}
