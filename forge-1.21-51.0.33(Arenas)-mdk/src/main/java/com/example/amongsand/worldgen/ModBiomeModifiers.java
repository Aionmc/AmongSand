package com.example.amongsand.worldgen;

import com.example.amongsand.AmongSand;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_QUICKSAND_TO_DESERT_KEY =
            ResourceKey.create(
                    ForgeRegistries.Keys.BIOME_MODIFIERS,
                    ResourceLocation.fromNamespaceAndPath(AmongSand.MODID, "add_quicksand_to_desert")
            );

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);
        var placed = context.lookup(Registries.PLACED_FEATURE);

        context.register(
                ADD_QUICKSAND_TO_DESERT_KEY,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.HAS_DESERT_PYRAMID),
                        HolderSet.direct(placed.getOrThrow(ModPlacedFeatures.QUICKSAND_DISK_PLACED_KEY)),
                        GenerationStep.Decoration.LOCAL_MODIFICATIONS
                )
        );
    }
}

