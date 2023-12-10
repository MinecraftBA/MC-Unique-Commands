package ba.minecraft.uniquecommands.common.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = UniqueCommandsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class UniqueCommandsModConfig {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.BooleanValue DIE_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue DIE_OP_LEVEL_CONFIG;

    public static Boolean DIE_ENABLED;
    public static int DIE_OP_LEVEL;
    
    static {
    	BUILDER.push("Configs for Unique Commands Mod");

    	DIE_ENABLED_CONFIG = BUILDER.comment("Defines whether /die command is enabled.")
	            .define("Die enabled", true);

    	DIE_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /die command requires.")
	            .defineInRange("Die OP level", 0, 0, 4);

    	BUILDER.pop();
    	
    	SPEC = BUILDER.build();
    }
    
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event)
    {
    	DIE_ENABLED = DIE_ENABLED_CONFIG.get();
    	DIE_OP_LEVEL = DIE_OP_LEVEL_CONFIG.get();
    }
}
