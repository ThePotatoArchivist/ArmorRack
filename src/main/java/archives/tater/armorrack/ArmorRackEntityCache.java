package archives.tater.armorrack;

import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<ItemStack, ArmorRackEntity> CACHE = new WeakHashMap<>();

    public static ArmorRackEntity getOrCreate(ItemStack itemStack, World world) {
        return CACHE.computeIfAbsent(itemStack, (_stack) -> ArmorRackEntity.fromItemStack(world, itemStack));
    }
}
