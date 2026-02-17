package archives.tater.armorrack.mixin;

import archives.tater.armorrack.entity.ArmorRackEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorStand.class)
public class ArmorStandEntityMixin {
    @ModifyExpressionValue(
            method = "hurtServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;isCreativePlayer()Z")
    )
    private boolean checkArmorRack(boolean original) {
        //noinspection ConstantValue
        return original && !(((Object) this) instanceof ArmorRackEntity);
    }
}
