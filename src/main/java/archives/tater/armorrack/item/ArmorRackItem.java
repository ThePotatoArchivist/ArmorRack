package archives.tater.armorrack.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ArmorRackItem extends ArmorStandItem implements ArmorStandProvider {
    public ArmorRackItem(Settings settings) {
        super(settings);
    }

    @Override
    public EntityType<ArmorRackEntity> getSpawnedEntityType() {
        return ArmorRack.ARMOR_RACK_ENTITY;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() != null && context.getPlayer().shouldCancelInteraction()) return ActionResult.PASS;
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.shouldCancelInteraction()) return super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);
        var resultStack = trySwap(user, itemStack);
        return resultStack == null ? super.use(world, user, hand) : TypedActionResult.success(resultStack);
    }

    /**
     * null means interaction failed
     */
    private static @Nullable ItemStack trySwap(PlayerEntity user, ItemStack stack) {
        var activeStack = stack.getCount() == 1 ? stack : user.getAbilities().creativeMode ? stack.copyWithCount(1) : stack.split(1);
        var armorComponent = activeStack.getOrDefault(ArmorRack.ARMOR_STAND_ARMOR, ArmorStandArmorComponent.EMPTY);
        var empty = armorComponent.isEmpty();
        if (empty && Arrays.stream(EquipmentSlot.values()).allMatch(slot -> !slot.isArmorSlot() || user.getEquippedStack(slot).isEmpty())) return null;
        var resultArmor = new HashMap<>(armorComponent.items());
        var fullSwap = empty || armorComponent.items().keySet().stream().anyMatch(slot -> slot.equipmentSlot.isArmorSlot() && user.getEquippedStack(slot.equipmentSlot).isEmpty());

        var didEquip = false;

        for (ArmorStandArmorComponent.Slot slot : ArmorStandArmorComponent.Slot.values()) {
            if (!slot.equipmentSlot.isArmorSlot()) continue;

            ItemStack armorItem = armorComponent.get(slot);
            ItemStack equippedArmor = user.getEquippedStack(slot.equipmentSlot);

            if (!fullSwap && armorItem.isEmpty()) continue;

            if (!armorItem.isEmpty()) didEquip = true;

            if (EnchantmentHelper.hasAnyEnchantmentsWith(equippedArmor, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)) continue;

            user.equipStack(slot.equipmentSlot, armorItem);
            resultArmor.put(slot, equippedArmor);
        }

        if (!didEquip) user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), 1f, 1f);

        activeStack.set(ArmorRack.ARMOR_STAND_ARMOR, new ArmorStandArmorComponent(resultArmor));

        var resultStack = flatten(activeStack);

        if (stack == activeStack) return resultStack;

        if (!user.giveItemStack(resultStack)) user.dropStack(resultStack);
        return stack;
    }

    public static ItemStack flatten(ItemStack stack) {
        @SuppressWarnings("DataFlowIssue") // checked with `contains()`
        var hasData = stack.contains(DataComponentTypes.ENTITY_DATA) && !stack.get(DataComponentTypes.ENTITY_DATA).isEmpty() || stack.contains(ArmorRack.ARMOR_STAND_ARMOR) && !stack.get(ArmorRack.ARMOR_STAND_ARMOR).isEmpty();
        var isFullRack = stack.isOf(ArmorRack.ARMOR_RACK_ITEM);
        if (hasData == isFullRack) return stack;
        return stack.copyComponentsToNewStack(hasData ? ArmorRack.ARMOR_RACK_ITEM : ArmorRack.EMPTY_ARMOR_RACK_ITEM, stack.getCount());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var armor = stack.get(ArmorRack.ARMOR_STAND_ARMOR);
        if (armor == null) return;
        armor.items().values().forEach(armorStack -> {
            if (!armorStack.isEmpty()) tooltip.add(armorStack.getName());
        });
    }
}
