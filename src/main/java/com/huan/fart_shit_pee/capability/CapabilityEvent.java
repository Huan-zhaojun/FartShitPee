package com.huan.fart_shit_pee.capability;

import com.huan.fart_shit_pee.customDamageSource;
import com.huan.fart_shit_pee.fart_shit_pee;
import com.huan.fart_shit_pee.network.Networking;
import com.huan.fart_shit_pee.network.SendPack;
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
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = fart_shit_pee.MOD_ID)
public class CapabilityEvent {
    /**
     * 所有能增加尿液值的食物的集合
     */
    public static HashMap<Item, Integer> items_urine = new HashMap<>();
    /**
     * 所有能增加屎值的食物的集合
     */
    public static HashMap<Item, Integer> items_shit = new HashMap<>();
    /**
     * 放屁的食物可能性的集合
     */
    public static HashMap<Item, Double> items_flatus = new HashMap<>();

    @ObjectHolder("hunger_plus:big_stomach_potion_small")
    public static Item big_stomach_potion_small;
    @ObjectHolder("hunger_plus:big_stomach_potion_medium")
    public static Item big_stomach_potion_medium;
    @ObjectHolder("hunger_plus:big_stomach_potion_large")
    public static Item big_stomach_potion_large;
    @ObjectHolder("hunger_plus:eatable_dirt")
    public static Item eatable_dirt;

    static {//初始化
        setItems_urine();
        setItems_flatus();
    }

    public static long lastTime_intestine = System.currentTimeMillis();
    public static long lastTime_bladder = System.currentTimeMillis();
    public static long lastTime_urine = System.currentTimeMillis();

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
                if (c.shitLevel >= c.shitLevel_Max && System.currentTimeMillis() - lastTime_intestine >= 1500) {
                    player.attackEntityFrom(customDamageSource.intestineDamageSource1, 1.5f);
                    lastTime_intestine = System.currentTimeMillis();
                }
                if (c.urineLevel >= c.urineLevel_Max && System.currentTimeMillis() - lastTime_bladder >= 1500) {
                    player.attackEntityFrom(customDamageSource.bladderDamageSource1, 1.5f);
                    lastTime_bladder = System.currentTimeMillis();
                }
                //因为人体体循环，随着时间缓慢增加尿液
                if (c.urineLevel < c.urineLevel_Max && System.currentTimeMillis() - lastTime_urine >= 60 * 1000) {
                    c.setUrineLevel(c.urineLevel + 1);
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
                int hunger = items_shit.getOrDefault(item, 0);
                ;
                if (food != null) hunger = food.getHealing();
                if (item.equals(eatable_dirt)) hunger = shitLevel_Max / 5;//吃土塞爆肠道

                //吃食物根据恢复的饥饿值增加屎值，超出最大屎值的数值将作为伤害
                if (!c.setShitLevel(shitLevel + hunger) && shitLevel + hunger - shitLevel_Max > 0) {
                    c.setShitLevel(shitLevel_Max);
                    player.attackEntityFrom(customDamageSource.intestineDamageSource2, shitLevel + hunger - shitLevel_Max);
                }

                //增加屁等级
                Random r = new Random();
                double d = items_flatus.getOrDefault(item, 0.0);
                double abs = Math.abs(d);
                int increment = abs > 1.0 ? (r.nextDouble() < abs - Math.floor(abs) ? (int) Math.ceil(abs) : (int) Math.floor(abs)) : (r.nextDouble() < abs ? 1 : 0);
                c.setFlatusLevel(d > 0 ? Math.min(flatusLevel + increment, 9) : Math.max(flatusLevel - increment, 0));

                //增加尿值
                int urine = items_urine.getOrDefault(item, 0);
                if (item instanceof PotionItem) urine = 5;
                if (!c.setUrineLevel(urineLevel + urine) && urineLevel + urine - urineLevel_Max > 0) {
                    c.setUrineLevel(urineLevel_Max);
                    player.attackEntityFrom(customDamageSource.bladderDamageSource2, urineLevel + urine - urineLevel_Max);
                }
            });
        }
    }

    /**
     * 增加新的增加尿值的食物<br>
     * 重复物品类实例名将重置为新值
     *
     * @param item    物品类实例名
     * @param integer 增加的尿液值
     * @see CapabilityEvent#items_urine
     */
    public static void addItems_urine(Item item, Integer integer) {
        items_urine.put(item, integer);
    }

    /**
     * 增加新的增加屎的食物<br>
     * 重复物品类实例名将重置为新值
     *
     * @param item    物品类实例名
     * @param integer 增加的屎值
     * @see CapabilityEvent#items_shit
     */
    public static void addItems_shit(Item item, Integer integer) {
        items_shit.put(item, integer);
    }

    /**
     * 增加新的放屁食物<br>
     * 重复物品类实例名将重置为新值
     *
     * @param item 物品类实例名
     * @param d    屁等级增减的概率，范围:[-9.0,9.0]，负数则为降低屁的概率；
     *             当概率绝对值>1 则增减的等级＞1，但不超过屁等级上下限范围.
     * @see drainCapability#flatusLevel
     * @see CapabilityEvent#items_flatus
     */
    public static boolean addItems_flatus(Item item, Double d) {
        if (d >= -9.0 && d <= 9.0) {
            items_flatus.put(item, d);
            return true;
        } else return false;
    }

    private static void setItems_urine() {
        items_urine.put(Items.TROPICAL_FISH, Items.TROPICAL_FISH.getFood().getHealing());
        items_urine.put(Items.PUFFERFISH, 1);
        items_urine.put(Items.SALMON, Items.SALMON.getFood().getHealing());
        items_urine.put(Items.COD, Items.COD.getFood().getHealing());

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

    private static void setItems_flatus() {
        //减屁
        items_flatus.put(Items.APPLE, -0.8);
        items_flatus.put(Items.GOLDEN_APPLE, -1.5);
        items_flatus.put(Items.ENCHANTED_GOLDEN_APPLE, -2.5);
        items_flatus.put(Items.CARROT, -0.8);
        items_flatus.put(Items.GOLDEN_CARROT, -1.5);
        items_flatus.put(Items.BEETROOT, -0.8);
        items_flatus.put(Items.BEETROOT_SOUP, -2.2);
        items_flatus.put(Items.MUSHROOM_STEW, -2.2);
        items_flatus.put(Items.KELP, -0.8);
        items_flatus.put(Items.MELON_SLICE, -0.8);
        items_flatus.put(Items.SWEET_BERRIES, -0.8);

        //增屁
        items_flatus.put(Items.BEEF, 0.7);
        items_flatus.put(Items.MUTTON, 0.7);
        items_flatus.put(Items.PORKCHOP, 0.7);
        items_flatus.put(Items.CHICKEN, 0.7);
        items_flatus.put(Items.RABBIT, 0.7);
        items_flatus.put(Items.SALMON, 0.7);
        items_flatus.put(Items.COD, 0.7);
        items_flatus.put(Items.TROPICAL_FISH, 0.7);

        items_flatus.put(Items.COOKED_BEEF, 1.2);
        items_flatus.put(Items.COOKED_MUTTON, 1.2);
        items_flatus.put(Items.COOKED_PORKCHOP, 1.2);
        items_flatus.put(Items.COOKED_CHICKEN, 1.2);
        items_flatus.put(Items.COOKED_RABBIT, 1.2);
        items_flatus.put(Items.COOKED_SALMON, 1.2);
        items_flatus.put(Items.COOKED_COD, 1.2);
        items_flatus.put(Items.RABBIT_STEW, 1.2);

        items_flatus.put(Items.POTATO, 0.7);
        items_flatus.put(Items.BAKED_POTATO, 1.2);
        items_flatus.put(Items.POISONOUS_POTATO, 4.0);

        items_flatus.put(Items.ROTTEN_FLESH, 2.0);
        items_flatus.put(Items.SPIDER_EYE, 2.0);

        items_flatus.put(Items.SUSPICIOUS_STEW, 9.0);
        items_flatus.put(Items.PUFFERFISH, 9.0);
    }

    public static void hunger_plus_mod() {//存在该模组，执行的特有代码
        addItems_urine(big_stomach_potion_small, 5);
        addItems_urine(big_stomach_potion_medium, 10);
        addItems_urine(big_stomach_potion_large, 15);
        addItems_flatus(eatable_dirt, 9.0);
        addItems_flatus(big_stomach_potion_small, -1.0);
        addItems_flatus(big_stomach_potion_medium, -2.5);
        addItems_flatus(big_stomach_potion_large, -5.0);
    }
}
