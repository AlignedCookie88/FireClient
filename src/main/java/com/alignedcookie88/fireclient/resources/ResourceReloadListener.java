package com.alignedcookie88.fireclient.resources;

import com.alignedcookie88.fireclient.FireClient;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return Identifier.of("fireclient", "resource_stuff");
    }

    @Override
    public void reload(ResourceManager manager) {
        TagDisplayData.cleanup();

        Gson gson = new Gson();

        Map<Identifier, Resource> resources = manager.findResources("tag_display", path -> path.getPath().endsWith(".json"));

        for (Identifier id : resources.keySet()) {
            Resource resource = resources.get(id);
            try (InputStream stream = resource.getInputStream()) {
                TagDisplayData s = gson.fromJson(new InputStreamReader(stream), TagDisplayData.class);
                String tag = id.getPath().replaceFirst("tag_display/", "").replace(".json", "");
                TagDisplayData.register(tag, s);
                FireClient.LOGGER.info("Loaded {} for tag {}!", id, tag);
            } catch (IOException e) {
                FireClient.LOGGER.info("Couldn't load tag display data for {}!", id);
            }
        }

    }
}
