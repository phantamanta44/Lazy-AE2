package io.github.phantamanta44.threng.util;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.definitions.IDefinitions;
import appeng.api.definitions.IItemDefinition;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.crafting.MECraftingInventory;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import io.github.phantamanta44.libnine.util.IDisplayableMatcher;
import io.github.phantamanta44.libnine.util.ImpossibilityRealizedException;
import io.github.phantamanta44.libnine.util.helper.ItemUtils;
import io.github.phantamanta44.libnine.util.helper.MirrorUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppEngUtils {

    public static final IDisplayableMatcher<ItemStack> IS_UPGRADE_ACCEL = genItemPredicate(defs -> defs.materials().cardSpeed());

    private static IDisplayableMatcher<ItemStack> genItemPredicate(Function<IDefinitions, IItemDefinition> item) {
        return item.apply(AEApi.instance().definitions()).maybeStack(1)
                .map(ItemUtils::matchesWithWildcard)
                .orElse(IDisplayableMatcher.of(() -> new ItemStack(Blocks.BARRIER), s -> false));
    }

    private static final MirrorUtils.IField<Map> fTasks = MirrorUtils.reflectField(CraftingCPUCluster.class, "tasks");
    private static final MirrorUtils.IField<MECraftingInventory> fInventory
            = MirrorUtils.reflectField(CraftingCPUCluster.class, "inventory");
    private static final MirrorUtils.IField<IItemList<IAEItemStack>> fWaitingFor
            = MirrorUtils.reflectField(CraftingCPUCluster.class, "waitingFor");
    private static final Field fValue;

    static {
        try {
            fValue = Class.forName("appeng.me.cluster.implementations.CraftingCPUCluster$TaskProgress")
                    .getDeclaredField("value");
            fValue.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new ImpossibilityRealizedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Set<CraftingTask> getTasks(CraftingCPUCluster cpu) {
        return (Set<CraftingTask>)fTasks.get(cpu).entrySet().stream()
                .map(t -> new CraftingTask(cpu, (Map.Entry)t))
                .collect(Collectors.toSet());
    }

    public static class CraftingTask {

        private final CraftingCPUCluster cpu;
        private final Map.Entry entry;

        CraftingTask(CraftingCPUCluster cpu, Map.Entry entry) {
            this.cpu = cpu;
            this.entry = entry;
        }

        public ICraftingPatternDetails getPattern() {
            return (ICraftingPatternDetails)entry.getKey();
        }

        public long getInvocations() {
            try {
                return fValue.getLong(entry.getValue());
            } catch (IllegalAccessException e) {
                throw new ImpossibilityRealizedException(e);
            }
        }

        public void decrement(IActionSource actionSource) {
            try {
                fValue.setLong(entry.getValue(), getInvocations() - 1);
                IItemList<IAEItemStack> waitingFor = fWaitingFor.get(cpu);
                for (IAEItemStack output : getPattern().getCondensedOutputs()) {
                    waitingFor.add(output.copy());
                }
                for (IAEItemStack input : getPattern().getCondensedInputs()) {
                    fInventory.get(cpu).extractItems(input, Actionable.MODULATE, actionSource);
                }
            } catch (IllegalAccessException e) {
                throw new ImpossibilityRealizedException(e);
            }
        }

    }

}
