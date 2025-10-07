package com.example.amongsand.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuickSandBlock extends FallingBlock {

    //para serializar el bloque, necesario para subclases de bloques desde la version 1.20
    public static final MapCodec<QuickSandBlock> CODEC = MapCodec.unit(QuickSandBlock::new);

    private static final VoxelShape MIN_COLLISION = Shapes.empty(); // la colisión del bloque, es vacia por lo que permite entrar en el
    private static final double HORIZONTAL_FACTOR = 0.75; // reduce la velocidad al moverse dentro del bloque, es la fricción
    private static final double TARGET_DESCENT = -0.002; // es el hundimiento lento cuando el jugador entra en el bloque
    private static final double MAX_DESCENT = -0.004; // es el límite de velocidad máxima de hundimiento.

    public QuickSandBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0.5f)
                .sound(SoundType.SAND)
                .noOcclusion()
        );
    }

    @Override
    public MapCodec<QuickSandBlock> codec() {
        return CODEC;
    }

    @Override
    //la forma de colisión del bloque
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return MIN_COLLISION;
    }

    @Override
    //cada tick que la entidad está dentro del bloque
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living && !living.isShiftKeyDown()) {

            double vx = entity.getDeltaMovement().x * HORIZONTAL_FACTOR;
            double vz = entity.getDeltaMovement().z * HORIZONTAL_FACTOR;
            double vy = entity.getDeltaMovement().y;

            if (vy < MAX_DESCENT) vy = MAX_DESCENT;
            if (vy > TARGET_DESCENT) vy = TARGET_DESCENT;

            entity.setDeltaMovement(vx, vy, vz); // aplica fricción horizontal y hundimiento vertical
            entity.setOnGround(false); // considera que la entidad está en el aire, es decir no sobre un bloque

            super.entityInside(state, level, pos, entity);
        }
    }

    @Override
    //indica que el bloque nunca se considera sólido completo.
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}
