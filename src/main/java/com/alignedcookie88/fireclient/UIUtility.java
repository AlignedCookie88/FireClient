package com.alignedcookie88.fireclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class UIUtility {

    public static void drawNineSlicedTexture(DrawContext context, Identifier texture, int x1, int y1, int x2, int y2) {
        // Corners
        context.drawTexture(texture, x1, y1, 0, 0, 4, 4, 32, 32);
        context.drawTexture(texture, x2-4, y1, 28, 0, 4, 4, 32, 32);
        context.drawTexture(texture, x2-4, y2-4, 28, 28, 4, 4, 32, 32);
        context.drawTexture(texture, x1, y2-4, 0, 28, 4, 4, 32, 32);
        // Edges
        context.drawTexture(texture, x1+4, y1, x2-x1-8, 4, 4, 0, 24, 4, 32, 32);
        context.drawTexture(texture, x1+4, y2-4, x2-x1-8, 4, 4, 28, 24, 4, 32, 32);
        context.drawTexture(texture, x1, y1+4, 4, y2-y1-8, 0, 4, 4, 24, 32, 32);
        context.drawTexture(texture, x2-4, y1+4, 4, y2-y1-8, 28, 4, 4, 24, 32, 32);
        // Center
        context.drawTexture(texture, x1+4, y1+4, x2-x1-8, y2-y1-8, 4, 4, 24, 24, 32, 32);
    }

}
