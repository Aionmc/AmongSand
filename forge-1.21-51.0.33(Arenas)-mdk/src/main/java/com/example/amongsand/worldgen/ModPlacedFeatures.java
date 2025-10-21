package com.example.amongsand.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;


import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> QUICKSAND_DISK_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath("amongsand", "quicksand_disk_placed"));

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                QUICKSAND_DISK_PLACED_KEY,
                new PlacedFeature(
                        configured.getOrThrow(ModConfiguredFeatures.QUICKSAND_DISK_KEY),
                        List.of(
                                CountPlacement.of(1),
                                InSquarePlacement.spread(),
                                HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE),
                                BiomeFilter.biome()
                        )
                )
        );
    }
}
