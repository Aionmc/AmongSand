package com.example.amongsand.block;

import com.example.amongsand.AmongSand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AmongSandBlock {
    public static final DeferredRegister<Block> SAND_BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AmongSand.MODID);

    public static final DeferredRegister<Item> SAND_ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, AmongSand.MODID);

    public static DeferredRegister<CreativeModeTab> C_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AmongSand.MODID);

    public static final RegistryObject<Block> QUICKSAND = SAND_BLOCK.register("amongsand_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5f).requiresCorrectToolForDrops().sound(SoundType.SAND)));

    public static final RegistryObject<Item> QUICKSAND_ITEM = SAND_ITEM.register("amongsand_block",
            () -> new BlockItem(QUICKSAND.get(), new Item.Properties()));

    public static RegistryObject<CreativeModeTab> QUICKSAND_CREATIVE =
            C_TAB.register("amongsand_block",
                    () -> CreativeModeTab.builder()
                            .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                            .icon(() -> QUICKSAND_ITEM.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(QUICKSAND.get());
                            }).build());

    public static void register(IEventBus eventBus) {
        SAND_BLOCK.register(eventBus);
        SAND_ITEM.register(eventBus);
        C_TAB.register(eventBus);
    }
}
