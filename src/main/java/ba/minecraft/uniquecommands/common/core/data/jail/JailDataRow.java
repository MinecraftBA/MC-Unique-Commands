package ba.minecraft.uniquecommands.common.core.data.jail;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public final class JailDataRow {
	
	private final static String NAME_KEY = "Name";
	private final static String DIM_KEY = "Dim";
	private final static String POS_X_KEY = "PosX";
	private final static String POS_Y_KEY = "PosY";
	private final static String POS_Z_KEY= "PosZ";
	
	private String name;
	private String dimension;
	private int posX;
	private int posY;
	private int posZ;
	
	public JailDataRow(String name, String dimension, int posX, int posY, int posZ) {
		this.name = name;
		this.dimension = dimension;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public CompoundTag serialize() {
		
		CompoundTag compoundTag = new CompoundTag();
		
		compoundTag.putString(NAME_KEY, name);
		compoundTag.putString(DIM_KEY,dimension);
		compoundTag.putInt(POS_X_KEY, posX);
		compoundTag.putInt(POS_Y_KEY, posY);
		compoundTag.putInt(POS_Z_KEY, posZ);
		
		return compoundTag;
	}
	
	public static JailDataRow deserialize(CompoundTag compoundTag) {

		// Get jail name.
		String name = compoundTag.getString(NAME_KEY);

		// Get dimension name.
		String dimension = compoundTag.getString(DIM_KEY);
		
		int posX = compoundTag.getInt(POS_X_KEY);
		
		int posY = compoundTag.getInt(POS_Y_KEY);
		
		int posZ = compoundTag.getInt(POS_Z_KEY);
		
		// Create new instance of saved data.
		JailDataRow dataRow = new JailDataRow( name, dimension, posX, posY, posZ);
		
		return dataRow;
	}
	
	public String getName() {
		return name;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getDimension() {
		return dimension;
	}
	
	public void setBlockPos(BlockPos blockPos) {
		this.posX = blockPos.getX();
		this.posY = blockPos.getY();
		this.posZ = blockPos.getZ();
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public int getPosZ() {
		return posZ;
	}
	
	public BlockPos getBlockPos() {
		return new BlockPos(posX,posY,posZ);
	}
}
