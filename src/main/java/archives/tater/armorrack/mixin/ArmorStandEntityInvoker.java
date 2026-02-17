package archives.tater.armorrack.mixin;

import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStand.class)
public interface ArmorStandEntityInvoker {
    @Invoker("playBrokenSound")
    void invokePlayBreakSound();

    @Accessor
    int getDisabledSlots();
}
