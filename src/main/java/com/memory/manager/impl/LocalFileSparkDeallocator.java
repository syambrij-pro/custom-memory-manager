package com.memory.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import com.memory.manager.prototype.CustomMemoryManagerException;
import com.memory.manager.prototype.Deallocator;
import com.memory.manager.utils.Utils;

/**
 * Java retrieves/evict java objects using spark/java8 using name. The class evict data into text file at specified location.
 * This class will use java8 stream API if you are considering less than 1000 objects.
 * For more than 1000 object names it uses apache spark.  
 * Folder path should be of HDFS if we are not running on standalone node.
 * 
 * @see Deallocator
 * 
 * @author syambrij
 *
 */
public class LocalFileSparkDeallocator implements Deallocator, Serializable {
	
	/**
	 * To get the most of implementing serializable.
	 */
	private static final long serialVersionUID = -137280920713012161L;
	private String folderPath;
	private static final String FILE_SUFFIX = ".txt";
	private JavaSparkContext sc = Utils.sc;

	public LocalFileSparkDeallocator(String folderPath) {
		Utils.validFolderAssertion(folderPath);
		this.folderPath = getFolderPath(folderPath);
	}

	@Override
	public void evict(String name) {
		try {
			Files.deleteIfExists(Paths.get(folderPath + name + FILE_SUFFIX));
		} catch (IOException e) {
			throw new CustomMemoryManagerException("Object could not be deleted from local files because of some I/O error.");
		}
	}

	@Override
	public void evictAll(List<String> names) {
		if (names.size() < 5_000) {
			callLocalFileEvictor(names);
		} else {
			callSparkEvictor(names);
		}
	}

	@Override
	public Object get(String name) {
		if (!Files.exists(Paths.get(folderPath + name + FILE_SUFFIX), LinkOption.NOFOLLOW_LINKS)) {
			System.out.println("Object with name could not be found.");
			return null;
		}
		return getObjectWithName(name);
	}

	private Object getObjectWithName(String name) {
		Object returnObject = null;
		try {
			FileInputStream fis = new FileInputStream(folderPath + name + FILE_SUFFIX);
			ObjectInputStream oi = new ObjectInputStream(fis);
			returnObject = oi.readObject();
			oi.close();
		} catch (IOException e) {
			System.out.println("I/O error occured while getting object from local file with name = " +name);
			return null;
		} catch (ClassNotFoundException cle) {
			System.out.println("Classloader throws error while getting object from local file.");
			return null;
		}
		return returnObject;
	}

	@Override
	public List<Object> getAll(List<String> names) {
		if (names.size() < 1_000) {
			return getAllUsingJava8(names);
		} else {
			return getAllUsingSpark(names);
		}
	}

	private List<Object> getAllUsingSpark(List<String> names) {
		JavaRDD<String> stringRDD = sc.parallelize(names);
		return stringRDD.map(new RetrieverFunction<String, Object>(folderPath)).collect();
	}

	private List<Object> getAllUsingJava8(List<String> names) {
		return names.parallelStream().map(new java.util.function.Function<String, Object> () {
			@Override
			public Object apply(String name) {
				return get(name);
			}
		}).collect(Collectors.toList());
	}

	private String getFolderPath(String folderPath2) {
		return folderPath2.endsWith(File.separator) ? folderPath2 : folderPath2 + File.separator;
	}

	private void callLocalFileEvictor(List<String> names) {
		names.parallelStream().map(new java.util.function.Function<String, Boolean> () {
			@Override
			public Boolean apply(String t) {
				evict(t);
				return Boolean.TRUE;
			}
		}).count();
	}

	private void callSparkEvictor(List<String> names) {
		JavaRDD<String> stringRDD = sc.parallelize(names);
		stringRDD.map(new EvictorFunction<String, Boolean>(folderPath)).count();
	}
}
