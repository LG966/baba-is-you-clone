package fr.baba.engine.property;

import fr.baba.engine.boardElement.*;
import fr.baba.utils.Direction;
import fr.baba.engine.board.*;

import static java.lang.Math.abs;

public class PropertyUpdater {
    /**
     * Updates the sprite properties.
     * @param board board on which the properties will be collected
     */
    public static void collectProperties(Board board){
        Sprite.clearAllSpriteProperties();
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                applyNounIsProperty(board, i, j);
            }
        }
    }

    /**
     * Checks if there is a IS operator at (colNumber, rowNumber) and a
     * noun and a property next to it. If there is, the property is
     * added to the sprite designated by the noun.
     * For example, WALL IS STOP will add the STOP property to WALL sprites.
     * @param board board on which the Noun IS PROPERTY will be checked
     * @param colNumber column number of the square
     * @param rowNumber row number of the square
     */
    static private void applyNounIsProperty(Board board, int colNumber, int rowNumber){
        Direction directions[] = {Direction.north, Direction.west};
        if (!board.coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        if (board.squareHasToken(colNumber, rowNumber, Token.Is)){
            for (var vector : directions){
                var nounObj = board.getObjectOnSquareOfType(colNumber + vector.getVector().get(0), rowNumber + vector.getVector().get(1), tokenType.noun);
                nounObj.ifPresent(noun -> {
                    var propertyObj = board.getObjectOnSquareOfType(colNumber + abs(vector.getVector().get(0)), rowNumber + abs(vector.getVector().get(1)), tokenType.property);
                    propertyObj.ifPresent(property -> {
                        ((Noun) noun).setRepProperty(true, property.getToken());
                    });
                });
            }
        }
    }
}
