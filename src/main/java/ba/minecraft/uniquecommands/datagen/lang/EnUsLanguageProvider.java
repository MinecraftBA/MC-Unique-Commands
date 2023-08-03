package ba.minecraft.uniquecommands.datagen.lang;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class EnUsLanguageProvider extends LanguageProvider {

	public EnUsLanguageProvider(DataGenerator dataGen) {
		super(dataGen.getPackOutput(), UniqueCommandsMod.MODID, "en_us");
	}

	private void addCommandSuccess(String commandName, String value) {
		add("command." + UniqueCommandsMod.MODID + "." + commandName + ".success", value);
	}

	private void addCommandFailure(String commandName, String value) {
		add("command." + UniqueCommandsMod.MODID + "." + commandName + ".failure", value);
	}

	@Override
	protected void addTranslations() {
		
		addCommands();
		
	}
	
	private void addCommands() {
		
		addCommandSuccess("home_set", "Home %1$s is set to: %2$s %3$s %4$s");

		addCommandSuccess("home_return", "Returned to home %1$s: %2$s %3$s %4$s");
		addCommandFailure("home_return", "Home was not set!");
		
		addCommandSuccess("home_list", "Home %1$s is set to: %2$s %3$s %4$s (%5$s)");
		
		addCommandSuccess("home_clear", "All homes have been cleared.");
		addCommandFailure("home_clear", "Homes were not cleared.");
		
		addCommandSuccess("home_delete", "Home %1$s was deleted.");
		addCommandFailure("home_delete", "Home %1$s was not found!");

		addCommandSuccess("seen", "Player %1$s was last seen %2$s (UUID: %3$s)");
		addCommandFailure("seen", "Player %1$s was never seen!");
		
		addCommandSuccess("where", "Player %1$s is located at: %2$s %3$s %4$s (%5$s)");
		addCommandFailure("where", "Player %1$s could not be found!");
		
		addCommandSuccess("die", "A Player has died");
	}
	
}
