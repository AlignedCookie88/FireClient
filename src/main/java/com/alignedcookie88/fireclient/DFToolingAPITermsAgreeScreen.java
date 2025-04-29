package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.task.TaskException;
import com.alignedcookie88.fireclient.task.TaskManager;
import com.alignedcookie88.fireclient.task.tasks.DFToolingApiTask;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class DFToolingAPITermsAgreeScreen extends Screen {

    private final DFToolingApiTask task;

    public DFToolingAPITermsAgreeScreen(DFToolingApiTask task) {
        super(Text.literal("DFTooling API"));
        this.task = task;
    }


    private void acceptAndRun() {
        Config.state.dfToolingApiAgreement = true;
        try {
            TaskManager.setCurrentTask(null);
            task.completed = false;
            TaskManager.setCurrentTask(task);
        } catch (TaskException.TaskCurrentlyActive ignored) {
            Utility.sendStyledMessage("Couldn't start the task after agreement as another has started running.");
        }
    }

    @Override
    protected void init() {

        addDrawableChild(new TextWidget(
                0,
                17,
                width,
                10,
                Text.literal("DFTooling API Terms Agreement"),
                textRenderer
        ));

        addDrawableChild(new ScrollableTextWidget(
                50,
                35,
                width-100,
                height-75,
                Utility.componentToText(MiniMessage.miniMessage().deserialize("""
                        <bold><c:#ff5a00>The action you are completing requires the use of the DFTooling API.</c></bold>
                        
                        
                        All requests to the DFTooling API are logged. Information such as the action you are performing, your IP address, your Minecraft username and UUID, and the precise timestamp you performed the action may be logged. This is solely for the purpose of protecting the API and other services from malicious activities. Logs older than 30 days are automatically deleted.
                        
                        In some cases, where public content is being posted, your Minecraft Username and UUID may be publicly visible and associated with your request and the data included with it.
                        
                        
                        If you wish to revoke your consent at any time, you can do so in the config menu accessed via <gray>/fireclient config</gray>. Please note this does not remove any content already uploaded via the API, and if you wish to request for this data to be removed please contact <gray>@alignedcookie88</gray> on discord. Any other concerns with held data and GDPR should also be directed there.
                        """)),
                textRenderer
        ));

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Cancel"),
                button -> this.close()
        ).dimensions((width/2)-152, height-30, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Agree and continue"),
                button -> {
                    this.close();
                    this.acceptAndRun();
                }
        ).dimensions((width/2)+2, height-30, 150, 20).build());

    }
}
