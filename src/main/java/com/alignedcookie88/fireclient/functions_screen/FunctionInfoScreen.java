package com.alignedcookie88.fireclient.functions_screen;

import com.alignedcookie88.fireclient.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FunctionInfoScreen extends Screen {

    private List<Drawable> drawables = Lists.newArrayList();
    private final List<Element> children = com.google.common.collect.Lists.newArrayList();
    private final List<Selectable> selectables = com.google.common.collect.Lists.newArrayList();

    FireFunction function;

    int sheight;

    List<Text> argText = new ArrayList<>();

    protected FunctionInfoScreen(FireFunction function) {
        super(Text.literal("Function Info - " + function.getName()));
        sheight = 55;
        this.function = function;
        for (FireArgument arg : function.getExpectedArguments()) {
            sheight += 11;
            Text argId = Text.literal(" - ").append(arg.getID()).formatted(Formatting.DARK_GRAY);
            argText.add(arg.getName().copy().append(argId));
        }
    }

    @Override
    protected void init() {
        int x1 = (width/2)-150;
        int x2 = (width/2)+150;
        int y1 = (height-sheight)/2;
        int y2 = (height+sheight)/2;
        int pane_width = x2-x1;
        int pane_height = y2-y1;

        addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            MinecraftClient.getInstance().setScreen(new FunctionsScreen());
        }).dimensions(x1+4, y2-24, ((pane_width-8)/2)-1, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Get Function"), button -> {
            Utility.giveItem(FireFunctionSerialiser.serialiseFunction(function));
            MinecraftClient.getInstance().setScreen(null);
        }).dimensions(x1+6+((pane_width-8)/2), y2-24, ((pane_width-8)/2)-2, 20).build());

        String wiki = function.getWikiLink();
        if (wiki != null) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Open Wiki Article"), button -> {
                ConfirmLinkScreen.open(this, wiki, true);
            }).dimensions(x1, y2+10, pane_width, 20).build());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int x1 = (width/2)-150;
        int x2 = (width/2)+150;
        int y1 = (height-sheight)/2;
        int y2 = (height+sheight)/2;
        int pane_width = x2-x1;
        int pane_height = y2-y1;

        UIUtility.drawNineSlicedTexture(context, Identifier.of("fireclient", "textures/gui/default_screen_bg.png"), x1, y1, x2, y2);

        Iterator var5 = this.drawables.iterator();

        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(context, mouseX, mouseY, delta);
        }

        Text title = Text.literal(function.getName()).formatted(Formatting.DARK_GRAY);
        int title_width = this.textRenderer.getWidth(title);
        context.drawText(textRenderer, title, x1 + ((pane_width-title_width)/2), y1+10, 0xFFFFFF, false);

        Text description = Text.literal(function.getDescription()).formatted(Formatting.DARK_GRAY);
        context.drawText(textRenderer, description, x1 + 4, y1+20, 0xFFFFFF, false);

        int y = 31;
        for (Text arg : argText) {
            context.drawText(textRenderer, arg, x1 + 9, y1+y, 0xFFFFFF, false);
            y += 11;
        }
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
}
