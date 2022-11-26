package net.pl3x.guithium.plugin.network;

import net.pl3x.guithium.api.Guithium;
import net.pl3x.guithium.api.Key;
import net.pl3x.guithium.api.action.player.PlayerJoinedAction;
import net.pl3x.guithium.api.gui.Screen;
import net.pl3x.guithium.api.gui.element.Button;
import net.pl3x.guithium.api.gui.element.Checkbox;
import net.pl3x.guithium.api.gui.element.Radio;
import net.pl3x.guithium.api.gui.element.Slider;
import net.pl3x.guithium.api.gui.texture.Texture;
import net.pl3x.guithium.api.network.PacketListener;
import net.pl3x.guithium.api.network.packet.ButtonClickPacket;
import net.pl3x.guithium.api.network.packet.CheckboxTogglePacket;
import net.pl3x.guithium.api.network.packet.CloseScreenPacket;
import net.pl3x.guithium.api.network.packet.ElementPacket;
import net.pl3x.guithium.api.network.packet.HelloPacket;
import net.pl3x.guithium.api.network.packet.OpenScreenPacket;
import net.pl3x.guithium.api.network.packet.RadioTogglePacket;
import net.pl3x.guithium.api.network.packet.SliderChangePacket;
import net.pl3x.guithium.api.network.packet.TexturesPacket;
import net.pl3x.guithium.api.player.WrappedPlayer;
import net.pl3x.guithium.plugin.player.BukkitPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BukkitPacketListener implements PacketListener {
    private final WrappedPlayer player;

    public BukkitPacketListener(@NotNull WrappedPlayer player) {
        this.player = player;
    }

    @Override
    public void handleButtonClick(@NotNull ButtonClickPacket packet) {
        Screen screen = this.player.getCurrentScreen();
        if (screen != null && screen.getKey().equals(packet.getScreen())) {
            if (screen.getElements().get(packet.getButton()) instanceof Button button) {
                Button.OnClick onClick = button.onClick();
                if (onClick != null) {
                    onClick.accept(screen, button, player);
                }
            }
        }
    }

    @Override
    public void handleCheckboxToggle(@NotNull CheckboxTogglePacket packet) {
        Screen screen = this.player.getCurrentScreen();
        if (screen != null && screen.getKey().equals(packet.getScreen())) {
            if (screen.getElements().get(packet.getCheckbox()) instanceof Checkbox checkbox) {
                Checkbox.OnToggled onToggled = checkbox.onToggled();
                if (onToggled != null) {
                    onToggled.accept(screen, checkbox, player, packet.getSelected());
                }
            }
        }
    }

    @Override
    public void handleCloseScreen(@NotNull CloseScreenPacket packet) {
        Screen screen = this.player.getCurrentScreen();
        if (screen != null) {
            if (screen.getKey().equals(packet.getScreenKey())) {
                this.player.setCurrentScreen(null);
            }
        }
    }

    @Override
    public void handleElement(@NotNull ElementPacket packet) {
        // client does not send this packet to the server
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void handleHello(@NotNull HelloPacket packet) {
        int protocol = packet.getProtocol();

        System.out.println(this.player.getName() + " is using Guithium with protocol " + protocol);

        // set the player's client protocol
        ((BukkitPlayer) this.player).setProtocol(protocol);

        // reply to the player
        this.player.getConnection().send(new HelloPacket(), true);

        // ensure the player has the correct guithium installed
        if (!this.player.hasGuithium()) {
            return;
        }

        // tell client about textures
        Map<Key, Texture> textures = Guithium.api().getTextureManager().getTextures();
        if (!textures.isEmpty()) {
            this.player.getConnection().send(new TexturesPacket(textures));
        }

        // tell other plugins about this hello
        Guithium.api().getActionRegistry().callAction(new PlayerJoinedAction(this.player));
    }

    @Override
    public void handleOpenScreen(@NotNull OpenScreenPacket packet) {
        // client does not send this packet to the server
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void handleRadioToggle(@NotNull RadioTogglePacket packet) {
        Screen screen = this.player.getCurrentScreen();
        if (screen != null && screen.getKey().equals(packet.getScreen())) {
            if (screen.getElements().get(packet.getRadio()) instanceof Radio radio) {
                Radio.OnToggled onToggled = radio.onToggled();
                if (onToggled != null) {
                    onToggled.accept(screen, radio, player, packet.getSelected());
                }
            }
        }
    }

    @Override
    public void handleSliderChange(@NotNull SliderChangePacket packet) {
        Screen screen = this.player.getCurrentScreen();
        if (screen != null && screen.getKey().equals(packet.getScreen())) {
            if (screen.getElements().get(packet.getSlider()) instanceof Slider slider) {
                Slider.OnChange onChange = slider.onChange();
                if (onChange != null) {
                    onChange.accept(screen, slider, player, packet.getValue());
                }
            }
        }
    }

    @Override
    public void handleTextures(@NotNull TexturesPacket packet) {
        // client does not send this packet to the server
        throw new UnsupportedOperationException("Not supported.");
    }
}
