package com.alignedcookie88.fireclient.add_nodes_screen;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddNodesScreen extends Screen {

    protected record Icon(Item item, int x, int y) {
        public void render(DrawContext context) {
            if (item == Items.AIR)
                return;
            context.drawItem(new ItemStack(item), x + 2, y + 2);
        }
    }

    protected final List<Icon> icons = new ArrayList<>();


    protected MultiplayerScreen parent;

    public AddNodesScreen(MultiplayerScreen parent) {
        super(Text.literal("Add Nodes"));
        this.parent = parent;
    }

    private final String[] node_names = new String[] {
            "Origins",
            "Arcade",
            "Fortress",
            "Valley",
            "Tropics",
            "Canyon",
            "Blossom"
    };

    @Override
    protected void init() {
        icons.clear();

        int y_change = 23;

        int button_count = 10;

        int x = (width - 200)/2;
        int y = (height - (button_count * y_change) - 3)/2;

        addButton("Add Nodes 1-7", button -> {
            addNumericalNode(1);
            addNumericalNode(2);
            addNumericalNode(3);
            addNumericalNode(4);
            addNumericalNode(5);
            addNumericalNode(6);
            addNumericalNode(7);
        }, x, y, Items.NETHER_STAR);

        y += y_change;
        addNumericalButton(1, x, y, Items.DIAMOND_BLOCK);

        y += y_change;
        addNumericalButton(2, x, y, Items.REDSTONE_BLOCK);

        y += y_change;
        addNumericalButton(3, x, y, Items.POLISHED_ANDESITE);

        y += y_change;
        addNumericalButton(4, x, y, Items.GRASS_BLOCK);

        y += y_change;
        addNumericalButton(5, x, y, Items.SAND);

        y += y_change;
        addNumericalButton(6, x, y, Items.RED_SAND);

        y += y_change;
        addNumericalButton(7, x, y, Items.PINK_TERRACOTTA);

        y += y_change;
        addButton("Add Node Beta - Project", button -> {
            addNode("Node Beta - Project", "beta");
        }, x, y, Items.STRUCTURE_BLOCK);

        y += y_change;
        addButton("Other & Staff Nodes", button -> {
            MinecraftClient.getInstance().setScreen(new AddOtherNodesScreen(parent));
        }, x, y, Items.BLAZE_POWDER);
    }

    protected void addNumericalButton(int number, int x, int y, Item icon) {
        addButton("Add Node %s - %s".formatted(number, node_names[number - 1]), button -> addNumericalNode(number), x, y, icon);
    }

    protected void addButton(String name, ButtonWidget.PressAction action, int x, int y) {
        addDrawableChild(ButtonWidget.builder(Text.literal(name), action).dimensions(x, y, 200, 20).build());
    }

    protected void addDisabledButton(String name, int x, int y, String tooltip, Item icon) {
        ButtonWidget button = ButtonWidget.builder(Text.literal(name), button2 -> {}).dimensions(x, y, 200, 20).tooltip(Tooltip.of(Text.literal(tooltip))).build();
        button.active = false;
        addDrawableChild(button);
        icons.add(new Icon(icon, x, y));
    }

    protected void addButton(String name, ButtonWidget.PressAction action, int x, int y, Item icon) {
        addButton(name, action, x, y);
        icons.add(new Icon(icon, x, y));
    }

    protected void addNumericalNode(int number) {
        addNode("Node %s - %s".formatted(number, node_names[number - 1]), "node%s".formatted(number));
    }

    protected void addNode(String display, String id) {
        addNode(display, id, null);
    }

    protected void addNode(String display, String id, String version) {
        addNode(display, id, version, version != null);
    }

    protected void addNode(String display, String id, String version, boolean viafabric_show_confirmation) {

        if (viafabric_show_confirmation) {
            MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                    accepted -> {
                        if (accepted) {
                            addNode(display, id, version, false);
                        } else {
                            close();
                        }
                    },
                    Text.literal("Node uses Minecraft "+version),
                    Text.literal("The node you are adding uses Minecraft version %s.\nYou will need to set the version for the server to %s using either ViaFabric or ViaFabricPlus to join the node.".formatted(version, version)),
                    Text.literal("I'll do that"),
                    Text.literal("Nevermind")
            ));
            return;
        }

        ServerList serverList = parent.getServerList();

        String ip = "%s.mcdiamondfire.com".formatted(id);

        FireClient.LOGGER.info("Adding node \"{}\" ({}).", display, id);

        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo info = serverList.get(i);
            if (Objects.equals(info.address, ip)) {
                FireClient.LOGGER.info("Server already added.");
                close();
                return;
            }
        }

        ServerInfo info = new ServerInfo(
                "DiamondFire (%s)".formatted(display),
                ip,
                ServerInfo.ServerType.OTHER
        );

        info.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);

        serverList.add(info, false);

        serverList.saveFile();

        close();
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(new MultiplayerScreen(new TitleScreen()));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for (Icon icon : icons) {
            icon.render(context);
        }
    }
}
