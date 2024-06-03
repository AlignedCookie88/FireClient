package com.alignedcookie88.fireclient.mixin;

import net.minecraft.client.render.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Accessor("rotation")
    public void setRotation(Quaternionf rotation);

    @Accessor("rotation")
    public Quaternionf getRotation();
}
