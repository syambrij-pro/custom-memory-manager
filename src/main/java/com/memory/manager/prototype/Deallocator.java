package com.memory.manager.prototype;

import java.util.List;

/**
 * Base prototype for eviction/retrieves of object from disk/local files.
 * Includes single object name, list of names method for retrieving, eviction.
 * @see LocalFileSparkDeallocator
 * @author syambrij
 *
 */
public interface Deallocator {

	//Two method for evicting object/objects from disk.
	void evict(String name);
	void evictAll(List<String> names);
	
	/**
	 * Two method for retrieving object/objects from files/disk.
	 * These methods will not remove object/objects from local files/HDFS.
	 *
	 */
	Object get(String name);
	List<Object> getAll(List<String> names);

}
