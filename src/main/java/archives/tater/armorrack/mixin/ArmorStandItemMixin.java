package archives.tater.armorrack.mixin;

import archives.tater.armorrack.item.ArmorStandProvider;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {
    @ModifyReceiver(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;getDimensions()Lnet/minecraft/entity/EntityDimensions;")
    )
    private EntityType<? extends ArmorStandEntity> checkCustom1(EntityType<ArmorStandEntity> original) {
        return this instanceof ArmorStandProvider provider ? provider.getSpawnedEntityType() : original;
    }

    @ModifyReceiver(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;")
    )
    private EntityType<? extends ArmorStandEntity> checkCustom2(EntityType<ArmorStandEntity> instance, ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Consumer<ArmorStandEntity> afterConsumer, BlockPos pos, SpawnReason reason, boolean alignPosition, boolean invertY) {
        return this instanceof ArmorStandProvider provider ? provider.getSpawnedEntityType() : instance;
    }
}
