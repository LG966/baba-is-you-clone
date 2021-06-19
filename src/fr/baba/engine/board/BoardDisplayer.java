package fr.baba.engine.board;

import java.awt.*;
import java.awt.geom.Rectangle2D;

class BoardDisplayer {
    private static final int PADDING = 5; // minimum % padding for each side of the screen
    private static final int GRID_SIZE = 2; // pixels
    private static final Color boardBackgroundColor = Color.DARK_GRAY;
    private float height;
    private float width;
    private int topLeftX;
    private int topLeftY;
    private int squareHeight;
    private int squareWidth;
    private float heightPaddingSize = PADDING * height /100;
    private float widthPaddingSize = PADDING * width /100;

    /**
     * Updates the display attributes in case the screen dimensions were to change.
     * @param topLeftX top left x coordinate of the board drawing
     * @param topLeftY top left y coordinate of the board drawing
     * @param heightSize height of the board drawing
     * @param widthSize width of the board drawing
     */
    private void updateDisplayAttributes(int topLeftX, int topLeftY, int heightSize, int widthSize){
        this.height = heightSize;
        this.width = widthSize;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.heightPaddingSize = PADDING * this.height /100;
        this.widthPaddingSize = PADDING * this.width /100;
        this.squareHeight = (int) ((this.height - (this.heightPaddingSize * 2) - (Board.MAX_NUMBER_OF_ROWS + 1) * GRID_SIZE) / Board.MAX_NUMBER_OF_ROWS);
        this.squareWidth = (int) ((this.width - (this.widthPaddingSize * 2) - (Board.MAX_NUMBER_OF_COLS + 1) * GRID_SIZE) / Board.MAX_NUMBER_OF_COLS);
    }

    /**
     * Draws the grid background
     * @param board board from which the background will be set
     * @param graphics graphics in which the background will be drawn
     */
    private void drawGridBackground(Board board, Graphics2D graphics){
        int x = (int) (this.topLeftX + this.widthPaddingSize + (((Board.MAX_NUMBER_OF_COLS - board.getNumberOfCol()) * this.squareWidth) / 2) + GRID_SIZE);
        int y = (int) (this.topLeftY + this.heightPaddingSize + (((Board.MAX_NUMBER_OF_ROWS - board.getNumberOfRow()) * this.squareHeight) / 2) + GRID_SIZE);
        graphics.setColor(boardBackgroundColor);
        graphics.fill(new Rectangle2D.Float(
                x,
                y,
                board.getNumberOfCol() * (GRID_SIZE + this.squareWidth),
                board.getNumberOfRow() * (GRID_SIZE + this.squareHeight)
        ));
    }

    /**
     * Draws all the objects present on the board, including the background objects.
     * @param board board on which the objects are
     * @param graphics graphics in which the board will be drawn
     */
    private void drawObjectsOnBoard(Board board, Graphics2D graphics){
        for(int i = 0; i != board.getNumberOfCol(); i++){
            for(int j = 0; j != board.getNumberOfRow(); j++){
                if(board.getBackgroundObjectOnGrid(i, j) != null) // background objects{
                    board.getBackgroundObjectOnGrid(i, j).draw(graphics,
                            (int) (this.topLeftX + this.widthPaddingSize + (((Board.MAX_NUMBER_OF_COLS - board.getNumberOfCol()) * this.squareWidth) / 2) + i * (GRID_SIZE + this.squareWidth) + GRID_SIZE),
                            (int) (this.topLeftY + this.heightPaddingSize + (((Board.MAX_NUMBER_OF_ROWS - board.getNumberOfRow()) * this.squareHeight) / 2) + j * (GRID_SIZE + this.squareHeight) + GRID_SIZE),
                            this.squareHeight,
                            this.squareWidth
                    );
                for(var object : board.getSquareOnGrid(i, j)){
                    object.draw(graphics,
                            (int) (this.topLeftX + this.widthPaddingSize + (((Board.MAX_NUMBER_OF_COLS - board.getNumberOfCol()) * this.squareWidth) / 2) + i * (GRID_SIZE + this.squareWidth) + GRID_SIZE),
                            (int) (this.topLeftY + this.heightPaddingSize + (((Board.MAX_NUMBER_OF_ROWS - board.getNumberOfRow()) * this.squareHeight) / 2) + j * (GRID_SIZE + this.squareHeight) + GRID_SIZE),
                            this.squareHeight,
                            this.squareWidth
                    );
                }
            }
        }
    }

    private void clearScreen(Graphics2D graphics){
        graphics.clearRect(topLeftX, topLeftY, (int)width, (int)height);
    }

    /**
     * Draws the board to fit the rectangle located at
     * (topLeftX, topLeftY), with the specified dimensions.
     * @param board board to be drawn
     * @param graphics graphics in which the board will be drawn
     * @param topLeftX top left x coordinate of the drawing
     * @param topLeftY top left y coordinate of the drawing
     * @param heightSize height of the drawing
     * @param widthSize width of the drawing
     */
    void drawBoard(Board board, Graphics2D graphics, int topLeftX, int topLeftY, int heightSize, int widthSize){
        this.updateDisplayAttributes(topLeftX, topLeftY, heightSize, widthSize);
        this.clearScreen(graphics);
        this.drawGridBackground(board, graphics);
        this.drawObjectsOnBoard(board, graphics);
    }
}
