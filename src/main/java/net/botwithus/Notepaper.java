package net.botwithus;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.js5.types.configs.ConfigManager;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.SelectableAction;
import net.botwithus.rs3.game.scene.entities.characters.player.LocalPlayer;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.config.ScriptConfig;
import net.botwithus.rs3.util.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Notepaper extends LoopingScript {

    private Random random = new Random();


    public Notepaper(String s, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(s, scriptConfig, scriptDefinition);
        this.sgc = new Graphics(getConsole(), this);
        this.isBackgroundScript = true;
    }

    @Override
    public void onLoop() {
        LocalPlayer player = Client.getLocalPlayer();
        if (player == null || Client.getGameState() != Client.GameState.LOGGED_IN) {
            Execution.delay(random.nextLong(3000, 7000));
            return;
        }
        useItemOnNotepaper();
    }
    public static String NotepaperName = "";

    public static String getNotepaperName() {
        return NotepaperName;
    }

    public static void setNotepaperName(String notepaperName) {
        NotepaperName = notepaperName;
    }

    public static void removeNotepaperName(String notepaperName) {
        ScriptConsole.println("[Info] Removing " + notepaperName + " from selected notepaper names.");
        selectedNotepaperNames.remove(notepaperName);
    }

    public static final List<String> selectedNotepaperNames = new ArrayList<>();

    public static List<String> getSelectedNotepaperNames() {
        return selectedNotepaperNames;
    }

    public static void addNotepaperName(String notepaperName) {
        ScriptConsole.println("[Info] Adding " + notepaperName + " to selected notepaper names.");
        selectedNotepaperNames.add(notepaperName);
    }
    public static void useItemOnNotepaper() {
        List<Item> backpackItems = new ArrayList<>(Backpack.getItems());

        for (String itemName : getSelectedNotepaperNames()) {
            List<Item> matchingItems = backpackItems.stream()
                    .filter(item -> item.getName().toLowerCase().contains(itemName.toLowerCase()))
                    .toList();

            for (Item targetItem : matchingItems) {
                var itemType = ConfigManager.getItemType(targetItem.getId());
                boolean isNote = itemType != null && itemType.isNote();
                if (isNote) {
                    continue;
                }

                Item notepaper = fetchNotepaperFromInventory();
                if (notepaper == null) {
                    ScriptConsole.println("[Error] Neither Magic Notepaper nor Enchanted Notepaper found in inventory.");
                    return;
                }

                boolean itemSelected = MiniMenu.interact(SelectableAction.SELECTABLE_COMPONENT.getType(), 0, targetItem.getSlot(), 96534533);
                ScriptConsole.println("[Info] Item selected: " + itemSelected);
                Execution.delay(RandomGenerator.nextInt(200, 300));

                if (itemSelected) {
                    boolean notepaperSelected = MiniMenu.interact(SelectableAction.SELECT_COMPONENT_ITEM.getType(), 0, notepaper.getSlot(), 96534533);
                    ScriptConsole.println("[Info] Notepaper selected: " + notepaperSelected);

                    if (notepaperSelected) {
                        String notepaperName = notepaper.getName();
                        ScriptConsole.println("[Success] " + itemName + " successfully used on " + notepaperName + ".");
                        break;
                    } else {
                        String notepaperName = notepaper.getName();
                        ScriptConsole.println("[Error] Failed to use " + itemName + " on " + notepaperName + ".");
                        ScriptConsole.println("[Debug] Notepaper details - Name: " + notepaper.getName() + ", ID: " + notepaper.getId());
                    }
                } else {
                    ScriptConsole.println("[Error] Failed to select " + itemName + ".");
                    ScriptConsole.println("[Debug] Item details - Name: " + targetItem.getName() + ", ID: " + targetItem.getId());
                }
            }
        }
    }

    private static Item fetchNotepaperFromInventory() {
        Item magicNotepaper = fetchSpecificNotepaper("Magic notepaper");

        if (magicNotepaper == null) {
            ScriptConsole.println("[Debug] Magic Notepaper not found in inventory. Trying to fetch Enchanted notepaper...");
            Item enchantedNotepaper = fetchSpecificNotepaper("Enchanted notepaper");

            if (enchantedNotepaper == null) {
                ScriptConsole.println("[Debug] Enchanted Notepaper not found in inventory.");
                return null;
            } else {
                return enchantedNotepaper;
            }
        } else {
            return magicNotepaper;
        }
    }

    private static Item fetchSpecificNotepaper(String notepaperName) {
        Item notepaper = Backpack.getItem(notepaperName);
        if (notepaper != null) {
            ScriptConsole.println("[Info] Notepaper found: " + notepaper.getName());
            return notepaper;
        }
        ScriptConsole.println("[Debug] " + notepaperName + " not found in inventory.");
        return null;
    }

}
