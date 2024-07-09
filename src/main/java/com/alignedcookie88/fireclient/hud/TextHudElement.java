package com.alignedcookie88.fireclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextHudElement implements HudElement {

    float x;
    float y;
    int xo;
    int yo;
    Text text;

    String id;

    public TextHudElement(String id, float x, float y, int xo, int yo, Text text) {
        this.id = id;

        this.x = x;
        this.y = y;
        this.xo = xo;
        this.yo = yo;
        this.text = text;
    }

    private int getX(int width) {
        return (int) (x * width) + xo;
    }

    private int getY(int height) {
        return (int) (y * height) + yo;
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, float tickDelta, int width, int height) {
        context.drawText(textRenderer, text, getX(width), getY(height), 0xFFFFFF, true);
    }

    @Override
    public String getID() {
        return id;
    }
}
