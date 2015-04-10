/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import model.piecegen.PieceGenerator;
import model.piecegen.RandomGenerator;
import model.piecegen.SequenceGenerator;
//import model.piecegen.SevenPieceBagGenerator;

/**
 * A representation of a Tetris game board.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public final class Board extends Observable {

    /** The number of rows that exist above the visible board area. */
    public static final int ROWS_ABOVE_BOARD = AbstractPiece.NUMBER_OF_BLOCKS;
    
    /** The minimum acceptable width and height for a Tetris Board.. */
    public static final int MIN_SIZE = 5;
    
    /** The character used in toString() to represent a side border. */
    public static final char SIDE_BORDER_CHAR = '|';

    /** The character used in toString() to represent the bottom border. */
    public static final char BOTTOM_BORDER_CHAR = '-';

    /** The character used in toString() to represent an empty block. */
    public static final char EMPTY_BLOCK_CHAR = ' ';

    /** The character used in toString() to represent a frozen block. */
    public static final char FROZEN_BLOCK_CHAR = 'X';

    /** The character used in toString() to represent a block of the current piece. */
    public static final char CURRENT_PIECE_BLOCK_CHAR = '+';

    // Instance Fields
    /** The height of the Board. */
    private final int myHeight;

    /** The width of the Board. */
    private final int myWidth;

    /** A list of rows of colors which represents the frozen blocks. */
    private final List<Color[]> myFrozenBlocks;

    /** The piece generator used by this board. */
    private final PieceGenerator myPieceGenerator;

    /** The current piece in play on the Board. */
    private ImmutablePiece myCurrentPiece;

    /** The next piece which will be used after the current piece is frozen in place. */
    private ImmutablePiece myNextPiece;

    /** The number of lines removed as a result of the last move or drop. */
    private int myLastLinesRemoved;

    /** A flag indicating whether or not the board is full. */
    private boolean myFullFlag;

    // Constructor
    /**
     * Constructs a new board with the specified dimensions and the specified
     * random seed for the piece generator.
     * 
     * @param theWidth The width - must be >= MIN_SIZE.
     * @param theHeight The height - must be >= MIN_SIZE.
     */
    public Board(final int theWidth, final int theHeight) {
        super();
        if (theWidth < MIN_SIZE || theHeight < MIN_SIZE) {
            throw new IllegalArgumentException();
        }
        myWidth = theWidth;
        myHeight = theHeight;
        
        //myPieceGenerator = new SevenPieceBagGenerator(
        //    new Point(theWidth / 2 - 1, theHeight));
        myPieceGenerator = new RandomGenerator(new Point(theWidth / 2 - 1, theHeight));
        
        myFrozenBlocks = new LinkedList<Color[]>();
        initialize();
    }

    /**
     * Constructs a new board with the specified dimensions and the specified
     * sequence of pieces to use.
     * 
     * @param theWidth The width - must be >= MIN_SIZE.
     * @param theHeight The height - must be >= MIN_SIZE.
     * @param theSequence The sequence.
     */
    public Board(final int theWidth, final int theHeight,
                 final List<ImmutablePiece> theSequence) {
        super();
        if (theWidth < MIN_SIZE || theHeight < MIN_SIZE) {
            throw new IllegalArgumentException();
        }
        myWidth = theWidth;
        myHeight = theHeight;
        myPieceGenerator = new SequenceGenerator(theSequence);
        myFrozenBlocks = new LinkedList<Color[]>();
        initialize();
    }

    // Queries
    /**
     * @return What is your width?
     */
    public int getWidth() {
        return myWidth;
    }
    
    /**
     * @return What is your height?
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Returns a clone of the row at theY.
     * 
     * @param theY The y-coordinate of the desired row - must be >= 0.
     * @return What is the row of frozen pieces at position theY?
     */
    public Color[] getRowAt(final int theY) {
        
        Color[] result = (Color[]) myFrozenBlocks.get(theY); // can throw an exception
        result = (Color[]) result.clone(); // return a defensive copy
        return result;
    }
    
    /**
     * @return an unmodifiable view of the list of frozen blocks.
     */
    public List<Color[]> getFrozenBlocks() {
        return Collections.unmodifiableList(myFrozenBlocks);
    }

    /**
     * @param theY The row position.
     * @return Is the row at position theY, not including the current piece, empty?
     */
    public boolean isRowEmpty(final int theY) {
        boolean result = true;
        for (int i = 0; i < AbstractPiece.NUMBER_OF_BLOCKS; i++) {
            result = result
                     && !(((AbstractPiece) myCurrentPiece).getAbsolutePosition(i).getY()
                                     == theY);
        }
        for (int x = 0; x < myWidth; x++) {
            result = result && getRowAt(theY)[x] == null;
        }
        return result;
    }

    /**
     * @param theY The row position.
     * @return Is the row at position theY, including the current piece, full?
     */
    public boolean isRowFull(final int theY) {
        final Color[] row = getRowAt(theY);
        for (int i = 0; i < AbstractPiece.NUMBER_OF_BLOCKS; i++) {
            final Point blockPos = ((AbstractPiece) myCurrentPiece).getAbsolutePosition(i);
            if (blockPos.getY() == theY) {
                row[blockPos.getX()] = ((AbstractPiece) myCurrentPiece).getColor();
            }
        }
        boolean result = true;
        for (int x = 0; x < myWidth; x++) {
            result = result && row[x] != null;
        }
        return result;
    }

    /**
     * @param theY The row position.
     * @return Is the row at position theY, not including the current piece, full?
     */
    private boolean isFrozenRowFull(final int theY) {
        final Color[] row = getRowAt(theY);
        boolean result = true;
        for (int x = 0; x < myWidth; x++) {
            result = result && row[x] != null;
        }
        return result;
    }

    /**
     * @param thePoint The position.
     * @return What color is the block at position (theX, theY)
     * (including blocks in the current piece)?
     */
    public Color getColorAt(final Point thePoint) {
        Color result = getRowAt(thePoint.getY())[thePoint.getX()];
        if (result == null) {
            // see if the current piece has a block there
            for (int i = 0; i < AbstractPiece.NUMBER_OF_BLOCKS && (result == null); i++) {
                final Point p = ((AbstractPiece) myCurrentPiece).getAbsolutePosition(i);
                if (p.equals(thePoint)) {
                    result = ((AbstractPiece) myCurrentPiece).getColor();
                }
            }
        }
        return result;
    }

    /**
     * @param thePiece The piece.
     * @return Does thePiece collide with the already-frozen pieces or the
     *         boundaries of the board?
     */
    public boolean collides(final ImmutablePiece thePiece) {
        boolean result = false;
        for (int i = 0; !result && i < AbstractPiece.NUMBER_OF_BLOCKS; i++) {
            final Point blockPos = ((AbstractPiece) thePiece).getAbsolutePosition(i);
            result = result || !isWithinBoard(blockPos);
            result = result || getRowAt(blockPos.getY())[blockPos.getX()] != null;
        }
        return result;
    }

    /**
     * @return How many lines were removed as a result of the last action?
     */
    public int getLastLinesRemoved() {
        return myLastLinesRemoved;
    }

    /**
     * @return What is the current piece?
     */
    public ImmutablePiece getCurrentPiece() {
        return myCurrentPiece;
    }

    /**
     * @return What is the next piece?
     */
    public ImmutablePiece getNextPiece() {
        return myNextPiece;
    }

    /**
     * @return Is the board full?
     */
    public boolean isFull() {
        return myFullFlag;
    }

    // Commands
    /**
     * Move the current piece left, if possible!
     */
    public void moveLeft() {
        myLastLinesRemoved = 0;
        final ImmutablePiece moved = myCurrentPiece.moveLeft();
        if (!collides(moved)) {
            myCurrentPiece = moved;
            setChanged();
        }
        notifyObservers();
    }

    /**
     * Move the current piece right, if possible!
     */
    public void moveRight() {
        myLastLinesRemoved = 0;
        final ImmutablePiece moved = myCurrentPiece.moveRight();
        if (!collides(moved)) {
            myCurrentPiece = moved;
            setChanged();
        }
        notifyObservers();
    }

    /**
     * Move the current piece down, if possible!
     */
    public void moveDown() {
        final ImmutablePiece moved = myCurrentPiece.moveDown();
        myLastLinesRemoved = 0;
        if (collides(moved)) {
            // freeze the current piece
            // by adding each block in the current piece to the frozen blocks
            for (int i = 0; i < AbstractPiece.NUMBER_OF_BLOCKS; i++) {
                final Point blockPos = ((AbstractPiece) myCurrentPiece).getAbsolutePosition(i);
                ((Color[]) myFrozenBlocks.get(blockPos.getY()))[blockPos.getX()] =
                                ((AbstractPiece) myCurrentPiece).getColor();
            }

            // clear all full rows
            clearFullRows();

            // check for end of game
            for (int y = getHeight(); !myFullFlag && y < myFrozenBlocks.size(); y++) {
                //myFullFlag = !isRowEmpty(y); // works but is slow
                myFullFlag = isRowEmpty(y) ^ true; // bitwise inversion is faster
            }

            // add empty rows at the top
            for (int i = 0; i < myLastLinesRemoved; i++) {
                myFrozenBlocks.add(new Color[myWidth]);
            }

            // replace the current piece with the next piece
            myCurrentPiece = myNextPiece;
            myNextPiece = myPieceGenerator.next();
        } else {
            // we actually just move the piece down if it doesn't collide
            myCurrentPiece = moved;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Rotate the current piece clockwise, if possible!
     */
    public void rotateClockwise() {
        myLastLinesRemoved = 0;
        final ImmutablePiece moved = myCurrentPiece.rotateClockwise();
        if (!collides(moved)) {
            myCurrentPiece = moved;
            setChanged();
        }
        notifyObservers();
    }

    /**
     * Rotate the current piece counterclockwise, if possible!
     */
    public void rotateCounterclockwise() {
        myLastLinesRemoved = 0;
        final ImmutablePiece moved = myCurrentPiece.rotateCounterclockwise();
        if (!collides(moved)) {
            myCurrentPiece = moved;
            setChanged();
        }
        notifyObservers();
    }

    /**
     * Drop the current piece as far as possible!
     */
    public void drop() {
        ImmutablePiece current = myCurrentPiece;
        ImmutablePiece downOneLine = current.moveDown();
        while (!collides(downOneLine)) { //move down as much as possible without colliding
            current = downOneLine;
            downOneLine = current.moveDown();
        }
        myCurrentPiece = current; // update the position on MyCurrentPiece
        moveDown(); // move myCurrentPiece down so that it freezes
    }

    // Overridden methods
    /**
     * @return What is your printable representation?
     */
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        // we'll generate a String by going row by row down the list of rows;
        // we only render the visible parts of the board
        for (int y = getHeight() - 1; 0 <= y; y--) {
            sb.append(SIDE_BORDER_CHAR);
            for (int x = 0; x < getWidth(); x++) {
                if (getColorAt(new Point(x, y)) == null) {
                    sb.append(EMPTY_BLOCK_CHAR);
                } else if (getRowAt(y)[x] == null) {
                    sb.append(CURRENT_PIECE_BLOCK_CHAR);
                } else {
                    sb.append(FROZEN_BLOCK_CHAR);
                }
            }
            sb.append(SIDE_BORDER_CHAR);
            sb.append('\n');
        }
        for (int x = 0; x <= getWidth() + 1; x++) {
            sb.append(BOTTOM_BORDER_CHAR);
        }
        sb.append('\n');
        return sb.toString();
    }

    // private helper methods
    /**
     * @param thePoint The point.
     * @return true if the specified point is within the boundaries of the
     *         board, including the play area above the board, and false
     *         otherwise.
     */
    private boolean isWithinBoard(final Point thePoint) {
        return 0 <= thePoint.getX() && thePoint.getX() < getWidth() && 0 <= thePoint.getY()
               && thePoint.getY() < getHeight() + ROWS_ABOVE_BOARD;
    }

    /**
     * Initializes some of the data structures.
     */
    private void initialize() {
        myCurrentPiece = myPieceGenerator.next();
        myNextPiece = myPieceGenerator.next();
        
        myLastLinesRemoved = 0;
        myFullFlag = false;
        myFrozenBlocks.clear();       
        for (int i = 0; i < myHeight + ROWS_ABOVE_BOARD; i++) {
            myFrozenBlocks.add(new Color[myWidth]);
        }
    }

    /**
     * Clears all full rows.
     */
    private void clearFullRows() {
        final ListIterator<Color[]> iterator = myFrozenBlocks.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (isFrozenRowFull(iterator.previousIndex())) {
                iterator.remove();
                myLastLinesRemoved = myLastLinesRemoved + 1;
            }
        }
    }
}
