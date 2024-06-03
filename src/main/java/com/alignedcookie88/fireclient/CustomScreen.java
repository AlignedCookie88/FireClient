package com.alignedcookie88.fireclient;

import net.kyori.adventure.text.Component;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.Iterator;
import java.util.List;

public class CustomScreen extends Screen {

    Integer swidth;

    Integer sheight;

    Identifier screenBg;
    private List<Drawable> drawables = Lists.newArrayList();
    private final List<Element> children = com.google.common.collect.Lists.newArrayList();
    private final List<Selectable> selectables = com.google.common.collect.Lists.newArrayList();


    public CustomScreen(Text title, Integer width, Integer height, Identifier screenBg) {
        super(title);

        this.swidth = width;
        this.sheight = height;
        this.screenBg = screenBg;

        State.screen = this;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        drawNineSlicedTexture(context, screenBg, (width/2) - (swidth/2), (height/2) - (sheight/2), (width/2) + (swidth/2), (height/2) + (sheight/2));

        Iterator var5 = this.drawables.iterator();

        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(context, mouseX, mouseY, delta);
        }
    }

    private void drawNineSlicedTexture(DrawContext context, Identifier texture, int x1, int y1, int x2, int y2) {
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

    protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        super.addDrawableChild(drawableElement);
        this.drawables.add((Drawable)drawableElement);
        return this.addSelectableChild(drawableElement);
    }

    protected <T extends Drawable> T addDrawable(T drawable) {
        super.addDrawable(drawable);
        this.drawables.add(drawable);
        return drawable;
    }

    protected <T extends Element & Selectable> T addSelectableChild(T child) {
        super.addSelectableChild(child);
        this.children.add(child);
        this.selectables.add((Selectable)child);
        return child;
    }

    protected void remove(Element child) {
        super.remove(child);
        if (child instanceof Drawable) {
            this.drawables.remove((Drawable)child);
        }

        if (child instanceof Selectable) {
            this.selectables.remove((Selectable)child);
        }

        this.children.remove(child);
    }

    protected void clearChildren() {
        super.clearChildren();
        this.drawables.clear();
        this.children.clear();
        this.selectables.clear();
    }

    @Override
    public void close() {
        super.close();
        State.screen = null;
    }

    public int localifyX(int x) {
        return (width/2) - (swidth/2) + x;
    }

    public int localifyY(int y) {
        return (height/2) - (sheight/2) + y;
    }

    public void addButton(Component text, int x, int y, int bwidth, int bheight, String click_command) {
        int lx = localifyX(x);
        int ly = localifyY(y);
        this.addDrawableChild(ButtonWidget.builder(Utility.componentToText(text), (button) -> {
            Utility.runPlotCommand(click_command);
        }).dimensions(lx, ly, bwidth, bheight).build());
    }
}
