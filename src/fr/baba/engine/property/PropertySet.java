package fr.baba.engine.property;

import fr.baba.engine.boardElement.Token;
import fr.baba.engine.boardElement.tokenType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a set of properties.
 * Ideally, every token has properties, so every token
 * on the board should have an instance of this class
 * associated with it in some kind of way.
 * (Hashmaps being a fairly good idea,
 * see AbstractTileObject to see the implementation)
 */

public class PropertySet {
    private static final List<Token> PROPERTY_LIST =
            Collections.
                unmodifiableList(
                       Arrays.stream(Token.values()).
                               filter(t -> t.getType() == tokenType.property).
                               collect(Collectors.toList())
                );

    private final ArrayList<Boolean> properties = new ArrayList<>(PROPERTY_LIST.size());

    /**
     * Initializes a PropertySet with no properties (= all properties set to false),
     * except for propToAssign properties which are set to true.
     * @param propToAssign properties to be assigned
     */
    public PropertySet(Token... propToAssign){
        for (var prop: propToAssign){
            if(prop.getType() != tokenType.property){
                Objects.requireNonNull(propToAssign);
                throw new IllegalArgumentException("Token has to be a property");
            }
        }
        for(int i = 0; i != PROPERTY_LIST.size(); i++){
            properties.add(false);
        }
        for(var prop: propToAssign){
            this.setProperty(true, prop);
        }
    }

    /**
     * Adds or remove the specified property from the PropertySet
     * @param is whether to add or remove that property
     * @param propToAssign property to be added or removed
     */
    public void setProperty(boolean is, Token propToAssign){
        Objects.requireNonNull(propToAssign);
        if(propToAssign.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        properties.set(PROPERTY_LIST.indexOf(propToAssign), is);
    }

    /**
     * Removes all properties contained in the PropertySet
     */
    public void clearProperties(){
        for(int i = 0; i != properties.size(); i++){
            properties.set(i, false);
        }
    }

    /**
     * Checks if the PropertySet has the specified property
     * @param property to be checked
     * @return whether or not the PropertySet has that property
     */
    public boolean hasProperty(Token property){
        Objects.requireNonNull(property);
        if(property.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        return properties.get(PROPERTY_LIST.indexOf(property));
    }
}
