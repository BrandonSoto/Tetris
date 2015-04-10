/*
 * TCSS 305 - Project Tetris
 */

package model.piecegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.AbstractPiece;
import model.IPiece;
import model.ImmutablePiece;
import model.JPiece;
import model.LPiece;
import model.OPiece;
import model.Point;
import model.SPiece;
import model.TPiece;
import model.ZPiece;

/**
 * A Tetris piece generator which follows the convention of putting one copy of each
 * available Tetris piece type into a collection and then removing pieces in random order
 * until the collection is empty. This process is repeated as necessary.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public class SevenPieceBagGenerator implements PieceGenerator {
    
    /** The list of available piece types. */
    private static final List<AbstractPiece> PIECE_LIST = Arrays.asList(
        new IPiece(), new JPiece(), new LPiece(), new OPiece(),
        new SPiece(), new TPiece(), new ZPiece());

    // Instance Fields
    /** The random number generator used by this piece generator. */
    private final Random myRandom;

    /** The origin to use for random pieces. */
    private final Point myOrigin;

    /** The piece list, used to keep track of each "set" of pieces. */
    private final List<ImmutablePiece> myPieceList;

    // Constructors

    /**
     * Constructs a new RandomGenerator with the specified piece origin.
     * 
     * @param theOrigin The origin.
     */
    public SevenPieceBagGenerator(final Point theOrigin) {
        myRandom = new Random();
        myOrigin = theOrigin;
        myPieceList = new ArrayList<ImmutablePiece>();
        fillPieceList();
    }

    // Instance Methods

    /**
     * {@inheritDoc}
     */
    public ImmutablePiece next() {
        final int choice = myRandom.nextInt(myPieceList.size());
        final ImmutablePiece result = (AbstractPiece) myPieceList.get(choice);
        myPieceList.remove(choice);
        if (myPieceList.isEmpty()) {
            fillPieceList();
        }
        return result;
    }

    /**
     * Populates the piece list with 7 pieces, one of each type.
     */
    private void fillPieceList() {
        for (final ImmutablePiece p : PIECE_LIST) {
            myPieceList.add(((AbstractPiece) p).setOrigin(myOrigin));
        }
    }
}
