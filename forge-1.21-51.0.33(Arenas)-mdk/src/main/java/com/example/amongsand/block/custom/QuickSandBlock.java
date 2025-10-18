
package com.example.amongsand.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuickSandBlock extends Block {

    private static final VoxelShape MIN_COLLISION = Shapes.empty(); // la colisión del bloque, es vacia por lo que permite entrar en el
    private static final double HORIZONTAL_FACTOR = 0.75; // reduce la velocidad al moverse dentro del bloque, es la fricción
    private static final double TARGET_DESCENT = -0.002; // es el hundimiento lento cuando el jugador entra en el bloque
    private static final double MAX_DESCENT = -0.004; // es el límite de velocidad máxima de hundimiento.

    private static final AtomicBoolean teleporting = new AtomicBoolean(false);
    private static boolean loading = false;
    //aqui iria la posibilidad de daño o lo que sea...  --> if num random <0.3 templo (damage = false)


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
                // Altura de la cabeza de la entidad
                double head = entity.getY() + entity.getEyeHeight();
                double blockHeight = pos.getY() + 1.0;

                // Si la cabeza está bajo la superficie hace daño
                if (head < blockHeight - 0.3) {
                    if (entity instanceof Player player) {
                        //Water drop
                        ServerLevel serverLevel = (ServerLevel) level;
                        ServerPlayer serverPlayer = (ServerPlayer) player;

                        long now = level.getGameTime();
                        long last = serverPlayer.getPersistentData().getLong("LastTeleport");
                        if (now - last < 20) {
                            return;
                        }
                        serverPlayer.getPersistentData().putLong("LastTeleport", now);

                        serverLevel.getServer().execute(() -> {
                            try {
                                serverPlayer.teleportTo(player.getX() + 20, player.getY() + 200, player.getZ());

                                ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
                                boolean bucketAdded = player.getInventory().add(waterBucket);
                                if (!bucketAdded) {
                                   //If the inventory is full, the bucket appears below the player
                                    ItemEntity airWaterBucket = new ItemEntity(serverLevel,
                                            player.getX(),
                                            player.getY() - 40,
                                            player.getZ(),
                                            waterBucket);

                                    serverLevel.addFreshEntity(airWaterBucket);

                                    player.displayClientMessage(Component.literal("¡Water drop!, ¡tienes el inventario lleno, hay un cubo de agua cayendo!"), true);
                                }else {
                                    player.displayClientMessage(Component.literal("¡Water drop a la vista!"), true);
                                }
                            } catch (Exception e) {
                                serverPlayer.teleportTo(player.getX() + 20, player.getY() + 200, player.getZ());
                            }
                        });
                        //TEMPLE
                        /*if (!teleporting.get()) {
                            teleporting.set(true);
                            ServerLevel serverLevel = (ServerLevel) level;
                            BlockPos templeOrigin = BlockPos.containing(entity.position());

                            try {
                                // Obtenemos el HolderSet que representa el conjunto con el Templo del Desierto
                                HolderSet<Structure> templeSet = serverLevel.registryAccess()
                                        .registryOrThrow(Registries.STRUCTURE)
                                        .getHolder(ResourceKey.create(Registries.STRUCTURE, ResourceLocation.withDefaultNamespace("desert_pyramid")))
                                        .map(HolderSet::direct)
                                        .orElseThrow(() -> new IllegalStateException("No temple was found"));


                                // Buscamos templo cercano
                                var result = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(
                                        serverLevel,
                                        templeSet,
                                        templeOrigin,
                                        Integer.MAX_VALUE, // sin límite
                                        false
                                );

                                if (result != null) {
                                    BlockPos templePos = result.getFirst();

                                    Holder<Structure> templeHolder = serverLevel.registryAccess()
                                            .registryOrThrow(Registries.STRUCTURE)
                                            .getHolder(ResourceKey.create(Registries.STRUCTURE, ResourceLocation.withDefaultNamespace("desert_pyramid")))
                                            .orElseThrow(() -> new IllegalStateException("No se pudo obtener el Holder del templo"));


                                    if (loading)
                                        return;

                                    new Thread(() -> {
                                        // Forzamos todos los chunks de StructurePieces
                                        loading = true;
                                        StructureStart templeStart = serverLevel.structureManager().getStructureAt(templePos, templeHolder.value());
                                        if (templeStart.isValid()) {
                                            for (StructurePiece piece : templeStart.getPieces()) {
                                                var box = piece.getBoundingBox();
                                                for (int cx = box.minX() >> 4; cx <= box.maxX() >> 4; cx++) {
                                                    for (int cz = box.minZ() >> 4; cz <= box.maxZ() >> 4; cz++) {
                                                        serverLevel.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, true);
                                                    }
                                                }
                                            }
                                        }

                                        //sleep
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }

                                        // Calculamos el centro X/Z
                                        int centerX = templePos.getX();
                                        int centerZ = templePos.getZ();
                                        if (templeStart != null && templeStart.isValid()) {
                                            var box = templeStart.getBoundingBox();
                                            centerX = (box.minX() + box.maxX()) / 2;
                                            centerZ = (box.minZ() + box.maxZ()) / 2;
                                        }

                                        // Buscamos el primer bloque sólido desde arriba
                                        int topY = serverLevel.getHeight() - 1;
                                        BlockPos teleportPos = new BlockPos(centerX, topY, centerZ);
                                        for (int y = topY; y > 0; y--) {
                                            BlockPos check = new BlockPos(centerX, y, centerZ);
                                            if (serverLevel.getBlockState(check).isSolidRender(serverLevel, check)) {
                                                teleportPos = check.above();
                                                break;
                                            }
                                        }
                                        if (loading) {

                                            // Teletransportamos
                                            player.teleportTo(
                                                    serverLevel,
                                                    teleportPos.getX() + 0.5,
                                                    teleportPos.getY(),
                                                    teleportPos.getZ() + 0.5,
                                                    Collections.emptySet(),
                                                    player.getYRot(),
                                                    player.getXRot()
                                            );
                                            loading = false;

                                            serverLevel.playSound(
                                                    null,                  // null → todos los jugadores cercanos lo oyen
                                                    player.blockPosition(),        // posición del jugador
                                                    SoundEvents.ENDERMAN_TELEPORT, // sonido de la Ender Pearl
                                                    SoundSource.BLOCKS,           // categoría de sonido (jugador)
                                                    1.0f,                          // volumen
                                                    1.0f                           // tono (1.0 = normal)
                                            );
                                        }
                                    }).start();


                                } else {
                                    living.hurt(level.damageSources().drown(), 2.0F);
                                }

                            } catch (Exception e) {
                                living.hurt(level.damageSources().drown(), 2.0F);
                            } finally {
                                teleporting.set(false);
                            }
                        } else {
                            living.hurt(level.damageSources().drown(), 2.0F);
                        }*/
                    } else {
                        // Aplica 1 punto de daño cada tick
                        living.hurt(level.damageSources().drown(), 2.0F);
                    }
                }
            }
            super.entityInside(state, level, pos, entity);
        }

        if (entity instanceof ItemEntity item) {
            //The item dissappears
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
