package tv.logisch.game.scoreboard;

import org.bukkit.ChatColor;

public enum EntryName {

    ENTRY_0(0,ChatColor.AQUA.toString()),
    ENTRY_1(1, ChatColor.BLACK.toString()),
    ENTRY_2(2, ChatColor.BLUE.toString()),
    ENTRY_3(3, ChatColor.BOLD.toString()),
    ENTRY_4(4, ChatColor.DARK_AQUA.toString()),
    ENTRY_5(5, ChatColor.DARK_BLUE.toString()),
    ENTRY_6(6, ChatColor.DARK_GRAY.toString()),
    ENTRY_7(7, ChatColor.DARK_GREEN.toString()),
    ENTRY_8(8,ChatColor.DARK_PURPLE.toString()),
    ENTRY_9(9, ChatColor.DARK_RED.toString()),
    ENTRY_10(10, ChatColor.GOLD.toString()),
    ENTRY_11(11, ChatColor.GRAY.toString()),
    ENTRY_12(12, ChatColor.GREEN.toString()),
    ENTRY_13(13, ChatColor.ITALIC.toString()),
    ENTRY_14(14, ChatColor.LIGHT_PURPLE.toString()),
    ENTRY_15(15, ChatColor.MAGIC.toString()),
    ENTRY_16(16, ChatColor.RED.toString()),
    ENTRY_17(17, ChatColor.RESET.toString());

    private final int entry;
    private final String entryName;

    EntryName(int entry, String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int getEntry() {
        return entry;
    }

    public String getEntryName() {
        return entryName;
    }

}
