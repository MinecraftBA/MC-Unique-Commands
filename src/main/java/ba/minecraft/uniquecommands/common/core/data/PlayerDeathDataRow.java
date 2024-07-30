package ba.minecraft.uniquecommands.common.core.data;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public final class PlayerDeathDataRow {
	
	private final static String UUID_KEY = "UUID";
	private final static String DIM_KEY = "Dim";
	private final static String POS_X_KEY = "PosX";
	private final static String POS_Y_KEY = "PosY";
	private final static String POS_Z_KEY= "PosZ";
	
	private UUID playerId;
	private String dimResId;
	private int posX;
	private int posY;
	private int posZ;
	
	public PlayerDeathDataRow(UUID playerId, String dimResId, int posX, int posY, int posZ) {
		this.playerId = playerId;
		this.dimResId = dimResId;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public CompoundTag serialize() {
		
		CompoundTag compoundTag = new CompoundTag();
		
		compoundTag.putUUID(UUID_KEY, playerId);
		compoundTag.putString(DIM_KEY,dimResId);
		compoundTag.putInt(POS_X_KEY, posX);
		compoundTag.putInt(POS_Y_KEY, posY);
		compoundTag.putInt(POS_Z_KEY, posZ);
		
		return compoundTag;
	}
	
	public static PlayerDeathDataRow deserialize(CompoundTag compoundTag) {

		// Get player UUID.
		UUID playerId = compoundTag.getUUID(UUID_KEY);

		// Get dimension resource ID.
		String dimResId = compoundTag.getString(DIM_KEY);
		
		int posX = compoundTag.getInt(POS_X_KEY);
		
		int posY = compoundTag.getInt(POS_Y_KEY);
		
		int posZ = compoundTag.getInt(POS_Z_KEY);
		
		// Create new instance of saved data.
		PlayerDeathDataRow dataRow = new PlayerDeathDataRow( playerId, dimResId, posX, posY, posZ);
		
		return dataRow;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public void setDimensionResId(String dimResId) {
		this.dimResId = dimResId;
	}

	public String getDimensionResId() {
		return dimResId;
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
