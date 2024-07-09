package com.alignedcookie88.fireclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public interface HudElement {

    void render(DrawContext context, TextRenderer renderer, float tickDelta, int width, int height);

    String getID();

}
