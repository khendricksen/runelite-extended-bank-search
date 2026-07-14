package com.github.khendricksen.extendedbanksearch;

/**
 * Comparison operators usable in a stat query, e.g. the {@code >} in {@code prayer>6}.
 *
 * <p>The two-character operators must be listed/matched before the single-character ones so that
 * {@code >=} is never mis-read as {@code >} followed by {@code =}. {@link #PATTERN} encodes that
 * ordering for the parser's tokeniser.
 */
enum Operator
{
	GE(">="),
	LE("<="),
	GT(">"),
	LT("<"),
	EQ("=");

	/** Alternation of every operator token, longest first — used to split operators out of the query. */
	static final String PATTERN = ">=|<=|>|<|=";

	private final String token;

	Operator(String token)
	{
		this.token = token;
	}

	/**
	 * Resolves a token to its operator, or {@code null} if it isn't one.
	 */
	static Operator fromToken(String token)
	{
		for (Operator op : values())
		{
			if (op.token.equals(token))
			{
				return op;
			}
		}
		return null;
	}

	boolean test(int actual, int threshold)
	{
		switch (this)
		{
			case GT:
				return actual > threshold;
			case LT:
				return actual < threshold;
			case GE:
				return actual >= threshold;
			case LE:
				return actual <= threshold;
			case EQ:
				return actual == threshold;
			default:
				return false;
		}
	}
}
