package squeek.tictooltips.helpers;

public class MathHelper
{
	public static boolean equals(final float a, final float b)
	{
		return equals(a, b, Float.MIN_NORMAL, 1);
	}

	/**
	 * Based on http://randomascii.wordpress.com/2012/02/25/comparing-floating-point-numbers-2012-edition/
	 */
	public static boolean equals(final float a, final float b, final float maxDiff, final int maxUlpsDiff)
	{
		final float absDiff = Math.abs(a - b);
		if (absDiff <= maxDiff)
			return true;

		final int aSign = (int) Math.signum(a);
		final int bSign = (int) Math.signum(b);
		if (aSign != bSign)
			return false;

		final int aInt = Float.floatToRawIntBits(a);
		final int bInt = Float.floatToRawIntBits(b);
		int ulpsDiff = Math.abs(aInt - bInt);
		return ulpsDiff <= maxUlpsDiff;
	}
}
