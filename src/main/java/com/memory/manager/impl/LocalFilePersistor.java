package com.memory.manager.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import com.memory.manager.prototype.CustomMemoryManagerException;
import com.memory.manager.prototype.Persistor;
import com.memory.manager.prototype.Wrapper;
import com.memory.manager.utils.Utils;

/**
 * Simple Java persistor for objects. The class saves data into text file at specified location.
 * This class will suggest you to use LocalFileSparkPersistor if you are considering more than 
 * 10,000 objects. 
 * For implementation on single node it uses java8 stream api features.
 * 
 * @see Persistor
 * 
 * @author syambrij
 * 
 */
public class LocalFilePersistor implements Persistor{

	private String folderPath = null;
	
	public LocalFilePersistor(String folderPath) {
		Utils.validFolderAssertion(folderPath);
		this.folderPath = getFolderPath(folderPath);
	}
	
	private String getFolderPath(String folderPath2) {
		return folderPath2.endsWith(File.separator) ? folderPath2 : folderPath2 + File.separator;
	}
	
	@Override
	public void persist(Wrapper wrapped) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(folderPath + wrapped.getName() + ".txt");
			ObjectOutput ou = new ObjectOutputStream(fos);
			ou.writeObject(wrapped);
			ou.close();
		} catch (IOException e) {
			throw new CustomMemoryManagerException("Object could not be saved on local files because of some I/O error.");
		}
	}

	@Override
	public <T extends Serializable> void persist(String name, T serializableObject) {
		persist(new Wrapper(name, serializableObject));
	}

	@Override
	public void persistObjects(List<String> names, List<? extends Serializable> serializableObjects) {
		int namesSize = names.size();
		validityCheck(namesSize, serializableObjects.size());
		performanceWarning(namesSize);
		for (int i = 0; i < namesSize; i++) {
			persist(new Wrapper(names.get(i), serializableObjects.get(i)));
		}
	}

	protected void validityCheck(int namesSize, int objectsSize) {
		if (namesSize != objectsSize) {
			throw new CustomMemoryManagerException("Length of list names should be same as of serializable objects size.");
		}		
	}

	@Override
	public void persistAllWrapper(List<Wrapper> wrappedList) {
		int listSize = wrappedList.size();
		performanceWarning(listSize);
		wrappedList.parallelStream().map(new Function<Wrapper, Boolean> () {
			@Override
			public Boolean apply(Wrapper t) {
				persist(t);
				return Boolean.TRUE;
			}
		}).count();
	}
	
	private void performanceWarning(int listSize) {
		if (listSize >= 1_00_00) {
			System.out.println("============================***WARNING***============================");
			System.out.println("You are trying to persist more than 1,00,00 objects.");
			System.out.println("You should consider using LocalFileSparkPersistor for better performance benefits.");
			System.out.println("========================***End of WARNING***=========================");
		}
	}

	public String getFolderPath() {
		return folderPath;
	}
}
