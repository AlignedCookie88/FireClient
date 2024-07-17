package com.alignedcookie88.fireclient.hud;

import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.FireClient;
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

    private float screenSafeArea(float area) {
        return area * Config.state.screenSafeArea;
    }

    private int screenSafeOffset(float actual) {
        float area = screenSafeArea(actual);
        return (int) ((actual-area)/2);
    }

    protected int getX(int screen_width, int element_width) {
        return (int) ((x * screenSafeArea(screen_width)) + xo - (x * element_width)) + screenSafeOffset(screen_width);
    }

    protected int getY(int screen_height, int element_height) {
        return (int) ((y * screenSafeArea(screen_height)) + yo - (y * element_height)) + screenSafeOffset(screen_height);
    }

    @Override
    public abstract void render(DrawContext context, TextRenderer renderer, float tickDelta, int width, int height);

    @Override
    public String getID() {
        return id;
    }
}
