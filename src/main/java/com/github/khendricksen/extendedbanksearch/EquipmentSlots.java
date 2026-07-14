package com.github.khendricksen.extendedbanksearch;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.runelite.api.EquipmentInventorySlot;

/**
 * Maps user-typed slot words to real OSRS equipment slots.
 *
 * <p>Only the 11 true gear slots are mapped. {@link EquipmentInventorySlot} also defines
 * player-appearance "kit" slots (ARMS, HAIR, JAW) which are not equippable gear and are never mapped
 * here.
 *
 * <p>Kept to true slot synonyms only. Item subtype words (e.g. "platebody", "ward", "mask") are
 * narrower than a slot, so they are deliberately absent here and fall through to the native
 * item-name search.
 */
final class EquipmentSlots
{
	private static final Map<String, EquipmentInventorySlot> ALIASES = build();

	private static Map<String, EquipmentInventorySlot> build()
	{
		Map<String, EquipmentInventorySlot> m = new HashMap<>();
		put(m, EquipmentInventorySlot.HEAD, "head", "helm", "helmet", "hat", "hood");
		put(m, EquipmentInventorySlot.CAPE, "cape", "cloak", "back");
		put(m, EquipmentInventorySlot.AMULET, "amulet", "ammy", "neck", "necklace");
		put(m, EquipmentInventorySlot.AMMO, "ammo", "ammunition", "arrow", "arrows", "bolt", "bolts", "quiver");
		put(m, EquipmentInventorySlot.WEAPON, "weapon", "wep", "weap", "mainhand", "main-hand", "main", "2h");
		put(m, EquipmentInventorySlot.BODY, "body", "chest", "top", "torso");
		put(m, EquipmentInventorySlot.SHIELD, "shield", "offhand", "off-hand", "off");
		put(m, EquipmentInventorySlot.LEGS, "legs", "leg", "bottom", "bottoms", "trousers");
		put(m, EquipmentInventorySlot.GLOVES, "hands", "hand", "gloves", "glove", "gauntlets", "vambraces");
		put(m, EquipmentInventorySlot.BOOTS, "feet", "foot", "boots", "boot", "shoes");
		put(m, EquipmentInventorySlot.RING, "ring");
		return Collections.unmodifiableMap(m);
	}

	private static void put(Map<String, EquipmentInventorySlot> m, EquipmentInventorySlot slot, String... aliases)
	{
		for (String alias : aliases)
		{
			m.put(alias, slot);
		}
	}

	/**
	 * Resolves a single lowercase word to its equipment slot, or {@code null} if it isn't a slot word.
	 */
	static EquipmentInventorySlot resolve(String word)
	{
		return ALIASES.get(word);
	}

	private EquipmentSlots()
	{
	}
}
