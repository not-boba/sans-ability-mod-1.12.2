package com.sansmod.network;

import com.sansmod.SansMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SansMod.MODID);
    public static int packetId = 0;

    public static void registerMessages() {
        INSTANCE.registerMessage(PacketTelekinesis.Handler.class, PacketTelekinesis.class, packetId++, Side.SERVER);
    }
}
