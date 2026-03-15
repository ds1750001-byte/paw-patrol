package com.pawpatrol.mod.model;

import com.pawpatrol.mod.entity.EntityRubble;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRubble extends ModelBase {

    public ModelRubble() {}

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount,
                       float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        EntityRubble rubble = (EntityRubble) entityIn;
        boolean sitting = rubble.isSitting();

        GlStateManager.pushMatrix();

        // Рабл чуть пониже Чейза (height 0.6 vs 0.74), масштаб подбираем
        float modelScale = 1.55F * scale;
        GlStateManager.scale(modelScale, modelScale, modelScale);
        GlStateManager.translate(0.0F, -0.5F / modelScale, 0.0F);

        if (sitting) {
            GlStateManager.translate(0F, -0.12F, 0.05F);
            GlStateManager.rotate(18F, 1F, 0F, 0F);
        } else {
            float walkBob = (float) Math.sin(limbSwing * 0.6662F) * limbSwingAmount * 0.15F;
            float breath  = (float) Math.sin(ageInTicks * 0.05F) * 0.02F;
            GlStateManager.translate(0F, walkBob + breath, 0F);
        }

        RubbleObjLoader.renderBody(limbSwing, limbSwingAmount, sitting);
        RubbleObjLoader.renderBackpack(sitting);

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {}
}
