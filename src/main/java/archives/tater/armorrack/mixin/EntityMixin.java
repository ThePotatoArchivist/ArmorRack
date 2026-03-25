package archives.tater.armorrack.mixin;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

@SuppressWarnings("NonExtendableApiUsage")
@Mixin(Entity.class)
public class EntityMixin implements AttachmentTarget {
    @ModifyReturnValue(
            method = "getTypeName",
            at = @At("RETURN")
    )
    private Component armorRackName(Component original) {
        return hasAttached(ArmorRack.IS_ARMOR_RACK) ? Component.translatable(ArmorRackEntity.ARMOR_RACK_ENTITY_TRANSLATION) : original;
    }
}
