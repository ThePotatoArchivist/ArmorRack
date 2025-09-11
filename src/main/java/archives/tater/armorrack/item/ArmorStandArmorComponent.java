package archives.tater.armorrack.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static archives.tater.armorrack.ArmorRackUtil.*;

public record ArmorStandArmorComponent(@NotNull Map<Slot, ItemStack> items) implements TooltipAppender {
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
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        items().values().forEach(armorStack -> {
            if (!armorStack.isEmpty()) textConsumer.accept(armorStack.getName());
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

        public int id() {
            return id;
        }

        @Override
        public String asString() {
            return equipmentSlot.asString();
        }

        public static final Map<EquipmentSlot, Slot> REVERSE = Arrays.stream(Slot.values()).collect(Collectors.toUnmodifiableMap(slot -> slot.equipmentSlot, slot -> slot));

        public static final IntFunction<Slot> ID_TO_VALUE = ValueLists.createIndexToValueFunction(
                Slot::id, Slot.values(), ValueLists.OutOfBoundsHandling.ZERO
        );

        public static final PacketCodec<ByteBuf, Slot> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE, Slot::id);
    }

    public static final Codec<ArmorStandArmorComponent> CODEC =
            Codec.unboundedMap(StringIdentifiable.createCodec(Slot::values), ItemStack.CODEC)
                    .xmap(ArmorStandArmorComponent::new, component -> component.items);

    private static final PacketCodec<RegistryByteBuf, Map<Slot, ItemStack>> SLOTS_PACKET_CODEC = PacketCodecs.map(Object2ObjectOpenHashMap::new, Slot.PACKET_CODEC, ItemStack.PACKET_CODEC);

    public static final PacketCodec<RegistryByteBuf, ArmorStandArmorComponent> PACKET_CODEC =
            SLOTS_PACKET_CODEC.xmap(ArmorStandArmorComponent::new, component -> component.items);
}
