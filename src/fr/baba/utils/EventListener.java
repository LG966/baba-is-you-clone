package fr.baba.utils;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;

public class EventListener {
    /**
     * Waits for the next move made by the user, by means of arrow keys.
     * @param context context
     * @return direction of the arrow key pressed by the user
     */
    public static Direction nextMove(ApplicationContext context){
        while(true){
            Event event = context.pollOrWaitEvent(10);
            if (event == null)
                continue;
            if(event.getAction() == Event.Action.KEY_PRESSED) {
                switch (event.getKey()) {
                    case Q:
                        context.exit(1);
                    case UP:
                        return Direction.north;
                    case DOWN:
                        return Direction.south;
                    case LEFT:
                        return Direction.west;
                    case RIGHT:
                        return Direction.east;
                    default:
                        break;
                }
            }
        }
    }
}
