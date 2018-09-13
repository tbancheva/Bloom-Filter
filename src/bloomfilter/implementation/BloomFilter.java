package bloomfilter.implementation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter<E> {
	private static final int SEED = 1;
	private BitSet bitArr;
	private int bitArrSize;
	private int numHashFunctions;

	public BloomFilter(double falsePositiveProbability, int expectedInsertions) {
		this.bitArrSize = BloomFilterFormulas.optimalBitArrSize(falsePositiveProbability, expectedInsertions);
		this.bitArr = new BitSet(bitArrSize);
		this.numHashFunctions = BloomFilterFormulas.optimalNumOfHashFunctions(bitArrSize, expectedInsertions);

		bitArr.clear();
	}

	public boolean insert(E element) {
		boolean bitsChanged = false;
		List<Integer> hashes = getHashes(element);

		int combinedHash = hashes.get(0);

		for (int i = 0; i < numHashFunctions; i++) {
			int index = combinedHash % bitArrSize;
			bitArr.set(index);
			bitsChanged |= bitArr.get(index);
			combinedHash += hashes.get(1);
		}

		return bitsChanged;
	}

	public boolean mightContain(E element) {
		List<Integer> hashes = getHashes(element);

		int combinedHash = hashes.get(0);

		for (int i = 0; i < numHashFunctions; i++) {
			int index = combinedHash % bitArrSize;
			if (!bitArr.get(index)) {
				return false;
			}
			combinedHash += hashes.get(1);
		}

		return true;
	}

	public List<Integer> getHashes(E element) {
		byte[] key = element.toString().getBytes();
		int hashing = MurmurHash.hash(SEED, key, key.length);
		byte[] bytes = intToByteArray(hashing);

		int hash1 = 0, hash2 = 0;
		for (int i = 0; i < 2; i++) {
			hash1 += ((int) bytes[i] & 0xffL) << (4 * i);
		}

		for (int i = 2; i < 4; i++) {
			hash2 += ((int) bytes[i] & 0xffL) << (4 * i);
		}

		List<Integer> hashes = new ArrayList<>();
		hashes.add(hash1);
		hashes.add(hash2);

		return hashes;
	}

	public int getNumHashFunctions() {
		return this.numHashFunctions;
	}

	public int getBitArrSize() {
		return this.bitArrSize;
	}

	public BitSet getBitSet() {
		return this.bitArr;
	}

	public static byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

}
