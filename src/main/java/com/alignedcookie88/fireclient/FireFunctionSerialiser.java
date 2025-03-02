package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.serialisation.DFArgument;
import com.alignedcookie88.fireclient.serialisation.DFBlock;
import com.alignedcookie88.fireclient.serialisation.DFTemplate;
import com.alignedcookie88.fireclient.serialisation.blocks.FunctionBlock;
import com.alignedcookie88.fireclient.serialisation.blocks.PlayerActionBlock;
import com.alignedcookie88.fireclient.serialisation.variables.FunctionParameterVariable;
import com.alignedcookie88.fireclient.serialisation.variables.ItemVariable;
import com.alignedcookie88.fireclient.serialisation.variables.StringVariable;
import com.alignedcookie88.fireclient.serialisation.variables.Variable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FireFunctionSerialiser {

    public static final String ARG_DENOTION = "\uF8F8";

    public static ItemStack serialiseFunction(FireFunction function) {
        DFTemplate template = new DFTemplate();

        // Function block
        FunctionBlock functionBlock = new FunctionBlock("fireclient."+function.getID());
        addArguments(functionBlock, function);
        template.push(functionBlock);

        // Push all the formatting blocks
        for (FireArgument argument : function.getExpectedArguments()) {
            Variable var = new Variable(argument.getID(), "line");
            for (DFBlock block : argument.getFormattingBlocks(var)) {
                template.push(block);
            }
        }

        // Send packet over actionbar
        PlayerActionBlock playerActionBlock = new PlayerActionBlock("ActionBar");
        playerActionBlock.push_argument(
                new DFArgument(0, new StringVariable(
                        getArgumentString(function)
                ))
        );
        template.push(playerActionBlock);

        // Clear actionbar, we don't want players without the mod seeing the messages
        PlayerActionBlock clearActionbar = new PlayerActionBlock("ActionBar");
        clearActionbar.push_argument(
                new DFArgument(0, new StringVariable(""))
        );
        template.push(clearActionbar);

//        return template.serialiseToItemStack(Items.CAMPFIRE, Text.literal("[").formatted(Formatting.DARK_GRAY)
//                .append(Text.literal("FireClient").withColor(0xFF5A00))
//                .append(Text.literal("] ").formatted(Formatting.DARK_GRAY))
//                .append(Text.literal(function.getName()).withColor(0xFF5A00))
//        );

        return template.serialiseToItemStack(Items.CAMPFIRE, Utility.componentToText(MiniMessage.miniMessage().deserialize(
                "<!italic><dark_grey>[<gradient:#FF5A00:#FFA500>FireClient<dark_grey>]:<reset><!italic> "+function.getName()
        )));
    }

    private static String getArgumentString(FireFunction function) {
        StringBuilder string = new StringBuilder(".fireclient " + FireClient.functionRegistry.getId(function).asString());

        for (FireArgument argument : function.getExpectedArguments()) {
            string.append(" ").append(ARG_DENOTION).append("%var(").append(argument.getID()).append(")").append(ARG_DENOTION);
        }

        return string.toString();
    }

    private static ItemStack createIconStack(FireFunction function) {
        ItemStack stack = new ItemStack(Items.CAMPFIRE);

        // Item name
        stack.set(
                DataComponentTypes.CUSTOM_NAME,
                Utility.miniMessage("<!italic><dark_gray>[<gradient:#ff5a00:#ffa500>FireClient<dark_gray>] <white>"+function.getName())
        );

        // Item lore
        LoreComponent lore = new LoreComponent(List.of())
                .with(Text.empty()) // Buffer line
                .with(Text.literal(function.getDescription()).formatted(Formatting.GRAY).styled(style -> style.withItalic(false))) // Description
                .with(Text.empty()) // Buffer line
                .with(Text.literal("Chest Parameters (FireClient Type Corrected)").styled(style -> style.withFormatting(Formatting.WHITE).withItalic(false).withUnderline(true))); // Chest params headers

        // Chest params
        for (FireArgument argument : function.getExpectedArguments()) {
            lore = lore.with(
                    argument.getName().copy().append(
                            Text.literal(" - ").formatted(Formatting.DARK_GRAY)
                    ).append(
                            Text.literal(argument.getID()).formatted(Formatting.GRAY)
                    ).styled(style -> style.withItalic(false))
            );
        }

        // Buffer lines
        lore = lore.with(Text.empty())
                .with(Text.empty())
                .with(Text.empty())
                .with(Text.empty())
                .with(Text.empty())
                .with(Text.empty());

        stack.set(DataComponentTypes.LORE, lore);

        return stack;
    }

    private static void addArguments(FunctionBlock functionBlock, FireFunction function) {
        functionBlock.push_argument(new DFArgument(
                0,
                new ItemVariable(
                        createIconStack(function)
                )
        ));

        int slot = 1;
        for (FireArgument argument : function.getExpectedArguments()) {
            functionBlock.push_argument(
                    new DFArgument(slot,
                            new FunctionParameterVariable(argument.getID(), argument.getDFType(), false, false)
                    )
            );
            slot++;
        }
    }
}
