package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.integration.CodeClientIntegration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {

    @Redirect(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;I)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 0))
    public <T extends Widget> T createButton(GridWidget.Adder instance, T widget, int occupiedColumns) {
        ButtonWidget button = (ButtonWidget) widget;

        if (button.getMessage() == ScreenTexts.DISCONNECT && FireClient.isCodeClientIntegrationEnabled() && Config.state.showLeavePlotInGameMenu) {
            if (CodeClientIntegration.onPlot()) {
                ButtonWidget leave = ButtonWidget.builder(Text.literal("Leave Plot"), button1 -> {
                    CommandQueue.queueCommand("leave");
                    MinecraftClient.getInstance().setScreen(null);
                }).width(98).build();

                button.setWidth(98);

                instance.add((T) leave, 1);
                return instance.add((T) button, 1);
            }
        }

        return instance.add(widget, occupiedColumns);
    }

}
