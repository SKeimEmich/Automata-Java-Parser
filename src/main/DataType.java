package main;

public enum DataType{
//	BYTE,
//	SHORT,
//	LONG,
//	FLOAT, // Removed for main project, added as stretch goal
	INT("int"),
	DOUBLE("double"),
	BOOLEAN("boolean"),
	CHAR("char");
	
	public final String name;
	
	private DataType(String name) {
		this.name = name;
	}
}