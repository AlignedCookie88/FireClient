package com.alignedcookie88.fireclient.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JsonEffectShaderProgram.class)
public class JsonEffectShaderProgramMixin {

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;", ordinal = 0))
    public Identifier fixIdentifier(String path, Operation<Identifier> original) {

        if (path.startsWith("shaders/program/"))
            path = path.replaceFirst("shaders/program/", "");


        Identifier tried = Identifier.tryParse(path);
        if (tried == null)
            tried = Identifier.of("minecraft", path);

        String path2 = tried.getPath();

        if (path2.startsWith("shaders/program/"))
            return tried;

        return Identifier.of(tried.getNamespace(), ("shaders/program/"+path2+".json").replace(".json.json", ".json"));
    }

    @WrapOperation(method = "loadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;", ordinal = 0))
    private static Identifier fixIdentifier2(String path, Operation<Identifier> original) {

        String[] parts = path.split("\\.");
        String extension = "." + parts[parts.length - 1];

        if (path.startsWith("shaders/program/"))
            path = path.replaceFirst("shaders/program/", "");


        Identifier tried = Identifier.tryParse(path);
        if (tried == null)
            tried = Identifier.of("minecraft", path);

        String path2 = tried.getPath();

        if (path2.startsWith("shaders/program/"))
            return tried;

        return Identifier.of(tried.getNamespace(), ("shaders/program/"+path2+extension).replace(extension+extension, extension));
    }

}
