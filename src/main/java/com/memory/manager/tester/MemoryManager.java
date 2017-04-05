package com.memory.manager.tester;

import java.util.ArrayList;
import java.util.List;

import com.memory.manager.impl.LocalFileSparkDeallocator;
import com.memory.manager.impl.LocalFileSparkPersistor;
import com.memory.manager.prototype.Deallocator;
import com.memory.manager.prototype.Persistor;
import com.memory.manager.prototype.Wrapper;

/**
 * Tester class for testing the application using main method.
 * Writer method for persisting object in disks.
 * Reader method for reading object based on list names.
 * @author syambrij
 *
 */
public class MemoryManager {

	public static void main(String[] args) {
		writerMethod();
		//readerMethod();
	}

	private static void writerMethod() {
		List<Wrapper> wrappers = new ArrayList<>();
		for (int i = 1; i < 1000000 ; i++) {
			wrappers.add(new Wrapper(String.valueOf(i), Integer.valueOf(i)));
		}
		long startTime = System.currentTimeMillis();
		Persistor po = new LocalFileSparkPersistor("/home/syambrij/Desktop/temp_simple1");
		po.persistAllWrapper(wrappers);
		System.out.println("Total time taken is: " + (System.currentTimeMillis() - startTime)/1000);
	}

	private static void readerMethod() {
		List<String> names = new ArrayList<>();
		for (int i = 1; i < 10000 ; i++) {
			names.add(String.valueOf(i));
		}
		Deallocator dao = new LocalFileSparkDeallocator("/home/syambrij/Desktop/temp");
		for (Object obj : dao.getAll(names)) {
			System.out.println(((Wrapper) obj).getObject());
		}
	}
	
}
