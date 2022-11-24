package net.pl3x.guithium.api.network.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.pl3x.guithium.api.Key;
import net.pl3x.guithium.api.gui.Screen;
import net.pl3x.guithium.api.gui.element.Checkbox;
import net.pl3x.guithium.api.network.PacketListener;
import org.jetbrains.annotations.NotNull;

public class CheckboxTogglePacket extends Packet {
    public static final Key KEY = Key.of("packet:checkbox_toggle");

    private final Key screen;
    private final Key checkbox;
    private final boolean selected;

    public CheckboxTogglePacket(@NotNull Screen screen, @NotNull Checkbox checkbox, boolean selected) {
        this.screen = screen.getKey();
        this.checkbox = checkbox.getKey();
        this.selected = selected;
    }

    public CheckboxTogglePacket(@NotNull ByteArrayDataInput in) {
        this.screen = Key.of(in.readUTF());
        this.checkbox = Key.of(in.readUTF());
        this.selected = in.readBoolean();
    }

    @Override
    @NotNull
    public Key getKey() {
        return KEY;
    }

    @NotNull
    public Key getScreen() {
        return this.screen;
    }

    @NotNull
    public Key getCheckbox() {
        return this.checkbox;
    }

    public boolean getSelected() {
        return this.selected;
    }

    @Override
    public void handle(@NotNull PacketListener listener) {
        listener.handleCheckboxToggle(this);
    }

    @Override
    @NotNull
    public ByteArrayDataOutput write() {
        ByteArrayDataOutput out = out(this);
        out.writeUTF(getScreen().toString());
        out.writeUTF(getCheckbox().toString());
        out.writeBoolean(getSelected());
        return out;
    }
}
