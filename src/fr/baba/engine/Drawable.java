package fr.baba.engine;

import java.awt.*;

public interface Drawable {
    /**
     * Draws the object to fit the rectangle located at
     * (topLeftX, topLeftY), with the specified dimensions.
     * @param graphics graphics in which the object will be drawn
     * @param topLeftX top left x coordinate of the drawing
     * @param topLeftY top left y coordinate of the drawing
     * @param heightSize height of the drawing
     * @param widthSize width of the drawing
     */
    void draw(Graphics2D graphics, int topLeftX, int topLeftY, int heightSize, int widthSize);
}
