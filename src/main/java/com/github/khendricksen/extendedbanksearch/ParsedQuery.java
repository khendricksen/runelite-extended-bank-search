package com.github.khendricksen.extendedbanksearch;

import java.util.List;

/**
 * The recognised parts of a bank-search query.
 *
 * <p>Equipment conditions (slot / stat / range) are tested against an item's {@link
 * net.runelite.client.game.ItemEquipmentStats}. The {@code warm} and {@code food} flags are separate
 * item-property tests that bypass the equipment-stats checks (edible items aren't equipable and
 * warm items don't always have stats). The caller requires all of them to match (AND).
 */
final class ParsedQuery
{
	private final List<ItemCondition> equipmentConditions;
	private final boolean warm;
	private final boolean food;

	ParsedQuery(List<ItemCondition> equipmentConditions, boolean warm, boolean food)
	{
		this.equipmentConditions = equipmentConditions;
		this.warm = warm;
		this.food = food;
	}

	List<ItemCondition> equipmentConditions()
	{
		return equipmentConditions;
	}

	boolean requiresWarm()
	{
		return warm;
	}

	boolean requiresFood()
	{
		return food;
	}

	/** True when nothing was recognised, so the caller should let the native name search handle it. */
	boolean isEmpty()
	{
		return equipmentConditions.isEmpty() && !warm && !food;
	}
}
