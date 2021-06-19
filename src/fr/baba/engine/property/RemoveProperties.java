package fr.baba.engine.property;

import fr.baba.engine.boardElement.*;
import fr.baba.utils.Direction;
import fr.baba.engine.board.*;

import static java.lang.Math.abs;

public class RemoveProperties {
    /**
     * Applies all remove-like properties, including
     *  IS NOUN, HOT/MELT and SINK.
     * @param board board on which the Remove properties will be applied
     */
    static public void applyRemoveProperties(Board board){
        applyBoom(board);
        applySpriteConversion(board);
        applySink(board);
        applyHotMelt(board);
        applyDefeatYou(board);
    }

    /**
     * Applies the Hot/MELT property, subsequently removing all
     * MELT objects overlapping a HOT object.
     * @param board board on which the Hot/MELT property will be applied
     */
    static private void applyHotMelt(Board board){
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                if(board.squareHasProperty(i, j, Token.Hot) && board.squareHasProperty(i, j, Token.Melt)) {
                    var melt_objects = board.getObjectsOnSquareWithProperty(i, j, Token.Melt);
                    int finalI = i;
                    int finalJ = j;
                    melt_objects.forEach(obj -> board.removeObjectFromSquare(obj, finalI, finalJ));
                }
            }
        }
    }

    /**
     * Applies the Defeat/You property, subsequently removing all
     * You objects overlapping a Defeat object.
     * @param board board on which the Defeat/You property will be applied
     */
    static private void applyDefeatYou(Board board){
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                if(board.squareHasProperty(i, j, Token.Defeat) && board.squareHasProperty(i, j, Token.You)) {
                    var you_objects = board.getObjectsOnSquareWithProperty(i, j, Token.You);
                    int finalI = i;
                    int finalJ = j;
                    you_objects.forEach(obj -> board.removeObjectFromSquare(obj, finalI, finalJ));
                }
            }
        }
    }

    /**
     * Applies the SINK property, subsequently removing all
     * objects overlapping a SINK object, as well as the SINK object
     * itself.
     * @param board board on which the Sink property will be applied
     */
    static private void applySink(Board board){
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                if(board.squareHasProperty(i, j, Token.Sink) && board.getSquareOnGrid(i, j).size() != 1) {
                    board.clearSquare(i, j);
                }
            }
        }
    }


    /**
     * Applies the Boom property, subsequently removing all
     * objects adjacent to a Boom object, as well as the Boom object
     * itself.
     * @param board board on which the Sink property will be applied
     */
    static private void applyBoom(Board board){
        var v = Direction.values();
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                if(board.squareHasProperty(i, j, Token.Boom) && board.getSquareOnGrid(i, j).size() != 1) {
                    for(int x=0; x < v.length; x++){
                        if(board.coordinatesAreValid(i + v[x].getVector().get(0), j + v[x].getVector().get(1)))
                            board.clearSquare(i + v[x].getVector().get(0), j + v[x].getVector().get(1));
                        for (int y=x+1; y < v.length; y++){
                            if(board.coordinatesAreValid(i + v[x].getVector().get(0) + v[y].getVector().get(0), j + v[x].getVector().get(1) + v[y].getVector().get(1)))
                                board.clearSquare(i + v[x].getVector().get(0) + v[y].getVector().get(0), j + v[x].getVector().get(1) + v[y].getVector().get(1));
                        }
                    }
                }
            }
        }
    }

    /**
     * Applies and detect every NOUN IS NOUN conversion on the board.
     * @param board board on which the conversion will take place
     */
    static private void applySpriteConversion(Board board){
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                applyNounIsNoun(board, i, j);
            }
        }
    }

    /**
     * Checks if there is a IS operator at (colNumber, rowNumber) and
     * nouns next to it. If there is, a conversion of all the sprites
     * designated by the first noun are replaced by the sprite designated
     * by the second noun.
     * For example, LAVA IS WATER will convert every LAVA sprite to a WATER sprite
     * @param board board on which the NounIsNoun property will be applied
     * @param colNumber column number of the square
     * @param rowNumber row number of the square
     */
    static private void applyNounIsNoun(Board board, int colNumber, int rowNumber){
        Direction directions[] = {Direction.north, Direction.west};
        if (!board.coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        if (board.squareHasToken(colNumber, rowNumber, Token.Is)){
            for (var vector : directions){
                var leftObj = board.getObjectOnSquareOfType(colNumber + vector.getVector().get(0), rowNumber + vector.getVector().get(1), tokenType.noun);
                leftObj.ifPresent(left -> {
                    var rightObj = board.getObjectOnSquareOfType(colNumber + abs(vector.getVector().get(0)), rowNumber + abs(vector.getVector().get(1)), tokenType.noun);
                    rightObj.ifPresent(right -> convertSpriteObjects(board, left.getToken().getRep(), right.getToken().getRep()));
                });
            }
        }
    }

    /**
     * Converts every Sprite object on the board which have an old token
     * into a new Sprite object with a neww token.
     * @param board board on which the conversion will take place
     * @param old old token
     * @param neww new token
     */
    static private void convertSpriteObjects(Board board, Token old, Token neww){
        if(old.getType() != tokenType.sprite || neww.getType() != tokenType.sprite)
            throw new IllegalArgumentException("token have to be of type Sprite");
        for (int i = 0; i < board.getNumberOfCol(); i++) {
            for (int j = 0; j < board.getNumberOfRow(); j++) {
                while(true){
                    var obj = board.getObjectOnSquareOfToken(i, j, old);
                    if(obj.isEmpty())
                        break;
                    board.removeObjectFromSquare(obj.get(), i, j);
                    board.addObjectToSquare(new Sprite(neww), i, j);
                }
            }
        }
    }
}
