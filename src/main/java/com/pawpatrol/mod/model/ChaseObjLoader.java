package com.pawpatrol.mod.model;

import com.pawpatrol.mod.PawPatrolMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Загружает и рендерит бинарные данные меша Чейза.
 * Формат бинарного файла: последовательность float[5] = {x, y, z, u, v}
 * Треугольники (каждые 3 вершины = 1 треугольник).
 */
@SideOnly(Side.CLIENT)
public class ChaseObjLoader {

    private static float[] bodyMesh = null;
    private static float[] packMesh = null;
    private static boolean loaded = false;

    private static final int BODY_VERT_COUNT = 7158;
    private static final int PACK_VERT_COUNT = 576;

    public static void ensureLoaded() {
        if (loaded) return;
        try {
            bodyMesh = loadBinaryMesh("assets/pawpatrol/models/entity/chase_body.bin", BODY_VERT_COUNT);
            packMesh = loadBinaryMesh("assets/pawpatrol/models/entity/chase_pack.bin", PACK_VERT_COUNT);
            loaded = true;
            PawPatrolMod.logger.info("Chase mesh loaded: body=" + BODY_VERT_COUNT + " verts, pack=" + PACK_VERT_COUNT + " verts");
        } catch (Exception e) {
            PawPatrolMod.logger.error("Failed to load Chase mesh!", e);
        }
    }

    private static float[] loadBinaryMesh(String path, int vertCount) throws IOException {
        InputStream is = ChaseObjLoader.class.getClassLoader().getResourceAsStream(path);
        if (is == null) throw new IOException("Resource not found: " + path);

        int floatCount = vertCount * 5;
        byte[] bytes = new byte[floatCount * 4];
        int read = 0;
        while (read < bytes.length) {
            int r = is.read(bytes, read, bytes.length - read);
            if (r < 0) break;
            read += r;
        }
        is.close();

        FloatBuffer fb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] result = new float[floatCount];
        fb.get(result);
        return result;
    }

    /**
     * Рендерит тело Чейза.
     * @param limbSwing угол покачивания ног
     * @param limbSwingAmount интенсивность анимации ходьбы
     * @param sitting стоит ли Чейз
     */
    public static void renderBody(float limbSwing, float limbSwingAmount, boolean sitting) {
        ensureLoaded();
        if (bodyMesh == null) return;

        GlStateManager.pushMatrix();

        // Анимация ходьбы: лёгкое покачивание тела
        if (!sitting && limbSwingAmount > 0.01F) {
            float bob = (float) Math.sin(limbSwing * 0.6662F) * limbSwingAmount * 0.08F;
            float tilt = (float) Math.sin(limbSwing * 0.3331F) * limbSwingAmount * 3.0F;
            GlStateManager.translate(0, bob, 0);
            GlStateManager.rotate(tilt, 0, 0, 1);
        }

        renderMesh(bodyMesh, BODY_VERT_COUNT);
        GlStateManager.popMatrix();
    }

    /**
     * Рендерит рюкзак Чейза.
     */
    public static void renderBackpack(boolean sitting) {
        ensureLoaded();
        if (packMesh == null) return;

        GlStateManager.pushMatrix();

        if (sitting) {
            GlStateManager.translate(0, 0.02F, -0.02F);
        }

        renderMesh(packMesh, PACK_VERT_COUNT);
        GlStateManager.popMatrix();
    }

    private static void renderMesh(float[] mesh, int vertCount) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        // Включаем backface culling и нормальное освещение
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);

        int stride = 5; // x,y,z,u,v
        for (int i = 0; i < vertCount; i++) {
            int base = i * stride;
            float x = mesh[base];
            float y = mesh[base + 1];
            float z = mesh[base + 2];
            float u = mesh[base + 3];
            float v = mesh[base + 4];
            buf.pos(x, y, z).tex(u, v).endVertex();
        }

        tess.draw();
    }
}
