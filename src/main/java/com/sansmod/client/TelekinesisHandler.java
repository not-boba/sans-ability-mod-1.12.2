package com.sansmod.client;

import com.sansmod.network.PacketHandler;
import com.sansmod.network.PacketTelekinesis;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class TelekinesisHandler {
    private static Entity lockedTarget = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Проверяем, что у нас выбран режим (сравниваем через имя режима или просто если режим существует)
        if (KeyInputHandler.currentMode != null) {
            
            // Нажатие ПКМ захватывает цель под прицелом
            if (mc.gameSettings.keyBindUseItem.isPressed()) {
                RayTraceResult ray = mc.objectMouseOver;
                if (ray != null && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
                    lockedTarget = ray.entityHit;
                } else {
                    lockedTarget = null;
                }
            }

            // Если целимся в моба, считываем W (толкнуть) и S (притянуть)
            if (lockedTarget != null && lockedTarget.isEntityAlive()) {
                boolean isWPressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
                boolean isSPressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());

                if (isWPressed) {
                    // Отправляем пакет на сервер: толкнуть вперед
                    PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), true));
                } else if (isSPressed) {
                    // Отправляем пакет на сервер: притянуть назад
                    PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), false));
                }
            } else {
                lockedTarget = null;
            }
        } else {
            lockedTarget = null;
        }
    }
}
