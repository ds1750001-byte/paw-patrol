package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntityEverest;
import com.pawpatrol.mod.model.ModelEverest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEverest extends RenderLiving<EntityEverest> {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation("pawpatrol", "textures/entity/everest.png");

    public RenderEverest(RenderManager rm) {
        super(rm, new ModelEverest(), 0.4F);
    }

    @Override
    public void doRender(EntityEverest entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0F, 1F, 0F);

        this.bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.mainModel.render(entity,
            entity.limbSwing, entity.limbSwingAmount,
            entity.ticksExisted + partialTicks,
            entity.rotationYawHead - entity.renderYawOffset,
            entity.rotationPitch, 0.0625F);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override protected ResourceLocation getEntityTexture(EntityEverest e) { return TEXTURE; }
    @Override protected void applyRotations(EntityEverest e, float a, float y, float p) {}
}
