package com.sansmod.client;

import com.sansmod.abilities.AbilityMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class GuiAbilityMenu extends GuiScreen {

    private static final ResourceLocation MENU_TEXTURE = new ResourceLocation("sansmod", "textures/gui/telekinesis.png");
    // Текстура снаряда для 4 временных клонов у хотбара
    private static final ResourceLocation PROJECTILE_TEXTURE = new ResourceLocation("sansmod", "textures/gui/projectile.png");

    private final int imageWidth = 128;
    private final int imageHeight = 128;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(MENU_TEXTURE);

        int guiLeft = (this.width - imageWidth) / 2;
        int guiTop = (this.height - imageHeight) / 2;

        // 1. Отрисовка основного меню с эффектом "почти в 50 раз ярче" для выбранного режима
        // Используем повышенную яркость/насыщенность цвета через OpenGL
        GlStateManager.color(50.0F, 50.0F, 50.0F, 1.0F); // Подсветка яркости (~в 50 раз)
        drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        
        // Сбрасываем цвет обратно к стандартному, чтобы не закрасить остальной интерфейс
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Текст заголовка
        String title = "Active: " + KeyInputHandler.currentMode.getName();
        this.drawCenteredString(this.fontRenderer, title, this.width / 2, guiTop - 15, 0xFFFFFF);

        // 2. Отрисовка 4 временных клонов projectile.png слева от хотбара
        mc.getTextureManager().bindTexture(PROJECTILE_TEXTURE);
        
        // Находим координаты хотбара (стандартный центр экрана по горизонтали, низ экрана)
        int hotbarX = (this.width / 2) - 91; // Начало хотбара слева
        int hotbarY = this.height - 22;      // Высота хотбара

        int iconSize = 16;
        int spacing = 4;
        // Размещаем 4 клона вертикально или горизонтально слева от хотбара
        int startX = hotbarX - (iconSize * 4) - (spacing * 3) - 10; 
        int startY = hotbarY + 3;

        for (int i = 0; i < 4; i++) {
            int currentX = startX + (i * (iconSize + spacing));
            drawModalRectWithCustomSizedTexture(currentX, startY, 0, 0, iconSize, iconSize, iconSize, iconSize);
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
