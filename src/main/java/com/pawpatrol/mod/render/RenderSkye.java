package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntitySkye;
import com.pawpatrol.mod.model.ModelSkye;
import com.pawpatrol.mod.model.SkyeObjLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkye extends RenderLiving<EntitySkye> {

    private static final ResourceLocation TEXTURE_BODY =
        new ResourceLocation("pawpatrol", "textures/entity/s2_skye.png");
    private static final ResourceLocation TEXTURE_PACK =
        new ResourceLocation("pawpatrol", "textures/entity/s2_skye_pack.png");

    public RenderSkye(RenderManager rm) {
        super(rm, new ModelSkye(), 0.4F);
    }

    @Override
    public void doRender(EntitySkye entity, double x, double y, double z,
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
        SkyeObjLoader.renderBackpack(entity.isSitting());

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override protected ResourceLocation getEntityTexture(EntitySkye e) { return TEXTURE_BODY; }
    @Override protected void applyRotations(EntitySkye e, float a, float y, float p) {}
}
