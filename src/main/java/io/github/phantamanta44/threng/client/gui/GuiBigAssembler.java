package io.github.phantamanta44.threng.client.gui;

import io.github.phantamanta44.libnine.client.gui.L9GuiContainer;
import io.github.phantamanta44.libnine.util.render.GuiUtils;
import io.github.phantamanta44.threng.client.gui.component.GuiComponentPageNav;
import io.github.phantamanta44.threng.constant.LangConst;
import io.github.phantamanta44.threng.constant.ResConst;
import io.github.phantamanta44.threng.inventory.ContainerBigAssembler;
import io.github.phantamanta44.threng.tile.TileBigAssemblerCore;
import io.github.phantamanta44.threng.util.IPaginated;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;

public class GuiBigAssembler extends L9GuiContainer implements IPaginated {

    private final ContainerBigAssembler cont;

    private int currentPage = 0;

    public GuiBigAssembler(ContainerBigAssembler cont) {
        super(cont, ResConst.GUI_BIG_ASSEMBLER.getTexture(), 176, 217);
        this.cont = cont;
        for (int i = 1; i < cont.getPageCount(); i++) {
            cont.getPage(i).setActive(false);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        addComponent(new GuiComponentPageNav(132, 93, this));
    }

    @Override
    public int getPageCount() {
        return cont.getPageCount();
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setPage(int pageNum) {
        cont.getPage(currentPage).setActive(false);
        currentPage = pageNum;
        cont.getPage(currentPage).setActive(true);
    }

    @Override
    public void drawForeground(float partialTicks, int mX, int mY) {
        super.drawForeground(partialTicks, mX, mY);
        drawContainerName(I18n.format(LangConst.CONTAINER_BIG_ASSEMBLER));

        TileBigAssemblerCore tile = cont.getAssemblerCore();
        int work = tile.getWork();
        if (work > 0) {
            ResConst.GUI_BIG_ASSEMBLER_PROGRESS
                    .drawPartial(36, 113, 0F, 0F, work / (float)TileBigAssemblerCore.WORK_PER_JOB, 1F);
        }
        int jobCount = tile.getJobCount();
        if (jobCount > 0) {
            ResConst.GUI_BIG_ASSEMBLER_QUEUE
                    .drawPartial(58, 94, 0F, 1F - jobCount / (float)TileBigAssemblerCore.MAX_JOB_QUEUE, 1F, 1F);
        }
        int cpuCount = tile.getCpuCount();
        if (cpuCount > 0) {
            float cpuBar;
            if (cpuCount >= TileBigAssemblerCore.MAX_EFFECTIVE_CPUS) {
                cpuBar = 1F;
            } else {
                cpuBar = (float)(Math.log1p(cpuCount) / Math.log1p(TileBigAssemblerCore.MAX_EFFECTIVE_CPUS));
            }
            ResConst.GUI_BIG_ASSEMBLER_CPUS.drawPartial(64, 94, 0F, 1F - cpuBar, 1F, 1F);
        }

        TileBigAssemblerCore.IAssemblyJob job = tile.getActiveJob();
        if (job != null) {
            int index = job.getJobIndex();
            IItemHandler craftBuf = tile.getCraftingBuffer();
            zLevel = 100.0F;
            itemRender.zLevel = 100.0F;
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5D, 0.5D, 0.5D);
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    drawStack(craftBuf.getStackInSlot(index * 9 + row * 3 + col), 15 + col * 18, 187 + row * 18);
                }
            }
            GlStateManager.popMatrix();
            drawStack(job.getResult(), 36, 94);
            RenderHelper.disableStandardItemLighting();
            itemRender.zLevel = 0.0F;
            zLevel = 0.0F;
        }
    }

    private void drawStack(ItemStack stack, int x, int y) {
        itemRender.renderItemAndEffectIntoGUI(mc.player, stack, x, y);
        itemRender.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, null);
    }

    @Override
    public void drawOverlay(float partialTicks, int mX, int mY) {
        TileBigAssemblerCore tile = cont.getAssemblerCore();
        if (GuiUtils.isMouseOver(35, 112, 18, 4, mX, mY)) { // progress bar
            drawTooltip(I18n.format(LangConst.TT_WORK_FRAC, tile.getWork(), TileBigAssemblerCore.WORK_PER_JOB), mX, mY);
        } else if (GuiUtils.isMouseOver(57, 93, 5, 27, mX, mY)) { // queue bar
            drawTooltip(I18n.format(LangConst.TT_JOB_COUNT, tile.getJobCount(), TileBigAssemblerCore.MAX_JOB_QUEUE), mX, mY);
        } else if (GuiUtils.isMouseOver(63, 93, 5, 27, mX, mY)) { // cpu bar
            drawTooltip(Arrays.asList(
                    I18n.format(LangConst.TT_CPU_COUNT, tile.getCpuCount()), I18n.format(LangConst.TT_WORK_RATE, tile.getWorkRate())),
                    mX, mY);
        }
    }

}
