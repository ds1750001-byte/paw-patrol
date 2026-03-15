package com.pawpatrol.mod.model;

import com.pawpatrol.mod.entity.EntityChase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Модель Чейза из Щенячьего патруля.
 * Рендерит OBJ-меш напрямую через Tessellator.
 * Анимации: ходьба (покачивание тела), idle (дыхание).
 */
@SideOnly(Side.CLIENT)
public class ModelChase extends ModelBase {

    // Данные меша загружаются из OBJ через ChaseObjLoader
    private float walkAnim = 0;
    private float breathAnim = 0;

    public ModelChase() {
        // Модель загружается через OBJ лоадер, здесь только инициализация
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount,
                       float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        EntityChase chase = (EntityChase) entityIn;
        boolean sitting = chase.isSitting();

        GlStateManager.pushMatrix();

        // Масштаб: OBJ в диапазоне ~0-0.74 единиц, нужно привести к размеру Minecraft
        // Масштаб ~1.35 даёт высоту ~1 блок
        float modelScale = 1.35F * scale;
        GlStateManager.scale(modelScale, modelScale, modelScale);

        // Центрируем модель
        GlStateManager.translate(0.0F, -0.5F / modelScale, 0.0F);

        if (sitting) {
            // Сидячая поза — наклон вперёд и вниз
            GlStateManager.translate(0F, -0.15F, 0.05F);
            GlStateManager.rotate(20F, 1F, 0F, 0F);
        } else {
            // Анимация ходьбы — лёгкое покачивание
            float walkBob = (float) Math.sin(limbSwing * 0.6662F) * limbSwingAmount * 0.15F;
            GlStateManager.translate(0F, walkBob, 0F);

            // Анимация дыхания в idle
            float breath = (float) Math.sin(ageInTicks * 0.05F) * 0.02F;
            GlStateManager.translate(0F, breath, 0F);
        }

        // Рендерим меши через OBJ лоадер
        ChaseObjLoader.renderBody(limbSwing, limbSwingAmount, sitting);
        ChaseObjLoader.renderBackpack(sitting);

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        // Анимация управляется в render()
    }
}
