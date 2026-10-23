package com.sansmod.items;

import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSharpBone extends ItemSword {

    public static final ToolMaterial BONE_MATERIAL = EnumHelper.addToolMaterial(
        "SANS_BONE", 
        3,     
        0,     
        8.0F,  
        0.0F,  
        0      // 0 зачаровываемости (предмет нельзя зачаровать)
    );

    public ItemSharpBone() {
        super(BONE_MATERIAL);
        setUnlocalizedName("sharp_bone");
        setRegistryName("sharp_bone");
        setCreativeTab(CreativeTabs.COMBAT);
    }

    // Запрещаем зачарования на уровне предмета (дополнительная гарантия)
    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.clear();
            
            // Урон: 1
            multimap.put(net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
            
            // Скорость атаки: 5.0 (очень быстро)
            multimap.put(net.minecraft.entity.SharedMonsterAttributes.ATTACK_SPEED.getName(), 
                new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 5.0D, 0));
        }

        return multimap;
    }
}
