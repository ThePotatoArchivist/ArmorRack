package archives.tater.armorrack.entity;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorRackItem;
import archives.tater.armorrack.item.ArmorStandArmorComponent;
import archives.tater.armorrack.mixin.ArmorStandEntityInvoker;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ArmorRackEntity extends ArmorStand {
    public ArmorRackEntity(EntityType<? extends ArmorStand> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void showBreakingParticles() {
        if (this.level() instanceof ServerLevel serverWorld) {
            serverWorld.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ANVIL.defaultBlockState()), this.getX(), this.getY(0.6666666666666666), this.getZ(), 10, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05);
        }
    }

    @Override
    public void brokenByPlayer(ServerLevel serverWorld, DamageSource damageSource) {
        var itemStack = this.toItemStack();

        if (damageSource.isCreativePlayer() && itemStack.get(DataComponents.ENTITY_DATA) == null && itemStack.get(ArmorRack.ARMOR_STAND_ARMOR) == null) return;

        if (damageSource.getDirectEntity() instanceof Player player) {
            if ((!itemStack.isStackable() || !player.getInventory().hasAnyOf(Set.of(itemStack.getItem()))) && player.getItemInHand(player.getUsedItemHand()).isEmpty()) {
                player.setItemInHand(player.getUsedItemHand(), itemStack);
            } else {
                if (!player.addItem(itemStack)) Block.popResource(level(), blockPosition(), itemStack);
            }
        } else {
            Block.popResource(level(), blockPosition(), itemStack);
        }
        ((ArmorStandEntityInvoker) this).invokePlayBreakSound();
        dropAllDeathLoot(serverWorld, damageSource);
    }

    private CompoundTag saveToItemNbt() {
        var nbt = new CompoundTag();
        nbt.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(getType()).toString());
        if (this.isInvisible()) nbt.putBoolean("Invisible", true);
        if (this.isSmall()) nbt.putBoolean("Small", true);
        if (this.showArms()) nbt.putBoolean("ShowArms", true);
        int disabledSlots = ((ArmorStandEntityInvoker) this).getDisabledSlots();
        if (disabledSlots != 0) nbt.putInt("DisabledSlots", disabledSlots);
        if (!this.showBasePlate()) nbt.putBoolean("NoBasePlate", true);
        if (this.isMarker()) nbt.putBoolean("Marker", true);

        var rotation = getArmorStandPose();
        if (!rotation.equals(ArmorStandPose.DEFAULT)) nbt.store("Pose", ArmorStandPose.CODEC, rotation);

        return nbt;
    }

    public ItemStack toItemStack() {
        var entityTag = saveToItemNbt();

        var itemStack = new ItemStack(ArmorRack.ARMOR_RACK_ITEM);
        if (entityTag.size() > 1) itemStack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(getType(), entityTag));
        itemStack.set(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.from(this));

        var resultStack = ArmorRackItem.flatten(itemStack);

        if (this.hasCustomName()) {
            resultStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        }

        return resultStack;
    }

    @Override
    public ItemStack getPickResult() {
        return ArmorRack.EMPTY_ARMOR_RACK_ITEM.getDefaultInstance();
    }

    public static ArmorRackEntity fromItemStack(Level world, ItemStack itemStack) {
        var entity =  new ArmorRackEntity(ArmorRack.ARMOR_RACK_ENTITY, world);
        var nbt = itemStack.get(DataComponents.ENTITY_DATA);
        if (nbt != null) nbt.loadInto(entity);
        var armor = itemStack.get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor != null) armor.apply(entity);
        return entity;
    }
}
