package net.pl3x.guithium.fabric.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.pl3x.guithium.api.gui.Point;
import net.pl3x.guithium.api.gui.element.Button;
import net.pl3x.guithium.api.gui.element.Element;
import net.pl3x.guithium.api.gui.element.Gradient;
import net.pl3x.guithium.api.gui.element.Image;
import net.pl3x.guithium.api.gui.element.Text;
import net.pl3x.guithium.fabric.gui.screen.RenderableScreen;
import org.jetbrains.annotations.NotNull;

public abstract class RenderableElement {
    private Element element;
    protected final RenderableScreen screen;

    protected final Point pos = new Point();

    protected AbstractWidget renderableWidget;

    public RenderableElement(@NotNull Element element, @NotNull RenderableScreen screen) {
        this.element = element;
        this.screen = screen;
    }

    @NotNull
    public Element getElement() {
        return this.element;
    }

    public void setElement(@NotNull Element element) {
        this.element = element;
        this.screen.refresh();
    }

    public AbstractWidget getRenderableWidget() {
        return this.renderableWidget;
    }

    public void init(@NotNull Minecraft minecraft, int width, int height) {
    }

    public abstract void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float delta);

    protected void calcScreenPos(float width, float height) {
        Point pos = getElement().getPos();
        if (pos == null) {
            pos = Point.ZERO;
        }

        double anchorX = 0;
        double anchorY = 0;
        if (getElement().getAnchor() != null) {
            anchorX = Math.ceil(this.screen.width * getElement().getAnchor().getX());
            anchorY = Math.ceil(this.screen.height * getElement().getAnchor().getY());
        }

        int offsetX = 0;
        int offsetY = 0;
        if (getElement().getOffset() != null) {
            offsetX = (int) (width * getElement().getOffset().getX());
            offsetY = (int) (height * getElement().getOffset().getY());
        }

        this.pos.setX((int) (anchorX + pos.getX() - offsetX));
        this.pos.setY((int) (anchorY + pos.getY() - offsetY));
    }

    public static RenderableElement createRenderableElement(@NotNull Element element, @NotNull RenderableScreen screen) {
        Element.Type type = element.getType();
        if (type == Element.Type.BUTTON) return new RenderableButton((Button) element, screen);
        if (type == Element.Type.GRADIENT) return new RenderableGradient((Gradient) element, screen);
        if (type == Element.Type.IMAGE) return new RenderableImage((Image) element, screen);
        if (type == Element.Type.TEXT) return new RenderableText((Text) element, screen);
        return null;
    }
}
