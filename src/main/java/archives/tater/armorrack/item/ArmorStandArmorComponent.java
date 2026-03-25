package archives.tater.armorrack.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record ArmorStandArmorComponent(Map<EquipmentSlot, ItemStackTemplate> items) implements TooltipProvider {
    public @Nullable ItemStackTemplate get(EquipmentSlot slot) {
        return items.get(slot);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        for (var slot : TOOLTIP_ORDER) {
            var template = get(slot);
            if (template != null)
                textConsumer.accept(template.create().getHoverName());
        }
    }

    public void apply(ArmorStand armorStand) {
        for (var slot : EquipmentSlot.values()) {
            var template = get(slot);
            armorStand.setItemSlot(slot, template == null ? ItemStack.EMPTY : template.create());
        }
    }

    public static ArmorStandArmorComponent from(ArmorStand armorStand) {
        return new ArmorStandArmorComponent(Arrays.stream(EquipmentSlot.values())
                .map(slot -> Map.entry(slot, armorStand.getItemBySlot(slot)))
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ItemStackTemplate.fromNonEmptyStack(entry.getValue())
                ))
        );
    }

    public static final EquipmentSlot[] TOOLTIP_ORDER = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.MAINHAND,
            EquipmentSlot.OFFHAND,
            EquipmentSlot.BODY,
            EquipmentSlot.SADDLE,
    };

    public static final ArmorStandArmorComponent EMPTY = new ArmorStandArmorComponent(Map.of());

    public static final Codec<ArmorStandArmorComponent> CODEC =
            Codec.unboundedMap(StringRepresentable.fromEnum(EquipmentSlot::values), ItemStackTemplate.CODEC)
                    .xmap(ArmorStandArmorComponent::new, component -> component.items);

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<EquipmentSlot, ItemStackTemplate>> SLOTS_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, EquipmentSlot.STREAM_CODEC, ItemStackTemplate.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorStandArmorComponent> STREAM_CODEC =
            SLOTS_STREAM_CODEC.map(ArmorStandArmorComponent::new, component -> component.items);
}
