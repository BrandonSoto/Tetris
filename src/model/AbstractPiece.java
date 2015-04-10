/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A set of rotations, with position and color information.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public abstract class AbstractPiece implements Cloneable, ImmutablePiece {

    // Static Fields
    /**
     * The number of blocks in a piece.
     */
    public static final int NUMBER_OF_BLOCKS = 4;

    /**
     * The character used in the String representation to represent an empty block.
     */
    public static final char EMPTY_BLOCK_CHAR = ' ';

    /**
     * The character used in the String representation to represent a full block.
     */
    public static final char FULL_BLOCK_CHAR = '+';

    // Instance Fields
    /**
     * The color.
     */
    private final Color myColor; // Color is immutable

    /**
     * The list of all possible rotations for this Tetris piece.
     */
    private final List<Rotation> myRotations;
    
    /**
     * The current rotation number.
     */
    private int myCurrentRotation;

    /**
     * The origin of the local coordinate system for this Tetris piece.
     */
    private Point myOrigin; // This Point class is immutable (don't use java.awt.Point!)

    // Constructor

    /**
     * These are your rotations! This is your color!
     * 
     * @param theColor The color.
     * @param theRotations The rotations.
     */
    public AbstractPiece(final Color theColor,
                         final List<Rotation> theRotations) {
        myColor = theColor; // colors are immutable - no need for a defensive copy

        // use an unmodifiable view of the parameter list
        myRotations = Collections.unmodifiableList(new ArrayList<Rotation>(theRotations));
        
        myCurrentRotation = 0;
        myOrigin = new Point(0, 0);
    }

    // Queries
    /**
     * @return What are your rotations?
     */
 
    public List<Rotation> getRotations() {
        return myRotations; // an unmodifiableList, so no need to return a defensive copy
    }

    /**
     * @return What is your current set of Points?
     */
    public Point[] getPoints() {
        return ((Rotation) getRotations().get(myCurrentRotation)).getPoints();
    }

    /**
     * @return What is your origin?
     */
    public Point getOrigin() {
        return myOrigin;
    }

    /**
     * @return What is your color?
     */
    public Color getColor() {
        return myColor;
    }

    /**
     * @param theIndex The index.
     * @return What is the absolute position of block number theIndex?
     */
    public Point getAbsolutePosition(final int theIndex) {
        return new Point(getOrigin().getX() + getPoints()[theIndex].getX(),
                         getOrigin().getY() + getPoints()[theIndex].getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutablePiece moveLeft() {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            ((AbstractPiece) result).myOrigin = new Point(myOrigin.getX() - 1,
                                                          myOrigin.getY());
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutablePiece moveRight() {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            ((AbstractPiece) result).myOrigin = new Point(myOrigin.getX() + 1,
                                                          myOrigin.getY());
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutablePiece moveDown() {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            ((AbstractPiece) result).myOrigin = new Point(myOrigin.getX(),
                                                          myOrigin.getY() - 1);
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutablePiece rotateClockwise() {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            ((AbstractPiece) result).myCurrentRotation =
                            (myCurrentRotation + 1) % getRotations().size();
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutablePiece rotateCounterclockwise() {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            if (myCurrentRotation == 0) {
                ((AbstractPiece) result).myCurrentRotation = getRotations().size() - 1;
            } else {
                ((AbstractPiece) result).myCurrentRotation =
                                (myCurrentRotation - 1) % getRotations().size();
            }
            
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    /**
     * @return What piece results from setting your origin to theOrigin?
     * 
     * @param theOrigin The new origin.
     */
    public ImmutablePiece setOrigin(final Point theOrigin) {
        ImmutablePiece result = null;
        try {
            result = (AbstractPiece) clone();
            ((AbstractPiece) result).myOrigin = theOrigin;
        } catch (final CloneNotSupportedException e) {
            // this should never happen
            assert false;
        }
        return result;
    }

    // Comparison Methods

    /**
     * @param theOtherPiece The other piece.
     * @return true if this piece and the_other_piece are equivalent in every
     *         aspect except their current rotation, false otherwise.
     */
    public boolean equalsExceptRotation(final ImmutablePiece theOtherPiece) {
        boolean result = ((AbstractPiece) theOtherPiece).getColor().equals(getColor())
                         && ((AbstractPiece) theOtherPiece).myOrigin.equals(myOrigin)
                         && ((AbstractPiece) theOtherPiece).getRotations().size()
                         == getRotations().size();
        for (int i = 0; result && i < getRotations().size(); i++) {
            result = result && ((AbstractPiece) theOtherPiece).getRotations().get(i).equals(
                                getRotations().get(i));
        }
        return result;
    }

    /**
     * @param theOtherPiece The other piece.
     * @return true if this piece and the_other_piece are equivalent in every
     *         aspect except their current origin, false otherwise.
     */
    public boolean equalsExceptOrigin(final ImmutablePiece theOtherPiece) {
        boolean result = ((AbstractPiece) theOtherPiece).getColor().equals(getColor())
                         && ((AbstractPiece) theOtherPiece).myCurrentRotation
                         == myCurrentRotation
                         && ((AbstractPiece) theOtherPiece).getRotations().size()
                         == getRotations().size();
        for (int i = 0; result && i < getRotations().size(); i++) {
            result = result && ((AbstractPiece) theOtherPiece).getRotations().get(i).equals(
                                getRotations().get(i));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object theOther) {
        boolean result = this == theOther;
        if (!result && theOther != null && theOther.getClass() == getClass()) {
            final ImmutablePiece otherPiece = (AbstractPiece) theOther;
            result = ((AbstractPiece) otherPiece).getColor().equals(getColor());
            result = result && ((AbstractPiece) otherPiece).myOrigin.equals(myOrigin);
            result = result
                     && (((AbstractPiece) otherPiece).myCurrentRotation == myCurrentRotation);
            for (int i = 0; result && i < getRotations().size(); i++) {
                result = result
                         && ((AbstractPiece) otherPiece).getRotations().get(i).equals(
                             getRotations().get(i));
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return myColor.hashCode() + myOrigin.hashCode() + myCurrentRotation; 
    }

    /**
     * @return What is your printable representation?
     */
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        // we'll generate a String by going line by line and checking for blocks
        for (int y = NUMBER_OF_BLOCKS - 1; 0 <= y; y--) { // output rows
            for (int x = 0; x < NUMBER_OF_BLOCKS; x++) {  // output columns
                boolean blockFound = false;
                for (int i = 0; i < NUMBER_OF_BLOCKS; i++) { // check the 4 blocks
                    final Point pos = getPoints()[i];
                    if (pos.getX() == x && pos.getY() == y) {
                        sb.append(FULL_BLOCK_CHAR);
                        blockFound = true;
                        break;
                    }
                }
                if (!blockFound) {
                    sb.append(EMPTY_BLOCK_CHAR);
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    /** {@inheritDoc} */
    @Override
    public ImmutablePiece clone() throws CloneNotSupportedException {
        return (AbstractPiece) super.clone();
    }

}
