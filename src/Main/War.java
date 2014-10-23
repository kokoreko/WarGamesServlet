package Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;


import WarWeapons.Destructor;
import WarWeapons.Launcher;
import WarWeapons.Missile;
import WarWeapons.Destructor.Type;
import WarWeapons.Missile.State;

/**
 * 
 * War class is a simulation of some kind of war in the middle east.
 * 
 * @author by Kosta Lazarev & Omri Glam
 * @version 25/08/2014
 */
public class War {

	
	/**
	 * The game works on a clock that logs events. the tick set in mili seconds.
	 */
	public static final int TIMER_TICK = 1000;
	private static final String WAR_LOG_FILE = "D:\\Users\\omri glam\\workspace\\warServlet\\Logs\\WarLog.txt";
	private List<Destructor> missileDestructors;
	private List<Destructor> missileLauncherDestructors;
	private List<Launcher> missileLaunchers;
	private List<Missile> inAirMissiles;
	private Timer WarTimer;
	private boolean started;
	private Statistics stats;
	public static int WarTimeInSeconds;

	private FileHandler fileHandler;
	
	/**
	 * Main war game constructor
	 */
	public War() {
		WarTimeInSeconds = 0;
		missileDestructors = new ArrayList<Destructor>();
		missileLauncherDestructors = new ArrayList<Destructor>();
		missileLaunchers = new ArrayList<Launcher>();
		inAirMissiles = new ArrayList<Missile>();
		WarTimer = new Timer();
		started = false;
		stats=new Statistics();
		addFileHandler();

	}
/**
 * Inner method for creating file handler for war log
 */
	private void addFileHandler() {
		try{		
			fileHandler = new FileHandler(WAR_LOG_FILE);
		}catch(Exception e){
			System.err.println("Logger was not found!");
		}
		
	}

	/**
	 * Inner method for starting simulation war clock (seconds ticker)
	 * the speed of the simulation is determined by TIMER_TICK constant
	 */
	private void startWarTimer() {
		WarTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				WarTimeInSeconds++;
				// System.out.println(WarTimeInSeconds);
				if (WarTimeInSeconds == Integer.MAX_VALUE) {
					endWar();
				}

			}
		}, 0, TIMER_TICK);
		started = true;
	}

	/**
	 * End war game, stops timer
	 */
	protected void endWar() {
		for (Launcher mLauncer : missileLaunchers) {
			mLauncer.interrupt();
		}
		for (Destructor destructor : missileDestructors) {
			destructor.interrupt();
		}
		for (Destructor destructor : missileLauncherDestructors) {
			destructor.interrupt();
		}
		WarTimer.cancel();

	}

	/**
	 * Starts the simulation by starting all threads that were loaded from config file
	 * and starts the simulation timer
	 */
	public void startWar() {
		startWarTimer();

		for (Launcher mLauncer : missileLaunchers) {
			mLauncer.start();
		}
		for (Destructor destructor : missileDestructors) {
			destructor.start();
		}
		for (Destructor destructor : missileLauncherDestructors) {
			destructor.start();
		}

	}

	
/**
 * Missiles that are in the air right now
 * @return inAirMissiles
 */
	public List<Missile> getInAirMissiles() {
		return inAirMissiles;
	}

	public List<Destructor> getMissileDestructors() {
		return missileDestructors;
	}

	public List<Destructor> getMissileLauncherDestructors() {
		return missileLauncherDestructors;
	}

	public List<Launcher> getMissileLaunchers() {
		return missileLaunchers;
	}
	public Statistics getStatistics(){
		return stats;
	}
	/**
	 * War started boolean
	 * 
	 * @return TRUE if war is started
	 */
 	public boolean isStarted() {
		return started;
	}


	/**
	 * Updates inAirMissile list method goes through all launcher and checks
	 * missiles in the air and set them in one list.
	 */
	public void updateInAirMissiles() {
		inAirMissiles = new ArrayList<Missile>();
		for (Launcher l : missileLaunchers) {
			for (Missile missile : l.getMissiles()) {
				if (missile.getMissileState() == Missile.State.Flying) {
					inAirMissiles.add(missile);
				}
			}
		}
	}

	/**
	 * Add missile destructor to list
	 */
	public void addMissileDestructor(int id) {

		Destructor tmp = new Destructor("D" + id,stats);
		tmp.start();
		missileDestructors.add(tmp);

	}

	/**
	 * Add launcher destructor to list
	 */
	public void addLauncherDestructor(String type) {
		Destructor tmp = new Destructor(Destructor.Type
				.valueOf(type),stats);
		tmp.start();
		missileLauncherDestructors.add(tmp);
	}
	/**
	 * 
	 * @param id
	 */
	public void addLauncher(int id){
		Launcher tmp = new Launcher("L" + id,true,stats);
		tmp.start();
		missileLaunchers.add(tmp);
		
	}

	/**
	 * getFileHandler returns fileHandler
	 * @return fileHandler
	 */
	public FileHandler getFileHandler(){
		return fileHandler;
	}
	
}
