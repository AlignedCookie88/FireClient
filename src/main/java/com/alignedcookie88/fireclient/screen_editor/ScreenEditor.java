package com.alignedcookie88.fireclient.screen_editor;

import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.UIUtility;
import com.alignedcookie88.fireclient.Utility;
import imgui.ImGui;
import imgui.type.ImInt;
import net.kyori.adventure.text.Component;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;
import org.python.antlr.ast.Str;
import xyz.breadloaf.imguimc.screen.ImGuiScreen;
import xyz.breadloaf.imguimc.screen.ImGuiWindow;
import xyz.breadloaf.imguimc.theme.ImGuiDarkTheme;

import java.util.*;

public class ScreenEditor extends ImGuiScreen {

    Integer swidth;

    Integer sheight;

    Identifier screenBg = Identifier.of("fireclient", "textures/gui/default_screen_bg.png");
    private List<Drawable> drawables = Lists.newArrayList();
    private final List<Element> children = com.google.common.collect.Lists.newArrayList();
    private final List<Selectable> selectables = com.google.common.collect.Lists.newArrayList();

    ImInt selectedWidget = new ImInt(0);

    String nextAction = null;

    public ScreenEditor() {
        super(Text.literal("Screen Editor"), false);
        swidth = 320;
        sheight = 180;
    }

    @Override
    protected List<ImGuiWindow> initImGui() {
        return new ArrayList<>(List.of(
                new ImGuiWindow(
                        new FireClientTheme(),
                        Text.literal("General properties"),
                        () -> {
                            ImGui.text("The screen editor is currently unfinished!");

                            ImInt width = new ImInt();
                            width.set(swidth);
                            ImGui.inputInt("Width", width, 2, 20);
                            swidth = clamp(width.get(), 10, 500);

                            ImInt height = new ImInt();
                            height.set(sheight);
                            ImGui.inputInt("Height", height, 2, 20);
                            sheight = clamp(height.get(), 10, 500);

                            clickableButton(Text.literal("Get template"), () -> {
                                Utility.sendStyledMessage("Clicked!");
                            });
                        },
                        false
                ),
                new ImGuiWindow(
                        new FireClientTheme(),
                        Text.literal("Widgets"),
                        () -> {
                            ImGui.text("Manage your widgets here.");

                            ImGui.listBox(
                                    "",
                                    selectedWidget,
                                    new String[]{"A", "B", "C"}
                            );
                            ImGui.newLine();
                            ImGui.sameLine();
                            clickableButton(Text.literal("Add"), () -> {
                                nextAction = "addWindow";
                            });
                            clickableButton(Text.literal("Delete"), () -> {

                            });
                            clickableButton(Text.literal("Settings"), () -> {

                            });
                        },
                        false
                )
        ));
    }

    public void openAddWindow() {
        pushWindow(new ImGuiWindow(
                new FireClientTheme(),
                Text.literal("Add widget"),
                () -> {
                    clickableButton(Text.literal("Add Button"), () -> {

                    });
                },
                true
        ));
    }

    private static final Map<String, Boolean> pressedBtns = new HashMap<>();

    private static void clickableButton(Text name, ButtonCallback callback) {
        boolean stored = pressedBtns.getOrDefault(name.getString(), false);
        boolean pressed = ImGui.button(name.getString());
        if (pressed && !stored) {
            callback.callback();
        }
        pressedBtns.put(name.getString(), pressed);
    }

    public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        if (Objects.equals(nextAction, "addWindow")) {
            openAddWindow();
        }
        nextAction = null;

        this.renderBackground(context, mouseX, mouseY, delta);

        UIUtility.drawNineSlicedTexture(context, screenBg, (width/2) - (swidth/2), (height/2) - (sheight/2), (width/2) + (swidth/2), (height/2) + (sheight/2));

        Iterator var5 = this.drawables.iterator();

        while(var5.hasNext()) {
            Drawable drawable = (Drawable)var5.next();
            drawable.render(context, mouseX, mouseY, delta);
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
}
