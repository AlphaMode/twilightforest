package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;

import java.util.function.Supplier;

@Deprecated // It appears we don't need any of this, since it's all delegated to noise settings now
public class ChunkGeneratorTwilightSky extends ChunkGeneratorTwilightBase {
	public static final Codec<ChunkGeneratorTwilightSky> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter((obj) -> obj.biomeSource),
			Codec.LONG.fieldOf("seed").stable().forGetter((obj) -> obj.seed),
			NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(ChunkGeneratorTwilightSky::getDimensionSettings)
	).apply(instance, instance.stable(ChunkGeneratorTwilightSky::new)));

	//private final boolean generateHollowTrees = TFConfig.COMMON_CONFIG.DIMENSION.skylightOaks.get();

	public ChunkGeneratorTwilightSky(BiomeSource provider, long seed, Supplier<NoiseGeneratorSettings> settings) {
		super(provider, seed, settings);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long l) {
		return new ChunkGeneratorTwilightSky(this.biomeSource.withSeed(l), l, this.dimensionSettings);
	}

	private Supplier<NoiseGeneratorSettings> getDimensionSettings() {
		return this.dimensionSettings;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion world, ChunkAccess chunk) {
		this.deformTerrainForFeature(world, chunk);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) { //decorate?
		//int x = region.getCenterX();
		//int z = region.getCenterZ();

		//TODO: Is there a point to this?
//		rand.setSeed(getSeed(x, z));

		//ChunkBitArray data = new ChunkBitArray();
		//getBaseHeight(x, z, data);
		//squishTerrain(data); Not required anymore; noise config

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		//this.biomesForGeneration = getBiomeProvider().getBiomesInArea(biomesForGeneration, x * 16, z * 16, 16, 16);

		//ProtoChunk primer = new DirectChunkPrimer(region.getCenter(), region.getLevel()); // Is this even correct?
		//initPrimer(primer, data); TODO : Should be do SurfaceBuilder

		//deformTerrainForFeature(x, z, region);
		//replaceBiomeBlocks(x, z, primer, biomesForGeneration);

//		generateFeatures(x, z, primer); TODO: Should move to biome decorator
//		if (generateHollowTrees) { TODO: Should be done via biome decorator
//			hollowTreeGenerator.generate(world, x, z, primer);
//		}

//		makeChunk(x, z, primer);
	}

	/*@Override
	protected void initPrimer(ChunkPrimer primer, ChunkBitArray data) {

		BlockState stone = Blocks.STONE.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
				if (biome != TFBiomes.highlandsCenter.get()) continue;

				for (int y = 0; y < 256; y++) {
					if (data.get(getIndex(x, y, z))) {
						primer.setBlockState(x, y, z, stone);
					}
				}
			}
		}
	}*/

//	@Override
//	public void decorate(int region) {
//
////		BlockFalling.fallInstantly = true;
//
////		int i = x * 16;
////		int j = z * 16;
////		BlockPos blockpos = new BlockPos(i, 0, j);
//		this.rand.setSeed(this.world.getSeed());
//		long k = this.rand.nextLong() / 2L * 2L + 1L;
//		long l = this.rand.nextLong() / 2L * 2L + 1L;
//		this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
////		boolean flag = false;
//
////		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);
//
//		//TODO: The following code block should be generated via Biome Decorator
////		for (MapGenTFMajorFeature generator : featureGenerators.values()) {
////			generator.place(world, world.getRandom(), chunkpos);
////		}
////
////		if (generateHollowTrees) {
////			hollowTreeGenerator.generateStructure(world, rand, chunkpos);
////		}
//
////		blockpos = blockpos.add(8, 0, 8);
//
////		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
////			for (int k2 = 0; k2 < 16; ++k2) {
////				for (int j3 = 0; j3 < 16; ++j3) {
////
//////					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
//////					BlockPos blockpos2 = blockpos1.down();
////
////					//TODO: Handled via SurfaceBuilder
//////					if (this.world.canBlockFreezeWater(blockpos2)) {
//////						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 16 | 2);
//////					}
////
////					//TODO: This is done via Feature now
//////					if (this.world.canSnowAt(blockpos1, true)) {
//////						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 16 | 2);
//////					}
////				}
////			}
////		}//Forge: End ICE
//
////		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);
//
////		BlockFalling.fallInstantly = false;
//	}

	//TODO : Should be move to biome decorator, or WorldCarver?
	/*@Override
	protected void deformTerrainForTrollCaves(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		int radius = (nearFeature.size * 2 + 1) * 8;
		int dist = (int) Math.sqrt(dx * dx + dz * dz);
		if (dist > radius) return;

		Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
		if (biome != TFBiomes.highlands.get()) return;

		for (int y = 0; y < 60; y++) {
			if (primer.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), false);
			}
		}
	}*/

	//TODO: See super
//	@Override
//	public void recreateStructures(Chunk chunk, int x, int z) {
//		super.recreateStructures(chunk, x, z);
//		if (generateHollowTrees) {
//			hollowTreeGenerator.generate(world, x, z, null);
//		}
//	}
}
