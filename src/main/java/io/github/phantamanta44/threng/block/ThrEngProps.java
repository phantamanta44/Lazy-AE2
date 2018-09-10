package io.github.phantamanta44.threng.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class ThrEngProps {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final IProperty<EnumFacing> ROTATION = PropertyEnum.create("rotation", EnumFacing.class, EnumFacing.HORIZONTALS);

}
