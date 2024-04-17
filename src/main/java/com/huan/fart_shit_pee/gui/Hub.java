package com.huan.fart_shit_pee.gui;

import com.huan.fart_shit_pee.api.Config;
import com.huan.fart_shit_pee.fart_shit_pee;
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
        this.minecraft = Minecraft.getInstance();
        this.matrixStack = matrixStack;
    }

    public void render(int urineLevel_Max, int shitLevel_Max, int urineLevel, int shitLevel, int flatusLevel) {
        //计算hub的百分比
        double urine_percentage = (double) urineLevel / urineLevel_Max, shit_percentage = (double) shitLevel / shitLevel_Max;
        int urine_y = 32 - (int) (23 * urine_percentage + 2), shit_y = 32 - (int) (30 * shit_percentage + 1);//hub绘制偏移量
        //更接近直观的百分比hub显示
        if (urine_percentage <= 0.0538 && urine_percentage > 0.01)
            urine_y = 32 - (int) (7 * urine_percentage / 0.0538 + 2);
        else if (urine_percentage <= 0.01 && urine_percentage > 0) urine_y = 32 - (1 + 2);//只要有值就显示1像素
        else if (urine_percentage > 0.0538) urine_y = 32 - (int) (16 * (urine_percentage - 0.0538) / 0.9462 + 7 + 2);
        if (shit_percentage < 0.035 && shit_percentage > 0) shit_y = 32 - (1 + 1);//只要有值就显示1像素

        //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        //绘制膀胱
        //int bladder_x = width / 2 - 125, bladder_y = height - 35;
        int bladder_x = width / 2 + Config.bladder_xOffset.get(), bladder_y = height + Config.bladder_yOffset.get();
        this.minecraft.getTextureManager().bindTexture(bladder_Hub);
        blit(matrixStack, bladder_x, bladder_y, 0, 0, 32, 32, 64, 32);
        blit(matrixStack, bladder_x, bladder_y + urine_y, 32, urine_y,
                32, 32 - urine_y, 64, 32);

        //绘制肠道
        //int intestine_x = width / 2 + 95, intestine_y = height - 35;
        int intestine_x = width / 2 + Config.intestine_xOffset.get(), intestine_y = height + Config.intestine_yOffset.get();
        this.minecraft.getTextureManager().bindTexture(intestine_Hub);
        blit(matrixStack, intestine_x, intestine_y, 0, 0, 32, 32, 352, 32);
        blit(matrixStack, intestine_x, intestine_y, 32 * (flatusLevel + 1), 0,
                32, 32 - shit_y, 352, 32);

        RenderSystem.disableBlend();
    }
}
