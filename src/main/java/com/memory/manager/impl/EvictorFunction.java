package com.memory.manager.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.spark.api.java.function.Function;

import com.memory.manager.prototype.CustomMemoryManagerException;

/**
 * This spark function evicts objects from disk/local file..
 * Used in LocalFileSparkDeallocator class to call eviction for 
 * given object list(or single object name).
 * @author syambrij
 *
 * @param <T>
 * @param <R>
 */
public class EvictorFunction<T, R> implements Function<String, Boolean>{

	/**
	 * To get the most of implementing serializable.
	 */
	private static final long serialVersionUID = -8494831381156157595L;
	private String folderPath;
	private final String FILE_SUFFIX = ".txt";
	
	public EvictorFunction(String folderPath) {
		this.folderPath = folderPath;
	}
	
	@Override
	public Boolean call(String v1) throws Exception {
		try {
			Files.deleteIfExists(Paths.get("" + folderPath + v1 + FILE_SUFFIX));
			return Boolean.TRUE;
		} catch (IOException e) {
			throw new CustomMemoryManagerException("Object could not be deleted from local files because of some I/O error.");
		}
	}
}
