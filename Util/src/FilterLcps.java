import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/*
 SELECT DISTINCT * FROM  
  (SELECT
       lcp.lcp_id,
       CONCAT_WS(':', location.name, carrier.name, player.name) AS lcp_name
   FROM 
       lcp, player, location, carrier, script_type_player, script_type, script, vu_controller_lcp, vu_controller
   WHERE
       script.script_id = script_id AND
       script.script_type_id = script_type.script_type_id AND
       script_type.script_type_id = script_type_player.script_type_id AND
       script_type_player.player_id = player.player_id AND
       player.player_id = lcp.player_id AND
       vu_controller_lcp.lcp_id = lcp.lcp_id AND
       vu_controller_lcp.vuc_id = vu_controller.vuc_id AND
       vu_controller.supports_f & (requires_f | script.requires_f) = (requires_f | script.requires_f) AND
       lcp.location_id = location.location_id AND
       lcp.carrier_id = carrier.carrier_id
    ORDER BY lcp_name ASC, lcp.lcp_id ASC) AS filtered_lcp; 

 */

public class FilterLcps {
	  /*
	   To validate the resuls of the stored proc execute the following query using phpmyadmin:
	   
	   SELECT vu_controller.vuc_id, vu_controller.supports_f, CONCAT_WS(':', location.name, carrier.name, player.name) AS lcp_name 
	            		FROM vu_controller, vu_controller_lcp, lcp, location, carrier, player 
	            		WHERE vu_controller.vuc_id = vu_controller_lcp.vuc_id AND 
	            		vu_controller_lcp.lcp_id = lcp.lcp_id AND 
	            		lcp.location_id = location.location_id AND
	            		lcp.carrier_id = carrier.carrier_id AND
	            		lcp.player_id = player.player_id AND
                                vu_controller.supports_f & 0 = 0 AND
                                player.player_id IN (1, 2, 3)
	            		ORDER BY lcp_name ASC, vu_controller.supports_f ASC
                                LIMIT 0, 100
	   */
	
	   public static void main(String args[]){
	        try{
		        String userName = "root";
		        String password = "claudia2";

		        String url = "jdbc:mysql://localhost:3306/apmng";

		        Class.forName("com.mysql.jdbc.Driver");
		        Connection conn = DriverManager.getConnection(url, userName, password);

		        executeStoredProc(conn, 2, 1);
	            
	        }
	        catch(Exception e){
	            System.out.println("Encountered "+e);

	        }
	    }

	    public static void executeStoredProc(Connection conn, int scriptId, int requiresF) throws SQLException, ClassNotFoundException {

	        CallableStatement calStatement = null;
	        ResultSet rs = null;

			try {
				calStatement = conn.prepareCall("{ call filter_lcps( ?, ? )}");
				calStatement.setEscapeProcessing(true);
	
				calStatement.setInt("script_id", scriptId);
				calStatement.setInt("requires_f", requiresF);
	
				calStatement.execute();
				boolean results = calStatement.execute();
	
				// Loop through the available result sets.
				while (results) {
	
					rs = calStatement.getResultSet();
	
					while (rs.next()) {
						long lcpId = rs.getLong("lcp_id");
						String lcpName = rs.getString("lcp_name");
						System.out.printf("[%d, %s]%n", lcpId, lcpName);
					}
					results = calStatement.getMoreResults();
				}
				calStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }

}
