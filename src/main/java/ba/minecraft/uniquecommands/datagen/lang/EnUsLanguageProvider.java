package ba.minecraft.uniquecommands.datagen.lang;

import ba.minecraft.uniquecommands.common.core.UniqueCommandsMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public final class EnUsLanguageProvider extends LanguageProvider {

	public EnUsLanguageProvider(DataGenerator dataGen) {
		super(dataGen, UniqueCommandsMod.MODID, "en_us");
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
		
		addCommandSuccess("home_set", "Home {0} is set to: {1} {2} {3}");

		addCommandSuccess("home_return", "Returned to home {0}: {1} {2} {3}");
		addCommandFailure("home_return", "Home was not set!");
		
		addCommandSuccess("home_list", "Home {0} is set to: {1} {2} {3} ({4})");
		
		addCommandSuccess("home_clear", "All homes have been cleared.");
		addCommandFailure("home_clear", "Homes were not cleared.");
		
		addCommandSuccess("home_delete", "Home {0} was deleted.");
		addCommandFailure("home_delete", "Home {0} was not found!");

		addCommandSuccess("seen", "Player {0} was last seen {1} (UUID: {2})");
		addCommandFailure("seen", "Player {0} was never seen!");
		
		addCommandSuccess("where", "Player {0} is located at: {1} {2} {3} ({4})");
		addCommandFailure("where", "Player {0} could not be found!");
	}
	
}
