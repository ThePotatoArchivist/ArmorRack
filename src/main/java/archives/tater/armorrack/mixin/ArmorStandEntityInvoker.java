package archives.tater.armorrack.mixin;

import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandEntityInvoker {
    @Invoker("playBreakSound")
    void invokePlayBreakSound();

    @Accessor
    int getDisabledSlots();

    @Invoker("poseToNbt")
    NbtCompound invokePoseToNbt();
}
