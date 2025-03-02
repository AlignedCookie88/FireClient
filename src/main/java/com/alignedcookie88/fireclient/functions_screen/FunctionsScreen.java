package com.alignedcookie88.fireclient.functions_screen;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.FireFunctionSerialiser;
import com.alignedcookie88.fireclient.Utility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;


public class FunctionsScreen extends Screen {
    public FunctionsScreen() {
        super(Text.literal("Functions"));
    }

    ButtonWidget a;

    ButtonWidget b;

    FunctionsList list;

    @Override
    protected void init() {
        list = new FunctionsList(MinecraftClient.getInstance(), width, height-50, 25, 35, textRenderer);
        addDrawableChild(list);
        for (FireFunction function : FireClient.functionRegistry) {
            if (!function.hidden())
                list.addFunction(function);
        }

        a = ButtonWidget.builder(Text.literal("Get Function"), button -> {
            FireFunction selected = list.selected();
            ItemStack stack = FireFunctionSerialiser.serialiseFunction(selected);
            Utility.giveItem(stack);
            MinecraftClient.getInstance().setScreen(null);
        }).dimensions((width/2)-125, height-23, 124, 20).build();
        a.active = false;
        addDrawableChild(a);


        b = ButtonWidget.builder(Text.literal("Info"), button -> MinecraftClient.getInstance().setScreen(new FunctionInfoScreen(list.selected()))).dimensions((width/2)+2, height-23, 123, 20).build();
        b.active = false;
        addDrawableChild(b);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (Utility.isCreative()) {
            super.render(context, mouseX, mouseY, delta);
            Text title = Text.literal("Functions");
            int title_width = this.textRenderer.getWidth(title);
            context.drawText(this.textRenderer, title, (width - title_width)/2, 7, 0xFFFFFF, true);
        } else {
            this.renderBackground(context, mouseX, mouseY, delta);
            Text text = Text.literal("You must be in creative mode to use this menu.");
            int text_width = this.textRenderer.getWidth(text);
            context.drawText(this.textRenderer, text, (width - text_width)/2, (height-5)
                    /2, 0xFFFFFF, true);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Utility.isCreative()) {
            boolean f = super.mouseClicked(mouseX, mouseY, button);
            boolean active = this.list.selected() != null;
            a.active = active;
            b.active = active;
            return f;
        }
        return false;
    }
}
