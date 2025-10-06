package com.example.amongsand.block;

import com.example.amongsand.AmongSand;
import com.example.amongsand.item.AmongSandItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AmongSandBlock {
    public static final DeferredRegister<Block> SAND_BLOCK = DeferredRegister.create(ForgeRegistries.BLOCKS, AmongSand.MODID);

    public static final RegistryObject<Block> QUICKSAND = registerBlock("amongsand_block",
            () -> new Block(BlockBehaviour.Properties.of()
                  .strength(0.5f).requiresCorrectToolForDrops().sound(SoundType.SAND)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = SAND_BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        AmongSandItem.SAND_ITEM.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        SAND_BLOCK.register(eventBus);
    }
}
