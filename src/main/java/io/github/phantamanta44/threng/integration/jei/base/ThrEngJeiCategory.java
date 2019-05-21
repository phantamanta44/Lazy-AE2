package io.github.phantamanta44.threng.integration.jei.base;

import io.github.phantamanta44.libnine.recipe.IRcp;
import io.github.phantamanta44.libnine.util.render.TextureRegion;
import io.github.phantamanta44.threng.integration.jei.ThrEngJei;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

public abstract class ThrEngJeiCategory<R extends IRcp<?, ?, ?>, W extends IRecipeWrapper> implements IRecipeCategory<W> {

    private final Class<R> type;
    private final String catId;
    private final String titleKey;
    private final TextureRegion bg;

    @Nullable
    private IDrawable bgDrawable = null;

    public ThrEngJeiCategory(Class<R> type, String catId, String titleKey, TextureRegion bg) {
        this.type = type;
        this.catId = catId;
        this.titleKey = titleKey;
        this.bg = bg;
    }

    @Override
    public String getUid() {
        return catId;
    }

    @Override
    public String getTitle() {
        return I18n.format(titleKey);
    }

    @Override
    public String getModName() {
        return "Lazy AE2";
    }

    @Override
    public IDrawable getBackground() {
        if (bgDrawable == null) {
            bgDrawable = ThrEngJei.wrapDrawable(bg);
        }
        return bgDrawable;
    }

    public abstract W wrap(R recipe);

    public void registerHandler(IModRegistry registry) {
        registry.handleRecipes(type, this::wrap, catId);
    }

}
