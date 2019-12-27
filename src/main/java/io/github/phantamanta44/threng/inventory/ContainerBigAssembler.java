package io.github.phantamanta44.threng.inventory;

import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.threng.tile.TileBigAssemblerCore;
import io.github.phantamanta44.threng.tile.TileBigAssemblerPatternStore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;
import java.util.Objects;

public class ContainerBigAssembler extends L9Container {

    private final TileBigAssemblerCore tile;
    private final Page[] pages;

    public ContainerBigAssembler(EntityPlayer player, World world, int x, int y, int z) {
        this(player.inventory, (TileBigAssemblerCore)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
    }

    public ContainerBigAssembler(InventoryPlayer ipl, TileBigAssemblerCore tile) {
        super(ipl, 217);
        this.tile = tile;
        List<TileBigAssemblerPatternStore> patternStores = tile.collectPatternStores();
        this.pages = new Page[patternStores.size()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = new Page(patternStores.get(i).getPatternInventory());
        }
    }

    public TileBigAssemblerCore getAssemblerCore() {
        return tile;
    }

    public Page getPage(int pageNum) {
        return pages[pageNum];
    }

    public int getPageCount() {
        return pages.length;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isActive();
    }

    public class Page {

        private boolean active = true;

        Page(IItemHandlerModifiable patternInv) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 9; col++) {
                    int index = row * 9 + col;
                    addSlotToContainer(new SlotPage(patternInv, index, 8 + col * 18, 18 + row * 18));
                }
            }
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        private class SlotPage extends SlotItemHandler {

            public SlotPage(IItemHandlerModifiable patternInv, int index, int posX, int posY) {
                super(patternInv, index, posX, posY);
            }

            @Override
            public boolean isEnabled() {
                return active;
            }

        }

    }

}
