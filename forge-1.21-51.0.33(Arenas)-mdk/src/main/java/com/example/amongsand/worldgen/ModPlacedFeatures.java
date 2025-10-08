package com.example.amongsand.worldgen;

import com.example.amongsand.AmongSand;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.*;
import java.util.List;



public class ModPlacedFeatures {

    // 🔹 Clave única para registrar la feature colocada (placed)
    public static final ResourceKey<PlacedFeature> QUICKSAND_PLACED_KEY =
            ResourceKey.create(
                    Registries.PLACED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(AmongSand.MODID, "quicksand_placed")
            );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {

        // 🔸 Obtener la referencia a la configured feature
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // 🔸 Cuántos grupos por chunk
        PlacementModifier count = CountPlacement.of(3);

        // 🔸 Altura de generación (niveles del mundo)
        PlacementModifier height = HeightRangePlacement.uniform(
                VerticalAnchor.absolute(50),
                VerticalAnchor.absolute(100)
        );

        // 🔸 Registrar la placed feature (cómo y dónde aparece)
        context.register(
                QUICKSAND_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModConfiguredFeatures.QUICKSAND_KEY),
                        List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome())
                )
        );
    }
}



