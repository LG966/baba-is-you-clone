package fr.baba.app;

import fr.baba.engine.Level;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Game {
    private static final Color BACKGROUND_COLOR = Color.PINK;
    private static final String DEFAULT_FILE = "levels/default-level.txt";
    private static final String LEVELS_OPTION = "--levels";
    private static final String LEVEL_OPTION = "--level";

    private final List<String> filesToOpen = new ArrayList<>();

    /**
     * Creates a baba is you game.
     * @param args cmd options like --levels, --level.
     */
    Game(String[] args){
        Objects.requireNonNull(args);
        String selectedDirectory = ParseLevelsDirectory(args);
        ParseLevelFile(args);
        if(selectedDirectory != null)
            try {
                loadFilesFromDirectory(selectedDirectory);
            }catch (IOException e){
                System.out.println("Could not open directory " + selectedDirectory + ".");
            }
    }

    /**
     * Looks for a "--levels dir" pattern in args and returns dir
     * @param args cmd options
     * @return the specified directory in args
     */
    private String ParseLevelsDirectory(String[] args){
        Objects.requireNonNull(args);
        for(int i=0; i < args.length-1; i++){
            if(args[i].equals(LEVELS_OPTION)){
                return args[i + 1];
            }
        }
        return null;
    }

    /**
     * Loads all the file names contained in directory
     * into filesToOpen.
     * @param directory where to look for files
     * @throws IOException if the directory couldn't be opened
     */
    private void loadFilesFromDirectory(String directory) throws IOException {
        Objects.requireNonNull(directory);
        filesToOpen.addAll(
                Files.
                list(Paths.get(directory)).
                map(Objects::toString).
                sorted().
                collect(Collectors.toList()));
    }

    /**
     * Looks for a "--level file.txt" pattern in args *
     * adds file.txt to filesToOpen.
     * @param args cmd options
     */
    private void ParseLevelFile(String[] args){
        Objects.requireNonNull(args);
        for(int i=0; i < args.length-1; i++){
            if(args[i].equals(LEVEL_OPTION)){
                filesToOpen.add(args[i + 1]);
                break;
            }
        }
    }

    /**
     * Launches the level specified by file.
     * @param context context in which the level will be launched
     * @param file name of the file that contains the level
     * @return true if file was successfully opened, false if not
     */
    private boolean launchLevel(ApplicationContext context, String file){
        try{
            var level = new Level(file);
            System.out.println("-- Level "+ file + " opened and running. --");
            level.run(context);
            return true;
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("-- Level "+ file + " cannot be opened. --");
            return false;
        }
    }

    /**
     * Runs the game, opening a window and displaying every level in fileToOpen one by one.
     */
    public void run(){
        Application.run(BACKGROUND_COLOR, context -> {
        boolean fileHasBeenOpened = false;
        for(var file : filesToOpen){
            fileHasBeenOpened |= launchLevel(context, file);
        }
        if(!fileHasBeenOpened)
            launchLevel(context, DEFAULT_FILE);
        context.exit(0);
        });
    }

    public static void main(String[] args) {
        var game = new Game(args);
        game.run();
    }

}
