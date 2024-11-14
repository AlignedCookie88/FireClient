package com.alignedcookie88.fireclient.hud;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BarHudElement extends PositionalHudElement {

    int width;
    float progress;
    int colour;

    int last_lerp;

    private static final Identifier[] BACKGROUND_TEXTURES = new Identifier[]{Identifier.of("textures/gui/sprites/boss_bar/pink_background.png"), Identifier.of("textures/gui/sprites/boss_bar/blue_background.png"), Identifier.of("textures/gui/sprites/boss_bar/red_background.png"), Identifier.of("textures/gui/sprites/boss_bar/green_background.png"), Identifier.of("textures/gui/sprites/boss_bar/yellow_background.png"), Identifier.of("textures/gui/sprites/boss_bar/purple_background.png"), Identifier.of("textures/gui/sprites/boss_bar/white_background.png")};
    private static final Identifier[] PROGRESS_TEXTURES = new Identifier[]{Identifier.of("textures/gui/sprites/boss_bar/pink_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/blue_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/red_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/green_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/yellow_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/purple_progress.png"), Identifier.of("textures/gui/sprites/boss_bar/white_progress.png")};

    public BarHudElement(String id, float x, float y, int xo, int yo, int width, float progress, int colour) {
        super(id, x, y, xo, yo);

        this.width = width;
        last_lerp = width;
        this.progress = progress;
        this.colour = Math.max(0, Math.min(6, colour));
    }

    @Override
    public void render(DrawContext context, TextRenderer renderer, float tickDelta, int width, int height) {
        // Get position
        int x = getX(width, this.width);
        int y = getY(height, 5);

        // Draw background
        drawBar(context, BACKGROUND_TEXTURES[colour], x, y, this.width, this.width);

        // Draw progress
        last_lerp = MathHelper.lerpPositive(tickDelta, last_lerp, (int) (this.width * progress));
        drawBar(context, PROGRESS_TEXTURES[colour], x, y, this.width, last_lerp);
    }

    private void drawBar(DrawContext context, Identifier texture, int x, int y, int width, int cutoff) {
        drawBarSection(context, texture, x, y, cutoff, 0, 3, 0, 3);
        drawBarSection(context, texture, x, y, cutoff, 4, 177, 4, width-4);
        drawBarSection(context, texture, x, y, cutoff, 178, 182, width-3, width);
    }

    private void drawBarSection(DrawContext context, Identifier texture, int x, int y, int cutoff, int tx_start, int tx_end, int sc_start, int sc_end) {
        if (cutoff < sc_start)
            return; // Don't draw if the cutoff is before the start of this section

        int sc_x = x + sc_start;
        int sc_x2 = Math.min(x + sc_end, x + cutoff);

        int ct_width = cutoff-tx_start;
        if (ct_width < 0)
            ct_width = 10000000;

        int rg_width = Math.min(tx_end-tx_start, ct_width);

        context.drawTexture(texture, sc_x, y, sc_x2-sc_x + 1, 5, tx_start, 0, rg_width, 5, 182, 5);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
}
