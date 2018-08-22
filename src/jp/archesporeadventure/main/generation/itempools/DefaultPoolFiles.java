package jp.archesporeadventure.main.generation.itempools;

public enum DefaultPoolFiles {
	
	WORLD("WorldItemPools.yml"), MOB("MobItemPools.yml"), CUSTOM("CustomItemPools.yml");
	
	String fileName;
	
	private DefaultPoolFiles(String file) {
		this.fileName = file;
	}
	
	public String getFileName() {
		return this.fileName;
	}
}
