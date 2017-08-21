package org.leolo.invitebot2.model;

public class RegularExpressionCommandAlias extends CommandAlias {

	RegularExpressionCommandAlias(String searchString, String replaceString) {
		super(searchString, replaceString);
	}

	@Override
	public boolean isMatch(String line) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String match(String line) {
		// TODO Auto-generated method stub
		return line;
	}

}
