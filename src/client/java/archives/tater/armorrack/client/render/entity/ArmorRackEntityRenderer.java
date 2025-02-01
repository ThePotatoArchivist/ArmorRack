package archives.tater.armorrack.client.render.entity;

import archives.tater.armorrack.ArmorRack;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Identifier;

public class ArmorRackEntityRenderer extends ArmorStandEntityRenderer {
    public static final Identifier TEXTURE = ArmorRack.id("textures/entity/armorrack.png");

    public ArmorRackEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ArmorStandEntity armorStandEntity) {
        return TEXTURE;
    }
}
