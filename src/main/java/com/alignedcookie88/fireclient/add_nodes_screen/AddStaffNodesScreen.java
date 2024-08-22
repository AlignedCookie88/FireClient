package com.alignedcookie88.fireclient.add_nodes_screen;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.item.Items;

public class AddStaffNodesScreen extends AddNodesScreen {

    public AddStaffNodesScreen(MultiplayerScreen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        icons.clear();

        int y_change = 23;

        int button_count = 4;

        int x = (width - 200)/2;
        int y = (height - (button_count * y_change) - 3)/2;

        addButton("Add Dev 1 - Parliament", button -> {
            addNode("Dev 1 - Parliament", "dev");
        }, x, y, Items.COMMAND_BLOCK);

        y += y_change;
        addButton("Add Dev 2 - Palace", button -> {
            addNumericalDevNode(2, "Palace");
        }, x, y, Items.CHAIN_COMMAND_BLOCK);

        y += y_change;
        addDisabledButton("Add Dev 3 - Forest", x, y, "Dev 3 cannot be joined through a direct IP. You must run /server dev3 in-game. (devs pls fix)", Items.REPEATING_COMMAND_BLOCK);

        y += y_change;
        addButton("Back", button -> {
            this.client.setScreen(new AddNodesScreen(parent));
        }, x, y, Items.ARROW);

    }

    protected void addNumericalDevNode(int number, String name) {
        addNode("Dev %s - %s".formatted(number, name), "dev%s".formatted(number));
    }
}
