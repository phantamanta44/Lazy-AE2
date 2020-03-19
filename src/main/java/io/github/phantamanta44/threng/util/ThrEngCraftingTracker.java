package io.github.phantamanta44.threng.util;

import appeng.api.AEApi;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.helpers.NonNullArrayIterator;
import com.google.common.collect.ImmutableSet;
import io.github.phantamanta44.libnine.util.ImpossibilityRealizedException;
import io.github.phantamanta44.libnine.util.data.ByteUtils;
import io.github.phantamanta44.libnine.util.data.ISerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Adapted from appeng.helpers.MultiCraftingTracker
 * https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/rv6-1.12/src/main/java/appeng/helpers/MultiCraftingTracker.java
 */
public class ThrEngCraftingTracker implements ISerializable {

    private final ICraftingRequester owner;
    private final Future<ICraftingJob>[] jobs;
    private final ICraftingLink[] links;

    public ThrEngCraftingTracker(ICraftingRequester owner, int size) {
        this.owner = owner;
        //noinspection unchecked
        this.jobs = new Future[size];
        this.links = new ICraftingLink[size];
    }

    public boolean requestCrafting(int slot, IAEItemStack item, World world, IGrid grid, ICraftingGrid crafting, IActionSource actionSrc) {
        if (links[slot] != null) {
            return false;
        }
        Future<ICraftingJob> jobTask = jobs[slot];
        if (jobTask == null) {
            jobs[slot] = crafting.beginCraftingJob(world, grid, actionSrc, item.copy(), null);
        } else if (jobTask.isDone()) {
            try {
                ICraftingJob job = jobTask.get();
                if (job != null) {
                    ICraftingLink link = crafting.submitJob(job, owner, null, false, actionSrc);
                    jobs[slot] = null;
                    if (link != null) {
                        links[slot] = link;
                        updateLinks();
                        return true;
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new ImpossibilityRealizedException(e);
            }
        }
        return false;
    }

    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return ImmutableSet.copyOf(new NonNullArrayIterator<>(links));
    }

    public boolean isSlotOpen(int slot) {
        return links[slot] == null;
    }

    public boolean onJobStateChange(final ICraftingLink link) {
        for (int i = 0; i < links.length; i++) {
            if (links[i] == link) {
                links[i] = null;
                updateLinks();
                return true;
            }
        }
        return false;
    }

    private void updateLinks() {
        for (int i = 0; i < links.length; i++) {
            if (links[i] != null && (links[i].isCanceled() || links[i].isDone())) {
                links[i] = null;
            }
        }
    }

    @Override
    public void serBytes(ByteUtils.Writer data) {
        int mask = 0;
        for (int i = 0; i < links.length; i++) {
            if (links[i] != null) {
                mask |= 1 << i;
            }
        }
        data.writeVarPrecision(mask);
        for (ICraftingLink link : links) {
            if (link != null) {
                NBTTagCompound dto = new NBTTagCompound();
                link.writeToNBT(dto);
                data.writeTagCompound(dto);
            }
        }
    }

    @Override
    public void deserBytes(ByteUtils.Reader data) {
        int mask = data.readVarPrecision();
        for (int i = 0; i < links.length; i++) {
            if ((mask & (1 << i)) != 0) {
                links[i] = AEApi.instance().storage().loadCraftingLink(data.readTagCompound(), owner);
            } else {
                links[i] = null;
            }
        }
    }

    @Override
    public void serNBT(NBTTagCompound tag) {
        NBTTagList linksDto = new NBTTagList();
        for (ICraftingLink link : links) {
            NBTTagCompound linkDto = new NBTTagCompound();
            if (link != null) {
                link.writeToNBT(linkDto);
            }
            linksDto.appendTag(linkDto);
        }
        tag.setTag("Links", linksDto);
    }

    @Override
    public void deserNBT(NBTTagCompound tag) {
        NBTTagList linksDto = tag.getTagList("Links", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < links.length; i++) {
            NBTTagCompound linkDto = linksDto.getCompoundTagAt(i);
            if (linkDto.isEmpty()) {
                links[i] = null;
            } else {
                links[i] = AEApi.instance().storage().loadCraftingLink(linkDto, owner);
            }
        }
    }

}
