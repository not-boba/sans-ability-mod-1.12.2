package com.sansmod.client;

import com.sansmod.network.PacketHandler;
import com.sansmod.network.PacketTelekinesis;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class TelekinesisHandler {
    private static Entity lockedTarget = null;
    private static boolean wasRMouseDown = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Проверяем нажатие ПКМ (кнопка 1) для захвата цели
        boolean rMouseDown = Mouse.isButtonDown(1);
        if (rMouseDown && !wasRMouseDown) {
            RayTraceResult ray = mc.objectMouseOver;
            if (ray != null && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
                lockedTarget = ray.entityHit;
                System.out.println("[SansMod] ЗАХВАЧЕН МОБ: " + lockedTarget.getName());
            }
        }
        wasRMouseDown = rMouseDown;

        // Если цель захвачена и жива
        if (lockedTarget != null) {
            if (!lockedTarget.isEntityAlive() || mc.player.getDistanceSq(lockedTarget) > 900.0D) {
                lockedTarget = null;
                System.out.println("[SansMod] Цель потеряна.");
                return;
            }

            // ПКМ зажата — притягиваем к себе (push = false)
            if (Mouse.isButtonDown(1)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), false));
            }
            // ЛКМ зажата (кнопка 0) — отталкиваем от себя (push = true)
            else if (Mouse.isButtonDown(0)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), true));
            }
        }
    }
}
