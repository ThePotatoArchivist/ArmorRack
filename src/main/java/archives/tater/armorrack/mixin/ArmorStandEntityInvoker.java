package archives.tater.armorrack.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.decoration.ArmorStand;

@Mixin(ArmorStand.class)
public interface ArmorStandEntityInvoker {
    @Accessor
    int getDisabledSlots();
}
