package archives.tater.armorrack.client.render;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import archives.tater.armorrack.item.ArmorStandArmorComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.WeakHashMap;

public class ArmorRackEntityCache {
    // Static utility class
    private ArmorRackEntityCache() {}

    private static final Map<Level, Map<TypedEntityData<?>, Map<ArmorStandArmorComponent, ArmorStandRenderState>>> CACHE = new WeakHashMap<>();

    private static final TypedEntityData<EntityType<?>> EMPTY = TypedEntityData.of(EntityTypes.ARMOR_STAND, new CompoundTag());

    public static ArmorStandRenderState getOrCreate(ItemStack itemStack, Level level) {
        return CACHE
                .computeIfAbsent(level, _ -> new WeakHashMap<>())
                .computeIfAbsent(itemStack.getOrDefault(DataComponents.ENTITY_DATA, EMPTY), _ -> new WeakHashMap<>())
                .computeIfAbsent(itemStack.getOrDefault(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY),_ -> {
                    var entity = ArmorRackEntity.fromItemStack(level, itemStack);
                    var renderer = (ArmorStandRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
                    var state = renderer.createRenderState();
                    entity.setId(-1);
                    renderer.extractRenderState(entity, state, 1f);
                    return state;
                });
    }

}
