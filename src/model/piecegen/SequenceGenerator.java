/*
 * TCSS 305 - Project Tetris
 */

package model.piecegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.ImmutablePiece;

/**
 * A piece generator that repeatedly cycles through a specified sequence of pieces.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public class SequenceGenerator implements PieceGenerator {
    
    // Instance Fields
    /** The sequence of pieces used by this piece generator. */
    private final List<ImmutablePiece> mySequence;

    /** The current index in the sequence of pieces. */
    private int myIndex;

    // Constructor
    /**
     * Constructs a new SequenceGenerator using the specified sequence.
     * 
     * @param theSequence The sequence.
     */
    public SequenceGenerator(final List<ImmutablePiece> theSequence) {
        mySequence = Collections.unmodifiableList(new ArrayList<ImmutablePiece>(theSequence));
        myIndex = 0;
    }

    // Instance Methods
    /**
     * {@inheritDoc}
     */
    public ImmutablePiece next() {
        final ImmutablePiece result = mySequence.get(myIndex);
        myIndex = (myIndex + 1) % mySequence.size();
        return result;
    }
}
