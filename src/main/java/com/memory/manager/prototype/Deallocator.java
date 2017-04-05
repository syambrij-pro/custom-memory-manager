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
	
	void evict(String name);
	void evictAll(List<String> names);
	
	Object get(String name);
	List<Object> getAll(List<String> names);

}
