package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntityMarshall;
import com.pawpatrol.mod.model.MarshallObjLoader;
import com.pawpatrol.mod.model.ModelMarshall;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMarshall extends RenderLiving<EntityMarshall> {

    private static final ResourceLocation TEXTURE_BODY =
        new ResourceLocation("pawpatrol", "textures/entity/s2_marshall.png");
    private static final ResourceLocation TEXTURE_PACK =
        new ResourceLocation("pawpatrol", "textures/entity/s2_marshall_pack.png");

    public RenderMarshall(RenderManager rm) {
        super(rm, new ModelMarshall(), 0.4F);
    }

    @Override
    public void doRender(EntityMarshall entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0F, 1F, 0F);

        this.bindTexture(TEXTURE_BODY);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.mainModel.render(entity,
            entity.limbSwing, entity.limbSwingAmount,
            entity.ticksExisted + partialTicks,
            entity.rotationYawHead - entity.renderYawOffset,
            entity.rotationPitch, 0.0625F);

        this.bindTexture(TEXTURE_PACK);
        MarshallObjLoader.renderBackpack(entity.isSitting());

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override protected ResourceLocation getEntityTexture(EntityMarshall e) { return TEXTURE_BODY; }
    @Override protected void applyRotations(EntityMarshall e, float a, float y, float p) {}
}
