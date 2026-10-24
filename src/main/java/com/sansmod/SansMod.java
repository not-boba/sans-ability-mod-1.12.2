package com.sansmod;

import com.sansmod.items.ItemSharpBone;
import com.sansmod.network.PacketHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = SansMod.MODID, name = SansMod.NAME, version = SansMod.VERSION)
@Mod.EventBusSubscriber
public class SansMod {
    public static final String MODID = "sansmod";
    public static final String NAME = "Sans Abilities";
    public static final String VERSION = "1.0.0";

    public static final ItemSharpBone SHARP_BONE = new ItemSharpBone();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[SansMod] Initialized successfully!");

        // Регистрация сетевых пакетов (телекинез и т.д.)
        PacketHandler.registerMessages();

        // Регистрация клиентских обработчиков и интерфейсов
        if (event.getSide().isClient()) {
            com.sansmod.client.KeyInputHandler.register();
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.sansmod.client.KeyInputHandler());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.sansmod.client.TelekinesisHandler());
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.sansmod.client.TelekinesisOverlay());
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(SHARP_BONE);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(
            SHARP_BONE, 
            0, 
            new ModelResourceLocation(SHARP_BONE.getRegistryName(), "inventory")
        );
    }
}
