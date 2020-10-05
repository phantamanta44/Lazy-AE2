package io.github.phantamanta44.threng.inventory;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.items.misc.ItemEncodedPattern;
import appeng.util.Platform;
import io.github.phantamanta44.libnine.gui.L9Container;
import io.github.phantamanta44.libnine.util.TriBool;
import io.github.phantamanta44.threng.inventory.slot.IDisplayModeSlot;
import io.github.phantamanta44.threng.tile.TileBigAssemblerCore;
import io.github.phantamanta44.threng.tile.TileBigAssemblerPatternStore;
import io.github.phantamanta44.threng.util.ISearchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContainerBigAssembler extends L9Container {

    private final TileBigAssemblerCore tile;
    private final Page[] pages;

    public ContainerBigAssembler(EntityPlayer player, World world, int x, int y, int z) {
        this(player.inventory, (TileBigAssemblerCore)Objects.requireNonNull(world.getTileEntity(new BlockPos(x, y, z))));
    }

    public ContainerBigAssembler(InventoryPlayer ipl, TileBigAssemblerCore tile) {
        super(ipl, 217);
        this.tile = tile;
        List<TileBigAssemblerPatternStore> patternStores = tile.getPatternStores();
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

        private class SlotPage extends SlotItemHandler implements IDisplayModeSlot, ISearchable {

            private boolean displayMode = false;
            @Nullable
            private String searchQuery = null;
            private TriBool matchesQuery = TriBool.NONE;
            @Nullable
            private List<String> resultNames = null;

            public SlotPage(IItemHandlerModifiable patternInv, int index, int posX, int posY) {
                super(patternInv, index, posX, posY);
            }

            @Override
            public boolean isEnabled() {
                return active;
            }

            @Override
            public void prepareDisplayMode() {
                displayMode = true;
            }

            @Override
            public void updateSearchQuery(@Nullable String query) {
                this.searchQuery = query;
                matchesQuery = TriBool.NONE;
            }

            @Override
            public boolean matchesQuery() {
                if (matchesQuery == TriBool.NONE) {
                    if (searchQuery == null || searchQuery.isEmpty()) {
                        matchesQuery = TriBool.TRUE;
                        return true;
                    }
                    ItemStack stack = getStack();
                    if (stack.isEmpty()) {
                        matchesQuery = TriBool.FALSE;
                        return false;
                    }
                    if (resultNames == null) {
                        get_names:
                        {
                            if (stack.getItem() instanceof ICraftingPatternItem) {
                                ICraftingPatternDetails pattern = ((ICraftingPatternItem)stack.getItem())
                                        .getPatternForItem(stack, tile.getWorld());
                                if (pattern != null) {
                                    resultNames = Arrays.stream(pattern.getCondensedOutputs())
                                            .map(o -> Platform.getItemDisplayName(o).toLowerCase())
                                            .collect(Collectors.toList());
                                    break get_names;
                                }
                            }
                            resultNames = Collections.emptyList();
                        }
                    }
                    for (String name : resultNames) {
                        if (name.contains(searchQuery)) {
                            matchesQuery = TriBool.TRUE;
                            return true;
                        }
                    }
                    matchesQuery = TriBool.FALSE;
                    return false;
                }
                return matchesQuery.value;
            }

            @Override
            public ItemStack getStack() {
                if (displayMode) {
                    displayMode = false;
                    if (Platform.isClient()) {
                        ItemStack stack = super.getStack();
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemEncodedPattern) {
                            ItemStack outputStack = ((ItemEncodedPattern)stack.getItem()).getOutput(stack);
                            if (!outputStack.isEmpty()) {
                                return outputStack;
                            }
                        }
                    }
                }
                return super.getStack();
            }

            @Override
            public void putStack(@Nonnull ItemStack stack) {
                super.putStack(stack);
                resultNames = null;
                matchesQuery = TriBool.NONE;
            }

            @Nonnull
            @Override
            public ItemStack decrStackSize(int amount) {
                ItemStack extracted = super.decrStackSize(amount);
                resultNames = null;
                matchesQuery = TriBool.NONE;
                return extracted;
            }

        }

    }

}
