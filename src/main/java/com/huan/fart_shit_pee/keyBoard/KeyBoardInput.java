package com.huan.fart_shit_pee.keyBoard;

import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Network;
import com.huan.fart_shit_pee.network.Server.drainSendPack;
import com.huan.fart_shit_pee.network.Server.fartSendPack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID, value = Dist.CLIENT)
public class KeyBoardInput {
    public static long lastTime_pee_KEY = System.currentTimeMillis();
    public static final KeyBinding pee_KEY = new KeyBinding(
            "key.pee",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.category." + fart_shit_pee.MOD_ID);

    public static final KeyBinding fart_KEY = new KeyBinding(
            "fart.pee",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.category." + fart_shit_pee.MOD_ID);

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        //消除键位冲突，消除冲突的原版键位设置
        if (Minecraft.getInstance() != null && Minecraft.getInstance().gameSettings.field_244602_au.getKey().getKeyCode() == GLFW.GLFW_KEY_P
                && KeyBoardInput.pee_KEY.getKey().getKeyCode() == GLFW.GLFW_KEY_P) {
            Minecraft.getInstance().gameSettings.field_244602_au.setKeyModifierAndCode(KeyModifier.NONE, InputMappings.INPUT_INVALID);
        }
        if (pee_KEY.isPressed() && System.currentTimeMillis() - lastTime_pee_KEY > 50) {
            Network.INSTANCE.sendToServer(new drainSendPack());//发送排泄指令
            lastTime_pee_KEY = System.currentTimeMillis();
        }
        if (fart_KEY.isPressed() && System.currentTimeMillis() - lastTime_pee_KEY > 500) {
            Network.INSTANCE.sendToServer(new fartSendPack());//发送放屁指令
            lastTime_pee_KEY = System.currentTimeMillis();
        }
    }
}
