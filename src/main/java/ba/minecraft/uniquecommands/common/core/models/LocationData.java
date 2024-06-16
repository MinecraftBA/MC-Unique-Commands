package ba.minecraft.uniquecommands.common.core.models;

import net.minecraft.core.BlockPos;

public class LocationData {
	
	private BlockPos blockPos;
	private String dimensionResId;
	
	public LocationData(BlockPos blockPos, String dimensionResId) {
		this.blockPos = blockPos;
		this.dimensionResId = dimensionResId;
	}
	
	public LocationData(int x, int y, int z, String dimensionResId) {
		this(new BlockPos(x,y,z), dimensionResId);
	}
	
	public int[] getCoords() {
		return new int[] {
			blockPos.getX(),
			blockPos.getY(),
			blockPos.getZ()
		};
	}
	
	public BlockPos getBlockPos() {
		return blockPos;
	}
	
	public String getDimensionResId() {
		return dimensionResId;
	}
	
	public int getX() {
		return blockPos.getX();
	}

	public int getY() {
		return blockPos.getY();
	}

	public int getZ() {
		return blockPos.getZ();
	}

}
