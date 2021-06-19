package fr.baba.engine.boardElement;

import java.util.Objects;

public class Operator extends AbstractTileObject {
    /**
     * @param token of type operator
     */
    public Operator(Token token) {
        super(token);
        Objects.requireNonNull(token);
        if(token.getType() != tokenType.operator){
            throw new IllegalArgumentException("Operator object has to have a operator token");
        }
        this.setProperty(true, Token.Push);
    }
}
