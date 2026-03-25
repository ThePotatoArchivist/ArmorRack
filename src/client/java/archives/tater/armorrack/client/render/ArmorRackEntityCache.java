package archives.tater.armorrack.client.render;

import archives.tater.armorrack.client.render.entity.ArmorRackEntityRenderer;
import archives.tater.armorrack.entity.ArmorRackEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<Level, Map<ItemStack, ArmorStandRenderState>> CACHE = new WeakHashMap<>();

    public static ArmorStandRenderState getOrCreate(ItemStack itemStack, Level world) {
        return CACHE.computeIfAbsent(
                world,
                _ -> new WeakHashMap<>()
        ).computeIfAbsent(
                itemStack,
                _ -> {
                    var entity = ArmorRackEntity.fromItemStack(world, itemStack);
                    var renderer = (ArmorRackEntityRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
                    var state = renderer.createRenderState();
                    renderer.extractRenderState(entity, state, 1f);
                    return state;
                }
        );
    }

}
