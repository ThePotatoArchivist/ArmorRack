package archives.tater.armorrack;

import archives.tater.armorrack.item.ArmorStandArmorComponent;
import net.minecraft.component.type.NbtComponent;

public record ArmorRackItemData(
        ArmorStandArmorComponent armor,
        NbtComponent entityData
) {}
