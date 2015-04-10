/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

import java.util.Objects;

/**
 * A set of 4 adjacent points that share the same color.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public class Rotation {

    /**
     * The Points.
     */
    private final Point[] myPoints;

    // Constructor

    /**
     * These are your Points!
     * 
     * @param thePoints The points.
     */
    public Rotation(final Point[] thePoints) {
        myPoints = (Point[]) thePoints.clone();
    }

    // Queries

    /**
     * @return What are your points?
     */
    public Point[] getPoints() {
        return (Point[]) myPoints.clone();
    }

    // Object Methods

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object theOther) {
        boolean result = this == theOther;
        if (!result && theOther != null && theOther.getClass() == getClass()) {
            final Rotation otherRotation = (Rotation) theOther;
            result = Objects.deepEquals(myPoints, otherRotation.getPoints());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return Objects.hashCode(myPoints);
    }

}
