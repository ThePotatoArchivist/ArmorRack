package archives.tater.armorrack;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import org.jspecify.annotations.Nullable;

public class ArmorRackUtil {
    public static ItemStack createNullable(@Nullable ItemStackTemplate template) {
        return template == null ? ItemStack.EMPTY : template.create();
    }

    private ArmorRackUtil() {}
}
