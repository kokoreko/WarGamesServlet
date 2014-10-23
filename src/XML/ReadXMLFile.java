package XML;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import Main.Statistics;
import WarWeapons.Destructor;
import WarWeapons.Launcher;
import WarWeapons.Missile;
import WarWeapons.Destructor.Type;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
/**
 * ReadXMLFile class reads the data of the war from the XML file and adds the data to the war.
 * <br><br>
 * <b>Functions links:<br></b>
 * {@link #loadFile(String)} <br>
 * {@link #getData(String, List, List, List, Statistics, FileHandler)}<br>
 * {@link #loadMissileDestructor(Document, List, List, Statistics)}<br>
 * {@link #loadMissileLauncher(Document, List, Statistics, FileHandler)}<br>
 * {@link #loadMissileLauncherDestructor(Document, List, List, Statistics)}<br>
 * 
 * @author Kosta Lazarev & Omri Glam
 * 
 */
public class ReadXMLFile {

	
	public static void getData(String FileName,
			List<Launcher> missileLaunchers,
			List<Destructor> missileDestructor,
			List<Destructor> missileLauncherDestructor, Statistics stats,FileHandler fileHandler) {

		try {

			Document doc = loadFile(FileName);
			System.out.println("Load element :"
					+ doc.getDocumentElement().getNodeName());
			System.out.println("----------------------------");
			loadMissileLauncher(doc, missileLaunchers,stats,fileHandler);
			loadMissileDestructor(doc, missileDestructor, missileLaunchers,stats);
			loadMissileLauncherDestructor(doc, missileLauncherDestructor,
					missileLaunchers,stats);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load XML file by file name and returns XML document
	 * 
	 * @param fileName
	 * @return XML document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document loadFile(String fileName)
			throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return doc;
	}

	/**
	 * 
	 * @param doc
	 * @param missileLauncherDestructor
	 * @param missileLaunchers
	 * @param stats 
	 * @throws Exception
	 */
	private static void loadMissileLauncherDestructor(Document doc,
			List<Destructor> missileLauncherDestructor,
			List<Launcher> missileLaunchers, Statistics stats) throws Exception {
		System.out.println("Loading missile launcher destructors");
		Node nMissileLauncherDestructor = doc.getElementsByTagName(
				"missileLauncherDestructors").item(0);
		NodeList nDestructors = ((Element) nMissileLauncherDestructor)
				.getElementsByTagName("destructor");
		for (int i = 0; i < nDestructors.getLength(); i++) {

			Destructor.Type dType;
			Element eDestructor = (Element) nDestructors.item(i);
			dType = Destructor.Type.valueOf(eDestructor.getAttribute("type"));
			Destructor mDestructor = new Destructor(dType,stats);
			System.out.println("\nCurrent Element :"
					+ eDestructor.getNodeName() + " type = " + dType);
			NodeList nDestructdLauncher = eDestructor
					.getElementsByTagName("destructedLanucher");
			for (int j = 0; j < nDestructdLauncher.getLength(); j++) {
				String dId;
				int dDestructTime;
				Element eDestructedLauncher = (Element) nDestructdLauncher
						.item(j);
				dId = eDestructedLauncher.getAttribute("id");
				dDestructTime = Integer.parseInt(eDestructedLauncher
						.getAttribute("destructTime"));
				System.out.println("	Destructd Launcher	: " + dId);
				System.out.println("	Destructd time		: " + dDestructTime);
				Launcher mDestructedLauncher = Launcher.getLauncher(
						missileLaunchers, dId);
				if (mDestructedLauncher != null) {
					mDestructedLauncher.setDestructTime(dDestructTime);
					mDestructor.addDestructLauncher(mDestructedLauncher);
				} else {
					throw new Exception("Lanucher not found");
				}
			}
			missileLauncherDestructor.add(mDestructor);
		}

	}
	
	/**
	 * 
	 * @param doc
	 * @param missileDestructors
	 * @param missileLaunchers
	 * @param stats
	 * @throws Exception
	 */
	
	private static void loadMissileDestructor(Document doc,
			List<Destructor> missileDestructors, List<Launcher> missileLaunchers, Statistics stats)
			throws Exception {
		System.out.println("Loading missile destructors");
		Node nMissileDestructor = doc
				.getElementsByTagName("missileDestructors").item(0);
		NodeList nDestructors = ((Element) nMissileDestructor)
				.getElementsByTagName("destructor");
		for (int i = 0; i < nDestructors.getLength(); i++) {

			String dId;

			Element eDestructor = (Element) nDestructors.item(i);
			dId = eDestructor.getAttribute("id");
			Destructor mDestructor = new Destructor(dId,stats);
			System.out.println("\nCurrent Element :"
					+ eDestructor.getNodeName() + " id = " + dId);
			NodeList nDestructdMissile = eDestructor
					.getElementsByTagName("destructdMissile");
			for (int j = 0; j < nDestructdMissile.getLength(); j++) {
				String mId;
				int mDestructAfterLaunch;
				Element eDestructedMissile = (Element) nDestructdMissile
						.item(j);
				mId = eDestructedMissile.getAttribute("id");
				mDestructAfterLaunch = Integer.parseInt(eDestructedMissile
						.getAttribute("destructAfterLaunch"));
				System.out.println("	Destructd missile	: " + mId);
				System.out
						.println("	Destructd time		: " + mDestructAfterLaunch);
				Missile mDestructedMissile = Missile.getMissile(
						missileLaunchers, mId);
				if (mDestructedMissile != null) {
					mDestructedMissile
							.setDestructAfterLaunch(mDestructAfterLaunch);
					mDestructor.addDestructMissile(mDestructedMissile);
				} else {
					throw new Exception("Missile not found");
				}
			}
			missileDestructors.add(mDestructor);
		}

	}
	
	/**
	 * 
	 * @param doc
	 * @param missileLaunchers
	 * @param stats
	 * @param fileHandler
	 */
	
	private static void loadMissileLauncher(Document doc,
			List<Launcher> missileLaunchers, Statistics stats,FileHandler fileHandler) {
		System.out.println("missileLaunchers:");
		NodeList nLaunchers = doc.getElementsByTagName("launcher");
		for (int i = 0; i < nLaunchers.getLength(); i++) {
			String mId;
			String mDestination;
			int mLaunchTime;
			int mFlyTime;
			int mDamage;
			Node nLauncher = nLaunchers.item(i);
			System.out.println("\nCurrent Element :" + nLauncher.getNodeName());
			if (nLauncher.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nLauncher;
				System.out.println("Launcher id : "
						+ eElement.getAttribute("id"));
				System.out.println("Launcher isHidden : "
						+ eElement.getAttribute("isHidden"));
				Launcher launcher = new Launcher(eElement.getAttribute("id"),
						Boolean.parseBoolean(eElement.getAttribute("isHidden")),stats);
				NodeList nMissiles = eElement.getElementsByTagName("missile");
				for (int j = 0; j < nMissiles.getLength(); j++) {
					mId = ((Element) (nMissiles.item(j))).getAttribute("id");
					mDestination = ((Element) (nMissiles.item(j)))
							.getAttribute("destination");
					mLaunchTime = Integer.parseInt(((Element) (nMissiles
							.item(j))).getAttribute("launchTime"));
					mFlyTime = Integer.parseInt(((Element) (nMissiles.item(j)))
							.getAttribute("flyTime"));
					mDamage = Integer.parseInt(((Element) (nMissiles.item(j)))
							.getAttribute("damage"));
					launcher.addMissile(new Missile(mId, mDestination,
							mLaunchTime, mFlyTime, mDamage, launcher));

					System.out.println("missile : " + mId);
					System.out.println("	destination	:	 " + mDestination);
					System.out.println("	launchTime 	:	 " + mLaunchTime);
					System.out.println("	flyTime 	:	 " + mFlyTime);
					System.out.println("	damage 		:	 " + mDamage);
				}
				missileLaunchers.add(launcher);
			} // end of missileLaunchers
		}
	}
}