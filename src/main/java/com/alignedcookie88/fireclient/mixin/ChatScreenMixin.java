package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.State;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "sendMessage", at = @At("HEAD"))
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        String[] reset_messages = new String[] {"/spawn", "/s", "/leave"};
        if (Arrays.stream(reset_messages).anyMatch(chatText::equalsIgnoreCase)) {
            State.reset();
        }
    }
}
