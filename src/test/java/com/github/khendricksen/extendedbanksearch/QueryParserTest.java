package com.github.khendricksen.extendedbanksearch;

import java.util.List;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.gameval.ItemID;
import net.runelite.client.game.ItemEquipmentStats;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class QueryParserTest
{
	private static boolean matches(String query, ItemEquipmentStats eq)
	{
		List<ItemCondition> conditions = QueryParser.parse(query).equipmentConditions();
		assertFalse("expected query to be recognised: " + query, conditions.isEmpty());
		for (ItemCondition c : conditions)
		{
			if (!c.test(eq))
			{
				return false;
			}
		}
		return true;
	}

	private static ItemEquipmentStats.ItemEquipmentStatsBuilder gear(EquipmentInventorySlot slot)
	{
		return ItemEquipmentStats.builder().slot(slot.getSlotIdx());
	}

	@Test
	public void unrecognisedQueryFallsThrough()
	{
		assertTrue(QueryParser.parse("dragon").isEmpty());
		assertTrue(QueryParser.parse("abyssal whip").isEmpty());
		assertTrue(QueryParser.parse("").isEmpty());
		assertTrue(QueryParser.parse(null).isEmpty());
	}

	@Test
	public void slotMatches()
	{
		ItemEquipmentStats helm = gear(EquipmentInventorySlot.HEAD).build();
		ItemEquipmentStats legs = gear(EquipmentInventorySlot.LEGS).build();
		assertTrue(matches("helm", helm));
		assertTrue(matches("hat", helm));   // alias
		assertFalse(matches("helm", legs));
		assertTrue(matches("legs", legs));
	}

	@Test
	public void statThresholdRespectsOperator()
	{
		ItemEquipmentStats prayer6 = gear(EquipmentInventorySlot.HEAD).prayer(6).build();
		ItemEquipmentStats prayer8 = gear(EquipmentInventorySlot.HEAD).prayer(8).build();
		assertFalse(matches("prayer>6", prayer6));  // strict >
		assertTrue(matches("prayer>6", prayer8));
		assertTrue(matches("prayer>=6", prayer6));
		assertTrue(matches("prayer=6", prayer6));
		assertFalse(matches("prayer<6", prayer6));
		assertTrue(matches("prayer<=6", prayer6));
	}

	@Test
	public void operatorSpacingIsIrrelevant()
	{
		ItemEquipmentStats prayer8 = gear(EquipmentInventorySlot.HEAD).prayer(8).build();
		assertTrue(matches("prayer > 6", prayer8));
		assertTrue(matches("prayer>6", prayer8));
		assertTrue(matches("  prayer   >=   6 ", prayer8));
	}

	@Test
	public void bareSlashCrushStabDefaultToAttack()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON).aslash(40).dslash(5).build();
		assertTrue(matches("slash>30", item));   // attack-slash 40 > 30
		assertFalse(matches("slash>50", item));
	}

	@Test
	public void multiWordKeysWinViaLongestMatch()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON)
			.amagic(25).dmagic(5).mdmg(15f).build();
		assertTrue(matches("magic attack>20", item));    // amagic 25
		assertFalse(matches("magic def>20", item));       // dmagic 5
		assertFalse(matches("magic damage>20", item));    // mdmg 15
		assertTrue(matches("magic damage>=15", item));
	}

	@Test
	public void bareMagicAndRangeDefaultToAttack()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON)
			.amagic(25).arange(30).dmagic(0).drange(0).build();
		assertTrue(matches("magic>20", item));   // defaults to attack-magic
		assertTrue(matches("range>20", item));   // defaults to attack-ranged
		assertTrue(matches("ranged>20", item));
	}

	@Test
	public void mageIsSynonymOfMagic()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON)
			.amagic(25).dmagic(40).mdmg(15f).build();
		assertTrue(matches("mage>20", item));          // bare mage -> attack-magic
		assertTrue(matches("mage attack>20", item));
		assertTrue(matches("mage def>30", item));       // defence-magic
		assertFalse(matches("mage attack>30", item));   // attack-magic is 25, not 40
		assertTrue(matches("mage damage>=15", item));
	}

	@Test
	public void rangedStrengthIsNotAttack()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON).rstr(80).arange(0).build();
		assertTrue(matches("ranged strength>50", item));
		assertTrue(matches("rstr>50", item));
	}

	@Test
	public void negativeThreshold()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.BODY).amagic(-10).build();
		assertTrue(matches("amagic>-30", item));   // -10 > -30
		assertFalse(matches("amagic>-5", item));    // -10 > -5 is false
	}

	@Test
	public void inclusiveRange()
	{
		ItemEquipmentStats prayer2 = gear(EquipmentInventorySlot.HEAD).prayer(2).build();
		ItemEquipmentStats prayer4 = gear(EquipmentInventorySlot.HEAD).prayer(4).build();
		ItemEquipmentStats prayer6 = gear(EquipmentInventorySlot.HEAD).prayer(6).build();
		ItemEquipmentStats prayer7 = gear(EquipmentInventorySlot.HEAD).prayer(7).build();
		assertTrue(matches("prayer 2-6", prayer2));    // low bound inclusive
		assertTrue(matches("prayer 2-6", prayer4));
		assertTrue(matches("prayer 2-6", prayer6));    // high bound inclusive
		assertFalse(matches("prayer 2-6", prayer7));
	}

	@Test
	public void rangeBoundsMayBeReversed()
	{
		ItemEquipmentStats prayer4 = gear(EquipmentInventorySlot.HEAD).prayer(4).build();
		assertTrue(matches("prayer 6-2", prayer4));
	}

	@Test
	public void rangeComposesWithSlotAndStat()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.LEGS).dstab(55).build();
		assertTrue(matches("legs dstab 40-60", item));
		assertFalse(matches("legs dstab 40-50", item));
	}

	@Test
	public void bareStatMeansGreaterThanZero()
	{
		ItemEquipmentStats prayer1 = gear(EquipmentInventorySlot.HEAD).prayer(1).build();
		ItemEquipmentStats prayer0 = gear(EquipmentInventorySlot.HEAD).prayer(0).build();
		assertTrue(matches("prayer", prayer1));
		assertFalse(matches("prayer", prayer0));

		ItemEquipmentStats str5 = gear(EquipmentInventorySlot.WEAPON).str(5).build();
		ItemEquipmentStats str0 = gear(EquipmentInventorySlot.WEAPON).str(0).build();
		assertTrue(matches("melee str", str5));   // "melee" ignored, bare "str" -> str > 0
		assertTrue(matches("strength", str5));
		assertFalse(matches("strength", str0));
	}

	@Test
	public void bareMultiWordStatMeansGreaterThanZero()
	{
		ItemEquipmentStats rstr = gear(EquipmentInventorySlot.BODY).rstr(20).build();
		assertTrue(matches("ranged strength", rstr));   // longest-match key, bare -> rstr > 0
		assertEquals(1, QueryParser.parse("ranged strength").equipmentConditions().size());
	}

	@Test
	public void bareStatComposesWithSlot()
	{
		ItemEquipmentStats helmPrayer = gear(EquipmentInventorySlot.HEAD).prayer(3).build();
		ItemEquipmentStats helmNoPrayer = gear(EquipmentInventorySlot.HEAD).prayer(0).build();
		assertEquals(2, QueryParser.parse("helm prayer").equipmentConditions().size());
		assertTrue(matches("helm prayer", helmPrayer));
		assertFalse(matches("helm prayer", helmNoPrayer));
	}

	@Test
	public void compoundQueryIsAnded()
	{
		ItemEquipmentStats helmPrayer3 = gear(EquipmentInventorySlot.HEAD).prayer(3).build();
		ItemEquipmentStats helmPrayer1 = gear(EquipmentInventorySlot.HEAD).prayer(1).build();
		ItemEquipmentStats legsPrayer3 = gear(EquipmentInventorySlot.LEGS).prayer(3).build();
		assertEquals(2, QueryParser.parse("helm prayer>2").equipmentConditions().size());
		assertTrue(matches("helm prayer>2", helmPrayer3));
		assertFalse(matches("helm prayer>2", helmPrayer1));   // prayer fails
		assertFalse(matches("helm prayer>2", legsPrayer3));   // slot fails
	}

	@Test
	public void attackAndDefenceWordOrdersBothResolve()
	{
		ItemEquipmentStats item = gear(EquipmentInventorySlot.WEAPON).astab(10).dstab(50).build();
		// defence-stab, all four spellings/orders
		assertTrue(matches("defence stab>40", item));
		assertTrue(matches("def stab>40", item));
		assertTrue(matches("stab defence>40", item));
		assertTrue(matches("stab def>40", item));
		// attack-stab, both orders
		assertTrue(matches("attack stab>5", item));
		assertTrue(matches("stab attack>5", item));
		// orders stay distinct: "stab defence" is defence (50), not attack (10)
		assertFalse(matches("stab defence>40", gear(EquipmentInventorySlot.WEAPON).astab(45).dstab(10).build()));
	}

	@Test
	public void foodIsRecognisedAndSeparate()
	{
		ParsedQuery food = QueryParser.parse("food");
		assertFalse(food.isEmpty());
		assertTrue(food.requiresFood());
		assertTrue(food.equipmentConditions().isEmpty());

		assertTrue(QueryParser.parse("edible").requiresFood());
		assertTrue(QueryParser.parse("eat").requiresFood());
		assertFalse(QueryParser.parse("dragon").requiresFood());
	}

	@Test
	public void warmAndFoodAreIndependentFlags()
	{
		ParsedQuery q = QueryParser.parse("warm food");
		assertTrue(q.requiresWarm());
		assertTrue(q.requiresFood());
	}

	@Test
	public void warmIsRecognisedAndSeparateFromEquipmentConditions()
	{
		ParsedQuery warm = QueryParser.parse("warm");
		assertFalse(warm.isEmpty());
		assertTrue(warm.requiresWarm());
		assertTrue(warm.equipmentConditions().isEmpty());

		assertTrue(QueryParser.parse("warmth").requiresWarm());
		assertTrue(QueryParser.parse("wintertodt").requiresWarm());
		assertFalse(QueryParser.parse("dragon").requiresWarm());
	}

	@Test
	public void warmComposesWithSlot()
	{
		ParsedQuery q = QueryParser.parse("warm helm");
		assertTrue(q.requiresWarm());
		assertEquals(1, q.equipmentConditions().size());
	}

	@Test
	public void warmClothingMembership()
	{
		assertTrue(WarmClothing.isWarm(ItemID.PYROMANCER_HOOD));
		assertTrue(WarmClothing.isWarm(ItemID.TOME_OF_FIRE));
		assertFalse(WarmClothing.isWarm(ItemID.BRONZE_DAGGER));
	}
}
