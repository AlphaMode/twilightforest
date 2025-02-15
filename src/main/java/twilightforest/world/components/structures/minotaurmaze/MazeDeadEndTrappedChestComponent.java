package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;

import java.util.Random;

public class MazeDeadEndTrappedChestComponent extends MazeDeadEndComponent {

	public MazeDeadEndTrappedChestComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMDETrC, nbt);
	}

	public MazeDeadEndTrappedChestComponent(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(MinotaurMazePieces.TFMMDETrC, feature, i, x, y, z, rotation);
		this.setOrientation(rotation);

		// specify a non-existant high spawn list value to stop actual monster spawns
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		//super.addComponentParts(world, rand, sbb, chunkPosIn);

		// dais
		this.placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), 2, 1, 4, sbb);
		this.placeBlock(world, Blocks.OAK_PLANKS.defaultBlockState(), 3, 1, 4, sbb);
		this.placeBlock(world, getStairState(Blocks.OAK_STAIRS.defaultBlockState(), Direction.NORTH, false), 2, 1, 3, sbb);
		this.placeBlock(world, getStairState(Blocks.OAK_STAIRS.defaultBlockState(), Direction.NORTH, false), 3, 1, 3, sbb);

		// chest
		this.setDoubleLootChest(world, 2, 2, 4,3, 2, 4, Direction.SOUTH, TFTreasure.labyrinth_deadend, sbb, true);

//		// torches
//		this.setBlockState(world, Blocks.TORCH, 0, 1, 3, 4, sbb);
//		this.setBlockState(world, Blocks.TORCH, 0, 4, 3, 4, sbb);

		// doorway w/ bars
		this.generateBox(world, sbb, 1, 1, 0, 4, 3, 1, TFBlocks.maze_stone_chiseled.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 1, 4, 0, 4, 4, 1, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 2, 1, 0, 3, 3, 1, Blocks.IRON_BARS.defaultBlockState(), AIR, false);

		// TNT!
		BlockState tnt = Blocks.TNT.defaultBlockState();
		this.placeBlock(world, tnt, 2,  0, 3, sbb);
		this.placeBlock(world, tnt, 3,  0, 3, sbb);
		this.placeBlock(world, tnt, 2,  0, 4, sbb);
		this.placeBlock(world, tnt, 3,  0, 4, sbb);

		return true;
	}
}
