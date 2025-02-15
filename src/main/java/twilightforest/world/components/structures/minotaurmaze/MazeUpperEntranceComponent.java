package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class MazeUpperEntranceComponent extends TFStructureComponentOld {

	public MazeUpperEntranceComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMUE, nbt);
	}

	public MazeUpperEntranceComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMUE, feature, i, x, y, z);
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));

		this.boundingBox = new BoundingBox(x, y, z, x + 15, y + 4, z + 15);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, Random random) {
		// NO-OP
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// ceiling
		this.generateMaybeBox(world, sbb, rand, 0.7F, 0, 5, 0, 15, 5, 15, TFBlocks.maze_stone.get().defaultBlockState(), AIR, true, false);

		this.generateBox(world, sbb, 0, 0, 0, 15, 0, 15, TFBlocks.maze_stone_mosaic.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 0, 1, 0, 15, 1, 15, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, true);
		this.generateBox(world, sbb, 0, 2, 0, 15, 3, 15, TFBlocks.maze_stone_brick.get().defaultBlockState(), AIR, true);
		this.generateBox(world, sbb, 0, 4, 0, 15, 4, 15, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, true);
		this.generateMaybeBox(world, sbb, rand, 0.2F, 0, 0, 0, 15, 5, 15, Blocks.GRAVEL.defaultBlockState(), AIR, true, false);

		// doorways
		generateBox(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 7, 1, 0, 8, 3, 0);
		generateBox(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 7, 1, 15, 8, 3, 15);
		generateBox(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 0, 1, 7, 0, 3, 8);
		generateBox(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.defaultBlockState(), AIR, false);
		generateAirBox(world, sbb, 15, 1, 7, 15, 3, 8);

		// random holes
		this.generateAirBox(world, sbb, 1, 1, 1, 14, 4, 14);

		// entrance pit
		this.generateBox(world, sbb, 5, 1, 5, 10, 1, 10, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 5, 4, 5, 10, 4, 10, TFBlocks.maze_stone_decorative.get().defaultBlockState(), AIR, false);
		this.generateMaybeBox(world, sbb, rand, 0.7F, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS.defaultBlockState(), AIR, false, false);
//		this.fillWithBlocks(world, sbb, 5, 2, 5, 10, 3, 10, Blocks.IRON_BARS, 0, AIR, false);

		this.generateAirBox(world, sbb, 6, 0, 6, 9, 4, 9);

		return true;
	}

	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	@Override
	protected int getAverageGroundLevel(WorldGenLevel world, ChunkGenerator generator, BoundingBox boundingBox) {
		int yTotal = 0;
		int count = 0;

		for (int z = this.boundingBox.minZ(); z <= this.boundingBox.maxZ(); ++z) {
			for (int x = this.boundingBox.minX(); x <= this.boundingBox.maxX(); ++x) {
				BlockPos pos = new BlockPos(x, 64, z);

				if (boundingBox.isInside(pos)) {
					final BlockPos topPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
					yTotal += Math.max(topPos.getY(), WorldUtil.getSeaLevel(generator));
					++count;
				}
			}
		}

		if (count == 0) {
			return -1;
		} else {
			return yTotal / count;
		}
	}
}
