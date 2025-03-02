package com.alignedcookie88.fireclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextHudElement extends PositionalHudElement {

    final Text text;

    public TextHudElement(String id, float x, float y, int xo, int yo, Text text) {
        super(id, x, y, xo, yo);

        this.text = text;
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, float tickDelta, int width, int height) {
        int text_width = textRenderer.getWidth(text);
        context.drawText(textRenderer, text, getX(width, text_width), getY(height, 10), 0xFFFFFF, true);
    }
}
