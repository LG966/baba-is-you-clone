package fr.baba.engine.boardElement;

import fr.baba.engine.Drawable;

public interface TileObject extends Drawable {
    /**
     * Checks whether or not the TileObject has the specified property.
     * @param property property to be checked
     * @return whether the TileObject has that property
     */
    boolean hasProperty(Token property);

    /**
     * Sets a property to the TileObject.
     * @param is whether to add or remove that property
     * @param property property to be added or removed
     */
    void setProperty(Boolean is, Token property);

    /**
     * Retruns the token assigned to the TileObject.
     * @return the token assigned to the TileObject
     */
    Token getToken();
}
