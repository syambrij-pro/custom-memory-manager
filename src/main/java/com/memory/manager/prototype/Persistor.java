package com.memory.manager.prototype;

import java.io.Serializable;
import java.util.List;

import com.memory.manager.impl.LocalFilePersistor;

/**
 * Base prototype for persisting object from disk/local files.
 * Includes single object name, list of names method for retrieving, eviction.
 * 
 * @see LocalFilePersistor
 * @see LocalFileSparkPersistor
 * @author syambrij
 *
 */
public interface Persistor {
	
	void persist(Wrapper wrapped);
	<T extends Serializable> void persist(String name, T serializableObject);
	void persistObjects(List<String> names, List<? extends Serializable> serializableObjects);
	void persistAllWrapper(List<Wrapper> wrappers);
}
