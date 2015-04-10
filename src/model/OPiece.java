/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;
import java.util.Arrays;

/**
 * A Tetris piece shaped like the letter 'O'.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public final class OPiece extends AbstractPiece {
    
    /** The color of an O piece. */
    public static final Color COLOR = Color.YELLOW.darker();

    /** Rotation 0 of an O piece. */
    private static final Point[] ROTATION_ZERO = new Point[] {
        new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2)};

    /**
     * Constructs an O piece.
     */
    public OPiece() {
        super(COLOR, Arrays.asList(new Rotation[] {new Rotation(ROTATION_ZERO)}));
    }
}
