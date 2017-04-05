package com.memory.manager.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.memory.manager.prototype.Persistor;
import com.memory.manager.prototype.Wrapper;
import com.memory.manager.utils.Utils;

/**
 * Java persistor for objects using spark. The class saves data into text file at specified location.
 * This class will use LocalFilePersistor if you are considering less than 1000 objects. 
 * For implementation on single node it uses spark features.
 * Folder path should be of HDFS if we are not running on standalone node.
 * 
 * @see Persistor
 * @see LocalFilePersistor
 * 
 * @author syambrij
 *
 */
public class LocalFileSparkPersistor extends LocalFilePersistor{	
	
	private JavaSparkContext sc = Utils.sc;
	
	public LocalFileSparkPersistor(String folderPath) {
		super(folderPath);
		//sc.addFile(folderPath);
	}

	@Override
	public void persist(Wrapper wrapped) {
		super.persist(wrapped);
	}

	@Override
	public <T extends Serializable> void persist(String name, T serializableObject) {
		persist(new Wrapper(name, serializableObject));
	}

	@Override
	public void persistObjects(List<String> names, List<? extends Serializable> serializableObjects) {
		int namesSize = names.size();
		if (names.size() <= 1000) {
			super.persistObjects(names, serializableObjects);
			return ;
		} 
		super.validityCheck(namesSize, serializableObjects.size());
		List<Wrapper> wrappers = new ArrayList<>();
		for (int i = 0; i < namesSize; i++) {
			wrappers.add(new Wrapper(names.get(i), serializableObjects.get(i)));
		}
		callSparkMethod(wrappers);
	}

	@Override
	public void persistAllWrapper(List<Wrapper> wrappedList) {
		int listSize = wrappedList.size();
		if (listSize <= 1_000) {
			super.persistAllWrapper(wrappedList);
			return ;
		}
		callSparkMethod(wrappedList);
	}
	
	private void callSparkMethod(List<Wrapper> wrappers) {
		String folderpath = getFolderPath();
		JavaRDD<Wrapper> wrappedRdd = sc.parallelize(wrappers);
		wrappedRdd.map(new WriteObjectFunction(folderpath)).count();
	}
}
