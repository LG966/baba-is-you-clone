package fr.baba.engine.boardElement;

import fr.baba.engine.property.PropertySet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class AbstractTileObject implements TileObject {
    protected final Token token;
    private static final HashMap<Token, Image> imageLoader = new HashMap<>();
    protected static final HashMap<Token, PropertySet> tokenProperties = new HashMap<>();

    /**
     * @param token token to be assigned to the TileObject
     */
    AbstractTileObject(Token token){
        Objects.requireNonNull(token);
        this.token = token;
        this.initTokenProperties();
    }

    @Override
    public Token getToken(){
        return this.token;
    }

    @Override
    public void draw(Graphics2D graphics, int topLeftX, int topLeftY, int heightSize, int widthSize){
        if(topLeftX < 0 || topLeftY < 0 ){
            throw new IllegalArgumentException("Can't draw at negative coordinates");
        }
        if(heightSize < 0 || widthSize < 0){
            throw new IllegalArgumentException("Image can't be scaled to fit 0 pixel");
        }

        Image image = this.getImage();
        graphics.drawImage(image,
                topLeftX,
                topLeftY,
                topLeftX + widthSize,
                topLeftY + heightSize,
                0,
                0,
                image.getWidth(null),
                image.getHeight(null),
                null);
    }

    /**
     * Gets an image
     * @return Image object representing the token
     */
    private Image getImage() {
        if(!imageLoader.containsKey(this.token)){
            try {
                imageLoader.put(this.token, ImageIO.read(new File(this.token.getImagePath())));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return imageLoader.get(this.token);
    }

    /**
     * Initializes the properties of the TileObject's token if
     * it has not been done already.
     */
    private void initTokenProperties(){
        if(!tokenProperties.containsKey(this.token)){
            tokenProperties.put(this.token, new PropertySet());
        }
    }

    @Override
    public boolean hasProperty(Token property){
        if(property.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        return tokenProperties.get(this.token).hasProperty(property);
    }

    @Override
    public void setProperty(Boolean is, Token property){
        Objects.requireNonNull(property);
        if(property.getType() != tokenType.property){
            throw new IllegalArgumentException("Token has to be a property");
        }
        tokenProperties.get(this.token).setProperty(is, property);
    }
}
