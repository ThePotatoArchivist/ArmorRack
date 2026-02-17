package archives.tater.armorrack.client.render;

import archives.tater.armorrack.ItemStackWrapper;
import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import archives.tater.armorrack.entity.ArmorRackEntity;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<Level, Map<ItemStackWrapper, ArmorStandRenderState>> CACHE = new WeakHashMap<>();

    public static ArmorStandRenderState getOrCreate(ItemStack itemStack, Level world) {
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
