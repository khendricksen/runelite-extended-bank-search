package com.github.khendricksen.extendedbanksearch;

import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemEquipmentStats;

/**
 * Matches items that occupy a given equipment slot, e.g. {@code helm} -&gt; everything in the head
 * slot. Compares the item's slot index against the enum's {@link EquipmentInventorySlot#getSlotIdx()}
 * (never a hardcoded number, since slot indices are not contiguous).
 */
final class SlotCondition implements ItemCondition
{
	private final EquipmentInventorySlot slot;

	SlotCondition(EquipmentInventorySlot slot)
	{
		this.slot = slot;
	}

	@Override
	public boolean test(ItemEquipmentStats eq)
	{
		return eq.getSlot() == slot.getSlotIdx();
	}
}
