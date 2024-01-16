package archives.tater.armorrack.entity;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.mixin.ArmorStandEntityMixin;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Set;

public class ArmorRackEntity extends ArmorStandEntity {
    public ArmorRackEntity(EntityType<? extends ArmorStandEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void spawnBreakParticles() {
        if (this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.ANVIL.getDefaultState()), this.getX(), this.getBodyY(0.6666666666666666), this.getZ(), 10, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05);
        }
    }

    @Override
    protected void breakAndDropItem(DamageSource damageSource) {
        ItemStack itemStack;

        NbtCompound entityTag = new NbtCompound();
        saveToItemNbt(entityTag);

        final boolean equipped = entityTag.getSize() != 0;

        if (equipped) {
            itemStack = new ItemStack(ArmorRack.ARMOR_RACK_ITEM);
            itemStack.setSubNbt("EntityTag", entityTag);
        } else {
            itemStack = new ItemStack(ArmorRack.EMPTY_ARMOR_RACK_ITEM);
        }

        if (this.hasCustomName()) {
            itemStack.setCustomName(this.getCustomName());
        }

        Entity source = damageSource.getSource();
        if (source instanceof PlayerEntity player) {
            if ((equipped || !player.getInventory().containsAny(Set.of(ArmorRack.EMPTY_ARMOR_RACK_ITEM))) && player.getStackInHand(player.getActiveHand()).isEmpty()) {
                player.setStackInHand(player.getActiveHand(), itemStack);
            } else {
                player.giveItemStack(itemStack);
            }
        } else {
            Block.dropStack(this.getWorld(), this.getBlockPos(), itemStack);
        }
        ((ArmorStandEntityMixin) this).invokePlayBreakSound();
        drop(damageSource);
    }

    private void saveToItemNbt(NbtCompound nbt) {
        NbtList armorItems = new NbtList();
        this.getArmorItems().forEach(armorItem -> armorItems.add(armorItem.isEmpty() ? new NbtCompound() : armorItem.writeNbt(new NbtCompound())));

        if (armorItems.stream().anyMatch(nbtElement -> ((NbtCompound) nbtElement).getSize() != 0))
            nbt.put("ArmorItems", armorItems);

        NbtList handItems = new NbtList();
        this.getHandItems().forEach(handItem -> handItems.add(handItem.isEmpty() ? new NbtCompound() : handItem.writeNbt(new NbtCompound())));

        if (handItems.stream().anyMatch(nbtElement -> ((NbtCompound) nbtElement).getSize() != 0))
            nbt.put("HandItems", handItems);

        if (this.isInvisible()) nbt.putBoolean("Invisible", true);
        if (this.isSmall()) nbt.putBoolean("Small", true);
        if (this.shouldShowArms()) nbt.putBoolean("ShowArms", true);
        int disabledSlots = ((ArmorStandEntityMixin) this).getDisabledSlots();
        if (disabledSlots != 0) nbt.putInt("DisabledSlots", disabledSlots);
        if (this.shouldHideBasePlate()) nbt.putBoolean("NoBasePlate", true);
        if (this.isMarker()) nbt.putBoolean("Marker", true);

        NbtCompound poseNbt = ((ArmorStandEntityMixin) this).invokePoseToNbt();
        if (poseNbt.getSize() != 0) nbt.put("Pose", poseNbt);
    }
}
