package archives.tater.armorrack.mixin;

import archives.tater.armorrack.entity.ArmorRackEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin {
    @ModifyExpressionValue(
            method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isSourceCreativePlayer()Z")
    )
    private boolean checkArmorRack(boolean original) {
        //noinspection ConstantValue
        return original && !(((Object) this) instanceof ArmorRackEntity);
    }
}
