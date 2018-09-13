/* *
 * Cheat sheet:
 * m: total bits, m = ceil((n * log(p)) / log(1 / pow(2, log(2))))
 * n: expected insertions, n = ceil(m / (-k / log(1 - exp(log(p) / k))))
 * p: expected false positive probability,  p = pow(1 - exp(-k / (m / n)), k)
 * k: number of hashing functions,  k = round((m / n) * log(2))
 */

package bloomfilter.implementation;

public class BloomFilterFormulas {
	
	/**
	 * The size of the filter(m) is calculated based on the desired false positive
	 * probability(p) and the number of expected elements(n).
	 */
	public static int optimalBitArrSize(double fpp, int numberOfElements) {
		return (int) Math.ceil((numberOfElements * Math.log(fpp)) / Math.log(1 / Math.pow(2, Math.log(2))));
	}
	

	/**
	 * The optimal number of hash functions(k) is calculated based on the number of
	 * expected elements(n) and the size of the bit array(m).
	 */
	public static int optimalNumOfHashFunctions(int bitArrSize, int numberOfElements) {
		return Math.max(1, (int) Math.ceil((bitArrSize / numberOfElements) * Math.log(2)));
	}
	
}
