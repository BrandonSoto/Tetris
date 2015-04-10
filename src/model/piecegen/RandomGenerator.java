/*
 * TCSS 305 - Project Tetris
 */

package model.piecegen;

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
 * A piece generator that uses random numbers to generate pieces.
 * 
 * @author Alan Fowler
 * @version Winter 2014
 */
public class RandomGenerator implements PieceGenerator {

    // Static Fields
    /** The list of available piece types. */
    private static final List<AbstractPiece> PIECE_LIST = Arrays.asList(
        new IPiece(), new JPiece(), new LPiece(), new OPiece(),
        new SPiece(), new TPiece(), new ZPiece());

    // Instance Fields
    /** The random number generator used by this piece generator. */
    private final Random myRandom;
    
    /** The origin to use for random pieces. */
    private final Point myOrigin;

    // Constructors

    /**
     * Constructs a new RandomGenerator with the specified piece origin.
     * 
     * @param theOrigin The origin.
     */
    public RandomGenerator(final Point theOrigin) {
        myRandom = new Random();
        myOrigin = theOrigin;
    }

    // Instance Methods

    /**
     * {@inheritDoc}
     */
    public ImmutablePiece next() {
        final int choice = myRandom.nextInt(PIECE_LIST.size()); 
        return ((AbstractPiece) PIECE_LIST.get(choice)).setOrigin(myOrigin);
    }
}
