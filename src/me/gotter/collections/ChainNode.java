package me.gotter.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChainNode implements Map<String, ChainNode>, Iterable<ChainNode>{

	/**
	 * Internal value
	 */
	protected Object value = null;
	
	private boolean isNative = false;
	
	// Constructors
	/**
	 * Creates new empty node
	 */
	public ChainNode()
	{
	}
	
	/**
	 * Creates new node with provided object as value
	 * 
	 * @param value
	 */
	public ChainNode(Object value)
	{
		if (value instanceof ChainNode) {
			// cloning values
			ChainNode source = (ChainNode) value;
			isNative = source.isNative;
			this.value = source.value;
		} else {
			this.value = value; 
		}
	}
	
	// Validators
	public boolean isNull()
	{
		return value == null;
	}

	public boolean isBool()
	{
		return !isNull() && value instanceof Boolean;
	}
	
	public boolean isTrue()
	{
		return isBool() && ((Boolean) value).booleanValue();
	}
	
	public boolean isString()
	{
		return !isNull() && value instanceof String;
	}

	public boolean isInt()
	{
		return !isNull() && value instanceof Integer;
	}
	
	public boolean isLong()
	{
		return !isNull() && (isInt() || value instanceof Long);
	}
	
	public boolean isFloat()
	{
		return !isNull() && value instanceof Float;
	}
	
	public boolean isDouble()
	{
		return !isNull() && (isFloat() || value instanceof Double);
	}
	
	public boolean isIterable()
	{
		return !isNull() && value instanceof Iterable<?>;
	}
	
	public boolean isCollection()
	{
		return !isNull() && value instanceof Collection<?>;
	}
	
	public boolean isMap()
	{
		return !isNull() && value instanceof Map<?,?>;
	}
	
	public boolean isChainNode()
	{
		return !isNull() && value instanceof ChainNode;
	}
	
	public boolean isChainNodeIterable()
	{
		return isNative && isIterable();
	}
	
	public boolean isChainNodeMap()
	{
		return isNative && isMap();
	}
	
	
	// Getters
	public Object getValue()
	{
		return value;
	}
	
	public boolean getBool()
	{
		return ((Boolean) value).booleanValue();
	}
	
	public String getString()
	{
		return value.toString();
	}
	
	public int getInt()
	{
		return ((Integer) value).intValue();
	}
	
	public long getLong()
	{
		if (isInt()) {
			return getInt();
		}
		return ((Long) value).longValue();
	}
	
	public float getFloat()
	{
		return ((Float) value).floatValue();
	}
	
	public double getDouble()
	{
		if (isFloat()) {
			return getFloat();
		}
		return ((Double) value).doubleValue();
	}
	
	// Setter
	public ChainNode set(Object value)
	{
		this.value = value;
		this.isNative = false;
		return this;
	}

	// Map interface 	
	@Override
	public void clear() {
		value = null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		return mapReference().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return mapReference().containsValue(value);
	}

	@Override
	public ChainNode get(Object key) {
		return mapReference().get(key);
	}

	@Override
	public ChainNode put(String key, ChainNode value) {
		return mapReference().put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends ChainNode> m) {
		mapReference().putAll(m);
	}

	@Override
	public Set<String> keySet() {
		return mapReference().keySet();
	}

	@Override
	public Collection<ChainNode> values() {
		return mapReference().values();
	}

	@Override
	public Set<java.util.Map.Entry<String, ChainNode>> entrySet() {
		return mapReference().entrySet();
	}
	
	@Override
	public int size() {
		if (isNull()) {
			return 0;
		}
		if (isMap()) {
			return ((Map<?,?>) value).size(); 
		}
		
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public ChainNode remove(Object key) {
		return mapReference().remove(key);
	}
	
	// Iterator
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<ChainNode> iterator() {
		if (isNull()) {
			throw new RuntimeException("Node is empty");
		}
		if (!isIterable()) {
			return Arrays.asList(new ChainNode(this.value)).iterator();
		}
		if (isChainNodeMap()) {
			return ((Map<String, ChainNode>) value).values().iterator();
		}
		
		return ((Iterable<ChainNode>) value).iterator();
	}
	
	
	// Internal helpers
	@SuppressWarnings("unchecked")
	private Map<String, ChainNode> mapReference()
	{
		if (isChainNodeMap()){
			// Already done
		} 
		else if (isNull()) {
			value = new HashMap<String, ChainNode>();
			isNative = true;
		} else {
			throw new RuntimeException("Cannot create map for " + value.getClass());
		}
		return (Map<String, ChainNode>) value;
	}
	
	// Java core
	@Override
	public String toString()
	{
		if (isNull()) {
			return "";
		}
		return getValue().toString();
	}
	
	@Override
	public int hashCode()
	{
		if (isNull()) {
			return 0;
		}
		
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		ChainNode cn = new ChainNode(o);
		if (this.value == null) {
			return cn.value == null;
		}
		
		return this.value.equals(o);
	}
}
