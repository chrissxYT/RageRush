package de.chrissx.ragerush.server;

import org.bukkit.command.CommandSender;

public abstract class CommandHandler {

	String command;

	public CommandHandler(String command) {
		this.command = command;
	}

	public abstract boolean handleCommand(CommandSender s, String[] args);
}
