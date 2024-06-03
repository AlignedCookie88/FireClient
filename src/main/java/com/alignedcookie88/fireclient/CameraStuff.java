package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.mixin.CameraAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CameraStuff {
    public static void rollCamera(float degrees) {
        Camera c = MinecraftClient.getInstance().gameRenderer.getCamera();
        Quaternionf q = ((CameraAccessor) c).getRotation();

        float angleRadians = (float) Math.toRadians(degrees);
        Vector3f axis = new Vector3f(0, 0, 1);

        Quaternionf qr = new Quaternionf().fromAxisAngleRad(axis, angleRadians);

        Quaternionf qPrime = new Quaternionf();
        qr.mul(q, qPrime);

        ((CameraAccessor) c).setRotation(qPrime);
    }
}
