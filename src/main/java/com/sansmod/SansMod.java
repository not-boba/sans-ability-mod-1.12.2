package com.sansmod.network;

import com.sansmod.SansMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    // Создаем сетевой канал для твоего мода
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SansMod.MODID);

    private static int nextId = 0;

    public static void registerMessages() {
        // Регистрируем наш пакет телекинеза для двусторонней связи (клиент -> сервер)
        INSTANCE.registerMessage(PacketTelekinesis.Handler.class, PacketTelekinesis.class, nextId++, Side.SERVER);
        
        System.out.println("[SansMod] PacketHandler registered successfully!");
    }
}
