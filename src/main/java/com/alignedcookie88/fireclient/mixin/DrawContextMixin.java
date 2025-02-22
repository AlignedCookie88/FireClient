package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.resources.TagDisplayData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow public abstract int drawText(TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow);

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderDecorations(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        List<Text> badge_lines = new ArrayList<>();
        int line_width = 0;
        MutableText current_line = Text.empty();
        for (TagDisplayData data : TagDisplayData.forStack(stack)) {
            data.parseData();
            line_width += textRenderer.getWidth(data.badgeText);
            if (line_width > 18) {
                line_width = textRenderer.getWidth(data.badgeText);
                badge_lines.add(current_line);
                current_line = Text.empty();
            }
            current_line.append(data.badgeText);
        }

        if (line_width > 0)
            badge_lines.add(current_line);

        int by = 0;
        for (Text line : badge_lines) {
            drawText(textRenderer, line, x-1, y-1+by, 0xFFFFFF, true);
            by += 11;
        }
    }
}
