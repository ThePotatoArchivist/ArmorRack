package archives.tater.armorrack.mixin.client;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.ArmorRackClient;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.ArmorStand;

@Mixin(ArmorStandRenderer.class)
public class ArmorStandRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ArmorStand;Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;F)V",
            at = @At("TAIL")
    )
    private void extractIsArmorRack(ArmorStand entity, ArmorStandRenderState state, float partialTicks, CallbackInfo ci) {
        state.setData(ArmorRackClient.IS_ARMOR_RACK, entity.hasAttached(ArmorRack.IS_ARMOR_RACK));
    }

    @ModifyReturnValue(
            method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ArmorStandRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN")
    )
    private Identifier armorRackTexture(Identifier original, ArmorStandRenderState state) {
        return state.getDataOrDefault(ArmorRackClient.IS_ARMOR_RACK, false) ? ArmorRackClient.ARMOR_RACK_TEXTURE : original;
    }
}
