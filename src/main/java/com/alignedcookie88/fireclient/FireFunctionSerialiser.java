package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.serialisation.DFArgument;
import com.alignedcookie88.fireclient.serialisation.DFBlock;
import com.alignedcookie88.fireclient.serialisation.DFTemplate;
import com.alignedcookie88.fireclient.serialisation.blocks.FunctionBlock;
import com.alignedcookie88.fireclient.serialisation.blocks.PlayerActionBlock;
import com.alignedcookie88.fireclient.serialisation.variables.FunctionParameterVariable;
import com.alignedcookie88.fireclient.serialisation.variables.StringVariable;
import com.alignedcookie88.fireclient.serialisation.variables.Variable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FireFunctionSerialiser {

    public static String ARG_DENOTION = "\uF8F8";

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
        String string = ".fireclient "+FireClient.functionRegistry.getId(function).asString();

        for (FireArgument argument : function.getExpectedArguments()) {
            string = string + " " + ARG_DENOTION + "%var(" + argument.getID() + ")" + ARG_DENOTION;
        }

        return string;
    }

    private static void addArguments(FunctionBlock functionBlock, FireFunction function) {
        int slot = 0;
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
