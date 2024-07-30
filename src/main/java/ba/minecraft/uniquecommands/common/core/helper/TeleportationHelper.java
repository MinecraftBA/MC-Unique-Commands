package ba.minecraft.uniquecommands.common.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent.TeleportCommand;

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
	
	public static boolean teleportCommand(ServerLevel level, Entity entity, double x, double y, double z) {
        
		// Generate teleportation event via FORGE bus.
		TeleportCommand event = ForgeEventFactory.onEntityTeleportCommand(entity, x, y, z);
        
		// IF: Teleportation was canceled by event handler.
		if (event.isCanceled()) {
			
			// Indicate that teleportation was not performed.
			return false;
		}
		
		// Take coordinates from event, just in case that event handler has modified them.
        x = event.getTargetX();
        y = event.getTargetY();
        z = event.getTargetZ();
        
        // Get block position on specified coordinates.
        BlockPos blockPos = BlockPos.containing(x, y, z);
		
        // IF: Position is not in spawnable bounds (outside of the dimension constraints).
        if (!Level.isInSpawnableBounds(blockPos)) {
        	
        	// Indicate that teleportation was not performed.
        	return false;
        }

        // Get yaw and pitch of entity.
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        
        // Round yaw and pitch.
        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.wrapDegrees(pitch);
        
        // IF: Teleportation was not successful.
        if(!entity.teleportTo(level, x, y, z, null, yaw, pitch)) {
        	
        	// Indicate it wasn't.
        	return false;
        }

        // IF: Entity is not living entity or it is not still falling.
        if (!(entity instanceof LivingEntity livingentity) || !livingentity.isFallFlying()) {
        	
        	// Prevent movement on Y axis.
        	entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
        	
        	// Indicate that entity has hit the ground.
        	entity.setOnGround(true);
        }

        // IF: Entity was pathfinder mob.
        if (entity instanceof PathfinderMob pathfindermob) {
        	
        	// Prevent further movement.
            pathfindermob.getNavigation().stop();
        }
        
        return true;
	}

}
