package com.alignedcookie88.fireclient;

import net.kyori.adventure.text.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomScreen extends Screen {

    Integer swidth;

    Integer sheight;

    Identifier screenBg;
    private List<Drawable> drawables = Lists.newArrayList();
    private final List<Element> children = com.google.common.collect.Lists.newArrayList();
    private final List<Selectable> selectables = com.google.common.collect.Lists.newArrayList();

    private ScreenHandler screenHandler = null;
    private HandledScreen<?> handledScreen = null;
    private List<CustomScreenSlot> slots = new ArrayList<>();

    private record CustomScreenSlot(Slot slot, int x, int y, boolean interactable, boolean background, ScreenHandler handler, HandledScreen<?> handledScreen, MinecraftClient client) {

        private boolean hovering(int mouseX, int mouseY) {
            return mouseX >= x && mouseY >= y && mouseX < x+16 && mouseY < y+16;
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta, TextRenderer textRenderer) {

            ItemStack stack = slot.getStack();

            if (background)
                context.drawTexture(Identifier.of("fireclient", "textures/gui/slot.png"), x-1, y-1, 0, 0, 18, 18, 18, 18);

            if (stack.isEmpty())
                return;

            context.drawItem(stack, x, y);

            context.drawItemInSlot(textRenderer, stack, x, y);

            if (hovering(mouseX, mouseY))
                context.drawItemTooltip(textRenderer, stack, mouseX, mouseY);

        }

        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (!hovering(mouseX, mouseY))
                return false;

            if (!interactable)
                return false;

            FireClient.LOGGER.info("Clicked.");
            clickSlot(button, SlotActionType.PICKUP);

            return true;
        }

        private void clickSlot(int button, SlotActionType actionType) {
            client.interactionManager.clickSlot(this.handler.syncId, slot.id, button, actionType, client.player);
        }

    }


    public CustomScreen(Text title, Integer width, Integer height, Identifier screenBg) {
        super(title);

        this.swidth = width;
        this.sheight = height;
        this.screenBg = screenBg;

        State.screen = this;
        getScreenHandlerFromCurrentScreenIfAvailable();

        // Manually open it to not de-activate the other screen
        MinecraftClient client = MinecraftClient.getInstance();
        client.currentScreen = this;
        onDisplayed();
        client.mouse.unlockCursor();
        KeyBinding.unpressAll();
        init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
    }

    public void getScreenHandlerFromCurrentScreenIfAvailable() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen == null)
            return;

        if (screen instanceof HandledScreen<?> handledScreen) {
            screenHandler = handledScreen.getScreenHandler();
            this.handledScreen = handledScreen;
            FireClient.LOGGER.info("Got screen handler!");
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        UIUtility.drawNineSlicedTexture(context, screenBg, (width/2) - (swidth/2), (height/2) - (sheight/2), (width/2) + (swidth/2), (height/2) + (sheight/2));

        Iterator var5 = this.drawables.iterator();

        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(context, mouseX, mouseY, delta);
        }

        for (CustomScreenSlot slot : slots) {
            slot.render(context, mouseX, mouseY, delta, textRenderer);
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

    public void addText(Component text, int x, int y) {
        int lx = localifyX(x);
        int ly = localifyY(y);
        Text text1 = Utility.componentToText(text);
        int width = this.textRenderer.getWidth(text1);
        this.addDrawableChild(new TextWidget(lx, ly, width, 10, text1, this.textRenderer));
    }

    public void addSlot(int id, int x, int y, boolean interactable, boolean background) {
        if (screenHandler == null) {
            Utility.sendStyledMessage("You must open a menu, then open a custom screen to use slots.");
            return;
        }

        Slot slot = screenHandler.getSlot(id);
        if (slot == null) {
            Utility.sendStyledMessage("That slot is out of range. Maybe make a bigger menu.");
            return;
        }

        slots.add(new CustomScreenSlot(slot, localifyX(x), localifyY(y), interactable, background, screenHandler, handledScreen, client));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        for (CustomScreenSlot slot : slots) {
            if (slot.mouseClicked((int) mouseX, (int) mouseY, button))
                return true;
        }

        return true;
    }
}
