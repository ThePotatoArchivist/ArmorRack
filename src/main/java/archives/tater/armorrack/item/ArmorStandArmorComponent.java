package archives.tater.armorrack.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Nullable;
import oshi.annotation.concurrent.Immutable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static archives.tater.armorrack.ArmorRackUtil.filterValues;

public class ArmorStandArmorComponent {
    private final @Immutable HashMap<Slot, ItemStack> items;

    public ArmorStandArmorComponent(@Nullable Map<Slot, ItemStack> items) {
        this.items = items == null ? new HashMap<>() : new HashMap<>(filterValues(items, stack -> !stack.isEmpty()));
    }

    public Map<Slot, ItemStack> items() { return items; }

    public ItemStack get(Slot slot) { return items.getOrDefault(slot, ItemStack.EMPTY); }
    public ItemStack get(EquipmentSlot slot) { return get(Slot.REVERSE.get(slot)); }

    public boolean isEmpty() { return items.isEmpty(); }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArmorStandArmorComponent that = (ArmorStandArmorComponent) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(items);
    }

    public void apply(ArmorStandEntity armorStand) {
        for (var slot : Slot.values()) {
            armorStand.equipStack(slot.equipmentSlot, get(slot));
        }
    }

    public static ArmorStandArmorComponent from(ArmorStandEntity armorStand) {
        return new ArmorStandArmorComponent(Arrays.stream(Slot.values())
                .collect(Collectors.toMap(slot -> slot, slot -> armorStand.getEquippedStack(slot.equipmentSlot)))
        );
    }

    public static final ArmorStandArmorComponent EMPTY = new ArmorStandArmorComponent(Map.of());

    public enum Slot implements StringIdentifiable {
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

        public int id() { return id; }

        @Override
        public String asString() {
            return equipmentSlot.asString();
        }

        public static final Map<EquipmentSlot, Slot> REVERSE = Arrays.stream(Slot.values()).collect(Collectors.toUnmodifiableMap(slot -> slot.equipmentSlot, slot -> slot));

        public static final IntFunction<Slot> ID_TO_VALUE = ValueLists.createIdToValueFunction(
                Slot::id, Slot.values(), ValueLists.OutOfBoundsHandling.ZERO
        );

        public static final PacketCodec<ByteBuf, Slot> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, Slot::id);
    }

    public static final Codec<ArmorStandArmorComponent> CODEC =
            Codec.unboundedMap(StringIdentifiable.createCodec(Slot::values), ItemStack.CODEC)
                    .xmap(ArmorStandArmorComponent::new, component -> component.items);

    public static final PacketCodec<RegistryByteBuf, ArmorStandArmorComponent> PACKET_CODEC =
            PacketCodecs.map(HashMap::new, Slot.PACKET_CODEC, ItemStack.PACKET_CODEC).xmap(ArmorStandArmorComponent::new, component -> component.items);
}
