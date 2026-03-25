package archives.tater.armorrack.entity;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.item.ArmorRackItem;
import archives.tater.armorrack.item.ArmorStandArmorComponent;
import archives.tater.armorrack.mixin.ArmorStandEntityInvoker;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;

public class ArmorRackEntity {
    private ArmorRackEntity() {}

    public static final String ARMOR_RACK_ENTITY_TRANSLATION = "entity.armorrack.armor_rack";

    private static CompoundTag saveToItemNbt(ArmorStand armorStand) {
        var nbt = new CompoundTag();
        if (armorStand.isInvisible()) nbt.putBoolean("Invisible", true);
        if (armorStand.isSmall()) nbt.putBoolean("Small", true);
        if (armorStand.showArms()) nbt.putBoolean("ShowArms", true);
        int disabledSlots = ((ArmorStandEntityInvoker) armorStand).getDisabledSlots();
        if (disabledSlots != 0) nbt.putInt("DisabledSlots", disabledSlots);
        if (!armorStand.showBasePlate()) nbt.putBoolean("NoBasePlate", true);
        if (armorStand.isMarker()) nbt.putBoolean("Marker", true);

        var rotation = armorStand.getArmorStandPose();
        if (!rotation.equals(ArmorStand.ArmorStandPose.DEFAULT)) nbt.store("Pose", ArmorStand.ArmorStandPose.CODEC, rotation);

        return nbt;
    }

    public static ItemStack toItemStack(ArmorStand armorStand, ItemStack base) {
        var entityTag = saveToItemNbt(armorStand);

        var itemStack = base.transmuteCopy(ArmorRack.ARMOR_RACK_ITEM);
        if (entityTag.size() > 1) itemStack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(armorStand.getType(), entityTag));
        itemStack.set(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.from(armorStand));

        return ArmorRackItem.flatten(itemStack);
    }

    public static ArmorStand fromItemStack(Level level, ItemStack itemStack) {
        var entity = new ArmorStand(EntityType.ARMOR_STAND, level);
        entity.setAttached(ArmorRack.IS_ARMOR_RACK, Unit.INSTANCE);
        var nbt = itemStack.get(DataComponents.ENTITY_DATA);
        if (nbt != null) nbt.loadInto(entity);
        var armor = itemStack.get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor != null) armor.apply(entity);
        return entity;
    }
}
