package com.example.amongsand.worldgen.feature;

import com.example.amongsand.block.AmongSandBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class QuickSandFeature extends Feature<DiskConfiguration> {

    public QuickSandFeature(Codec<DiskConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DiskConfiguration> context) {
        LevelAccessor world = context.level();
        DiskConfiguration config = context.config();
        BlockPos origin = context.origin();
        RandomSource random = world.getRandom();

        BlockState quicksand = AmongSandBlock.QUICKSAND.get().defaultBlockState();

        int radius = config.radius().sample(random);
        int halfHeight = config.halfHeight();

        // Genera el disco de QuickSand
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -halfHeight; y <= halfHeight; y++) {
                    BlockPos pos = origin.offset(x, y, z);
                    BlockState stateAtPos = world.getBlockState(pos);

                    // Reemplaza solo arena o arenisca
                    if (!stateAtPos.is(Blocks.SAND) && !stateAtPos.is(Blocks.SANDSTONE)) continue;

                    world.setBlock(pos, quicksand, 2);
                }
            }
        }

        return true;
    }
}
