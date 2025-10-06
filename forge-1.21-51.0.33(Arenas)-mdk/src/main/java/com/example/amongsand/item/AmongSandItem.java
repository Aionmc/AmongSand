package com.example.amongsand.item;

import com.example.amongsand.AmongSand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AmongSandItem {
    public static final DeferredRegister<Item> SAND_ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, AmongSand.MODID);

    public static DeferredRegister<CreativeModeTab> C_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AmongSand.MODID);

    public static final RegistryObject<Item> QUICKSAND = SAND_ITEM.register("amongsand",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<CreativeModeTab> QUICKSAND_CREATIVE =
            C_TAB.register("amongsand",
                    () -> CreativeModeTab.builder()
                            .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                            .icon(() -> QUICKSAND.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(QUICKSAND.get());
                            }).build());

    public static void register(IEventBus eventBus) {
        SAND_ITEM.register(eventBus);
        C_TAB.register(eventBus);
    }


}
