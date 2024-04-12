package com.huan.fart_shit_pee.common.Item;

import com.huan.fart_shit_pee.common.Fluid.FluidRegistry;
import com.huan.fart_shit_pee.fart_shit_pee;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.item.Items.BUCKET;

public class itemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, fart_shit_pee.MOD_ID);
    public static RegistryObject<Item> urineFluidBucket = ITEMS.register("urine_fluid_bucket",
            () -> new BucketItem(FluidRegistry.urineFluid, new Item.Properties()
                    .group(ItemGroup.MISC).containerItem(BUCKET)));
}