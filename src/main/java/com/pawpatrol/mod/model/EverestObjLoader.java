package com.pawpatrol.mod.model;

import com.pawpatrol.mod.PawPatrolMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Эверест — один меш, одна текстура (без отдельного рюкзака).
 */
@SideOnly(Side.CLIENT)
public class EverestObjLoader {

    private static float[] bodyMesh = null;
    private static boolean loaded = false;

    private static final int BODY_VERT_COUNT = 8814;

    public static void ensureLoaded() {
        if (loaded) return;
        try {
            bodyMesh = loadBinaryMesh("assets/pawpatrol/models/entity/everest_body.bin", BODY_VERT_COUNT);
            loaded = true;
            PawPatrolMod.logger.info("Everest mesh loaded!");
        } catch (Exception e) {
            PawPatrolMod.logger.error("Failed to load Everest mesh!", e);
        }
    }

    private static float[] loadBinaryMesh(String path, int vertCount) throws IOException {
        InputStream is = EverestObjLoader.class.getClassLoader().getResourceAsStream(path);
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

    public static void renderBody(float limbSwing, float limbSwingAmount, boolean sitting) {
        ensureLoaded();
        if (bodyMesh == null) return;
        GlStateManager.pushMatrix();
        if (!sitting && limbSwingAmount > 0.01F) {
            float bob = (float) Math.sin(limbSwing * 0.6662F) * limbSwingAmount * 0.08F;
            float tilt = (float) Math.sin(limbSwing * 0.3331F) * limbSwingAmount * 3.0F;
            GlStateManager.translate(0, bob, 0);
            GlStateManager.rotate(tilt, 0, 0, 1);
        }
        renderMesh(bodyMesh, BODY_VERT_COUNT);
        GlStateManager.popMatrix();
    }

    private static void renderMesh(float[] mesh, int vertCount) {
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        for (int i = 0; i < vertCount; i++) {
            int b = i * 5;
            buf.pos(mesh[b], mesh[b+1], mesh[b+2]).tex(mesh[b+3], mesh[b+4]).endVertex();
        }
        tess.draw();
    }
}
