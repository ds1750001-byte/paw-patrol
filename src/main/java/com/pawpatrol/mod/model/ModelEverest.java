package com.pawpatrol.mod.model;

import com.pawpatrol.mod.entity.EntityEverest;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEverest extends ModelBase {

    public ModelEverest() {}

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount,
                       float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        EntityEverest everest = (EntityEverest) entityIn;
        boolean sitting = everest.isSitting();

        GlStateManager.pushMatrix();
        // Эверест выше остальных (0.936) — чуть меньший масштаб
        float modelScale = 1.05F * scale;
        GlStateManager.scale(modelScale, modelScale, modelScale);
        GlStateManager.translate(0.0F, -0.5F / modelScale, 0.0F);

        if (sitting) {
            GlStateManager.translate(0F, -0.15F, 0.05F);
            GlStateManager.rotate(20F, 1F, 0F, 0F);
        } else {
            float walkBob = (float) Math.sin(limbSwing * 0.6662F) * limbSwingAmount * 0.15F;
            float breath  = (float) Math.sin(ageInTicks * 0.05F) * 0.02F;
            GlStateManager.translate(0F, walkBob + breath, 0F);
        }

        EverestObjLoader.renderBody(limbSwing, limbSwingAmount, sitting);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float a, float b, float c, float d, float e, float f, Entity en) {}
}
