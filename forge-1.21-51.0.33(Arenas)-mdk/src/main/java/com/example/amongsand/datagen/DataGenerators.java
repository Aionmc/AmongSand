package com.example.amongsand.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.amongsand.AmongSand;

@Mod.EventBusSubscriber(modid = AmongSand.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        // Añadimos nuestro datapack entries
        generator.addProvider(
                event.includeServer(),
                new ModDatapackEntries(output, event.getLookupProvider())
        );
    }
}
