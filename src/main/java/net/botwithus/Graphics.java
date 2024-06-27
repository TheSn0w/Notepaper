package net.botwithus;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;

import java.util.ArrayList;
import java.util.List;

import static net.botwithus.Notepaper.*;

public class Graphics extends ScriptGraphicsContext {

    private Notepaper script;

    public Graphics(ScriptConsole scriptConsole, Notepaper script) {
        super(scriptConsole);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin("My script", ImGuiWindowFlag.None.getValue())) {
            ImGui.SeparatorText("Notepaper Options");

            if (ImGui.Button("Add Notepaper") && !getNotepaperName().isEmpty()) {
                addNotepaperName(getNotepaperName());
                predefinedNotepaperNames.add(getNotepaperName());
                setNotepaperName("");
            }

            if (ImGui.IsItemHovered()) {
                ImGui.SetTooltip("Enter the name of the item to add to your list. Case-sensitive.");
            }

            ImGui.SameLine();
            ImGui.SetItemWidth(248.0F);

            setNotepaperName(ImGui.InputText("##Notepapername", getNotepaperName()));

            List<String> comboItemsList = new ArrayList<>();
            comboItemsList.add("Select an item...");
            for (String item : predefinedNotepaperNames) {
                if (item.toLowerCase().contains(getNotepaperName().toLowerCase())) {
                    comboItemsList.add(item);
                }
            }
            String[] comboItems = comboItemsList.toArray(new String[0]);
            NativeInteger selectedItemIndex = new NativeInteger(0);
            ImGui.SetItemWidth(361.0F);

            if (ImGui.Combo("##NotepaperType", selectedItemIndex, comboItems)) {
                int selectedIndex = selectedItemIndex.get();
                if (selectedIndex > 0 && selectedIndex < comboItems.length) {
                    String selectedName = comboItems[selectedIndex];
                    addNotepaperName(selectedName);
                    ScriptConsole.println("Predefined notepaper added: " + selectedName);
                    selectedItemIndex.set(0);
                }
            }

            if (!getSelectedNotepaperNames().isEmpty()) {
                if (ImGui.BeginTable("Notepaper List", 2, ImGuiWindowFlag.None.getValue())) {
                    ImGui.TableNextRow();

                    ImGui.TableSetupColumn("Notepaper Name", 0);
                    ImGui.TableSetupColumn("Action", 1);
                    ImGui.TableHeadersRow();

                    for (String notepaperName : new ArrayList<>(getSelectedNotepaperNames())) {
                        ImGui.TableNextRow();
                        ImGui.Separator();
                        ImGui.TableNextColumn();
                        ImGui.Text(notepaperName);
                        ImGui.Separator();
                        ImGui.TableNextColumn();
                        if (ImGui.Button("Remove##" + notepaperName)) {
                            removeNotepaperName(notepaperName);
                        }
                        if (ImGui.IsItemHovered()) {
                            ImGui.SetTooltip("Click to remove this notepaper");
                        }
                    }
                    ImGui.EndTable();
                }
            }
            ImGui.End();
        }

    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }


    public static List<String> predefinedNotepaperNames = List.of(
            "Huge bladed rune salvage",
            "Huge spiky rune salvage",
            "Huge plated rune salvage",
            "Huge blunt rune salvage",
            "Large blunt rune salvage",
            "Large bladed rune salvage",
            "Large plated rune salvage",
            "Large spiky rune salvage",
            "Huge bladed orikalkum salvage",
            "Huge spiky orikalkum salvage",
            "Huge plated orikalkum salvage",
            "Huge blunt orikalkum salvage",
            "Large blunt orikalkum salvage",
            "Large bladed orikalkum salvage",
            "Large plated orikalkum salvage",
            "Large spiky orikalkum salvage",
            "Ascension grips",
            "Ascension Keystone Primus",
            "Ascension Keystone Secundus",
            "Ascension Keystone Tertius",
            "Ascension Keystone Quartus",
            "Ascension Keystone Quintus",
            "Ascension Keystone Sextus"

    );
}
