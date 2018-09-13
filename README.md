# Description

A **Bloom filter** is a data structure designed to tell you, *rapidly* and *memory-efficiently*, whether an element is present in a set.

The price paid for this efficiency is that a Bloom filter is a **probabilistic** data structure: it tells us that the element either *definitely is not* in the set or *may be* in the set.

# Implementation
The base data structure of a Bloom filter is a Bit Array. At first all bits are set to 0. 
* To **add** an element to the Bloom filter, we hash it a few times and set the bits in the bit array at the index of those hashes to 1.
* To **test for membership**, you hash an element with the same hash functions, then see if those values are set in the bit array. If they aren't, you know that the element isn't in the set.
If they are, you only know that it might be, because another element or some combination of other elements could have set the same bits.

## Hash functions

The hash functions used should be *independent* and *uniformly distributed*. They should also be as fast as possible (that's why cryptographic hashes are not very efficient).
In this implementation the used hash function is Murmur. It passes the *chi-square test*(it's uniformly distributed) and the *avalanche test*(when changing a single bit in the key more than 50% of the hash result changes as well).
The result of the murmur hash function is used to produce two hash values for every element. The second one gets added to the first one according to the number of needed hash functions, so that the implementation uses one hash to generate however many needed, 
which as is said [here] (https://www.youtube.com/watch?v=vx1JRs4M3HI&t=781s) is very efficient. 

## Examples

To create an empty Bloom filter, just call the constructor with the required false positive probability and the number of elements you expect to add to the Bloom filter.

```
    double falsePositiveProbability = 0.1;
    int expectedNumberOfElements = 100;

    BloomFilter<String> bloomFilter = new BloomFilter<String>(falsePositiveProbability, expectedNumberOfElements);

```

The constructor chooses the length of the bit array and the number of hash functions which will provide the given false positive probability
according to the given [formulas] (https://hur.st/bloomfilter/). Note that if you insert more elements than the number of expected elements you specify, the actual false positive probability will rapidly increase.

Elements are added via the insert method and searched for with the mightContain method:

``` 
    String element = "abcd";
	bloomFilter.insert(element);
	
	bloomFilter.mightContain(element); //true
	bloomFilter.mightContain("abcf");  //false
	

```

