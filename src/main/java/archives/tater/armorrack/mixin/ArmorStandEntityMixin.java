package archives.tater.armorrack.mixin;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorStandArmorComponent;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.objectweb.asm.Opcodes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@Mixin(ArmorStand.class)
public abstract class ArmorStandEntityMixin extends LivingEntity {
    protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(
            method = "hurtServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;isCreativePlayer()Z")
    )
    private boolean armorRackBreak(boolean original) {
        return original && !hasAttached(ArmorRack.IS_ARMOR_RACK);
    }

    @ModifyExpressionValue(
            method = "showBreakingParticles",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;OAK_PLANKS:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.GETSTATIC)
    )
    private Block armorRackParticles(Block original) {
        return hasAttached(ArmorRack.IS_ARMOR_RACK) ? Blocks.ANVIL : original;
    }

    @ModifyArg(
            method = "getPickResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V")
    )
    private ItemLike armorRackPick(ItemLike item) {
        return hasAttached(ArmorRack.IS_ARMOR_RACK) ? ArmorRack.EMPTY_ARMOR_RACK_ITEM : item;
    }

    @WrapOperation(
            method = "brokenByPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V")
    )
    private void armorRackDrop(Level level, BlockPos pos, ItemStack itemStack, Operation<Void> original, ServerLevel serverLevel, DamageSource source) {
        if (!hasAttached(ArmorRack.IS_ARMOR_RACK)) {
            original.call(level, pos, itemStack);
            return;
        }

        var modifiedStack = ArmorRackEntity.toItemStack((ArmorStand) (Object) this, itemStack);

        equipment.clear();

        if (source.isCreativePlayer() && modifiedStack.get(DataComponents.ENTITY_DATA) == null && modifiedStack.getOrDefault(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY).isEmpty()) return;

        if (!(source.getDirectEntity() instanceof Player player)) {
            original.call(level, pos, modifiedStack);
            return;
        }
        if ((!modifiedStack.isStackable() || !player.getInventory().hasAnyMatching(stack -> stack.is(ArmorRack.ARMOR_RACK_ITEM))) && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, modifiedStack);
            return;
        }
        if (!player.addItem(modifiedStack))
            original.call(level, pos, modifiedStack);
    }
}
