package fr.baba.utils;

import java.util.List;
import java.util.Objects;


/**
 * Enumerates all the possible direction on a board
 */
public enum Direction {
    north(List.of(0, -1)),
    east(List.of(1, 0)),
    west(List.of(-1, 0)),
    south(List.of(0, 1));

    private final List<Integer> vector;

    /**
     * Sets a vector for a given direction.
     * @param vector translation of the direction into a vector
     */
    Direction(List<Integer> vector){
        Objects.requireNonNull(vector);
        this.vector = vector;
    }

    /**
     * @return A translation of the direction into a vector
     */
    public List<Integer> getVector(){
        return this.vector;
    }

}
