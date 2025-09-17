package archives.tater.armorrack.client.render;

import archives.tater.armorrack.ItemStackWrapper;
import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<World, Map<ItemStackWrapper, ArmorStandEntityRenderState>> CACHE = new WeakHashMap<>();

    public static ArmorStandEntityRenderState getOrCreate(ItemStack itemStack, World world) {
        return CACHE.computeIfAbsent(
                world,
                _world -> new WeakHashMap<>()
        ).computeIfAbsent(
                new ItemStackWrapper(itemStack),
                (_data) -> {
                    var entity = ArmorRackEntity.fromItemStack(world, itemStack);
                    var renderer = (ArmorRackEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
                    var state = renderer.createRenderState();
                    renderer.updateRenderState(entity, state, 1f);
                    return state;
                }
        );
    }

}
