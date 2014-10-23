package Main;
/**
 * Statistics class saves all the war statistics during the war.
 * <br><br>
 * Functions links:<br>
 * {@link #addDamage(int)}<br>
 * {@link #toString()}<br>
 * 
 * @author Kosta Lazarev & Omri Glam
 * 
 */
public class Statistics {

	private int missileLaunched;
	private int missileIntercepted;
	private int missileHit;
	private int destroidLaunchers;
	private int totalDamage;
	/**
	 * Create clean statistics
	 */
	public Statistics(){
		missileLaunched=
		missileHit=
		missileIntercepted=
		destroidLaunchers=
		totalDamage=0;
		
	}
	
	public void addMissileLaunch(){
		missileLaunched++;
	}
	public void addMissileIntercept(){
		missileIntercepted++;
	}
	public void addMissileHit(){
		missileHit++;
	}
	public void addDeistroidLauncher(){
		destroidLaunchers++;
	}
	/**
	 * Add damage cost to total damage sum
	 * @param damage is a positive integer
	 * @throws Exception if damage is negative
	 */
	public void addDamage(int damage) throws Exception{
		if (damage <0){
			throw new Exception("damage can't fix stuff!");
		} else {
			totalDamage+=damage;
		}
	}
	
	public int getMissileLaunched() {
		return missileLaunched;
	}
	public void setMissileLaunched(int missileLaunched) {
		this.missileLaunched = missileLaunched;
	}
	public int getMissileIntercepted() {
		return missileIntercepted;
	}
	public void setMissileIntercepted(int missileIntercepted) {
		this.missileIntercepted = missileIntercepted;
	}
	public int getMissileHit() {
		return missileHit;
	}
	public void setMissileHit(int missileHit) {
		this.missileHit = missileHit;
	}
	public int getDestroidLaunchers() {
		return destroidLaunchers;
	}
	public void setDestroidLaunchers(int destroidLaunchers) {
		this.destroidLaunchers = destroidLaunchers;
	}
	public int getTotalDamage() {
		return totalDamage;
	}
	public void setTotalDamage(int totalDamage) {
		this.totalDamage = totalDamage;
	}
	
	@Override
	public String toString(){
		String ret = "\t **Statistics** \n" +
				"Number of missile launched:\t"+missileLaunched +
				"\nNumber of missile intersepted:\t"+missileIntercepted+
				"\nNumber of missile target hit:\t"+missileHit+
				"\nNumber of destroid launchers:\t"+destroidLaunchers+
				"\nTotal damage is:\t\t"+totalDamage+"\n";
		return ret;
	}
}
