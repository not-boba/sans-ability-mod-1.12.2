package com.sansmod.client;

import com.sansmod.abilities.AbilityMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class KeyInputHandler {

    public static final KeyBinding SWITCH_ABILITY_KEY = new KeyBinding("key.sansmod.switch", Keyboard.KEY_TAB, "key.categories.sansmod");
    public static final KeyBinding USE_ABILITY_KEY = new KeyBinding("key.sansmod.use", Keyboard.KEY_X, "key.categories.sansmod");
    public static final KeyBinding TELEKINESIS_KEY = new KeyBinding("key.sansmod.telekinesis", Keyboard.KEY_C, "key.categories.sansmod");

    public static AbilityMode currentMode = AbilityMode.BONE_THROW;
    
    // Вот эта переменная, которой не хватало!
    public static EntityLivingBase telekinesisTarget = null;

    public static void register() {
        ClientRegistry.registerKeyBinding(SWITCH_ABILITY_KEY);
        ClientRegistry.registerKeyBinding(USE_ABILITY_KEY);
        ClientRegistry.registerKeyBinding(TELEKINESIS_KEY);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        // Переключение способностей на TAB (или открытие твоего кастомного GUI меню!)
        if (SWITCH_ABILITY_KEY.isPressed()) {
            currentMode = currentMode.next();
            mc.player.sendStatusMessage(
                new TextComponentString(TextFormatting.AQUA + "[Sans Mod] " + TextFormatting.WHITE + "Ability: " + TextFormatting.YELLOW + currentMode.getName()), 
                true
            );
        }

        // Использование активной абилки на X
        if (USE_ABILITY_KEY.isPressed()) {
            switch (currentMode) {
                case BONE_THROW:
                    mc.player.sendStatusMessage(new TextComponentString(TextFormatting.WHITE + "Bone Shot!"), true);
                    break;
                case GASTER_BLASTER:
                    mc.player.sendStatusMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Gaster Blaster summoned!"), true);
                    break;
                default:
                    break;
            }
        }

        // Выбор цели Телекинеза на C
        if (TELEKINESIS_KEY.isPressed()) {
            telekinesisTarget = getTargetEntity(mc, 15.0D);
            if (telekinesisTarget != null) {
                mc.player.sendStatusMessage(
                    new TextComponentString(TextFormatting.BLUE + "Telekinesis locked on: " + TextFormatting.WHITE + telekinesisTarget.getName() + TextFormatting.GRAY + " [W - Push | S - Pull]"), 
                    true
                );
            } else {
                mc.player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "No target in line of sight!"), true);
            }
        }
    }

    private EntityLivingBase getTargetEntity(Minecraft mc, double distance) {
        Vec3d eyes = mc.player.getPositionEyes(1.0F);
        Vec3d look = mc.player.getLook(1.0F);
        Vec3d targetVec = eyes.add(look.x * distance, look.y * distance, look.z * distance);

        AxisAlignedBB searchBox = mc.player.getEntityBoundingBox().expand(look.x * distance, look.y * distance, look.z * distance).grow(1.0D);
        List<EntityLivingBase> entities = mc.world.getEntitiesWithinAABB(EntityLivingBase.class, searchBox, e -> e != mc.player);

        EntityLivingBase closestEntity = null;
        double closestDist = distance;

        for (EntityLivingBase entity : entities) {
            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(0.3D);
            RayTraceResult intercept = aabb.calculateIntercept(eyes, targetVec);
            if (intercept != null) {
                double dist = eyes.distanceTo(intercept.hitVec);
                if (dist < closestDist) {
                    closestDist = dist;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }
}
