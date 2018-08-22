package jp.archesporeadventure.main.generation.itempools;

public abstract class ItemPool {

	protected String itemPoolName;
	protected String itemPoolDisplay;
	
	/**
	 * Creates a new ItemPool with the specified name.
	 * @param name Internal name used to identify the pool.
	 */
	public ItemPool(String name) {
		this(name, "");
	}
	
	/**
	 * Creates a new ItemPool with the specified name, and display name.
	 * @param name Internal name used to identify the pool.
	 * @param displayName the display name used by the pool.
	 */
	public ItemPool(String name, String displayName) {
		itemPoolName = name;
		itemPoolDisplay = displayName;
	}
	
	/**
	 * Gets the name of this ItemPool
	 * @return the name of this pool.
	 */
	public String getPoolName() {
		return itemPoolName;
	}
	
	/**
	 * Gets the display name of this ItemPool
	 * @return the name of this pool.
	 */
	public String getPoolDisplayName() {
		return itemPoolDisplay;
	}
	
}
