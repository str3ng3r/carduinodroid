package de.carduinodroid;

import java.util.TimerTask;;

/**
 * \brief This Class is used to start TimerTasks from the inside of the same TimerTask
 * @author Alexander Rose
 *
 */

public class Dummy extends TimerTask {

	private TimerTask tt = null;

	public Dummy(TimerTask tt) {
		this.tt = tt;
	}

	@Override
	public void run() {
		tt.run();
	}

}
