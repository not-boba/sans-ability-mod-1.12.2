package com.sansmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TelekinesisOverlay extends Gui {

    // Указываем путь к твоей текстуре синей души
    private static final ResourceLocation TELEKINESIS_ICON = new ResourceLocation("sansmod", "textures/gui/telekinesis.png");

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        // Показываем иконку в углу экрана, когда моб захвачен телекинезом
        if (KeyInputHandler.telekinesisTarget != null) {
            mc.getTextureManager().bindTexture(TELEKINESIS_ICON);

            int x = 10; // Отступ слева (пиксели)
            int y = 10; // Отступ сверху (пиксели)
            int width = 32;  // Размер иконки на экране
            int height = 32;

            // Рендерим иконку
            drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        }
    }
}
