package squeek.tictooltips.helpers;

import java.util.TreeMap;

// adapted from http://stackoverflow.com/a/22039673 and http://stackoverflow.com/a/19759564
public class RomanNumeralHelper
{

	private static final TreeMap<Character, Integer> RomanNumberDictionary = new TreeMap<Character, Integer>();
	static
	{
		RomanNumberDictionary.put('I', 1);
		RomanNumberDictionary.put('V', 5);
		RomanNumberDictionary.put('X', 10);
		RomanNumberDictionary.put('L', 50);
		RomanNumberDictionary.put('C', 100);
		RomanNumberDictionary.put('D', 500);
		RomanNumberDictionary.put('M', 1000);
	}
	private static final TreeMap<Integer, String> NumberRomanDictionary = new TreeMap<Integer, String>();
	static
	{
		NumberRomanDictionary.put(1000, "M");
		NumberRomanDictionary.put(900, "CM");
		NumberRomanDictionary.put(500, "D");
		NumberRomanDictionary.put(400, "CD");
		NumberRomanDictionary.put(100, "C");
		NumberRomanDictionary.put(50, "L");
		NumberRomanDictionary.put(40, "XL");
		NumberRomanDictionary.put(10, "X");
		NumberRomanDictionary.put(9, "IX");
		NumberRomanDictionary.put(5, "V");
		NumberRomanDictionary.put(4, "IV");
		NumberRomanDictionary.put(1, "I");
	}

	public static String toRoman(int number)
	{
		int key = NumberRomanDictionary.floorKey(number);

		if (number == key)
			return NumberRomanDictionary.get(key);

		return NumberRomanDictionary.get(key) + toRoman(number - key);
	}

	public static int fromRoman(String roman)
	{
		int total = 0;
		int current, previous = 0;
		char currentRoman, previousRoman = '\0';

		for (int i = 0; i < roman.length(); i++)
		{
			currentRoman = roman.charAt(i);
			previous = previousRoman != '\0' ? RomanNumberDictionary.get(previousRoman) : '\0';
			current = RomanNumberDictionary.get(currentRoman);

			if (previous != 0 && current > previous)
				total = total - (2 * previous) + current;
			else
				total += current;

			previousRoman = currentRoman;
		}

		return total;
	}

}
