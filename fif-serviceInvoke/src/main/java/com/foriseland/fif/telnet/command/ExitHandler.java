package com.foriseland.fif.telnet.command;

public class ExitHandler implements CommandHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.akhettar.telnet.command.CommandHandler#handle()
	 */
	@Override
	public String handle() {
		return "Goodbye...";
	}
}