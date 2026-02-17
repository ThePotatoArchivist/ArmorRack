package archives.tater.armorrack.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class ArmorRackItem extends ArmorStandItem implements ArmorStandProvider {
    public ArmorRackItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public EntityType<ArmorRackEntity> getSpawnedEntityType() {
        return ArmorRack.ARMOR_RACK_ENTITY;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isSecondaryUseActive()) return InteractionResult.PASS;
        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (!user.isSecondaryUseActive()) return super.use(world, user, hand);
        ItemStack itemStack = user.getItemInHand(hand);
        var resultStack = trySwap(user, itemStack);

        return resultStack == null
                ? super.use(world, user, hand)
                : resultStack == itemStack
                        ? InteractionResult.SUCCESS
                        : InteractionResult.SUCCESS.heldItemTransformedTo(resultStack);
    }

    /**
     * null means interaction failed
     */
    private static @Nullable ItemStack trySwap(Player user, ItemStack stack) {
        var activeStack = stack.getCount() == 1 ? stack : user.getAbilities().instabuild ? stack.copyWithCount(1) : stack.split(1);
        var armorComponent = activeStack.getOrDefault(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY);
        var empty = armorComponent.isEmpty();
        if (empty && Arrays.stream(EquipmentSlot.values()).allMatch(slot -> !slot.isArmor() || user.getItemBySlot(slot).isEmpty()))
            return null;
        var resultArmor = new HashMap<>(armorComponent.items());
        var fullSwap = empty || armorComponent.items().keySet().stream().anyMatch(slot -> slot.equipmentSlot.isArmor() && user.getItemBySlot(slot.equipmentSlot).isEmpty());

        var didEquip = false;

        for (ArmorStandArmorComponent.Slot slot : ArmorStandArmorComponent.Slot.values()) {
            if (!slot.equipmentSlot.isArmor()) continue;

            ItemStack armorItem = armorComponent.get(slot);
            ItemStack equippedArmor = user.getItemBySlot(slot.equipmentSlot);

            if (!fullSwap && armorItem.isEmpty()) continue;

            if (!armorItem.isEmpty()) didEquip = true;

            if (EnchantmentHelper.has(equippedArmor, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))
                continue;

            user.setItemSlot(slot.equipmentSlot, armorItem);
            resultArmor.put(slot, equippedArmor);
        }

        if (!didEquip) user.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1f, 1f);

        activeStack.set(ArmorRack.ARMOR_STAND_ARMOR, new ArmorStandArmorComponent(resultArmor));

        var resultStack = flatten(activeStack);

        if (stack == activeStack) return resultStack;

        user.handleExtraItemsCreatedOnUse(resultStack);
        return stack;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack flatten(ItemStack stack) {
        @SuppressWarnings("DataFlowIssue") // checked with `contains()`
        var hasData = stack.has(DataComponents.ENTITY_DATA) && !stack.get(DataComponents.ENTITY_DATA).getUnsafe().isEmpty() || stack.has(ArmorRack.ARMOR_STAND_ARMOR) && !stack.get(ArmorRack.ARMOR_STAND_ARMOR).isEmpty();
        var isFullRack = stack.is(ArmorRack.ARMOR_RACK_ITEM);
        if (hasData == isFullRack) return stack;
        return stack.transmuteCopy(hasData ? ArmorRack.ARMOR_RACK_ITEM : ArmorRack.EMPTY_ARMOR_RACK_ITEM, stack.getCount());
    }
}
