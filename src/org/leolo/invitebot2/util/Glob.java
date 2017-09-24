package org.leolo.invitebot2.util;

import java.text.ParseException;
import java.util.regex.Pattern;

import org.pircbotx.User;

public class Glob {
	
	private String nick;
	private String ident;
	private String host;
	
	private String patNick;
	private String patIdent;
	private String patHost;
	
	private boolean ready = false;
	
	public void compile(){
		ready = false;
		patNick = getPattern(nick);
		patIdent = getPattern(ident);
		patHost = getPattern(host);
		ready = true;
	}
	
	public boolean match(User user){
		if(!ready){
			throw new IllegalStateException("Pattern is not ready");
		}
		return Pattern.matches(patNick, Helper.toLowerCase(user.getNick())) &&
				Pattern.matches(patIdent, Helper.toLowerCase(user.getIdent())) &&
				Pattern.matches(patHost, Helper.toLowerCase(user.getHostname()));
	}
	
	public void parse(String hostmask){
		ready = false;
		int split1 = hostmask.indexOf("!");
		int split2 = hostmask.lastIndexOf("@");
		nick = hostmask.substring(0, split1);
		host = hostmask.substring(split2+1);
		ident = hostmask.substring(split1+1, split2);
		if(ident.indexOf("!")==-1 && ident.indexOf("@")== -1){
			compile();
		}else{
			throw new RuntimeException("Unparsable mask");
		}
	}
	
	public String getHostmask(){
		return nick+"!"+ident+"@"+host;
	}
	
	private String getPattern(String glob){
		glob = glob.replace(".", "\\.");
		glob = glob.replace("^", "\\^");
		glob = glob.replace("$", "\\$");
		glob = Helper.toLowerCase(glob);
		glob = glob.replace("?", ".");
		glob = glob.replace("*", ".*");
		return "^"+glob+"$";
	}
}
