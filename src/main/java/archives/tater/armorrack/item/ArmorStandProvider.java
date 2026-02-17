package archives.tater.armorrack.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;

public interface ArmorStandProvider {
    EntityType<? extends ArmorStand> getSpawnedEntityType();
}
