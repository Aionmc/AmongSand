
package com.example.amongsand.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class QuickSandBlock extends Block {

    private static final VoxelShape MIN_COLLISION = Shapes.empty(); // la colisión del bloque, es vacia por lo que permite entrar en el
    private static final double HORIZONTAL_FACTOR = 0.75; // reduce la velocidad al moverse dentro del bloque, es la fricción
    private static final double TARGET_DESCENT = -0.002; // es el hundimiento lento cuando el jugador entra en el bloque
    private static final double MAX_DESCENT = -0.004; // es el límite de velocidad máxima de hundimiento.

    public QuickSandBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0.2f)
                .sound(SoundType.SAND)
                .noOcclusion()
        );
    }

    @Override
    public java.util.List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(this.asItem()));
        return drops;
    }

    @Override
    //la forma de colisión del bloque
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return MIN_COLLISION;
    }

    @Override
    //cada tick que la entidad está dentro del bloque
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living) {

            double vx = entity.getDeltaMovement().x * HORIZONTAL_FACTOR;
            double vz = entity.getDeltaMovement().z * HORIZONTAL_FACTOR;
            double vy = entity.getDeltaMovement().y;

            if (vy < MAX_DESCENT) vy = MAX_DESCENT;
            if (vy > TARGET_DESCENT) vy = TARGET_DESCENT;

            entity.setDeltaMovement(vx, vy, vz); // aplica fricción horizontal y hundimiento vertical
            entity.setOnGround(false); // considera que la entidad está en el aire, es decir no sobre un bloque

            if (!level.isClientSide) {
                // Altura de la cabeza del jugador
                double head = entity.getY() + entity.getEyeHeight();
                double blockHeight = pos.getY() + 1.0;

                // Si la cabeza está bajo la superficie hace daño
                if (head < blockHeight - 0.3) {
                    // Aplica 1 punto de daño cada tick
                    living.hurt(level.damageSources().drown(), 2.0F);
                }
            }
            super.entityInside(state, level, pos, entity);
        }

        if (entity instanceof ItemEntity item) {
             //Te item dissapears
             item.discard();

             //The object drowns and later disappears
             /*item.setDeltaMovement(item.getDeltaMovement().multiply(HORIZONTAL_FACTOR, HORIZONTAL_FACTOR, HORIZONTAL_FACTOR));
             if (level.getGameTime() % 5 == 0){
                 item.discard();
             }*/
        }
    }

    @Override
    //indica que el bloque nunca se considera sólido completo.
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}
