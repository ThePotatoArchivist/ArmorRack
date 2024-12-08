package archives.tater.armorrack.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) return ActionResult.PASS;
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) return super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);
        var resultStack = trySwap(user, itemStack);
        return resultStack == null ? super.use(world, user, hand) : TypedActionResult.success(resultStack);
    }

    private static <T> Stream<T> stream(Iterable<T> items) {
        return StreamSupport.stream(items.spliterator(), false);
    }

    /**
     * null means interaction failed
     */
    private static @Nullable ItemStack trySwap(PlayerEntity user, ItemStack itemStack) {
        var entity = ArmorRackEntity.fromItemStack(user.getWorld(), itemStack);

        var itemEmpty = stream(entity.getArmorItems()).allMatch(ItemStack::isEmpty);

        var equippedEmpty = stream(user.getArmorItems()).allMatch(ItemStack::isEmpty);

        if (equippedEmpty && itemEmpty) return null;

        var didEquip = false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            ItemStack armorItem = entity.getEquippedStack(slot);
            ItemStack equippedArmor = user.getEquippedStack(slot);

            if (!armorItem.isEmpty()) didEquip = true;

            if (EnchantmentHelper.hasBindingCurse(equippedArmor)) continue;

            user.equipStack(slot, armorItem);
            entity.equipStack(slot, equippedArmor);
        }

        if (!didEquip) user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);

        var resultStack = entity.toItemStack();

        if (itemStack.getCount() <= 1) return resultStack;

        if (!user.giveItemStack(resultStack)) user.dropStack(resultStack);
        if (!user.getAbilities().creativeMode) itemStack.decrement(1);
        return itemStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound entityTag = stack.getSubNbt("EntityTag");
        if (entityTag == null) return;

        NbtList armorItems = entityTag.getList("ArmorItems", NbtElement.COMPOUND_TYPE);
        if (armorItems != null) {
            ArrayList<NbtElement> armorList = new ArrayList<>(armorItems);
            Collections.reverse(armorList);
            armorList.forEach(armorItem -> {
                ItemStack armorStack = ItemStack.fromNbt((NbtCompound) armorItem);
                if (!armorStack.isEmpty()) tooltip.add(armorStack.getName());
            });
        }

        NbtList handItems = entityTag.getList("HandItems", NbtElement.COMPOUND_TYPE);
        if (handItems != null) {
            handItems.forEach(handItem -> {
                ItemStack handStack = ItemStack.fromNbt((NbtCompound) handItem);
                if (!handStack.isEmpty()) tooltip.add(handStack.getName());
            });
        }
    }
}
