package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.resources.TagDisplayData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow public abstract void drawText(TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow);

    @Shadow @Final private Matrix3x2fStack matrices;

    @Inject(method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;"))
    public void renderDecorations(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        matrices.pushMatrix();
        //matrices.translate(0.0, 0.0, 200.0);
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
        matrices.popMatrix();
    }
}
