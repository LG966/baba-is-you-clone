package fr.baba.engine.boardElement;

import java.util.Objects;

public class Sprite extends AbstractTileObject {
    /**
     * @param token of type sprite
     */
    public Sprite(Token token) {
        super(token);
        Objects.requireNonNull(token);
        if(token.getType() != tokenType.sprite){
            throw new IllegalArgumentException("Sprite object has to have a sprite token");
        }
    }

    /**
     * Adds or removes a property to the Sprite's token.
     * @param is whether the property will be added or removed
     * @param propToAssign property to be assigned
     */
    void setProperty(boolean is, Token propToAssign){
        Objects.requireNonNull(propToAssign);
        if(propToAssign.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        tokenProperties.get(this.token).setProperty(is, propToAssign);
    }

    /**
     * Removes the properties of all token of type Sprite
     */
    public static void clearAllSpriteProperties() {
        for (var token : tokenProperties.keySet()) {
            if (token.getType() == tokenType.sprite) {
                tokenProperties.get(token).clearProperties();
            }
        }
    }
}
