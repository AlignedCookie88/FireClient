package com.alignedcookie88.fireclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class PositionalHudElement implements HudElement {

    String id;

    float x;
    float y;

    int xo;
    int yo;

    protected PositionalHudElement(String id, float x, float y, int xo, int yo) {
        this.id = id;

        this.x = x;
        this.y = y;

        this.xo = xo;
        this.yo = yo;
    }

    protected int getX(int screen_width, int element_width) {
        return (int) ((x * screen_width) + xo + (x * element_width));
    }

    protected int getY(int screen_height, int element_height) {
        return (int) ((y * screen_height) + yo + (y * element_height));
    }

    @Override
    public abstract void render(DrawContext context, TextRenderer renderer, float tickDelta, int width, int height);

    @Override
    public String getID() {
        return id;
    }
}
