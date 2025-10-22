package com.example.amongsand.worldgen;

import com.example.amongsand.AmongSand;
import com.example.amongsand.worldgen.feature.QuickSandFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    // Registro diferido donde se guardan todas las features personalizadas del mod
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, AmongSand.MODID);

    // Registro de la feature "quicksand_feature"
    public static final RegistryObject<Feature<DiskConfiguration>> QUICKSAND_FEATURE =
            FEATURES.register("quicksand_feature", () -> new QuickSandFeature(DiskConfiguration.CODEC));
}
