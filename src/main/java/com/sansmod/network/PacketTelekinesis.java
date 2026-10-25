package com.sansmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
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
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) return null;

            player.getServerWorld().addScheduledTask(() -> {
                Entity target = player.getServerWorld().getEntityByID(message.targetId);
                if (target instanceof EntityLivingBase && player.getDistanceSq(target) <= 900.0D) {
                    Vec3d look = player.getLookVec();
                    double mult = message.push ? 0.6D : -0.6D;

                    // Используем addVelocity — он корректно суммирует импульс
                    target.addVelocity(look.x * mult, 0.35D, look.z * mult);

                    target.isAirBorne = true;
                    target.velocityChanged = true;

                    // КРИТИЧЕСКИ ВАЖНО: Принудительно отправляем клиентам пакет изменения скорости сущности,
                    // чтобы сервер сообщил клиенту: "эй, этот моб полетел!"
                    player.getServerWorld().getMinecraftServer().getPlayerList()
                            .sendToAllNearExcept(player, target.posX, target.posY, target.posZ, 64.0D, 
                                    player.getServerWorld().provider.getDimension(), 
                                    new SPacketEntityVelocity(target));
                }
            });
            return null;
        }
    }
}
