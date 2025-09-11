package archives.tater.armorrack.client.render;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorStandArmorComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<World, Map<ItemData, ArmorRackEntity>> CACHE = new WeakHashMap<>();

    public static ArmorRackEntity getOrCreate(ItemStack itemStack, World world) {
        return CACHE.computeIfAbsent(
                world,
                _world -> new WeakHashMap<>()
        ).computeIfAbsent(
                new ItemData(itemStack.get(ArmorRack.ARMOR_STAND_ARMOR), itemStack.get(DataComponentTypes.ENTITY_DATA)),
                (_data) -> ArmorRackEntity.fromItemStack(world, itemStack)
        );
    }

    public record ItemData(
            @Nullable ArmorStandArmorComponent armor,
            @Nullable NbtComponent entityData
    ) {}
}
