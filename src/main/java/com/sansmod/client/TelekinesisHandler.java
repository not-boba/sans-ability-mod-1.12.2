package com.sansmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TelekinesisHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || KeyInputHandler.telekinesisTarget == null) return;

        // Если цель умерла или слишком далеко — сбрасываем захват
        if (!KeyInputHandler.telekinesisTarget.isEntityAlive() || mc.player.getDistance(KeyInputHandler.telekinesisTarget) > 25.0D) {
            KeyInputHandler.telekinesisTarget = null;
            return;
        }

        Vec3d look = mc.player.getLookVec();
        
        // Нажата W — Отталкиваем цель по вектору взгляда
        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            KeyInputHandler.telekinesisTarget.motionX = look.x * 1.8D;
            KeyInputHandler.telekinesisTarget.motionY = look.y * 1.8D + 0.2D;
            KeyInputHandler.telekinesisTarget.motionZ = look.z * 1.8D;
            KeyInputHandler.telekinesisTarget.velocityChanged = true;
        }

        // Нажата S — Притягиваем цель к себе
        if (mc.gameSettings.keyBindBack.isKeyDown()) {
            KeyInputHandler.telekinesisTarget.motionX = -look.x * 1.8D;
            KeyInputHandler.telekinesisTarget.motionY = -look.y * 1.8D + 0.2D;
            KeyInputHandler.telekinesisTarget.motionZ = -look.z * 1.8D;
            KeyInputHandler.telekinesisTarget.velocityChanged = true;
        }
    }
}
