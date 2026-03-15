package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntityRocky;
import com.pawpatrol.mod.model.ModelRocky;
import com.pawpatrol.mod.model.RockyObjLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRocky extends RenderLiving<EntityRocky> {

    private static final ResourceLocation TEXTURE_BODY =
        new ResourceLocation("pawpatrol", "textures/entity/s2_rocky.png");
    private static final ResourceLocation TEXTURE_PACK =
        new ResourceLocation("pawpatrol", "textures/entity/s2_rocky_pack.png");

    public RenderRocky(RenderManager rm) {
        super(rm, new ModelRocky(), 0.4F);
    }

    @Override
    public void doRender(EntityRocky entity, double x, double y, double z,
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
        RockyObjLoader.renderBackpack(entity.isSitting());

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override protected ResourceLocation getEntityTexture(EntityRocky e) { return TEXTURE_BODY; }
    @Override protected void applyRotations(EntityRocky e, float a, float y, float p) {}
}
