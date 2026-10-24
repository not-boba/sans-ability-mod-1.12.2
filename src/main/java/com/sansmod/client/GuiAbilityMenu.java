package com.sansmod.client;

import com.sansmod.abilities.AbilityMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiAbilityMenu extends GuiScreen {

    private static final ResourceLocation MENU_TEXTURE = new ResourceLocation("sansmod", "textures/gui/telekinesis.png");
    private static final ResourceLocation PROJECTILE_TEXTURE = new ResourceLocation("sansmod", "textures/gui/projectile.png");

    private final int imageWidth = 128;
    private final int imageHeight = 128;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Убираем drawDefaultBackground(), чтобы экран не затемнялся целиком, 
        // раз меню теперь привязано к хотбару внизу.

        Minecraft mc = Minecraft.getMinecraft();
        
        // Координаты хотбара снизу по центру экрана
        int hotbarY = this.height - 22;

        // Позиционируем меню ровно по центру по горизонтали и прямо над хотбаром по вертикали
        int guiLeft = (this.width - imageWidth) / 2;
        int guiTop = hotbarY - imageHeight - 6; // Сдвигаем на 6 пикселей выше хотбара

        // 1. Отрисовка основного меню
        mc.getTextureManager().bindTexture(MENU_TEXTURE);
        GlStateManager.color(50.0F, 50.0F, 50.0F, 1.0F);
        this.drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, imageWidth, imageHeight, 128, 128);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Текст названия способности прямо над меню
        String title = "Active: " + KeyInputHandler.currentMode.getName();
        this.drawCenteredString(this.fontRenderer, title, this.width / 2, guiTop - 12, 0xFFFFFF);

        // 2. Отрисовка 4 клонов projectile.png слева от хотбара
        mc.getTextureManager().bindTexture(PROJECTILE_TEXTURE);
        
        int hotbarX = (this.width / 2) - 91;
        int iconSize = 16;
        int spacing = 4;
        int startX = hotbarX - (iconSize * 4) - (spacing * 3) - 10; 
        int startY = hotbarY + 3;

        for (int i = 0; i < 4; i++) {
            int currentX = startX + (i * (iconSize + spacing));
            this.drawModalRectWithCustomSizedTexture(currentX, startY, 0, 0, iconSize, iconSize, 16, 16);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_TAB || keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
