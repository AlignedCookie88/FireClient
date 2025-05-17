package com.alignedcookie88.fireclient.sdk;

import com.alignedcookie88.fireclient.codegen.Template;
import com.alignedcookie88.fireclient.codegen.block.ActionBlock;
import com.alignedcookie88.fireclient.codegen.block.CodeBlock;
import com.alignedcookie88.fireclient.codegen.block.FunctionBlock;
import com.alignedcookie88.fireclient.codegen.item.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.Optional;

public abstract class SDKFunction {

    public abstract String getId();

    public abstract Text getHumanName();

    public String getSignName() {
        return getHumanName().getString().replace(" ", "");
    }

    public void execute(String argString) {

        String[] rawArgs = argString.split(SDKArgument.SPLITTER);
        SDKArgument<?>[] expectedArgs = getArguments();
        int providedArgs = argString.isEmpty() ? 0 : rawArgs.length;

        if (providedArgs != expectedArgs.length) {
            throw new IllegalStateException(
                    "Incorrect argument count supplied to function %s, expected %s arguments, found %s."
                            .formatted(getId(), expectedArgs.length, providedArgs)
            );
        }

        for (int i = 0; i < providedArgs; i++) {
            expectedArgs[i].parse(rawArgs[i]);
        }

        onExecute();
    }

    public abstract void onExecute();

    public abstract SDKArgument<?>[] getArguments();

    public boolean executeSynchronously() {
        return false;
    }

    /**
     * If this is true the function should be hidden from any UIs and be excluded from any bundles, however it should still be accessible with /fireclient sdk get_function.
     */
    public boolean isHidden() {
        return false;
    }

    public Template createTemplate() {
        Template template = new Template();

        ItemStack icon = new ItemStack(Items.CAMPFIRE);
        Text displayName = FireClientSDK.styleSDKFunctionName(getHumanName());
        icon.set(DataComponentTypes.ITEM_NAME, displayName);
        icon.set(DataComponentTypes.CUSTOM_NAME, displayName);
        template.withName(displayName);

        StringBuilder argString = new StringBuilder();
        CodeBlock function = new FunctionBlock(".fireclient action/"+getId(), false)
                .withItem(new VanillaItem(icon));
        for (SDKArgument<?> arg : getArguments()) {
            if (!argString.isEmpty())
                argString.append(SDKArgument.SPLITTER);
            argString.append("%var(").append(arg.getName()).append(")");
            function.withItem(new ParameterItem(
                    arg.getName(),
                    arg.getHypercubeType(),
                    false,
                    false,
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            ));
        }
        template.add(function);

        for (SDKArgument<?> arg : getArguments()) {
            arg.addBlocks(template);
        }

        return template.add(
                new ActionBlock(ActionBlock.Type.PLAYER_ACTION, "DisplaySignText")
                        .withItem(new LocationItem(-10_000, 0, -10_000)) // Location is way out of range, prevents it affecting anything
                        .withItem(new StringItem("!fireclient!")) // Signature line
                        .withItem(new StringItem(getId())) // ID line
                        .withItem(new StringItem(FireClientSDK.SDK_VERSION)) // Version line
                        .withItem(new StringItem(argString.toString())) // Arguments
                        .withSpecialisedItem(block -> new TagItem(
                                "Glowing",
                                "Disable",
                                block
                        ))
                        .withSpecialisedItem(block -> new TagItem(
                                "Text Color",
                                "Black",
                                block
                        ))
                        .withSpecialisedItem(block -> new TagItem(
                                "Sign Side",
                                "Front",
                                block
                        ))
                );
    }

}
