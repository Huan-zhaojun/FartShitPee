package com.huan.fart_shit_pee.common.Item;

import com.huan.fart_shit_pee.common.Block.blockRegistry;
import com.huan.fart_shit_pee.common.Fluid.FluidRegistry;
import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.huan.fart_shit_pee.common.customDamageSource.eatShit_DamageSource;
import static net.minecraft.item.Items.BUCKET;

public class itemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, fart_shit_pee.MOD_ID);
    public static RegistryObject<Item> urineFluidBucket = ITEMS.register("urine_fluid_bucket",
            () -> new BucketItem(FluidRegistry.urineFluid, new Item.Properties()
                    .group(ItemGroup.MISC).containerItem(BUCKET)));
    public static RegistryObject<Item> shitBlock = ITEMS.register("shit_block",
            () -> new BlockItem(blockRegistry.shitBlock.get(),
                    new Item.Properties().food(new Food.Builder().hunger(2).saturation(1).setAlwaysEdible().build())
                            .group(ItemGroup.MISC)) {
                @Override
                public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
                    entityLiving.attackEntityFrom(eatShit_DamageSource, 8f);
                    return super.onItemUseFinish(stack, worldIn, entityLiving);
                }
            });
}