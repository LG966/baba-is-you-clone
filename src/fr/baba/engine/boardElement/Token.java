package fr.baba.engine.boardElement;

import java.util.Objects;


/**
 * Inventory of all available tokens.
 * Feel free to add some.
 */
public enum Token {

    You("img/YOU/Prop_YOU.gif", tokenType.property),
    Win("img/WIN/Prop_WIN.gif", tokenType.property),
    Stop("img/STOP/Prop_STOP.gif", tokenType.property),
    Push("img/PUSH/Prop_PUSH.gif", tokenType.property),
    Melt("img/MELT/Prop_MELT.gif", tokenType.property),
    Hot("img/HOT/Prop_HOT.gif", tokenType.property),
    Defeat("img/DEFEAT/Prop_DEFEAT.gif", tokenType.property),
    Sink("img/SINK/Prop_SINK.gif", tokenType.property),
    Boom("img/BOOM/Prop_BOOM.png", tokenType.property),


    Is("img/IS/Text_IS_0.gif", tokenType.operator),

    sprBaba("img/BABA/BABA_0.gif", tokenType.sprite),
    sprFlag("img/FLAG/FLAG_0.gif", tokenType.sprite),
    sprWall("img/WALL/WALL_0.gif", tokenType.sprite),
    sprWater("img/WATER/WATER_0.gif", tokenType.sprite),
    sprSkull("img/SKULL/SKULL_0.gif", tokenType.sprite),
    sprLava("img/LAVA/LAVA_0.gif", tokenType.sprite),
    sprRock("img/ROCK/ROCK_0.gif", tokenType.sprite),
    sprBomb("img/BOMB/BOMB_0.gif", tokenType.sprite),
    sprTile("img/TILE/TILE_0.gif", tokenType.sprite),
    sprGrass("img/GRASS/GRASS_0.gif", tokenType.sprite),
    sprFlower("img/FLOWER/FLOWER_0.gif", tokenType.sprite),
    sprBrick("img/BRICK/BRICK_0.gif", tokenType.sprite),

    nounBaba("img/BABA/Text_BABA_0.gif", tokenType.noun, sprBaba),
    nounFlag("img/FLAG/Text_FLAG_0.gif", tokenType.noun, sprFlag),
    nounWall("img/WALL/Text_WALL_0.gif", tokenType.noun, sprWall),
    nounWater("img/WATER/Text_WATER_0.gif", tokenType.noun, sprWater),
    nounSkull("img/SKULL/Text_SKULL_0.gif", tokenType.noun, sprSkull),
    nounLava("img/LAVA/Text_LAVA_0.gif", tokenType.noun, sprLava),
    nounRock("img/ROCK/Text_ROCK_0.gif", tokenType.noun, sprRock),
    nounBomb("img/BOMB/Text_BOMB_0.png", tokenType.noun, sprBomb);

    private final String imagePath;
    private final tokenType type;
    private Token rep; // This attribute is exclusive to nouns and designate a sprite

    /**
     * Use this constructor for every token except those of type noun
     * @param imagePath path of the image which is going to be displayed if the token is on the board
     * @param type type of the token
     */
    Token(String imagePath, tokenType type){
        this.imagePath = Objects.requireNonNull(imagePath);
        this.type = Objects.requireNonNull(type);
    }

    /**
     * This constructor is exclusive to nouns
     * @param imagePath path of the image which is going to be displayed if the token is on the board
     * @param type type of the token (=Noun)
     * @param rep the sprite token designated by the noun token
     */
    Token(String imagePath, tokenType type, Token rep){
        this(imagePath, tokenType.noun);
        Objects.requireNonNull(rep);
        if(type != tokenType.noun) {
            throw new ExceptionInInitializerError("Only nouns have a matching sprite");
        }
        if(rep.type != tokenType.sprite){
            throw new ExceptionInInitializerError("Nouns can only match sprites");
        }
        this.rep = rep;
    }

    /**
     * @return ImagePath of the token
     */
    String getImagePath(){
        return this.imagePath;
    }

    /**
     * @return type of the token
     */
    public tokenType getType(){ return this.type;}

    /**
     * This function is only to be called on token of type noun !
     * @return the sprite token designated by the caller
     * (e.g. nounBaba.getType() gives sprBaba)
     */
    public Token getRep(){
        if(this.type != tokenType.noun){
            throw new IllegalCallerException("Only tokens of type noun have a rep");
        }
        return this.rep;
    }
}
