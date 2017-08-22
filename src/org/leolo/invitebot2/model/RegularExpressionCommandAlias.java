package org.leolo.invitebot2.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionCommandAlias extends CommandAlias {
	
	private Pattern pattern;
	
	RegularExpressionCommandAlias(String searchString, String replaceString) {
		super(searchString, replaceString);
		pattern =  Pattern.compile("^"+searchString+"(.*)$");
//		int count = Matcher.
	}

	@Override
	public boolean isMatch(String line) {
		return pattern.matcher(line).matches();
	}

	@Override
	public String match(String line) {
		Matcher matcher = pattern.matcher(line);
		if(matcher.matches()){
			int groupCount = matcher.groupCount();
			return matcher.replaceAll(replaceString+" $"+groupCount);
		}
		return line;
	}

}
