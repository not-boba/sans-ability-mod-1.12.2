package com.sansmod.client;

import com.sansmod.network.PacketHandler;
import com.sansmod.network.PacketTelekinesis;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class TelekinesisHandler {
    private static Entity lockedTarget = null;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Если цель умерла или отбежала слишком далеко — сбрасываем захват
        if (lockedTarget != null && (!lockedTarget.isEntityAlive() || mc.player.getDistanceSq(lockedTarget) > 900.0D)) {
            lockedTarget = null;
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        // Кнопка 1 — это ПКМ (Правая кнопка мыши) для захвата цели
        if (Mouse.isButtonDown(1)) {
            RayTraceResult ray = mc.objectMouseOver;
            if (ray != null && ray.typeOfHit == RayTraceResult.Type.ENTITY) {
                lockedTarget = ray.entityHit;
                System.out.println("[SansMod] Target locked: " + lockedTarget.getName());
            }
        }

        // Если цель уже захвачена, управляем ей через мышь:
        if (lockedTarget != null && lockedTarget.isEntityAlive()) {
            // ПКМ зажата (кнопка 1) — притягиваем к себе (push = false)
            if (Mouse.isButtonDown(1)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), false));
            }
            // ЛКМ зажата (кнопка 0) — отталкиваем от себя (push = true)
            else if (Mouse.isButtonDown(0)) {
                PacketHandler.INSTANCE.sendToServer(new PacketTelekinesis(lockedTarget.getEntityId(), true));
            }
        }
        
        // Если отпустили все кнопки мыши, но цель была — можно сбросить по желанию, 
        // но пока держим захват, пока не нажмем ПКМ на воздух или другую цель.
    }
}
