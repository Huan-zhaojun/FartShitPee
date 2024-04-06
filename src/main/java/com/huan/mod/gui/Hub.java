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
    private final ResourceLocation HUD = new ResourceLocation(fart_shit_pee.MOD_ID, "textures/gui/a.png");
    private MatrixStack matrixStack;

    public Hub(MatrixStack matrixStack) {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.matrixStack = matrixStack;
    }

    public void render() {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(HUD);
        blit(matrixStack, width / 2, height / 2, 0, 0, 32, 32, 32, 32);
    }
}
