package com.sansmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTelekinesis implements IMessage {
    private int targetId;
    private boolean push;

    public PacketTelekinesis() {}

    public PacketTelekinesis(int targetId, boolean push) {
        this.targetId = targetId;
        this.push = push;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.targetId = buf.readInt();
        this.push = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(targetId);
        buf.writeBoolean(push);
    }

    public static class Handler implements IMessageHandler<PacketTelekinesis, IMessage> {
        @Override
        public IMessage onMessage(PacketTelekinesis message, MessageContext ctx) {
            // Исправлено получение игрока на сервере Forge 1.12.2
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            player.getServerWorld().addScheduledTask(() -> {
                Entity target = player.getServerWorld().getEntityByID(message.targetId);
                if (target instanceof EntityLivingBase && player.getDistanceSq(target) <= 900.0D) {
                    Vec3d look = player.getLookVec();
                    double mult = message.push ? 1.5D : -1.5D;

                    target.motionX = look.x * mult;
                    target.motionY = look.y * mult + 0.3D;
                    target.motionZ = look.z * mult;
                    target.isAirBorne = true;
                    target.velocityChanged = true;
                }
            });
            return null;
        }
    }
}
