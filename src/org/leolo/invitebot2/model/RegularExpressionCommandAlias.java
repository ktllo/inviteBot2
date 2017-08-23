package org.leolo.invitebot2.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegularExpressionCommandAlias extends CommandAlias {
	Logger logger = LoggerFactory.getLogger(RegularExpressionCommandAlias.class);
	private Pattern pattern;
	private int mode;
	RegularExpressionCommandAlias(String searchString, String replaceString, int mode) {
		super(searchString, replaceString);
		this.mode = mode;
		if(mode == CommandAlias.REGULAR_EXPRESSION_FULL)
			pattern =  Pattern.compile("^"+searchString+" ?(.*)$",Pattern.CASE_INSENSITIVE);
		else
			pattern =  Pattern.compile("^"+searchString+"$",Pattern.CASE_INSENSITIVE);
		logger.info(pattern.pattern());
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
			if(mode == CommandAlias.REGULAR_EXPRESSION_FULL)
				return matcher.replaceAll(replaceString+" $"+groupCount);
			else
				return matcher.replaceAll(replaceString);
			
		}
		return line;
	}

}
