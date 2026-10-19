package com.sansmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSharpBone extends ItemSword {

    // Создаем уникальный материал предмета:
    // "SANS_BONE", harvestLevel=3, maxUses=0 (0 = бесконечная прочность!), efficiency=8.0F, damage=6.0F, enchantability=22
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
        this.setUnlocalizedName("sharp_bone");
        this.setRegistryName("sharp_bone");
        this.setCreativeTab(CreativeTabs.COMBAT); // Добавляем во вкладку "Бой"
    }
}
