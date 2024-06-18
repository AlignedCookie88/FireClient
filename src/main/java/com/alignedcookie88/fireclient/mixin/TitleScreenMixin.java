package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.screen_editor.ScreenEditor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
//        this.addDrawableChild(ButtonWidget.builder(Text.literal("Screen Editor"), (button) -> {
//            this.client.setScreen(new ScreenEditor());
//        }).dimensions(3, 3, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("FireClient Options"), (button) -> {
            this.client.setScreen(Config.getConfig(this));
        }).dimensions(3, /*26*/3, 100, 20).build());
    }

}
