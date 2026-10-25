package com.sansmod.client;

import com.sansmod.network.PacketHandler;
import com.sansmod.network.PacketTelekinesis;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class TelekinesisHandler {
    private static Entity lockedTarget = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Если цель умерла или далеко — сбрасываем захват
        if (lockedTarget != null && (!lockedTarget.isEntityAlive() || mc.player.getDistanceSq(lockedTarget) > 900.0D)) {
            lockedTarget = null;
        }
    }

    // Ловим нажатия мыши и клавиатуры четко по событиям ввода
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Захват цели на ПКМ (если зажата или нажата)
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            RayTraceResult ray = mc.objectMouseOver;
            if (ray != null && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
                lockedTarget = ray.entityHit;
            }
        }

        // Если цель захвачена, управляем ей через W (толкнуть) и S (притянуть)
        if (lockedTarget != null && lockedTarget.isEntityAlive()) {
            // Клавиша W (вперед) - толкаем от себя
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), true));
            }
            // Клавиша S (назад) - притягиваем к себе
            else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), false));
            }
        }
    }
}
