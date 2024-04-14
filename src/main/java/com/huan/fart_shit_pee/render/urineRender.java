package com.huan.fart_shit_pee.render;

import com.huan.fart_shit_pee.fart_shit_pee;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID, value = Dist.CLIENT)
public class urineRender {
    public static double a, b;//抛物线参数
    public static Vector3d from;//起始点
    public static float radianYaw;//玩家旋转弧度值
    public static double endx;
    public static boolean pee = false;//是否绘制

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!pee) return;
        Minecraft mc = Minecraft.getInstance();
        MatrixStack matrixStack = event.getMatrixStack();
        Vector3d playerPos = mc.gameRenderer.getActiveRenderInfo().getProjectedView();

        // 设置渲染矩阵的投影视图
        matrixStack.push();
        matrixStack.translate(-playerPos.x, -playerPos.y, -playerPos.z);

        // 设置渲染模式
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.lineWidth(10F);

        // 获取渲染器
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // 渲染线段
        renderLine(matrixStack, buffer);

        // 结束渲染
        tessellator.draw();


        // 恢复渲染设置
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

        matrixStack.pop();
    }

    private static void renderLine(MatrixStack matrixStack, BufferBuilder buffer) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        float red = 173 / 255f, green = 132 / 255f, blue = 43 / 255f;
        float alpha = 0.4f;

        // 开始渲染线段
        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        double leftPoint = parabolaX(a, b);//抛物线左零点
        double gap = 0.1;
        buffer.pos(matrix, (float) from.x, (float) from.y, (float) from.z).color(red, green, blue, alpha).endVertex();
        for (double x = leftPoint + gap; x < endx; x += gap) {
            float Y = (float) (parabolaY(a, b, x) + from.y);
            float X = (float) ((-(x - leftPoint) * Math.sin(radianYaw)) + from.x);
            float Z = (float) (((x - leftPoint) * Math.cos(radianYaw)) + from.z);
            buffer.pos(matrix, X, Y, Z).color(red, green, blue, alpha).endVertex();
        }
    }

    //抛物线计算
    private static double parabolaY(double a, double b, double x) {
        return -a * Math.pow(x, 2) + b;
    }

    //求左端零点
    private static double parabolaX(double a, double b) {
        return -Math.sqrt((0 - b) / -a);
    }
}
