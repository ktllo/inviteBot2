package org.leolo.invitebot2.model;

public abstract class CommandAlias {
	
	public static final int PLAIN = 1;
	public static final int REGULAR_EXPRESSION = 2;
	
	protected String searchString;
	protected String replaceString; 
	
	public static CommandAlias getAlias(String searchString, String replaceString, int mode){
		if(mode==PLAIN)
			return new PlainCommandAlias(searchString, replaceString);
		else if(mode == REGULAR_EXPRESSION)
			return new RegularExpressionCommandAlias(searchString, replaceString);
		return null;
	}
	
	CommandAlias(String searchString, String replaceString){
		this.searchString = searchString;
		this.replaceString = replaceString;
	}
	
	public abstract boolean isMatch(String line);
	
	public abstract String match(String line);
}
