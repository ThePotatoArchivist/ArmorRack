package archives.tater.armorrack;

import net.minecraft.item.ItemStack;

public record ItemStackWrapper(ItemStack stack) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemStackWrapper that = (ItemStackWrapper) o;
        return ItemStack.areItemsAndComponentsEqual(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return ItemStack.hashCode(stack);
    }
}
