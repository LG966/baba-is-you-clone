package fr.baba.engine;

import fr.baba.engine.board.*;
import fr.baba.engine.boardElement.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class LevelLoader {

    private static final String EMPTY_SQUARE = "EMPTY";
    private static final String COMMENT = "#";
    private static final String GRID_SEPARATOR = "%";
    private static final String SAME_SQUARE = "&";

    /**
     * Creates a new board with the dimensions and
     * objects specified in the file levelFilePath.
     * @param levelFilePath name of the file containing the board description
     * @return the aforementioned board
     * @throws IOException thrown if reading or parsing of the file has failed.
     */
    static Board loadLevel(String levelFilePath) throws IOException {
        Objects.requireNonNull(levelFilePath);
        var reader = Files.newBufferedReader(Path.of(levelFilePath));
        var lines = reader.lines().filter(line -> !(line.isBlank() || line.strip().startsWith(COMMENT))).collect(Collectors.toList());
        if(lines.size() <= 2)
            throw new IOException("Error : There has to be at least 1 valid line in your level");
        var dimensions = fetchDimensions(lines.get(0));
        Board board = new Board(dimensions.get(0), dimensions.get(1));
        lines.remove(0);
        fetchObjects(lines, board);
        fetchBackgroundObjects(lines, board);
        return board;
    }

    /**
     * Retrieves the dimensions (col, row)
     * contained in a string of type "15 25"
     * @param line string to be parsed
     * @return an array containing the two dimensions
     * @throws IOException if parsing goes wrong
     */
    private static List<Integer> fetchDimensions(String line) throws IOException{
        Objects.requireNonNull(line);
        var list = new ArrayList<Integer>();
        var items = line.split(" ");
        if(items.length != 2)
            throw new IOException("Error : Failed to parse the dimensions of the board");
        try {
            list.add(Integer.parseInt(items[0]));
            list.add(Integer.parseInt(items[1]));
            return list;
        }catch (NumberFormatException e){
            throw new IOException("Error : Failed to parse the dimensions of the board");
        }
    }

    /**
     * Parses a TileObject from the string object and
     * returns it.
     * @param object string to be parsed
     * @return a TileObject
     * @throws IOException if the string doesn't match anything
     */
    private static TileObject fetchObject(String object) throws IOException{
        Objects.requireNonNull(object);
        if(object.equals(EMPTY_SQUARE))
            return null;
        try{
            var token = Token.valueOf(object);
            return switch (token.getType()) {
                case noun -> new Noun(token);
                case operator -> new Operator(token);
                case sprite -> new Sprite(token);
                case property -> new Property(token);
            };
        }catch (IllegalArgumentException e){
            throw new IOException("Error : failed to parse item " + object);
        }
    }

    /**
     * Parses lines for TileObject that
     * are then added to the board.
     * @param lines list of lines to be parsed
     * @param board board in which the TileObjects will be added
     * @throws IOException if parsing fails
     */
    private static void fetchObjects(List<String> lines, Board board) throws IOException{
        Objects.requireNonNull(lines);
        Objects.requireNonNull(board);
        for(int i = 0; i != lines.size() && i != board.getNumberOfRow(); i++){
            if(lines.get(i).strip().equals(GRID_SEPARATOR))
                return;
            var items = lines.get(i).split(" ");
            int SAME_SQUARE_count = 0;
            for(int j = 0; j - SAME_SQUARE_count != board.getNumberOfCol() && j - SAME_SQUARE_count != items.length; j++){
                if(items[j].equals(SAME_SQUARE)){
                    SAME_SQUARE_count++;
                    continue;
                }
                var item = fetchObject(items[j]);
                if(item == null)
                    continue;
                board.addObjectToSquare(item,j - SAME_SQUARE_count * 2, i);
            }
        }
    }

    /**
     * Parses lines for TileObjects that
     * are then added to the board as background objects.
     * @param lines list of lines to be parsed
     * @param board board in which the TileObjects will be added
     * @throws IOException if an object could not be parsed
     */
    private static void fetchBackgroundObjects(List<String> lines, Board board) throws IOException{
        int i;
        Objects.requireNonNull(lines);
        Objects.requireNonNull(board);
        for(i = 0; i < lines.size() && !lines.get(i).strip().equals(GRID_SEPARATOR); i++);
        i++;
        for(int x = 0; i < lines.size() && x < board.getNumberOfRow(); i++, x++){
            var items = lines.get(i).split(" ");
            for(int j = 0; j != board.getNumberOfCol() && j != items.length; j++){
                var item = fetchObject(items[j]);
                if(item == null)
                    continue;
                board.setObjectInBackground(item,j, x);
            }
        }
    }
}
