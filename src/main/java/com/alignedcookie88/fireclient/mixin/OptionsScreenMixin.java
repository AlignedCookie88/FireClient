package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin {

    @Shadow protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 9))
    public <T extends Widget> T init(GridWidget.Adder instance, T widget) {

        T added = instance.add(widget);

        instance.add(
                this.createButton(
                        Text.literal("FireClient"),
                        () -> Config.getConfig((Screen) (Object) this)
                )
        );

        return added;
    }

}
