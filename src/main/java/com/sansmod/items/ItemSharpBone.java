package com.sansmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSharpBone extends ItemSword {

    public static final ToolMaterial BONE_MATERIAL = EnumHelper.addToolMaterial(
        "SANS_BONE", 
        3, 
        0, 
        8.0F, 
        6.0F, 
        22
    );

    public ItemSharpBone() {
        super(BONE_MATERIAL);
        this.setRegistryName("sharp_bone");
        this.setUnlocalizedName("sansmod.sharp_bone");
        this.setCreativeTab(CreativeTabs.COMBAT);
    }
}
