package archives.tater.armorrack.mixin;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorStandProvider;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {
    @ModifyReceiver(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;getDimensions()Lnet/minecraft/world/entity/EntityDimensions;")
    )
    private EntityType<? extends ArmorStand> checkCustom1(EntityType<ArmorStand> original) {
        return this instanceof ArmorStandProvider provider ? provider.getSpawnedEntityType() : original;
    }

    @ModifyReceiver(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/EntitySpawnReason;ZZ)Lnet/minecraft/world/entity/Entity;")
    )
    private EntityType<? extends ArmorStand> checkCustom2(EntityType<ArmorStand> instance, ServerLevel world, @Nullable Consumer<ArmorStand> afterConsumer, BlockPos pos, EntitySpawnReason reason, boolean alignPosition, boolean invertY) {
        return this instanceof ArmorStandProvider provider ? provider.getSpawnedEntityType() : instance;
    }

    @ModifyExpressionValue(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/EntitySpawnReason;ZZ)Lnet/minecraft/world/entity/Entity;")
    )
    private <T extends Entity> @Nullable T checkCustom2(@Nullable T original, @Local(argsOnly = true)UseOnContext context) {
        if (!(original instanceof ArmorStand armorStand)) return original;
        var armor = context.getItemInHand().get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor == null) return original;
        armor.apply(armorStand);
        return original;
    }
}
