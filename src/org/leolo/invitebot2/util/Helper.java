package org.leolo.invitebot2.util;

public class Helper {
	
	/*
	 * Extract from RFC1459
	 * Because of IRC's scandanavian origin, the characters {}| are
	 * considered to be the lower case equivalents of the characters []\,
	 * respectively. This is a critical issue when determining the
	 * equivalence of two nicknames.
	 */
	public static String toLowerCase(String string){
		StringBuffer sb = new StringBuffer();
		for(char ch:string.toCharArray()){
			switch(ch){
			case '[':
				sb.append('{');
				break;
			case ']':
				sb.append('}');
				break;
			case '\\':
				sb.append('|');
				break;
			default:
				sb.append(Character.toLowerCase(ch));
			}
		}
		return sb.toString();
	}
	
	public static String toUpperCase(String string){
		StringBuffer sb = new StringBuffer();
		for(char ch:string.toCharArray()){
			switch(ch){
			case '{':
				sb.append('[');
				break;
			case '}':
				sb.append(']');
				break;
			case '|':
				sb.append('\\');
				break;
			default:
				sb.append(Character.toUpperCase(ch));
			}
		}
		return sb.toString();
	}
}
