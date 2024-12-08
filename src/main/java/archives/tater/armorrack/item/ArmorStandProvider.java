package archives.tater.armorrack.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;

public interface ArmorStandProvider {
    EntityType<? extends ArmorStandEntity> getSpawnedEntityType();
}
