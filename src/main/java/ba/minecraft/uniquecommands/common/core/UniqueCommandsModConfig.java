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

    private static final ForgeConfigSpec.BooleanValue GRAVEBACK_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue GRAVEBACK_OP_LEVEL_CONFIG;
    
    private static final ForgeConfigSpec.BooleanValue MEET_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue MEET_OP_LEVEL_CONFIG;
    
    private static final ForgeConfigSpec.BooleanValue SEEN_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue SEEN_OP_LEVEL_CONFIG;
    
    private static final ForgeConfigSpec.BooleanValue TP_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue TP_OP_LEVEL_CONFIG;
    
    private static final ForgeConfigSpec.BooleanValue WHERE_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue WHERE_OP_LEVEL_CONFIG;
    
    private static final ForgeConfigSpec.BooleanValue HOME_ENABLED_CONFIG;
    private static final ForgeConfigSpec.IntValue HOME_OP_LEVEL_CONFIG;

    public static Boolean DIE_ENABLED;
    public static int DIE_OP_LEVEL;
    
    public static Boolean GRAVEBACK_ENABLED;
    public static int GRAVEBACK_OP_LEVEL;
    
    public static Boolean MEET_ENABLED;
    public static int MEET_OP_LEVEL;
    
    public static Boolean SEEN_ENABLED;
    public static int SEEN_OP_LEVEL;
    
    public static Boolean TP_ENABLED;
    public static int TP_OP_LEVEL;
    
    public static Boolean WHERE_ENABLED;
    public static int WHERE_OP_LEVEL;
    
    public static Boolean HOME_ENABLED;
    public static int HOME_OP_LEVEL;
    
    static {
    	BUILDER.push("Configs for Unique Commands Mod");

    	DIE_ENABLED_CONFIG = BUILDER.comment("Defines whether /die command is enabled.")
	            .define("Die enabled", true);

    	DIE_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /die command requires.")
	            .defineInRange("Die OP level", 0, 0, 4);
    	
    	GRAVEBACK_ENABLED_CONFIG = BUILDER.comment("Defines whether /graveback command is enabled.")
	            .define("Graveback enabled", true);
    	
    	GRAVEBACK_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /graveback command requires.")
	            .defineInRange("Graveback OP level", 0, 0, 4);
    	
    	MEET_ENABLED_CONFIG = BUILDER.comment("Defines whether /meet command is enabled.")
	            .define("Meet enabled", true);
    	
    	MEET_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /meet command requires.")
	            .defineInRange("Meet OP level", 4, 0, 4);
    	
    	SEEN_ENABLED_CONFIG = BUILDER.comment("Defines whether /seen command is enabled.")
	            .define("Seen enabled", true);
    	
    	SEEN_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /seen command requires.")
	            .defineInRange("Seen OP level", 3, 0, 4);
    	
    	TP_ENABLED_CONFIG = BUILDER.comment("Defines whether /tp command is enabled.")
	            .define("Tp enabled", true);
    	
    	TP_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /tp command requires.")
	            .defineInRange("Tp OP level", 0, 0, 4);
    	
    	WHERE_ENABLED_CONFIG = BUILDER.comment("Defines whether /where command is enabled.")
	            .define("Where enabled", true);
    	
    	WHERE_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /where command requires.")
	            .defineInRange("Where OP level", 3, 0, 4);
    	
    	HOME_ENABLED_CONFIG = BUILDER.comment("Defines whether /home command is enabled.")
	            .define("Home enabled", true);
    	
    	HOME_OP_LEVEL_CONFIG = BUILDER.comment("Defines what op level /home command requires.")
	            .defineInRange("Home OP level", 0, 0, 4);

    	BUILDER.pop();
    	
    	SPEC = BUILDER.build();
    }
    
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event)
    {
    	DIE_ENABLED = DIE_ENABLED_CONFIG.get();
    	DIE_OP_LEVEL = DIE_OP_LEVEL_CONFIG.get();

    	GRAVEBACK_ENABLED = GRAVEBACK_ENABLED_CONFIG.get();
    	GRAVEBACK_OP_LEVEL = GRAVEBACK_OP_LEVEL_CONFIG.get();
    	
    	MEET_ENABLED = MEET_ENABLED_CONFIG.get();
    	MEET_OP_LEVEL = MEET_OP_LEVEL_CONFIG.get();
    	
    	SEEN_ENABLED = SEEN_ENABLED_CONFIG.get();
    	SEEN_OP_LEVEL = SEEN_OP_LEVEL_CONFIG.get();
    	
    	TP_ENABLED = TP_ENABLED_CONFIG.get();
    	TP_OP_LEVEL = TP_OP_LEVEL_CONFIG.get();
    	
    	WHERE_ENABLED = WHERE_ENABLED_CONFIG.get();
    	WHERE_OP_LEVEL = WHERE_OP_LEVEL_CONFIG.get();
    	
    	HOME_ENABLED = HOME_ENABLED_CONFIG.get();
    	HOME_OP_LEVEL = HOME_OP_LEVEL_CONFIG.get();
    }
}
