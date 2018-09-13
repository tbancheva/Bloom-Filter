import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import bloomfilter.implementation.BloomFilter;
import bloomfilter.implementation.BloomFilterFormulas;

public class BloomFilterTest {

	private static BloomFilter<String> bloomFilter;
	private static int insertedElements = 0;

	@BeforeClass
	public static void setUpBeforeClass() {
		double falsePositiveProbability = 0.1;
		int expectedInsertions = 100;

		bloomFilter = new BloomFilter<>(falsePositiveProbability, expectedInsertions);

		String element = "abcd";
		String anotherElement = "abce";

		bloomFilter.insert(element);
		bloomFilter.insert(anotherElement);
		insertedElements += 2;
	}
	
	/*
	 * If all elements are inserted successfully then the number of set bits in the bit
	 * array should be equal to the number of all inserted elements multiplied by
	 * the number of hash functions since for each element a single bit per hash
	 * function is set.
	 */
	@Test
	public void insertTest() {
		int setBits = bloomFilter.getBitSet().cardinality();
		int numberOfHashFunctions = bloomFilter.getNumHashFunctions();

		assertEquals(setBits, numberOfHashFunctions * insertedElements);
	}

	@Test
	public void mightContainTest() {
		String missingElement = "abcf";

		assertTrue(bloomFilter.mightContain("abcd"));
		assertTrue(bloomFilter.mightContain("abce"));
		assertTrue(!bloomFilter.mightContain(missingElement));
	}


	/*
	 * Site for calculating expected results: https://hur.st/bloomfilter/
	 */

	@Test
	public void optimalBitArrSizeTest() {
		assertEquals(480, BloomFilterFormulas.optimalBitArrSize(0.1, 100));
		assertEquals(480, BloomFilterFormulas.optimalBitArrSize(0.01, 50));
	}

	@Test
	public void optimalNumberOfHashFunctionsTest() {
		assertEquals(3, BloomFilterFormulas.optimalNumOfHashFunctions(480, 100));
		assertEquals(7, BloomFilterFormulas.optimalNumOfHashFunctions(480, 50));
	}

}
