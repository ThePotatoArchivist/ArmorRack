package archives.tater.armorrack.mixin;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorRackItem;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;

import org.jetbrains.annotations.Nullable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {
    @ModifyExpressionValue(
            method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/server/level/ServerLevel;Ljava/util/function/Consumer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/EntitySpawnReason;ZZ)Lnet/minecraft/world/entity/Entity;")
    )
    private <T extends Entity> @Nullable T checkCustom2(@Nullable T original, @Local(argsOnly = true)UseOnContext context) {
        if ((Object) this instanceof ArmorRackItem)
            original.setAttached(ArmorRack.IS_ARMOR_RACK, Unit.INSTANCE);

        if (!(original instanceof ArmorStand armorStand)) return original;
        var armor = context.getItemInHand().get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor == null) return original;
        armor.apply(armorStand);
        return original;
    }
}
