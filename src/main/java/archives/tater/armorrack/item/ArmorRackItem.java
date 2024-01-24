package archives.tater.armorrack.item;

import archives.tater.armorrack.ArmorRack;
import archives.tater.armorrack.entity.ArmorRackEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class ArmorRackItem extends Item {
    public ArmorRackItem(Settings settings) {
        super(settings);
    }

    @Override
    // This was mostly copied from ArmorStandItem
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction direction = context.getSide();
        if (direction == Direction.DOWN) {
            return ActionResult.FAIL;
        }
        World world = context.getWorld();
        ItemPlacementContext itemPlacementContext = new ItemPlacementContext(context);
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        ItemStack itemStack = context.getStack();
        Vec3d vec3d = Vec3d.ofBottomCenter(blockPos);
        Box box = ArmorRack.ARMOR_RACK_ENTITY.getDimensions().getBoxAt(vec3d.getX(), vec3d.getY(), vec3d.getZ());
        if (!world.isSpaceEmpty(null, box) || !world.getOtherEntities(null, box).isEmpty()) {
            return ActionResult.FAIL;
        }
        if (world instanceof ServerWorld serverWorld) {
            Consumer<ArmorRackEntity> consumer = EntityType.copier(serverWorld, itemStack, context.getPlayer());
            ArmorRackEntity armorRackEntity = ArmorRack.ARMOR_RACK_ENTITY.create(serverWorld, itemStack.getNbt(), consumer, blockPos, SpawnReason.SPAWN_EGG, true, true);
            if (armorRackEntity == null) {
                return ActionResult.FAIL;
            }
            float f = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getPlayerYaw() - 180.0f) + 22.5f) / 45.0f) * 45.0f;
            armorRackEntity.refreshPositionAndAngles(armorRackEntity.getX(), armorRackEntity.getY(), armorRackEntity.getZ(), f, 0.0f);
            serverWorld.spawnEntityAndPassengers(armorRackEntity);
            world.playSound(null, armorRackEntity.getX(), armorRackEntity.getY(), armorRackEntity.getZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f);
            armorRackEntity.emitGameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
        }
        itemStack.decrement(1);
        return ActionResult.success(world.isClient);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) return super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);
        return trySwap(user, hand, itemStack) ? TypedActionResult.success(itemStack) : super.use(world, user, hand);
    }

    private static boolean trySwap(PlayerEntity user, Hand hand, ItemStack itemStack) {
        NbtCompound itemTag = itemStack.getNbt();

        NbtCompound entityTag = itemTag == null ? null : itemTag.getCompound("EntityTag");
        NbtList armorItems = entityTag == null ? null : entityTag.getList("ArmorItems", NbtElement.COMPOUND_TYPE);

        boolean itemEmpty = armorItems == null || armorItems.isEmpty();

        List<ItemStack> equippedArmorItems = StreamSupport.stream(user.getArmorItems().spliterator(), false).toList();
        boolean equippedEmpty = equippedArmorItems.isEmpty();
        if (equippedEmpty && itemEmpty) return false;


        NbtList resultArmorItems = new NbtList();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            ItemStack armorItem = itemEmpty ? ItemStack.EMPTY : ItemStack.fromNbt(armorItems.getCompound(slot.getEntitySlotId()));
            ItemStack equippedArmor = equippedEmpty ? ItemStack.EMPTY : equippedArmorItems.get(slot.getEntitySlotId());

            if (!EnchantmentHelper.hasBindingCurse(equippedArmor)) {
                user.equipStack(slot, armorItem);
                resultArmorItems.add(equippedArmor.isEmpty() ? new NbtCompound() : equippedArmor.writeNbt(new NbtCompound()));
            } else {
                resultArmorItems.add(armorItem.isEmpty() ? new NbtCompound() : armorItem.writeNbt(new NbtCompound()));
            }
        }

        boolean oldEmpty = itemStack.isOf(ArmorRack.EMPTY_ARMOR_RACK_ITEM);
        boolean newEmpty = resultArmorItems.stream().allMatch(e -> ((NbtCompound) e).isEmpty());

        if (newEmpty == oldEmpty && itemStack.getCount() <= 1) {
            itemStack.getOrCreateSubNbt("EntityTag").put("ArmorItems", resultArmorItems);
        } else {
            ItemStack newStack = new ItemStack(newEmpty ? ArmorRack.EMPTY_ARMOR_RACK_ITEM : ArmorRack.ARMOR_RACK_ITEM);
            if ((entityTag == null || !entityTag.isEmpty()) && !newEmpty) {
                NbtCompound newEntityTag = entityTag == null ? new NbtCompound() : entityTag;
                newEntityTag.put("ArmorItems", resultArmorItems);
                newStack.setSubNbt("EntityTag", newEntityTag);
            }
            if (itemStack.getCount() > 1) {
                user.giveItemStack(newStack);
                itemStack.decrement(1);
            } else {
                user.setStackInHand(hand, newStack);
            }
        }

        return true;
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
