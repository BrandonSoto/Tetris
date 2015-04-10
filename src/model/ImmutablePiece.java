/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

/**
 * This interface defines the required operations of immutable Tetris pieces.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public interface ImmutablePiece {

    /**
     * @return What piece results from moving you left?
     */
    ImmutablePiece moveLeft();

    /**
     * @return What piece results from moving you right?
     */
    ImmutablePiece moveRight();

    /**
     * @return What piece results from moving you down?
     */
    ImmutablePiece moveDown();

    /**
     * @return What piece results from rotating you clockwise?
     */
    ImmutablePiece rotateClockwise();

    /**
     * @return What piece results from rotating you counterclockwise?
     */
    ImmutablePiece rotateCounterclockwise();

}
