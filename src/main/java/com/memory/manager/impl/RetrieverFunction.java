package com.memory.manager.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.spark.api.java.function.Function;

/**
 * This spark function retrieves objects from disk/local file.
 * Used in LocalFileSparkDeallocator class to call eviction for 
 * given object list(or single object name).
 * 
 * @author syambrij
 *
 * @param <T>
 * @param <R>
 */
public class RetrieverFunction<T, R> implements Function<String, Object>{

	/**
	 * To get the most of implementing serializable.
	 */
	private static final long serialVersionUID = -819649500679466L;
	private String folderPath ;
	public RetrieverFunction(String folderPath) {
		this.folderPath = folderPath;
	}
	
	@Override
	public Object call(String v1) throws Exception {
		Object returnObject = null;
		try {
			FileInputStream fis = new FileInputStream("" + folderPath + v1 + ".txt");
			ObjectInputStream oi = new ObjectInputStream(fis);
			returnObject = oi.readObject();
			oi.close();
		} catch (IOException e) {
			System.out.println("I/O error occured while getting object from local file with name = " +v1);
			return null;
		} catch (ClassNotFoundException cle) {
			System.out.println("Classloader throws error while getting object from local file.");
			return null;
		}
		return returnObject;
	}


}
