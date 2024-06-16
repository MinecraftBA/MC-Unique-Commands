package ba.minecraft.uniquecommands.common.core.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public final class PlayerAccountDataRow {

	private final static String PLAYER_NAME_KEY = "Name";
	private final static String PASSWORD_KEY = "Pass";
	private final static String BANNED_KEY = "Banned";
	private final static String FAILED_LOGINS_COUNT_KEY = "Fails";
	private final static String POS_X_KEY = "PosX";
	private final static String POS_Y_KEY = "PosZ";
	private final static String POS_Z_KEY = "PosY";
	private final static String DIMENSION_KEY = "Dim";
	private final static String OP_LEVEL_KEY = "Op";
	
	private String playerName;
	private String password;
	private boolean banned;
	private int failedLoginsCount;
	
	private int posX; // X coordinate of player login location
	private int posY; // Y coordinate of player login location
	private int posZ; // Z coordinate of player login location
	private String dimension; // Dimension of player login location
	private int opLevel; // OP level of player login location
	
	public PlayerAccountDataRow(String playerName, String password, boolean banned, int failedLoginsCount, int posX, int posY, int posZ, String dimension, int opLevel) {
		this.playerName = playerName;
		this.password = password;
		this.banned = banned;
		this.failedLoginsCount = failedLoginsCount;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.dimension = dimension;
		this.opLevel = opLevel;
	}
	
	public CompoundTag serialize() {
		
		CompoundTag compoundTag = new CompoundTag();
		
		compoundTag.putString(PLAYER_NAME_KEY, playerName);
		compoundTag.putString(PASSWORD_KEY, password);
		compoundTag.putBoolean(BANNED_KEY, banned);
		compoundTag.putInt(FAILED_LOGINS_COUNT_KEY, failedLoginsCount);
		compoundTag.putInt(POS_X_KEY, posX);
		compoundTag.putInt(POS_Y_KEY, posY);
		compoundTag.putInt(POS_Z_KEY, posZ);
		compoundTag.putString(DIMENSION_KEY, dimension);
		compoundTag.putInt(OP_LEVEL_KEY, opLevel);
		
		return compoundTag;
	}
	
	public static PlayerAccountDataRow deserialize(CompoundTag compoundTag) {

		// Get player name.
		String playerName = compoundTag.getString(PLAYER_NAME_KEY);

		// Get password.
		String password = compoundTag.getString(PASSWORD_KEY);
		
		// Get banned status.
		boolean banned = compoundTag.getBoolean(BANNED_KEY);

		// Get number of failed logins.
		int failedLoginsCount = compoundTag.getInt(FAILED_LOGINS_COUNT_KEY);

		// Get X coordinate of login location.
		int posX = compoundTag.getInt(POS_X_KEY);

		// Get Y coordinate of login location.
		int posY = compoundTag.getInt(POS_Y_KEY);

		// Get Z coordinate of login location.
		int posZ = compoundTag.getInt(POS_Z_KEY);

		// Get dimension of login location.
		String dimension = compoundTag.getString(DIMENSION_KEY);

		// Get OP level on login.
		int opLevel = compoundTag.getInt(OP_LEVEL_KEY);

		// Create new instance of saved data.
		PlayerAccountDataRow dataRow = new PlayerAccountDataRow(playerName, password, banned, failedLoginsCount, posX, posY, posZ, dimension, opLevel);
		
		return dataRow;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getBanned() {
		return banned;
	}
	
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
	public int getFailedLoginsCount() {
		return failedLoginsCount;
	}
	
	public void setFailedLoginsCount(int failedLoginsCount) {
		this.failedLoginsCount = failedLoginsCount;
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
	
	public int getOpLevel() {
		return opLevel;
	}
	
	public void setOpLevel(int opLevel) {
		this.opLevel = opLevel;
	}

}
