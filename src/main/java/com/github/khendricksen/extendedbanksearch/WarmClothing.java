package com.github.khendricksen.extendedbanksearch;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.gameval.ItemID;

/**
 * The full set of warm-clothing item ids for the {@code warm} query.
 *
 * <p>Warmth is a Wintertodt mechanic, not an equipment stat, so it cannot be derived from item data
 * and must be hard-coded. This list is sourced from the OSRS Wiki "Warm clothing" page. Callers
 * should {@link net.runelite.client.game.ItemManager#canonicalize(int)} an item id (to fold
 * placeholders onto the base id) before calling {@link #isWarm}.
 *
 * <p>MAINTENANCE: this needs occasional updates as Jagex adds warm items. Dummy / worn / minigame
 * (Soul Wars, PvP Arena) and beta variants are intentionally excluded as they never sit in a bank.
 */
final class WarmClothing
{
	static final Set<Integer> ITEM_IDS = ImmutableSet.copyOf(new Integer[]{
		ItemID.SANTA_MASK, // Santa mask
		ItemID.ANTISANTA_MASK, // Antisanta mask
		ItemID.EASTER19_BUNNY_MASK, // Bunnyman mask
		ItemID.HUNTING_HAT_JAGUAR, // Larupia hat
		ItemID.HUNTING_HAT_LEOPARD, // Graahk headdress
		ItemID.HUNTING_HAT_TIGER, // Kyatt hat
		ItemID.EASTER07_CHICKEN_HEAD, // Chicken head
		ItemID.EVIL_CHICKEN_HEAD, // Evil chicken head
		ItemID.PYROMANCER_HOOD, // Pyromancer hood
		ItemID.SANTA_HAT, // Santa hat
		ItemID.BLACK_SANTA_HAT, // Black santa hat
		ItemID.INVERTED_SANTA_HAT, // Inverted santa hat
		ItemID.XM21_FESTIVE_HAT, // Festive elf hat
		ItemID.XMAS22_SANTA_CROWN, // Festive games crown
		ItemID.MDAUGHTER_BEAR_HELMET, // Bearhead
		ItemID.TIARA_FIRE, // Fire tiara
		ItemID.TIARA_ELEMENTAL, // Elemental tiara
		ItemID.RAMBLE_LUMBERJACK_HAT, // Lumberjack hat
		ItemID.FORESTRY_LUMBERJACK_HAT, // Forestry hat
		ItemID.XMAS22_GOGGLES, // Snow goggles & hat
		ItemID.XMAS23_HELMET, // Snowglobe helmet
		ItemID.SKILLCAPE_FIREMAKING_HOOD, // Firemaking hood
		ItemID.SKILLCAPE_MAX_HOOD_FIRECAPE, // Fire max hood
		ItemID.SKILLCAPE_MAX_HOOD_INFERNALCAPE, // Infernal max hood
		ItemID.ZEP_BOMBER_CAP, // Bomber cap
		ItemID.ZEP_BOMBER_CAP_GOGGLES, // Cap and goggles
		ItemID.WIN05_HAT1, // Bobble hat
		ItemID.SLAYER_EARMUFFS, // Earmuffs
		ItemID.WOLF_MASK, // Wolf mask
		ItemID.WIN05_HAT4, // Woolly hat
		ItemID.WIN05_HAT2, // Jester hat
		ItemID.WIN05_HAT3, // Tri-jester hat
		ItemID.SLAYER_HELM, // Slayer helmet
		ItemID.SLAYER_HELM_I, // Slayer helmet (i)
		ItemID.SLAYER_HELM_ARAXYTE, // Araxyte slayer helmet
		ItemID.SLAYER_HELM_I_ARAXYTE, // Araxyte slayer helmet (i)
		ItemID.SLAYER_HELM_BLACK, // Black slayer helmet
		ItemID.SLAYER_HELM_I_BLACK, // Black slayer helmet (i)
		ItemID.SLAYER_HELM_GREEN, // Green slayer helmet
		ItemID.SLAYER_HELM_I_GREEN, // Green slayer helmet (i)
		ItemID.SLAYER_HELM_HYDRA, // Hydra slayer helmet
		ItemID.SLAYER_HELM_I_HYDRA, // Hydra slayer helmet (i)
		ItemID.SLAYER_HELM_PURPLE, // Purple slayer helmet
		ItemID.SLAYER_HELM_I_PURPLE, // Purple slayer helmet (i)
		ItemID.SLAYER_HELM_RED, // Red slayer helmet
		ItemID.SLAYER_HELM_I_RED, // Red slayer helmet (i)
		ItemID.SLAYER_HELM_TURQUOISE, // Turquoise slayer helmet
		ItemID.SLAYER_HELM_I_TURQUOISE, // Turquoise slayer helmet (i)
		ItemID.SLAYER_HELM_TWISTED, // Twisted slayer helmet
		ItemID.SLAYER_HELM_I_TWISTED, // Twisted slayer helmet (i)
		ItemID.SLAYER_HELM_ZUK, // Tzkal slayer helmet
		ItemID.SLAYER_HELM_I_ZUK, // Tzkal slayer helmet (i)
		ItemID.SLAYER_HELM_JAD, // Tztok slayer helmet
		ItemID.SLAYER_HELM_I_JAD, // Tztok slayer helmet (i)
		ItemID.SLAYER_HELM_VERZIK, // Vampyric slayer helmet
		ItemID.SLAYER_HELM_I_VERZIK, // Vampyric slayer helmet (i)
		ItemID.SLAYER_HELM_HOODED, // Hooded slayer helmet
		ItemID.SLAYER_HELM_I_HOODED, // Hooded slayer helmet (i)
		ItemID.WIN05_SCARF2, // Jester scarf
		ItemID.WIN05_SCARF3, // Tri-jester scarf
		ItemID.WIN05_SCARF4, // Woolly scarf
		ItemID.WIN05_SCARF1, // Bobble scarf
		ItemID.ALUFT_GNOME_SCARF, // Gnome scarf
		ItemID.PRIDE17_SCARF, // Rainbow scarf
		ItemID.XMAS24_FESTIVE_SCARF, // Festive scarf
		ItemID.SANTA_GLOVES, // Santa gloves
		ItemID.ANTISANTA_GLOVES, // Antisanta gloves
		ItemID.EASTER16_ONSIE_GLOVES, // Bunny paws
		ItemID.CLUE_HUNTER_GLOVES, // Clue hunter gloves
		ItemID.HUNTING_SILENT_GLOVES, // Gloves of silence
		ItemID.VIKINGGLOVES, // Fremennik gloves
		ItemID.PYROMANCER_GLOVES, // Warm gloves
		ItemID.WOLFENGLOVES_GREY, // Grey gloves
		ItemID.WOLFENGLOVES_CRIMSON, // Red gloves
		ItemID.WOLFENGLOVES_TANGERINE, // Yellow gloves
		ItemID.WOLFENGLOVES_OCEAN, // Teal gloves
		ItemID.WOLFENGLOVES_PURPLE, // Purple gloves
		ItemID.SKILLCAPE_FIREMAKING, // Firemaking cape
		ItemID.SKILLCAPE_FIREMAKING_TRIMMED, // Firemaking cape (t)
		ItemID.SKILLCAPE_MAX, // Max cape
		ItemID.TZHAAR_CAPE_FIRE, // Fire cape
		ItemID.SKILLCAPE_MAX_FIRECAPE, // Fire max cape
		ItemID.INFERNAL_CAPE, // Infernal cape
		ItemID.SKILLCAPE_MAX_INFERNALCAPE, // Infernal max cape
		ItemID.TZHAAR_CAPE_OBSIDIAN, // Obsidian cape
		ItemID.TZHAAR_CAPE_OBSIDIAN_R, // Obsidian cape (r)
		ItemID.SKILLCAPE_MAX_ANMA, // Accumulator max cape
		ItemID.SKILLCAPE_MAX_ARDY, // Ardougne max cape
		ItemID.SKILLCAPE_MAX_ASSEMBLER, // Assembler max cape
		ItemID.SKILLCAPE_MAX_MYTHICAL, // Mythical max cape
		ItemID.SKILLCAPE_MAX_GUTHIX2, // Imbued guthix max cape
		ItemID.SKILLCAPE_MAX_SARADOMIN2, // Imbued saradomin max cape
		ItemID.SKILLCAPE_MAX_ZAMORAK2, // Imbued zamorak max cape
		ItemID.SKILLCAPE_MAX_GUTHIX, // Guthix max cape
		ItemID.SKILLCAPE_MAX_SARADOMIN, // Saradomin max cape
		ItemID.SKILLCAPE_MAX_ZAMORAK, // Zamorak max cape
		ItemID.WOLF_CLOAK, // Wolf cloak
		ItemID.PRIDE24_CAPE, // Rainbow cape
		ItemID.CLUE_HUNTER_CLOAK, // Clue hunter cloak
		ItemID.STAFF_OF_FIRE, // Staff of fire
		ItemID.FIRE_BATTLESTAFF, // Fire battlestaff
		ItemID.LAVA_BATTLESTAFF, // Lava battlestaff
		ItemID.LAVA_BATTLESTAFF_PRETTY, // Lava battlestaff
		ItemID.STEAM_BATTLESTAFF, // Steam battlestaff
		ItemID.STEAM_BATTLESTAFF_PRETTY, // Steam battlestaff
		ItemID.SMOKE_BATTLESTAFF, // Smoke battlestaff
		ItemID.MYSTIC_FIRE_STAFF, // Mystic fire staff
		ItemID.MYSTIC_LAVA_STAFF, // Mystic lava staff
		ItemID.MYSTIC_LAVA_STAFF_PRETTY, // Mystic lava staff
		ItemID.MYSTIC_STEAM_BATTLESTAFF, // Mystic steam staff
		ItemID.MYSTIC_STEAM_BATTLESTAFF_PRETTY, // Mystic steam staff
		ItemID.MYSTIC_SMOKE_BATTLESTAFF, // Mystic smoke staff
		ItemID.TWINFLAME_STAFF, // Twinflame staff
		ItemID.INFERNAL_AXE, // Infernal axe
		ItemID.INFERNAL_PICKAXE, // Infernal pickaxe
		ItemID.INFERNAL_HARPOON, // Infernal harpoon
		ItemID.ABYSSAL_WHIP_LAVA, // Volcanic abyssal whip
		ItemID.ALE_GODS, // Ale of the gods
		ItemID.WINT_TORCH, // Bruma torch
		ItemID.OSB10_DRAGON_CANDLE, // Dragon candle dagger
		ItemID.TOME_OF_FIRE, // Tome of fire
		ItemID.SLAYER_BUGLAN_ON, // Lit bug lantern
		ItemID.SANTA_JACKET, // Santa jacket
		ItemID.ANTISANTA_JACKET, // Antisanta jacket
		ItemID.EASTER16_ONSIE_TOP, // Bunny top
		ItemID.CLUE_HUNTER_GARB, // Clue hunter garb
		ItemID.HUNTING_CAMOFLAUGE_ROBE_POLAR, // Polar camo top
		ItemID.HUNTING_CAMOFLAUGE_ROBE_WOOD, // Wood camo top
		ItemID.HUNTING_CAMOFLAUGE_ROBE_JUNGLE, // Jungle camo top
		ItemID.HUNTING_CAMOFLAUGE_ROBE_DESERT, // Desert camo top
		ItemID.HUNTING_TORSO_JAGUAR, // Larupia top
		ItemID.HUNTING_TORSO_LEOPARD, // Graahk top
		ItemID.HUNTING_TORSO_TIGER, // Kyatt top
		ItemID.ZEP_BOMBER_JACKET, // Bomber jacket
		ItemID.YAK_HIDE_ARMOUR_BODY, // Yak-hide armour (top)
		ItemID.PYROMANCER_TOP, // Pyromancer garb
		ItemID.EASTER07_CHICKEN_WINGS, // Chicken wings
		ItemID.EVIL_CHICKEN_WINGS, // Evil chicken wings
		ItemID.HW21_UGLY_JUMPER01, // Ugly halloween jumper
		ItemID.HW21_UGLY_JUMPER02, // Ugly halloween jumper
		ItemID.XMAS22_JUMPER, // Christmas jumper
		ItemID.OSB10_JUMPER_RED, // Oldschool jumper
		ItemID.OSB10_JUMPER_YELLOW, // Oldschool jumper
		ItemID.OSB10_JUMPER_BLUE, // Oldschool jumper
		ItemID.OSB10_JUMPER_PURPLE, // Oldschool jumper
		ItemID.OSB10_JUMPER_GREEN, // Oldschool jumper
		ItemID.OSB10_JUMPER_WHITE, // Oldschool jumper
		ItemID.PRIDE23_JUMPER, // Rainbow jumper
		ItemID.XMAS23_JUMPER, // Icy jumper
		ItemID.SANTA_PANTS, // Santa pantaloons
		ItemID.ANTISANTA_PANTS, // Antisanta pantaloons
		ItemID.EASTER16_ONSIE_BOTTOM, // Bunny legs
		ItemID.CLUE_HUNTER_TROUSERS, // Clue hunter trousers
		ItemID.HUNTING_TROUSERS_POLAR, // Polar camo legs
		ItemID.HUNTING_TROUSERS_WOOD, // Wood camo legs
		ItemID.HUNTING_TROUSERS_JUNGLE, // Jungle camo legs
		ItemID.HUNTING_TROUSERS_DESERT, // Desert camo legs
		ItemID.HUNTING_TROUSERS_JAGUAR, // Larupia legs
		ItemID.HUNTING_TROUSERS_LEOPARD, // Graahk legs
		ItemID.HUNTING_TROUSERS_TIGER, // Kyatt legs
		ItemID.YAK_HIDE_ARMOUR_GREAVES, // Yak-hide armour (legs)
		ItemID.EASTER07_CHICKEN_LEGS, // Chicken legs
		ItemID.EVIL_CHICKEN_LEGS, // Evil chicken legs
		ItemID.PYROMANCER_BOTTOM, // Pyromancer robe
		ItemID.RING_OF_ELEMENTS, // Ring of the elements
		ItemID.RING_OF_ELEMENTS_CHARGED, // Ring of the elements
		ItemID.SANTA_BOOTS, // Santa boots
		ItemID.ANTISANTA_BOOTS, // Antisanta boots
		ItemID.BUNNYFEET, // Bunny feet
		ItemID.CLUE_HUNTER_BOOTS, // Clue hunter boots
		ItemID.PYROMANCER_BOOTS, // Pyromancer boots
		ItemID.EASTER07_CHICKEN_FEET, // Chicken feet
		ItemID.EVIL_CHICKEN_FEET, // Evil chicken feet
		ItemID.XM21_FESTIVE_SLIPPERS, // Festive elf slippers
		ItemID.MOLE_SLIPPERS, // Mole slippers
		ItemID.HOLY_MOLEYS, // Holy moleys
		ItemID.BEAR_SLIPPERS, // Bear feet
		ItemID.DEMON_SLIPPERS, // Demon feet
		ItemID.FROG_SLIPPERS, // Frog slippers
		ItemID.OSB10_BOB_SLIPPERS, // Bob the cat slippers
		ItemID.OSB10_JAD_SLIPPERS, // Jad slippers
		ItemID.COW_SLIPPERS, // Cow slippers
		ItemID.COW_SLIPPERS_RECOL_1, // Cow slippers
		ItemID.COW_SLIPPERS_RECOL_2, // Cow slippers
		ItemID.COW_SLIPPERS_RECOL_3, // Cow slippers
		ItemID.COW_SLIPPERS_RECOL_4, // Brutus slippers
	});

	static boolean isWarm(int canonicalItemId)
	{
		return ITEM_IDS.contains(canonicalItemId);
	}

	private WarmClothing()
	{
	}
}
