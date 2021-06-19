package fr.baba.engine.boardElement;

import java.util.Objects;

public class Noun extends AbstractTileObject {
    /**
     * @param token of type Noun
     */
    public Noun(Token token) {
        super(token);
        Objects.requireNonNull(token);
        if(token.getType() != tokenType.noun){
            throw new IllegalArgumentException("Noun object has to have a noun token");
        }
        this.setProperty(true, Token.Push);
    }

    /**
     * @return the sprite token designated by the noun itself
     */
    Token getRep(){
        return this.token.getRep();
    }

    /**
     * Adds or removes a property to the sprite designated by the noun itself
     * @param is whether the property will be added or removed
     * @param propToAssign property to be assigned
     */
    public void setRepProperty(boolean is, Token propToAssign){
        Objects.requireNonNull(propToAssign);
        if(propToAssign.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        new Sprite(this.getRep()).setProperty(is, propToAssign);
    }
}
