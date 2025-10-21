package com.example.amongsand.datagen;

import com.example.amongsand.worldgen.ModBiomeModifiers;
import com.example.amongsand.worldgen.ModConfiguredFeatures;
import com.example.amongsand.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                        .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
                        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap),
                Set.of("amongsand"));
    }
}
