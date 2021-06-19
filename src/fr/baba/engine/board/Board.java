package fr.baba.engine.board;

import fr.baba.engine.Drawable;
import fr.baba.engine.boardElement.TileObject;
import fr.baba.engine.boardElement.Token;
import fr.baba.engine.boardElement.tokenType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board implements Drawable {

    public static final int MAX_NUMBER_OF_COLS = 33;
    public static final int MAX_NUMBER_OF_ROWS = 18;

    private final ArrayList<ArrayList<ArrayList<TileObject>>> grid;
    private final ArrayList<ArrayList<TileObject>> backgroundGrid;
    private final int numberOfCol;
    private final int numberOfRow;

    private final BoardDisplayer boardDisplayer = new BoardDisplayer();


    /**
     * Initializes an empty board with a default background color.
     * @param numberOfCol number of columns of the board
     * @param numberOfRow number of rows of the board
     */
    public Board(int numberOfCol, int numberOfRow){
        if(numberOfCol < 0 || numberOfRow < 0)
            throw new IllegalArgumentException("there has to be at least one row and/or one column");
        if(numberOfCol > MAX_NUMBER_OF_COLS || numberOfRow > MAX_NUMBER_OF_ROWS)
            throw new IllegalArgumentException("number of Rows or Columns exceeded the authorized maximum");

        this.numberOfCol = numberOfCol;
        this.numberOfRow = numberOfRow;

        this.grid = new ArrayList<>(numberOfCol);
        this.backgroundGrid = new ArrayList<>(numberOfCol);
        for (int i = 0; i != numberOfCol; i++){
            this.grid.add(new ArrayList<>(numberOfRow));
            this.backgroundGrid.add(new ArrayList<>(numberOfRow));
            for (int j = 0; j != numberOfRow; j++){
                this.grid.get(i).add(new ArrayList<>());
                this.backgroundGrid.get(i).add(null);
            }
        }
    }

    /**
     * Checks if the coordinates (colNumber, rowNumber) are out of bounds of the board.
     * @param colNumber column coordinate
     * @param rowNumber row coordinate
     * @return if the coordinates (colNumber, rowNumber) are out of bounds of the board.
     */
    public Boolean coordinatesAreValid(int colNumber, int rowNumber){
        return colNumber >= 0 && rowNumber >= 0 && colNumber < this.numberOfCol && rowNumber < this.numberOfRow;
    }

    /**
     * Returns the width, (= number of columns) of the board.
     * @return the number of columns of the board.
     */
    public int getNumberOfCol() {
        return numberOfCol;
    }

    /**
     * Returns the height, (= number of rows) of the board.
     * @return the number of rows of the board.
     */
    public int getNumberOfRow() {
        return numberOfRow;
    }

    /**
     * Adds object to the square located at (colNUmber, rowNumber).
     * @param object object to be added
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     */
    void setObjectOnSquare(TileObject object, int colNumber, int rowNumber){
        Objects.requireNonNull(object);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        getSquareOnGrid(colNumber, rowNumber).add(object);
    }

    public void setObjectInBackground(TileObject object, int colNumber, int rowNumber){
        Objects.requireNonNull(object);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        backgroundGrid.get(colNumber).set(rowNumber, object);
    }

    /**
     * Returns the square located at (colNUmber, rowNumber).
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @return an ArrayList containing the references (not copies) of the objects on the square.
     */
    public ArrayList<TileObject> getSquareOnGrid(int colNumber, int rowNumber){
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        return this.grid.get(colNumber).get(rowNumber);
    }

    TileObject getBackgroundObjectOnGrid(int colNumber, int rowNumber){
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        return this.backgroundGrid.get(colNumber).get(rowNumber);
    }

    /**
     * Empties the square located at (colNUmber, rowNumber).
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     */
    public void clearSquare(int colNumber, int rowNumber){
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        this.getSquareOnGrid(colNumber, rowNumber).clear();
    }

    /**
     * Checks if an object on the square located at (colNUmber, rowNumber)
     * has the specified property.
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @param property property to be had
     * @return true if at least one object has that property
     */
    public Boolean squareHasProperty(int colNumber, int rowNumber, Token property){
        Objects.requireNonNull(property);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        if(Objects.requireNonNull(property).getType() != tokenType.property)
            throw new IllegalArgumentException("Token has to be a property");
        return this.getSquareOnGrid(colNumber, rowNumber).stream().anyMatch(obj -> obj.hasProperty(property));
    }

    /**
     * Checks if an object on the square located at (colNUmber, rowNumber)
     * has the specified token.
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @param token token to be had
     * @return true if at least one object has that token
     */
    public Boolean squareHasToken(int colNumber, int rowNumber, Token token){
        Objects.requireNonNull(token);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        return this.getSquareOnGrid(colNumber, rowNumber).
                stream().
                anyMatch(obj -> obj.getToken() == token);
    }

    /**
     * Searches for an object with a token of tokenType type
     * on the square located at (colNUmber, rowNumber).
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @param type tokenType to be had by the object
     * @return An optional object which is empty if no such object
     * is encountered, else containing the first object matching
     * the query.
     * If the coordinates are out of bounds, an empty object is returned.
     */
    public Optional<TileObject> getObjectOnSquareOfType(int colNumber, int rowNumber, tokenType type){
        Objects.requireNonNull(type);
        if(!coordinatesAreValid(colNumber, rowNumber)){
            return Optional.empty();
        }
        return this.getSquareOnGrid(colNumber, rowNumber).
                stream().
                filter(obj -> obj.getToken().getType() == type).
                findFirst();
    }

    /**
     * Searches for an object with the specified token
     * on the square located at (colNUmber, rowNumber).
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @param token token to be had by the object
     * @return An optional object which is empty if no such object
     * is encountered, else containing the first object matching
     * the query.
     * If the coordinates are out of bounds, an empty object is returned.
     */
    public Optional<TileObject> getObjectOnSquareOfToken(int colNumber, int rowNumber, Token token){
        Objects.requireNonNull(token);
        if(!coordinatesAreValid(colNumber, rowNumber)){
            return Optional.empty();
        }
        return this.getSquareOnGrid(colNumber, rowNumber).
                stream().
                filter(obj -> obj.getToken() == token).
                findFirst();
    }

    /**
     * Gets all objects which have the specified property
     * on the square located at (colNUmber, rowNumber).
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     * @param property property to be had by the objects
     * @return List containing those objects
     */
    public List<TileObject> getObjectsOnSquareWithProperty(int colNumber, int rowNumber, Token property){
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        if(property.getType() != tokenType.property)
            throw new IllegalArgumentException("Token has to be a property");
        return  getSquareOnGrid(colNumber, rowNumber).
                stream().
                filter(obj -> obj.hasProperty(property)).
                collect(Collectors.toList());
    }

    /**
     * Removes the specified object from the square located at (colNUmber, rowNumber).
     * If the object is not there, nothing happens.
     * @param obj object to be removed
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     */
    public void removeObjectFromSquare(TileObject obj, int colNumber, int rowNumber){
        Objects.requireNonNull(obj);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        getSquareOnGrid(colNumber, rowNumber).remove(obj);
    }

    /**
     * Adds the specified object to the square located at (colNUmber, rowNumber).
     * @param obj object to be added
     * @param colNumber column coordinate of the square
     * @param rowNumber row coordinate of the square
     */
    public void addObjectToSquare(TileObject obj, int colNumber, int rowNumber){
        Objects.requireNonNull(obj);
        if(!coordinatesAreValid(colNumber, rowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        setObjectOnSquare(obj, colNumber, rowNumber);
    }

    /**
     * Removes the specified object located at (colNUmber, rowNumber),
     * then adds that same object at (newColNUmber, newRowNumber)
     * @param obj object to be transferred
     * @param colNumber old column coordinate of the object
     * @param rowNumber old row coordinate of the object
     * @param newColNumber new column coordinate of the object
     * @param newRowNumber new row coordinate of the object
     */
    public void transferObject(TileObject obj, int colNumber, int rowNumber, int newColNumber, int newRowNumber){
        Objects.requireNonNull(obj);
        if(!coordinatesAreValid(colNumber, rowNumber) || !coordinatesAreValid(newColNumber, newRowNumber))
            throw new IllegalArgumentException("colNumber or/and rowNumber out of bounds of the grid");
        removeObjectFromSquare(obj, colNumber, rowNumber);
        addObjectToSquare(obj, newColNumber, newRowNumber);
    }

    /**
     * Checks if the player has won.
     * @return if the player has won or not.
     */
    public Boolean won(){
        for (int i = 0; i < this.numberOfCol; i++) {
            for (int j = 0; j < this.numberOfRow; j++) {
                if(this.squareHasProperty(i, j, Token.You)
                        && this.squareHasProperty(i, j, Token.Win))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player has lost.
     * @return if the player has lost or not.
     */
    public Boolean lost(){
        for (int i = 0; i < this.numberOfCol; i++) {
            for (int j = 0; j < this.numberOfRow; j++) {
                if(this.squareHasProperty(i, j, Token.You)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void draw(Graphics2D graphics, int topLeftX, int topLeftY, int heightSize, int widthSize) {
        this.boardDisplayer.drawBoard(this, graphics, topLeftX, topLeftY, heightSize, widthSize);
    }
}
