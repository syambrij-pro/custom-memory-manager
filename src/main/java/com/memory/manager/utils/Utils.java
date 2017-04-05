package com.memory.manager.utils;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.memory.manager.prototype.CustomMemoryManagerException;

/**
 * Utility class for projects more generic and repeated tasks.
 * @author syambrij
 *
 */
public class Utils {
	
	public static SparkConf conf = new SparkConf().setAppName("custom-memory-manager").setMaster("local").set("spark.executor.memory","1g").set("spark.cores.max", "10");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	
	public static void validFolderAssertion(String folderPath) {
		assert folderPath != null;
		Path path = Paths.get(folderPath);
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS) ||
			!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)	||
			!Files.isReadable(path)) {
			throw new CustomMemoryManagerException("This is not a valid folder path. Make sure path exists and accessible via JVM.");
		}
	}	
}
