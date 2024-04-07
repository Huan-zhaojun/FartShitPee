package com.huan.mod.gui;

import com.huan.mod.fart_shit_pee;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class Hub extends AbstractGui {
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final ResourceLocation bladder_Hub = new ResourceLocation(fart_shit_pee.MOD_ID, "textures/gui/bladder_hub.png");
    private final ResourceLocation intestine_Hub = new ResourceLocation(fart_shit_pee.MOD_ID, "textures/gui/intestine_hub.png");
    private final MatrixStack matrixStack;

    public Hub(MatrixStack matrixStack) {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        System.out.println(width);
        System.out.println(height);
        this.minecraft = Minecraft.getInstance();
        this.matrixStack = matrixStack;
    }

    public void render() {
        //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        //绘制膀胱
        int bladder_x = width / 2 - 125, bishop_y = height - 35;
        this.minecraft.getTextureManager().bindTexture(bladder_Hub);
        blit(matrixStack, bladder_x, bishop_y, 0, 0, 32, 32, 64, 32);
        blit(matrixStack, bladder_x, bishop_y, 32, 0, 32, 32, 64, 32);

        //绘制肠道
        int intestine_x = width / 2 + 95, intestine_y = height - 35;
        this.minecraft.getTextureManager().bindTexture(intestine_Hub);
        blit(matrixStack, intestine_x, intestine_y, 0, 0, 32, 32, 352, 32);
        blit(matrixStack, intestine_x, intestine_y, 32, 0, 32, 32, 352, 32);

        RenderSystem.disableBlend();
    }
}
