package com.example.amongsand.worldgen;

import com.example.amongsand.AmongSand;
import com.example.amongsand.block.AmongSandBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> QUICKSAND_KEY =
            ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(AmongSand.MODID, "quicksand")
            );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        DiskConfiguration config = new DiskConfiguration(
                RuleBasedBlockStateProvider.simple(AmongSandBlock.QUICKSAND.get()),
                BlockPredicate.matchesBlocks(Blocks.SAND),
                UniformInt.of(2, 4),
                2
        );

        context.register(QUICKSAND_KEY, new ConfiguredFeature<>(Feature.DISK, config));
    }
}
