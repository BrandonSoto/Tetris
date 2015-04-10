/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;
import java.util.Arrays;

/**
 * A Tetris piece shaped like the letter 'T'.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public final class TPiece extends AbstractPiece {
    
    /** The color of a T piece. */
    public static final Color COLOR = Color.MAGENTA.darker();

    /** Rotation 0 of a T piece. */
    private static final Point[] ROTATION_ZERO = new Point[] {
        new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2)};

    /** Rotation 1 of a T piece. */
    private static final Point[] ROTATION_ONE = new Point[] {
        new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(2, 1)};

    /** Rotation 2 of a T piece. */
    private static final Point[] ROTATION_TWO = new Point[] {
        new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0)};

    /** Rotation 3 of a T piece. */
    private static final Point[] ROTATION_THREE = new Point[] {
        new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(0, 1)};

    /**
     * Constructs a T piece.
     */
    public TPiece() {
        super(COLOR, Arrays.asList(new Rotation[] {
            new Rotation(ROTATION_ZERO), new Rotation(ROTATION_ONE),
            new Rotation(ROTATION_TWO), new Rotation(ROTATION_THREE)}));
    }
}
