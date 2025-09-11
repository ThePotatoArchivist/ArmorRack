package archives.tater.armorrack.entity;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorRackItem;
import archives.tater.armorrack.item.ArmorStandArmorComponent;
import archives.tater.armorrack.mixin.ArmorStandEntityInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
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
    public void breakAndDropItem(ServerWorld serverWorld, DamageSource damageSource) {
        var itemStack = this.toItemStack();

        if (damageSource.isSourceCreativePlayer() && itemStack.get(DataComponentTypes.ENTITY_DATA) == null && itemStack.get(ArmorRack.ARMOR_STAND_ARMOR) == null) return;

        if (damageSource.getSource() instanceof PlayerEntity player) {
            if ((!itemStack.isStackable() || !player.getInventory().containsAny(Set.of(itemStack.getItem()))) && player.getStackInHand(player.getActiveHand()).isEmpty()) {
                player.setStackInHand(player.getActiveHand(), itemStack);
            } else {
                if (!player.giveItemStack(itemStack)) Block.dropStack(getWorld(), getBlockPos(), itemStack);
            }
        } else {
            Block.dropStack(getWorld(), getBlockPos(), itemStack);
        }
        ((ArmorStandEntityInvoker) this).invokePlayBreakSound();
        drop(serverWorld, damageSource);
    }

    private NbtCompound saveToItemNbt() {
        var nbt = new NbtCompound();
        nbt.putString("id", Registries.ENTITY_TYPE.getId(getType()).toString());
        if (this.isInvisible()) nbt.putBoolean("Invisible", true);
        if (this.isSmall()) nbt.putBoolean("Small", true);
        if (this.shouldShowArms()) nbt.putBoolean("ShowArms", true);
        int disabledSlots = ((ArmorStandEntityInvoker) this).getDisabledSlots();
        if (disabledSlots != 0) nbt.putInt("DisabledSlots", disabledSlots);
        if (!this.shouldShowBasePlate()) nbt.putBoolean("NoBasePlate", true);
        if (this.isMarker()) nbt.putBoolean("Marker", true);

        var rotation = packRotation();
        if (!rotation.equals(PackedRotation.DEFAULT)) nbt.put("Pose", PackedRotation.CODEC, rotation);

        return nbt;
    }

    public ItemStack toItemStack() {
        var entityTag = saveToItemNbt();

        var itemStack = new ItemStack(ArmorRack.ARMOR_RACK_ITEM);
        if (entityTag.getSize() > 1) itemStack.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(entityTag));
        itemStack.set(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.from(this));

        var resultStack = ArmorRackItem.flatten(itemStack);

        if (this.hasCustomName()) {
            resultStack.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
        }

        return resultStack;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return ArmorRack.EMPTY_ARMOR_RACK_ITEM.getDefaultStack();
    }

    public static ArmorRackEntity fromItemStack(World world, ItemStack itemStack) {
        var entity =  new ArmorRackEntity(ArmorRack.ARMOR_RACK_ENTITY, world);
        var nbt = itemStack.get(DataComponentTypes.ENTITY_DATA);
        if (nbt != null) nbt.applyToEntity(entity);
        var armor = itemStack.get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor != null) armor.apply(entity);
        return entity;
    }
}
