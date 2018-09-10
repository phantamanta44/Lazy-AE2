package io.github.phantamanta44.threng.client.gui.base;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.threng.client.gui.component.GuiComponentEnergyBar;
import io.github.phantamanta44.threng.inventory.base.ContainerEnergized;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class GuiEnergized<T extends ContainerEnergized> extends L9GuiContainer {

    protected final T cont;

    public GuiEnergized(T cont, @Nullable ResourceLocation bg) {
        super(cont, bg);
        this.cont = cont;
    }

    @Override
    public void initGui() {
        super.initGui();
        addComponent(new GuiComponentEnergyBar(cont.getEnergyStorage()));
    }

}
