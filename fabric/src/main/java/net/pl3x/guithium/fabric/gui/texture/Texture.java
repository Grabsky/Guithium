package net.pl3x.guithium.fabric.gui.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.pl3x.guithium.api.Key;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Texture {
    private final ResourceLocation identifier;
    private final String url;

    private boolean isLoaded;

    public Texture(@NotNull Key key, @NotNull String url) {
        System.out.println(key + " --- " + url);
        this.url = url;
        if (url.startsWith("http")) {
            System.out.println("http");
            // custom texture
            this.identifier = new ResourceLocation(key.toString());
            loadFromInternet();
        } else {
            System.out.println("vanilla");
            // vanilla texture
            this.identifier = new ResourceLocation(url);
            this.isLoaded = true;
        }
    }

    @NotNull
    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    private void loadFromInternet() {
        System.out.println(0);
        if (!RenderSystem.isOnRenderThread()) {
            System.out.println(0.5);
            RenderSystem.recordRenderCall(this::loadFromInternet);
            return;
        }
        try {
            System.out.println(1);
            BufferedImage image = ImageIO.read(new URL(this.url));
            DynamicTexture texture = new DynamicTexture(image.getWidth(), image.getHeight(), true);
            NativeImage nativeImage = texture.getPixels();
            if (nativeImage == null) {
                return;
            }
            System.out.println(2);
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getWidth(); y++) {
                    nativeImage.setPixelRGBA(x, y, rgb2bgr(image.getRGB(x, y)));
                }
            }
            System.out.println(3);
            texture.upload();
            Minecraft.getInstance().getTextureManager().register(getIdentifier(), texture);
            this.isLoaded = true;
            System.out.println(4);
        } catch (IOException e) {
            System.out.println(getIdentifier() + " " + this.url);
            throw new RuntimeException(e);
        }
    }

    public void unload() {
        Minecraft.getInstance().getTextureManager().release(getIdentifier());
    }

    private static int rgb2bgr(int color) {
        // Minecraft flips red and blue for some reason
        // lets flip them back
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        return (a << 24) | (b << 16) | (g << 8) | r;
    }
}
