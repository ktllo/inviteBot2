package org.leolo.invitebot2.model;

public class PlainCommandAlias extends CommandAlias {

	PlainCommandAlias(String searchString, String replaceString) {
		super(searchString, replaceString);
	}

	@Override
	public boolean isMatch(String line) {
		// TODO Auto-generated method stub
		return line.toLowerCase().startsWith(searchString.toLowerCase());
	}

	@Override
	public String match(String line) {
		if(isMatch(line)){
			return replaceString + line.substring(searchString.length());
		}else{
			return line;
		}
	}

}
