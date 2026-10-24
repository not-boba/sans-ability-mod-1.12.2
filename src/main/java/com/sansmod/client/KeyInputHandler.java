package com.sansmod.client;

import com.sansmod.abilities.AbilityMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler {

    public static final KeyBinding SWITCH_ABILITY_KEY = new KeyBinding(
        "key.sansmod.switch_ability", 
        Keyboard.KEY_TAB, 
        "key.categories.sansmod"
    );

    public static AbilityMode currentMode = AbilityMode.BONE_THROW;

    public static void register() {
        ClientRegistry.registerKeyBinding(SWITCH_ABILITY_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (SWITCH_ABILITY_KEY.isPressed()) {
            currentMode = currentMode.next();
            
            if (Minecraft.getMinecraft().player != null) {
                Minecraft.getMinecraft().player.sendStatusMessage(
                    new TextComponentString(TextFormatting.CYAN + "[Sans Mod] " + TextFormatting.WHITE + "Способность: " + TextFormatting.YELLOW + currentMode.getName()), 
                    true // Показывать над хотбаром
                );
            }
        }
    }
}
