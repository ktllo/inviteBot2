package invitebot2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.leolo.invitebot2.model.CommandAlias;

public class CA {

	private List<CommandAlias> list;
	
	
	
	@Test
	public void test(){
		CommandAlias ca = CommandAlias.getAlias("boo", "echo boo", CommandAlias.PLAIN);
		if(!ca.match("boo line").equals("echo boo line"))
			fail();
		if(!ca.match("poo line").equals("poo line"))
			fail();
		
	}
}
