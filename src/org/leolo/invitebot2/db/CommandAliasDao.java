package org.leolo.invitebot2.db;

import java.util.List;

import org.leolo.invitebot2.model.CommandAlias;

public interface CommandAliasDao {

	List<CommandAlias> getAll();

}
