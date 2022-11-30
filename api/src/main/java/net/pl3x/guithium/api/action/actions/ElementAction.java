package net.pl3x.guithium.api.action.actions;

import net.pl3x.guithium.api.gui.Screen;
import net.pl3x.guithium.api.gui.element.Element;
import net.pl3x.guithium.api.player.WrappedPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Action that fires when an element action is performed.
 */
public abstract class ElementAction extends ScreenAction {
    private final Element element;

    /**
     * Creates a new action for when an element action is performed.
     *
     * @param player  Player that performed the action
     * @param screen  Screen action was performed on
     * @param element Element action was performed on
     */
    public ElementAction(@NotNull WrappedPlayer player, @NotNull Screen screen, @NotNull Element element) {
        super(player, screen);
        this.element = element;
    }

    /**
     * Get the element involved in this action.
     *
     * @return Element
     */
    @NotNull
    public Element getElement() {
        return this.element;
    }
}