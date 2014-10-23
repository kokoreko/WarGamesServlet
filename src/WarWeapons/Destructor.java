package WarWeapons;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Main.Statistics;
import Main.War;

public class Destructor extends Thread {

	private PriorityQueue<Launcher> destructdLauncher;
	private PriorityQueue<Missile> destructdMissile;
//	private Object LockMissile;
//	private Object LockLuncher;
	private String id;
	private boolean isActive;
	private Statistics stats;
	private FileHandler fileHandler;
	private static Logger logger;
	private final static String DEAFULT_ID = "D00";

	public enum Type {
		plane, ship, iron_dome
	};

	private Type dType;

	public Destructor() {
		this.id = DEAFULT_ID; // Default id
//		LockMissile = new Object();
	}

	
	public Destructor(String dId) {
		super();
		destructdMissile = new PriorityQueue<Missile>(
				(Comparator<? super Missile>) (new Missile.MissileDestructComparator()));
		this.id = dId;
		dType = Type.iron_dome;
//		LockMissile = new Object();
		isActive = true;

	}

	public Destructor(Type type) {
		super();
		destructdLauncher = new PriorityQueue<Launcher>(
				(Comparator<? super Launcher>) (new Launcher.DestructComparator()));
		this.dType = type;
//		LockLuncher = new Object();
		isActive = true;
	}

	public Destructor(String dId, Statistics stats) {
		super();
		destructdMissile = new PriorityQueue<Missile>(
				(Comparator<? super Missile>) (new Missile.MissileDestructComparator()));
		this.id = dId;
		dType = Type.iron_dome;
//		LockMissile = new Object();
		isActive = true;
		this.stats = stats;

	}

	public Destructor(Type type, Statistics stats) {
		super();
		destructdLauncher = new PriorityQueue<Launcher>(
				(Comparator<? super Launcher>) (new Launcher.DestructComparator()));
		this.dType = type;
//		LockLuncher = new Object();
		isActive = true;
		this.stats = stats;
		

	}

	public void setStatistics(Statistics s) {
		stats = s;
	}

	public void addDestructLauncher(Launcher l) {
		destructdLauncher.add(l);
	}

	public void addDestructMissile(Missile m) {
		destructdMissile.add(m);
	}

	public String getDestructorId() {
		return id;
	}

	public void run() {
		try {
			if (dType == Type.iron_dome) {
				MissileDestructorRun();
			} else {
				LauncherDestructorRun();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void MissileDestructorRun() {
		while (isActive) {
			try {
				Missile m = destructdMissile.peek();

				if (m != null) {

					if (m.getDestructAfterLaunch() == War.WarTimeInSeconds || m.getDestructAfterLaunch() == 0) {
						logger.log(Level.INFO, "Trying to intercept "
								+ m.toString(),this);
						intercept(m);
						destructdMissile.poll();
					}

				}
				sleep(100);
			} catch (Exception e) {

			}
		}
	}

	private void LauncherDestructorRun() throws InterruptedException {
		while (isActive) {
			try {
				Launcher l = destructdLauncher.peek();
				if (l.getDestructTime() == War.WarTimeInSeconds || l.getDestructTime() == 0) {
					logger.log(Level.INFO,"Trying to destroy " + l.toString(),this);
					if (l.destroyLauncher()) {
						logger.log(Level.INFO,l.toString() + " Distroyed!!",this);
						if (stats != null) {
							stats.addDeistroidLauncher();
						}
					} else {
						logger.log(Level.INFO,"Fail to destroy " + l.toString(),this);
					}
					destructdLauncher.poll();
				}
			} catch (Exception e) {

			}
			sleep(500);
		}
	}

	public boolean intercept(Missile m) {
		if (m.Intercep()) {
			logger.log(Level.INFO,this.toString() + " Intercepted " + m.toString(),this);
			if (stats != null) {
				stats.addMissileIntercept();
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Destructor type:" + this.dType;
	}

}
