package io.github.phantamanta44.threng.client.gui.base;

import io.github.phantamanta44.threng.client.gui.component.GuiComponentSidedIo;
import io.github.phantamanta44.threng.inventory.base.ContainerSimpleProcessor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class GuiSimpleProcessor<T extends ContainerSimpleProcessor<?>> extends GuiEnergized<T> {

    public GuiSimpleProcessor(T cont, @Nullable ResourceLocation bg) {
        super(cont, bg);
        addComponent(new GuiComponentSidedIo(cont));
    }

}
