

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import WarWeapons.Destructor;
import WarWeapons.Launcher;
import WarWeapons.Missile;
import XML.ReadXMLFile;
import Main.War;

/**
 * Servlet implementation class servlet
 * @author Omri Glam & Kosta Lazarev
 */
@WebServlet( urlPatterns = "/servlet")
public class servlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final String CONFIG_FILE = "D:\\Kosta\\git\\WarGamesServlet\\src\\war.xml";
    private War warGames;
    private boolean failed = false;
    private List<Launcher> launchers;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		int id, ironID = 0, missileID = 0, launcherID = 0;
		String type = null;
		HttpSession theSession = request.getSession(true);
		if(theSession.isNew()){
			id=1;
			theSession.setAttribute("id", id);
		}else{
			id = (Integer)theSession.getAttribute("id");
		}
		if(request.getParameter("startWar") != null){
			warGames = new War();
			try {
				ReadXMLFile.getData(CONFIG_FILE, warGames.getMissileLaunchers(),
						warGames.getMissileDestructors(),
						warGames.getMissileLauncherDestructors(),warGames.getStatistics(),warGames.getFileHandler());
				warGames.startWar();
			}catch(Exception e){System.out.println(":P");};
		}
		
		PrintWriter out = response.getWriter();
		try{
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"css.css\" type=\"text/css\" media=\"all\">");
            out.println("<title>Good Side</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1 align=\"center\"> IDF HTML War Panel </h1><br/>");
            out.println("<div style=\"width:100%\">");
            out.println("<table class=\"main\">");
            out.println("<thead class=\"thead\"><tr>");
            out.println("<th>Add munition</th>");
            out.println("<th>New Munition ID</th>");
            out.println("<th>Action state</th>");
            out.println("</tr></thead>");
            // end of table head
            
            out.println("<tbody>");
            out.println("<tr>");
            out.println("<td align=\"center\"><form method=\"get\" action=\"servlet\">");
            out.println("<input name=\"iron\" type=\"submit\" value=\"Iron dome\" />");
            out.println("</td>");
            out.println("<td align=\"center\">Enter ID: ");
            out.println("<input type=\"text\" name=\"ironID\" /></br></form></td>");	
            out.println("<td></td></tr>");
            if(request.getParameter("iron") != null){

            	out.println("<tr><td align=\"center\"> New IronDome </td>");
            	
            	try{
            	ironID = Integer.parseInt(request.getParameter("ironID"));
            	warGames.addMissileDestructor(ironID);
            	}catch(Exception e){
            		failed = true;
            		out.println("<td></td><td align=\"center\"> failed</td>");
            	}
            	if(!failed){
            		out.println("<td align=\"center\">D"+ ironID +"</td><td align=\"center\"> Was added successfully</td>");
            		out.println("</tr>");
            	}
            }
            out.println("<tr><td align=\"center\"><form method=\"get\" action=\"servlet\">");
            out.println("<input name=\"launcher\" type=\"submit\" value=\"Launcher Destractor\" />");
            out.println("</td>");
            out.println("<td align=\"center\">Type(Plane/Ship): ");
            out.println("<input type=\"text\" name=\"launcher_type\" /></br></form></td>");	
            out.println("<td></td></tr>");
            
            if(request.getParameter("launcher") != null){
            	out.println("<tr><td align=\"center\"> New Launcher Destractor </td>");
            	
            	try{
            		type = request.getParameter("launcher_type");
            		warGames.addLauncherDestructor(type);
            	}catch(Exception e){ 
            		failed =true;
            		out.println("<td></td><td align=\"center\"> failed</td>");
            	}
            	if(!failed){
            		out.println("<td align=\"center\">Type: "+ type +"</td><td align=\"center\">"
            				+ " Was added successfully</td>");
            		out.println("</tr></table></td></tr>");
            	}
            }
            out.println("<tr><td  align =\"center\">");
            out.println("<table style=\"width:100%\">");
            out.println("<tr>");
            out.println("<td> Choose Missile to intercept </td>");
            out.println("<td> Action State </td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td align=\"center\"> ");
            out.println("<form method=\"post\" action=\"servlet\"> ");
            out.println("<input name=\"showMissile\" type=\"submit\" value=\"Show Missiles\" />");
            out.println("</td>");
            out.println("<td align=\"center\">  </td>");
            out.println("</tr>");
            if(request.getParameter("showMissile") != null){
            	try{
            		warGames.updateInAirMissiles();
            		List<Missile> inAirMissile = warGames.getInAirMissiles();
            		if(inAirMissile.isEmpty()){
            			out.println("<tr><td align=\"center\"> No Missile In Air</td></tr>");
            			failed = true;
            		}
            		
            		for(Missile m : inAirMissile){
            			out.println("<tr><td align=\"center\"> Missile ID:</td>");
            			out.println("<td>" +m.getId() +"</td></tr>");
            		}
            		
            	}catch(Exception e){
            		failed = true;
            		out.println("<tr><td align=\"center\"> Failed </td><td align=\"center\"> No Missile In Air</td></tr>");
            	}
            	if(!failed){
            		out.println("<tr><td align=\"center\">");
            		out.println("<form method=\"post\" action=\"servlet\"> ");
            		out.println("<input name=\"interceptMissile\" type=\"submit\" value=\"Intercept Missile\" />");
                    out.println("</td>");
                    out.println("<td> Enter Missile ID ");
                    out.println("<input type=\"text\" name=\"Missile_ID\" /></br></form></td></tr>");	
            	}
            }
            if(request.getParameter("interceptMissile") != null){
            	try{
            		missileID = Integer.parseInt(request.getParameter("Missile_ID"));
        			Missile selectedMissile = Missile.getMissileFromList(
        					warGames.getInAirMissiles(), "M" + missileID);
        			Destructor selectedDestractor = warGames.getMissileDestructors().get(0);
        			selectedDestractor.addDestructMissile(selectedMissile);
        			

            	}catch(Exception e){
            		failed = true;
            		out.println("<tr><td align=\"center\"> Missile: M"+missileID);
            		out.println("</td><td> No Longer In Air </td>");
            	}
            	if(!failed){
            		out.println("<tr><td align=\"center\"> Missile: M"+missileID);
            		out.println("</td><td> was set for interception </td>");
            	}
            }
            out.println("</table></td></tr>");
            
            out.println("<tr><td  align =\"center\">");
            out.println("<table style=\"width:100%\">");
            out.println("<tr><td> Choose Launcher to Destroy </td>");
            out.println("<td> Action State </td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td align=\"center\"> ");
            out.println("<form method=\"post\" action=\"servlet\"> ");
            out.println("<input name=\"showLaunchers\" type=\"submit\" value=\"Show Launchers\" />");
            out.println("</td>");
            out.println("<td align=\"center\">  </td>");
            out.println("</tr>");
            
            if(request.getParameter("showLaunchers") != null){
            	try{
            		int counter=0;
            		launchers = warGames.getMissileLaunchers();
            		for (Launcher launcher : launchers) {
            			if (!launcher.isHidden() && !launcher.isDestroyed()) {
            				out.println("<tr><td align=\"center\"> Launcher id: </td>" );
            				out.println("<td align=\"center\">"+ launcher.getLauncherId()+"</td></tr>");
            				counter++;
            			}
            		}
            		if(counter==0) failed = true;
            	}catch(Exception e){
            		failed = true;
            	}
            	if(!failed){
            		out.println("<tr><td align=\"center\">");
            		out.println("<form method=\"post\" action=\"servlet\">");
            		out.println("<input name=\"interceptLauncher\" type=\"submit\" value=\"Intercept Launcher\" />");
                    out.println("</td>");
                    out.println("<td> Enter Launcher ID ");
                    out.println("<input type=\"text\" name=\"Launcher_ID\" /></br></form></td></tr>");	
            	}else{
            		out.println("<tr><td align=\"center\"> No Launcher are Visable</td></tr>");
            	}
            }
            if(request.getParameter("interceptLauncher") != null){
            	try{
            		launcherID = Integer.parseInt(request.getParameter("Launcher_ID"));
            		warGames.getMissileLauncherDestructors().get(0).addDestructLauncher(launchers.get(launchers.indexOf(new Launcher("L"+launcherID))));
            	}catch(Exception e){
            		failed = true;
            		out.println("<tr><td align=\"center\"> Launcher: L"+launcherID);
            		out.println("</td><td> Failed </td>");
            	}
            	if(!failed){
            		out.println("<tr><td align=\"center\"> Launcher: L"+launcherID);
            		out.println("</td><td> was set for interception </td>");
            	}
            }
            out.println("</tbody></table></div>");
            out.println("</body></html>");
            failed = false;
		
            theSession.setAttribute("id", ++id);
            
		}finally{
			out.close();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
