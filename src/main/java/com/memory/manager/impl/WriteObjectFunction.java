package com.memory.manager.impl;

import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.spark.api.java.function.Function;

import com.memory.manager.prototype.Wrapper;

/**
 * Write object function used in LocalFileSparkPersistor.
 * @author syambrij
 *
 */
public class WriteObjectFunction implements Function<Wrapper, String>{
	
	/**
	 * To get the most of implementing serializable.
	 */
	private static final long serialVersionUID = 7868277613913988135L;
	private String folderPath;
	
	WriteObjectFunction(String folderPath) {
		this.folderPath = folderPath;
	}
	
	@Override
	public String call(Wrapper v1) throws Exception {
		FileOutputStream fos = new FileOutputStream(folderPath + v1.getName() + ".txt");
		ObjectOutput ou = new ObjectOutputStream(fos);
		ou.writeObject(v1);
		ou.close();
		return "saved";
	}
	
}
