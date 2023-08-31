package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class TeleportationHelper {
	
	public static boolean isSafe(ServerLevel level, BlockPos pos) {
		
		// Create coordinates for blocks above and below.
		BlockPos aboveBlockPos = pos.above();
		BlockPos belowBlockPos = pos.below();
		
		// Get blockstate of block, block above and block below.
		BlockState blockState = level.getBlockState(pos);
		BlockState aboveBlockState = level.getBlockState(aboveBlockPos);
		BlockState belowBlockState = level.getBlockState(belowBlockPos);
		
		// Check whether block is air, above block is air and below block is not air.
		boolean isSafe = blockState.isAir() && aboveBlockState.isAir() && !belowBlockState.isAir();
		
		return isSafe;
	}

}
