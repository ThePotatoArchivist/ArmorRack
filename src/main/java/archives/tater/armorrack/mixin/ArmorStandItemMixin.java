package archives.tater.armorrack.mixin;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorStandProvider;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemUsageContext;
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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;")
    )
    private EntityType<? extends ArmorStandEntity> checkCustom2(EntityType<ArmorStandEntity> instance, ServerWorld world, @Nullable Consumer<ArmorStandEntity> afterConsumer, BlockPos pos, SpawnReason reason, boolean alignPosition, boolean invertY) {
        return this instanceof ArmorStandProvider provider ? provider.getSpawnedEntityType() : instance;
    }

    @ModifyExpressionValue(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;")
    )
    private <T extends Entity> @Nullable T checkCustom2(@Nullable T original, @Local(argsOnly = true)ItemUsageContext context) {
        if (!(original instanceof ArmorStandEntity armorStand)) return original;
        var armor = context.getStack().get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor == null) return original;
        armor.apply(armorStand);
        return original;
    }
}
