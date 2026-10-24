package com.sansmod.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class EffectKarma extends Potion {

    public EffectKarma() {
        // false - не вредоносный статус для игры (но наносит урон), цвет 0x990000 (темно-красный/пурпурный)
        super(true, 0x990000);
        setRegistryName("karma");
        setPotionName("effect.sansmod.karma");
    }

    // Как часто срабатывает эффект (каждый тик)
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0; // Наносит урон каждые 10 тиков (0.5 секунды)
    }

    // Логика урона от Кармы
    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn.getHealth() > 1.0F) {
            // Наносит урон по 1 единице, сжигая ХП, но оставляя минимум 1 ХП (каноничная карма)
            entityLivingBaseIn.attackEntityFrom(DamageSource.MAGIC, 1.0F);
        }
    }
}
