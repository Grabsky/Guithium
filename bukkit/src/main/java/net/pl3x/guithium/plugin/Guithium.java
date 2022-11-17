package net.pl3x.guithium.plugin;

import net.pl3x.guithium.plugin.listener.PlayerListener;
import net.pl3x.guithium.plugin.net.NetworkHandler;
import net.pl3x.guithium.api.gui.texture.TextureManager;
import net.pl3x.guithium.api.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class Guithium extends JavaPlugin implements net.pl3x.guithium.api.Guithium {
    private static Guithium instance;

    public static Guithium instance() {
        return instance;
    }

    private final net.pl3x.guithium.api.net.NetworkHandler networkHandler;
    private final PlayerManager playerManager;
    private final TextureManager textureManager;

    public Guithium() {
        instance = this;

        this.networkHandler = new NetworkHandler(this);
        this.playerManager = new PlayerManager();
        this.textureManager = new TextureManager();

        try {
            Field api = net.pl3x.guithium.api.Guithium.Provider.class.getDeclaredField("api");
            api.setAccessible(true);
            api.set(null, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void onEnable() {
        this.networkHandler.register();

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    @NotNull
    public net.pl3x.guithium.api.net.NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }

    @Override
    @NotNull
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    @NotNull
    public TextureManager getTextureManager() {
        return this.textureManager;
    }
}