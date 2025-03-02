package com.alignedcookie88.fireclient.add_nodes_screen;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.item.Items;

public class AddOtherNodesScreen extends AddNodesScreen {

    public AddOtherNodesScreen(MultiplayerScreen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        icons.clear();

        int y_change = 23;

        int button_count = 8;

        int x = (width - 200)/2;
        int y = (height - (button_count * y_change) - 3)/2;

        addButton("Add Events Node", button -> addNode("Events", "event"), x, y, Items.DEEPSLATE_GOLD_ORE);

        y += y_change;
        addButton("Add Events Dev Node", button -> addNode("Events Dev", "dev-events"), x, y, Items.DEEPSLATE_REDSTONE_ORE);

        y += y_change;
        if (FabricLoader.getInstance().isModLoaded("viafabric") || FabricLoader.getInstance().isModLoaded("viafabricplus")) {
            addButton("Add DiamondFire Education", button -> addNode("Education", "edu", "1.16.5"), x, y, Items.BOOKSHELF);
        } else {
            addDisabledButton("Add DiamondFire Education", x, y, "DiamondFire Education is only compatible with 1.16.5. Please switch to 1.16.5 and use the IP edu.mcdiamondfire.com or install either ViaFabric or ViaFabricPlus to join on this version.", Items.BOOKSHELF);
        }

        y += y_change;
        addButton("Add Dev 1 - Parliament", button -> addNode("Dev 1 - Parliament", "dev"), x, y, Items.COMMAND_BLOCK);

        y += y_change;
        addButton("Add Dev 2 - Palace", button -> addNumericalDevNode(2, "Palace"), x, y, Items.CHAIN_COMMAND_BLOCK);

        y += y_change;
        addButton("Add Dev 3 - Forest", button -> addNumericalDevNode(3, "Forest"), x, y, Items.REPEATING_COMMAND_BLOCK);

        y += y_change;
        addButton("Add Build Node", button -> addNode("Build", "build"), x, y, Items.STRUCTURE_BLOCK);

        y += y_change;
        addButton("Back", button -> this.client.setScreen(new AddNodesScreen(parent)), x, y, Items.ARROW);

    }

    protected void addNumericalDevNode(int number, String name) {
        addNode("Dev %s - %s".formatted(number, name), "dev%s".formatted(number));
    }
}
