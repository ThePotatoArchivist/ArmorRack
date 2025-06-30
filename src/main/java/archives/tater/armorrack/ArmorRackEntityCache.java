package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<ArmorRackItemData, ArmorRackEntity> CACHE = new WeakHashMap<>();

    public static ArmorRackEntity getOrCreate(ItemStack itemStack, World world) {
        return CACHE.computeIfAbsent(
                new ArmorRackItemData(itemStack.get(ArmorRack.ARMOR_STAND_ARMOR), itemStack.get(DataComponentTypes.ENTITY_DATA)),
                (_data) -> ArmorRackEntity.fromItemStack(world, itemStack)
        );
    }
}
