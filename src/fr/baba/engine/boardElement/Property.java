package fr.baba.engine.boardElement;

import java.util.Objects;

public class Property extends AbstractTileObject {

    public Property(Token token) {
        super(token);
        Objects.requireNonNull(token);
        if(token.getType() != tokenType.property){
            throw new IllegalArgumentException("Property object has to have a property token");
        }
        this.setProperty(true, Token.Push);
    }


}
