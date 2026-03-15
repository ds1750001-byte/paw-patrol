package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntityRubble;
import com.pawpatrol.mod.model.ModelRubble;
import com.pawpatrol.mod.model.RubbleObjLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRubble extends RenderLiving<EntityRubble> {

    private static final ResourceLocation TEXTURE_BODY =
        new ResourceLocation("pawpatrol", "textures/entity/s2_rubble.png");
    private static final ResourceLocation TEXTURE_PACK =
        new ResourceLocation("pawpatrol", "textures/entity/s2_rubble_pack.png");

    public RenderRubble(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelRubble(), 0.4F);
    }

    @Override
    public void doRender(EntityRubble entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);

        this.bindTexture(TEXTURE_BODY);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                                  GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.mainModel.render(entity,
            entity.limbSwing, entity.limbSwingAmount,
            entity.ticksExisted + partialTicks,
            entity.rotationYawHead - entity.renderYawOffset,
            entity.rotationPitch, 0.0625F);

        this.bindTexture(TEXTURE_PACK);
        RubbleObjLoader.renderBackpack(entity.isSitting());

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityRubble entity) { return TEXTURE_BODY; }

    @Override
    protected void applyRotations(EntityRubble e, float age, float yaw, float partial) {}
}
