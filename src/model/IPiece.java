/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;
import java.util.Arrays;

/**
 * A Tetris piece shaped like the letter 'I'.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public final class IPiece extends AbstractPiece {
    
    /** The color of an I piece. */
    public static final Color COLOR = Color.CYAN.darker();

    /** Rotation 0 of an I piece. */
    private static final Point[] ROTATION_ZERO = new Point[] {
        new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2)};

    /** Rotation 1 of an I piece. */
    private static final Point[] ROTATION_ONE = new Point[] {
        new Point(2, 3), new Point(2, 2), new Point(2, 1), new Point(2, 0)};
    
    /** Rotation 2 of an I piece. */
    private static final Point[] ROTATION_TWO = new Point[] {
        new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)};

    /** Rotation 3 of an I piece. */
    private static final Point[] ROTATION_THREE = new Point[] {
        new Point(1, 3), new Point(1, 2), new Point(1, 1), new Point(1, 0)};

    /**
     * Constructs an I piece.
     */
    public IPiece() {
        super(COLOR, Arrays.asList(new Rotation[] {
            new Rotation(ROTATION_ZERO), new Rotation(ROTATION_ONE),
            new Rotation(ROTATION_TWO), new Rotation(ROTATION_THREE)}));
    }
}
