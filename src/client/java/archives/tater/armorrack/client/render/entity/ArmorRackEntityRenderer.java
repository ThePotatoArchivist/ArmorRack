package archives.tater.armorrack.client.render.entity;

import archives.tater.armorrack.ArmorRack;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.resources.Identifier;

public class ArmorRackEntityRenderer extends ArmorStandRenderer {
    public static final Identifier TEXTURE = ArmorRack.id("textures/entity/armorrack.png");

    public ArmorRackEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(ArmorStandRenderState armorStandEntityRenderState) {
        return DEFAULT_SKIN_LOCATION;
    }
}
