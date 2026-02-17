package archives.tater.armorrack.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import static archives.tater.armorrack.ArmorRackUtil.*;

public record ArmorStandArmorComponent(@NotNull Map<Slot, ItemStack> items) implements TooltipProvider {
    public ArmorStandArmorComponent {
        items = filterValues(items, stack -> !stack.isEmpty());
    }

    public ItemStack get(Slot slot) {
        return items.getOrDefault(slot, ItemStack.EMPTY);
    }

    public ItemStack get(EquipmentSlot slot) {
        return get(Slot.REVERSE.get(slot));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        items().values().forEach(armorStack -> {
            if (!armorStack.isEmpty()) textConsumer.accept(armorStack.getHoverName());
        });
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArmorStandArmorComponent that = (ArmorStandArmorComponent) o;
        return stackMapsEqual(this.items, that.items);
    }

    @Override
    public int hashCode() {
        return stackMapHash(items);
    }

    public void apply(ArmorStand armorStand) {
        for (var slot : Slot.values()) {
            armorStand.setItemSlot(slot.equipmentSlot, get(slot));
        }
    }

    public static ArmorStandArmorComponent from(ArmorStand armorStand) {
        return new ArmorStandArmorComponent(Arrays.stream(Slot.values())
                .collect(Collectors.toMap(slot -> slot, slot -> armorStand.getItemBySlot(slot.equipmentSlot)))
        );
    }

    public static final ArmorStandArmorComponent EMPTY = new ArmorStandArmorComponent(Map.of());

    public enum Slot implements StringRepresentable {
        HEAD(0, EquipmentSlot.HEAD),
        CHEST(1, EquipmentSlot.CHEST),
        LEGS(2, EquipmentSlot.LEGS),
        FEET(3, EquipmentSlot.FEET),
        MAINHAND(4, EquipmentSlot.MAINHAND),
        OFFHAND(5, EquipmentSlot.OFFHAND);

        public final int id;
        public final EquipmentSlot equipmentSlot;

        Slot(int id, EquipmentSlot equipmentSlot) {
            this.id = id;
            this.equipmentSlot = equipmentSlot;
        }

        public int id() {
            return id;
        }

        @Override
        public String getSerializedName() {
            return equipmentSlot.getSerializedName();
        }

        public static final Map<EquipmentSlot, Slot> REVERSE = Arrays.stream(Slot.values()).collect(Collectors.toUnmodifiableMap(slot -> slot.equipmentSlot, slot -> slot));

        public static final IntFunction<Slot> ID_TO_VALUE = ByIdMap.continuous(
                Slot::id, Slot.values(), ByIdMap.OutOfBoundsStrategy.ZERO
        );

        public static final StreamCodec<ByteBuf, Slot> PACKET_CODEC = ByteBufCodecs.idMapper(ID_TO_VALUE, Slot::id);
    }

    public static final Codec<ArmorStandArmorComponent> CODEC =
            Codec.unboundedMap(StringRepresentable.fromEnum(Slot::values), ItemStack.CODEC)
                    .xmap(ArmorStandArmorComponent::new, component -> component.items);

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Slot, ItemStack>> SLOTS_PACKET_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, Slot.PACKET_CODEC, ItemStack.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorStandArmorComponent> PACKET_CODEC =
            SLOTS_PACKET_CODEC.map(ArmorStandArmorComponent::new, component -> component.items);
}
