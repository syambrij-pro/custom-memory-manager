package com.memory.manager.prototype;

import java.io.Serializable;

/**
 * This is a wrapper object if you are considering to use 
 * more objects. It is better substitution for following method.
 * Use of this class in - 
 * void persistObjects(List<String> names, List<? extends Serializable> serializableObjects); 
 * 					vs
 * void persistAllWrapper(List<Wrapper> wrappers);
 * @author syambrij
 *
 */
public class Wrapper implements Serializable{

	private static final long serialVersionUID = -6314754750497891415L;
	
	private String name;
	private Object object;
	
	public Wrapper(String name, Object object) {
		isObjectSerializable(object);
		this.name = name;
		this.object = object;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		isObjectSerializable(object);
		this.object = object;
	}
	private void isObjectSerializable(Object object) {
		if (!(object instanceof Serializable)) {
			throw new CustomMemoryManagerException("The object is not serializable.");
		}
	}
}
