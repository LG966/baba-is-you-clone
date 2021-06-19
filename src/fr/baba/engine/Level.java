package fr.baba.engine;

import fr.baba.engine.board.*;
import fr.baba.engine.property.MoveProperties;
import fr.baba.engine.property.PropertyUpdater;
import fr.baba.engine.property.RemoveProperties;
import fr.baba.utils.EventListener;
import fr.umlv.zen5.ApplicationContext;

import java.io.IOException;
import java.util.Objects;

public class Level {
    private final Board levelBoard;

    /**
     * Creates a level based on the level specified in 
     * levelFilePath. 
     * @param levelFilePath Path of the file which contains the level
     */
    public Level(String levelFilePath) throws IOException {
        Objects.requireNonNull(levelFilePath);
        this.levelBoard = LevelLoader.loadLevel(levelFilePath);
    }

    /**
     * Creates an empty level with the specified number
     * of columns and rows
     * Use this constructor if you want to create a level
     * manually without reading a file.
     * @param numberOfCol number of columns of the board
     * @param numberOfRow number of rows of the board
     */
    Level(int numberOfCol, int numberOfRow){
        if(numberOfCol < 0 || numberOfRow < 0){
            throw new IllegalArgumentException("there has to be at least one row and/or one column");
        }
        if(numberOfCol > Board.MAX_NUMBER_OF_COLS || numberOfRow > Board.MAX_NUMBER_OF_ROWS){
            throw new IllegalArgumentException("number of Rows or Columns exceeded the the authorized maximum");
        }
        this.levelBoard = new Board(numberOfCol, numberOfRow);
    }

    /**
     * Renders a new frame and draws the grid background as well as the board itself.
     * @param context context in which the new frame will be rendered
     */
    private void updateDisplay(ApplicationContext context){
        context.renderFrame(graphics -> levelBoard.draw(graphics,
                0,
                0,
                (int)context.getScreenInfo().getHeight(),
                (int)context.getScreenInfo().getWidth()
                )
        );
    }

    /**
     * Launches the level until it is won or lost.
     * @param context context in which the game will be ran
     */
    public void run(ApplicationContext context){
        while(true){
            this.updateDisplay(context);
            PropertyUpdater.collectProperties(levelBoard);
            MoveProperties.moveAllYouObjects(levelBoard, EventListener.nextMove(context));
            PropertyUpdater.collectProperties(levelBoard);
            RemoveProperties.applyRemoveProperties(levelBoard);
            if(levelBoard.lost()){
                System.out.println("Lost");
                break;
            }
            if(levelBoard.won()){
                System.out.println("Won");
                break;
            }
        }
        this.updateDisplay(context);
    }
}
