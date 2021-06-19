package fr.baba.engine.property;

import fr.baba.utils.Direction;
import fr.baba.engine.boardElement.TileObject;
import fr.baba.engine.boardElement.Token;
import fr.baba.engine.board.*;

import java.util.HashSet;
import java.util.Objects;

public class MoveProperties {
    /**
     * Moves the object obj located at (colNumber, rowNumber) towards direction.
     * The object may or may not be moved depending on :
     * Out of bounds move
     * Objects with STOP or PUSH property
     * @param board board on which the movement will happen
     * @param colNumber column number of the square where obj is located
     * @param rowNumber row number of the square where obj is located
     * @param obj to be moved object
     * @param direction where the object is headed
     * @return true if the object was successfully moved, else false
     */
    private static boolean moveObject(Board board, int colNumber, int rowNumber, TileObject obj, Direction direction){
        if(!board.coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        Objects.requireNonNull(direction); Objects.requireNonNull(obj);
        int newColNumber = colNumber + direction.getVector().get(0);
        int newRowNumber = rowNumber + direction.getVector().get(1);
        if (!board.coordinatesAreValid(newColNumber, newRowNumber) || board.squareHasProperty(newColNumber, newRowNumber, Token.Stop))
            return false;
        if (moveAllPushObjects(board, newColNumber, newRowNumber, direction))
            board.transferObject(obj, colNumber, rowNumber, newColNumber, newRowNumber);
        else
            return false;
        return true;
    }

    /**
     * Moves all push objects at (colNumber, rowNumber) to
     * the next square specified by direction.
     * @param board board on which the movements will happen
     * @param colNumber column number of the square
     * @param rowNumber row number of the square
     * @param direction where the objects are headed
     * @return if the objects were moved or not
     */
    static boolean moveAllPushObjects(Board board, int colNumber, int rowNumber, Direction direction){
        if(!board.coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        var push_objects =  board.getObjectsOnSquareWithProperty(colNumber, rowNumber, Token.Push);
        for(var push_object : push_objects){
            if(!moveObject(board, colNumber, rowNumber, push_object, direction)){
                return false;
            }
        }
        return true;
    }

    /**
     * Moves all you objects on the board towards direction.
     * @param board board on which the action will be performed
     * @param direction where the objects are headed
     */
    static public void moveAllYouObjects(Board board, Direction direction){
        Objects.requireNonNull(direction);
        var alreadyMoved = new HashSet<TileObject>();
        for (int i = 0; i != board.getNumberOfCol(); i++){
            for(int j = 0; j != board.getNumberOfRow(); j++){
                var you_objects = board.getObjectsOnSquareWithProperty(i, j, Token.You);
                for(var you_object : you_objects){
                    if(!alreadyMoved.contains(you_object))
                        moveObject(board, i, j, you_object, direction);
                    alreadyMoved.add(you_object);
                }
            }
        }
    }
}
