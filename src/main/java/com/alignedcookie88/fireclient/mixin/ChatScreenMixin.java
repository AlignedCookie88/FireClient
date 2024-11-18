package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.State;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!CommandQueue.isSafeToChat()) {
            Text message = Text.literal("Chatting now may result in a spamkick!");
            context.drawText(this.textRenderer, message, 3, height-24, 0xFF0000, true);
        }
    }

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfo ci) {
        if (State.plotCommandsForChat && !(chatText.startsWith("/") || chatText.startsWith("@"))) {
            CommandQueue.queuePlotCommand(chatText);
            MinecraftClient.getInstance().setScreen(null);
            ci.cancel();
            return;
        }

        String[] reset_messages = new String[] {"/spawn", "/s", "/leave", "/dev", "/build", "/code", "/mode dev", "/mode build"};
        if (Arrays.stream(reset_messages).anyMatch(chatText::equalsIgnoreCase)) {
            State.reset();
        } else if (chatText.startsWith("/join ")) {
            State.reset();
        }
        CommandQueue.playerChatted();
    }
}
