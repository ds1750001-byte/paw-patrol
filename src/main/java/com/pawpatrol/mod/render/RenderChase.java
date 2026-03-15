package com.pawpatrol.mod.render;

import com.pawpatrol.mod.entity.EntityChase;
import com.pawpatrol.mod.model.ModelChase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChase extends RenderLiving<EntityChase> {

    // Текстуры
    private static final ResourceLocation TEXTURE_BODY =
        new ResourceLocation("pawpatrol", "textures/entity/s2_chase.png");
    private static final ResourceLocation TEXTURE_BACKPACK =
        new ResourceLocation("pawpatrol", "textures/entity/s2_chase_backpack.png");

    private final ModelChase chaseModel;

    public RenderChase(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelChase(), 0.4F);
        this.chaseModel = (ModelChase) this.mainModel;
    }

    @Override
    public void doRender(EntityChase entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        // Поворот по направлению движения
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);

        // Рендерим тело с текстурой тела
        this.bindTexture(TEXTURE_BODY);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        float limbSwing = entity.limbSwing;
        float limbSwingAmount = entity.limbSwingAmount;
        float ageInTicks = entity.ticksExisted + partialTicks;

        // Рендерим через ModelChase (который вызывает ChaseObjLoader)
        this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks,
            entity.rotationYawHead - entity.renderYawOffset,
            entity.rotationPitch, 0.0625F);

        // Теперь рендерим рюкзак отдельно с его текстурой
        this.bindTexture(TEXTURE_BACKPACK);
        com.pawpatrol.mod.model.ChaseObjLoader.renderBackpack(entity.isSitting());

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        // Рендерим имя если нужно
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChase entity) {
        return TEXTURE_BODY;
    }

    @Override
    protected void applyRotations(EntityChase entityLiving, float ageInTicks,
                                   float rotationYaw, float partialTicks) {
        // Не применяем стандартные повороты - управляем вручную
    }
}
