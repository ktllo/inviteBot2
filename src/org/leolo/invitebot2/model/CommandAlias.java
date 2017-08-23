package org.leolo.invitebot2.model;

public abstract class CommandAlias {
	
	public static final int PLAIN = 1;
	public static final int REGULAR_EXPRESSION = 2;
	public static final int REGULAR_EXPRESSION_FULL = 3;
	
	
	protected String searchString;
	protected String replaceString; 
	
	public static CommandAlias getAlias(String searchString, String replaceString, int mode){
		if(mode==PLAIN)
			return new PlainCommandAlias(searchString, replaceString);
		else if(mode == REGULAR_EXPRESSION || mode == REGULAR_EXPRESSION_FULL)
			return new RegularExpressionCommandAlias(searchString, replaceString, mode);
		return null;
	}
	
	CommandAlias(String searchString, String replaceString){
		this.searchString = searchString;
		this.replaceString = replaceString;
	}
	
	public abstract boolean isMatch(String line);
	
	public abstract String match(String line);

	@Override
	public String toString() {
		return this.getClass().getName()+" [searchString=" + searchString + ", replaceString=" + replaceString + "]";
	}
}
