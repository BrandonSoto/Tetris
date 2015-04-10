/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

/**
 * The location of a single block on the board. Point objects are immutable.
 * 
 * @author Alan Fowler
 * @version WInter 2014
 */
public class Point {

    // Instance Fields
    /**
     * The x-coordinate.
     */
    private final int myX;

    /**
     * The y-coordinate.
     */
    private final int myY;

    // Constructor
    /**
     * This is your x-coordinate! This is your y-coordinate!
     * 
     * @param theX The x-coordinate.
     * @param theY The y-coordinate.
     */
    public Point(final int theX, final int theY) {
        myX = theX;
        myY = theY;
    }

    // Queries
    /**
     * @return What is your x-coordinate?
     */
    public int getX() {
        return myX;
    }

    /**
     * @return What is your y-coordinate?
     */
    public int getY() {
        return myY;
    }

    // overridden methods of class Object
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object theOther) {
        boolean result = this == theOther;
        if (!result && theOther != null && theOther.getClass() == getClass()) {
            final Point otherPoint = (Point) theOther;
            result = myX == otherPoint.myX && myY == otherPoint.myY;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getX() + getY();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + myX + ", " + myY + ")";
    }
}
