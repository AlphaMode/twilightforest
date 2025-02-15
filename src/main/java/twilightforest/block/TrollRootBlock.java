package twilightforest.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import twilightforest.item.TFItems;

public class TrollRootBlock extends Block {

	protected static final VoxelShape AABB = Shapes.create(new AABB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9));

	protected TrollRootBlock(Properties props) {
		super(props);
	}

	public static boolean canPlaceRootBelow(LevelReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		return state.is(BlockTags.BASE_STONE_OVERWORLD) || block == TFBlocks.trollvidr.get() || block == TFBlocks.trollber.get() || block == TFBlocks.unripe_trollber.get();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(state.getBlock() == TFBlocks.trollber.get()) {
			level.setBlock(pos, TFBlocks.trollvidr.get().defaultBlockState(), 2);
			level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
			ItemEntity torchberries = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFItems.torchberries.get()));
			level.addFreshEntity(torchberries);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return canPlaceRootBelow(world, pos.above());
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction dirToNeighbor, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (dirToNeighbor == Direction.UP) {
			return canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
		}
		return state;
	}
}
