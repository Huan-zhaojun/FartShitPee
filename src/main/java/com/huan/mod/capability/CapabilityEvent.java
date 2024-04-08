package com.huan.mod.capability;

import com.huan.mod.customDamageSource;
import com.huan.mod.fart_shit_pee;
import com.huan.mod.network.Networking;
import com.huan.mod.network.SendPack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID)
public class CapabilityEvent {
    public static HashMap<Item, Integer> items_urine = new HashMap<>();

    static {
        setItems_urine();
    }//初始化所有能增加尿液值的食物的集合

    public static long lastTime_intestine = System.currentTimeMillis();
    public static long lastTime_bladder = System.currentTimeMillis();

    @SubscribeEvent//给玩家注册上能力
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(fart_shit_pee.MOD_ID, "drain_capability"), new drainCapabilityProvider());
        }
    }

    @SubscribeEvent//能力保留
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {//死亡不保存
            LazyOptional<drainCapability> originalCap = event.getOriginal().getCapability(fart_shit_pee.Drain_Capability);
            LazyOptional<drainCapability> nowCap = event.getPlayer().getCapability(fart_shit_pee.Drain_Capability);
            if (originalCap.isPresent() && nowCap.isPresent()) {
                nowCap.ifPresent((cap) -> {
                    originalCap.ifPresent((oldCap) -> cap.deserializeNBT(oldCap.serializeNBT()));
                });
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (!event.player.world.isRemote) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
            cap.ifPresent(c -> {
                //超出上限持续扣血
                if (c.shitLevel >= c.shitLevel_Max && System.currentTimeMillis() - lastTime_intestine >= 1000) {
                    player.attackEntityFrom(customDamageSource.intestineDamageSource1, 3);
                    lastTime_intestine = System.currentTimeMillis();
                }
                if (c.urineLevel >= c.urineLevel_Max && System.currentTimeMillis() - lastTime_bladder >= 1000) {
                    player.attackEntityFrom(customDamageSource.bladderDamageSource1, 3);
                    lastTime_bladder = System.currentTimeMillis();
                }

                //发包同步数据
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player)
                        , new SendPack(c.urineLevel_Max, c.shitLevel_Max, c.urineLevel, c.shitLevel, c.flatusLevel, player.getUniqueID()));
            });
        }
    }

    @SubscribeEvent
    public static void onEatFinish(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntity().world.isRemote && event.getEntityLiving() instanceof PlayerEntity) {
            Item item = event.getItem().getItem();
            Food food = item.getFood();
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.isCreative()) return;
            LazyOptional<drainCapability> cap = player.getCapability(fart_shit_pee.Drain_Capability);
            cap.ifPresent(c -> {
                int urineLevel = c.urineLevel, shitLevel = c.shitLevel;
                int urineLevel_Max = c.urineLevel_Max, shitLevel_Max = c.shitLevel_Max;
                int flatusLevel = c.flatusLevel;
                int hunger = 0;
                if (food != null) hunger = food.getHealing();

                //吃食物根据恢复的饥饿值增加屎值，超出最大屎值的数值将作为伤害
                if (!c.setShitLevel(shitLevel + hunger) && shitLevel + hunger - shitLevel_Max > 0) {
                    c.setShitLevel(shitLevel_Max);
                    player.attackEntityFrom(customDamageSource.intestineDamageSource2, shitLevel + hunger - shitLevel_Max);
                }
                //增加屁等级

                //增加尿值
                int urine = 0;
                if (item instanceof PotionItem) urine = 5;
                if (items_urine.get(item) != null) {
                    urine = items_urine.get(item);
                }
                if (!c.setUrineLevel(urineLevel + urine) && urineLevel + urine - urineLevel_Max > 0) {
                    c.setUrineLevel(urineLevel_Max);
                    player.attackEntityFrom(customDamageSource.bladderDamageSource2, urineLevel + urine - urineLevel_Max);
                }
            });
        }
    }

    public static void addItems_urine(Item item, Integer integer) {
        items_urine.put(item, integer);
    }

    private static void setItems_urine() {
        items_urine.put(Items.TROPICAL_FISH, (int) (Items.TROPICAL_FISH.getFood().getHealing() * 0.8));
        items_urine.put(Items.PUFFERFISH, 1);
        items_urine.put(Items.SALMON, (int) (Items.SALMON.getFood().getHealing() * 0.8));
        items_urine.put(Items.COD, (int) (Items.COD.getFood().getHealing() * 0.8));

        items_urine.put(Items.APPLE, Items.APPLE.getFood().getHealing());
        items_urine.put(Items.MELON_SLICE, Items.MELON_SLICE.getFood().getHealing());
        items_urine.put(Items.BEETROOT, Items.BEETROOT.getFood().getHealing());
        items_urine.put(Items.CARROT, Items.CARROT.getFood().getHealing());
        items_urine.put(Items.POTATO, Items.POTATO.getFood().getHealing());
        items_urine.put(Items.POISONOUS_POTATO, Items.POISONOUS_POTATO.getFood().getHealing());
        items_urine.put(Items.SWEET_BERRIES, Items.SWEET_BERRIES.getFood().getHealing());

        items_urine.put(Items.SUSPICIOUS_STEW, 10);
        items_urine.put(Items.RABBIT_STEW, 10);
        items_urine.put(Items.MUSHROOM_STEW, 10);
        items_urine.put(Items.BEETROOT_SOUP, 10);

        items_urine.put(Items.HONEY_BOTTLE, 5);
    }
}
