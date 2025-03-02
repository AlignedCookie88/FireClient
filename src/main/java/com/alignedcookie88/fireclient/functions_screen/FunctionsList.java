package com.alignedcookie88.fireclient.functions_screen;

import com.alignedcookie88.fireclient.FireFunction;
import com.alignedcookie88.fireclient.UIUtility;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FunctionsList extends EntryListWidget<FunctionsList.FunctionEntry> {

    final TextRenderer textRenderer;

    FunctionEntry selected2;

    public FunctionsList(MinecraftClient client, int width, int height, int y, int itemHeight, TextRenderer textRenderer) {
        super(client, width, height, y, itemHeight);
        this.textRenderer = textRenderer;
        this.selected2 = null;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    public void addFunction(FireFunction function) {
        addEntry(new FunctionEntry(function, textRenderer, this));
    }

    public FireFunction selected() {
        if (selected2 == null) {
            return null;
        }
        return selected2.function;
    }


    public static class FunctionEntry extends EntryListWidget.Entry<FunctionEntry> {

        final FireFunction function;

        final TextRenderer textRenderer;

        final FunctionsList parent;

        private FunctionEntry(FireFunction function, TextRenderer textRenderer, FunctionsList parent) {
            this.function = function;
            this.textRenderer = textRenderer;
            this.parent = parent;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            // Draw background
            UIUtility.drawNineSlicedTexture(context, Identifier.of("fireclient", "textures/gui/default_screen_bg.png"), x, y, x+entryWidth, y+entryHeight);
            // Draw name
            int argument_count = function.getExpectedArguments().size();
            MutableText text = Text.literal(function.getName()).append(" - ").append(Integer.toString(argument_count));
            if (argument_count == 1) {
                text = text.append(" arg");
            } else {
                text = text.append(" args");
            }
            context.drawText(textRenderer, text.formatted(Formatting.DARK_GRAY), x+5, y+5, 0xFFFFFF, false);
            // Draw description
            context.drawText(textRenderer, Text.literal(function.getDescription()).formatted(Formatting.DARK_GRAY), x+5, y+16, 0xFFFFFF, false);
            // Draw selected text
            Text selectedText = Text.empty();
            if (parent.selected2 == this) {
                selectedText = Text.literal("Selected").formatted(Formatting.DARK_GREEN);
            } else if (this.isMouseOver(mouseX, mouseY)) {
                selectedText = Text.literal("Click to select").formatted(Formatting.DARK_AQUA);
            }
            int selectedTextWidth = textRenderer.getWidth(selectedText);
            context.drawText(textRenderer, selectedText, x+entryWidth-4-selectedTextWidth, y+5, 0xFFFFFF, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            parent.selected2 = this;
            return false;
        }
    }
}
