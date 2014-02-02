import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class GenerateScripts {
	
	/*
	 INSERT INTO script_type (script_type_id, name)
   VALUES (1, 'Browser');
INSERT INTO script_type (script_type_id, name)
   VALUES (2, 'Android Native');
INSERT INTO script_type (script_type_id, name)
   VALUES (3, 'iOS Native');
INSERT INTO script_type (script_type_id, name)
   VALUES (4, 'Chrome');
INSERT INTO script_type (script_type_id, name)
   VALUES (5, 'Firefox');
INSERT INTO script_type (script_type_id, name)
   VALUES (6, 'Internet Explorer');
	 */
	
	private static Node browser = new Node(1, "Browser");
	private static Node android = new Node(2, "Android Native");
	private static Node ios = new Node(3, "iOS Native");
	private static Node chrome = new Node(4, "Chrome");
	private static Node firefox = new Node(5, "Firefox");
	private static Node ie = new Node(6, "Internet Explorer");
	
	private static Node [] browserScriptTypes = { browser, chrome, firefox, ie };
	private static Node [] nativeScriptTypes = { android, ios };
	
	private static int smsMask = 4;

	public static void main(String[] args) throws NoSuchAlgorithmException {
		    int script_id = 1;
		    for (int tenantId = 1; tenantId <= 2; tenantId++) {
		    	for (Node scriptType : browserScriptTypes) {
		    		generateScript(script_id++, tenantId, scriptType, false);
		    	}
		    	for (Node scriptType : nativeScriptTypes) {
		    		generateScript(script_id++, tenantId, scriptType, false);
		    		generateScript(script_id++, tenantId, scriptType, true);
		    	}
	        }
	    	
	}
	
	private static void generateScript(int id, int tenantId, Node scriptType, boolean requiresSms) throws NoSuchAlgorithmException {
		int requiresF = 0;
		String name = scriptType.getName() + " Script";
		if (requiresSms) {
			name += " (Requires SMS)";
			requiresF = smsMask;
		}
		//String digest = generateDigest();
		//System.out.println("INSERT INTO script (script_id, script_type_id, tenant_id, name, digest, requires_f)");
		//System.out.printf("    VALUES (%d, %d, %d, '%s', '%s', %d);%n", id, scriptType.getId(), tenantId, name, digest, requiresF);
		System.out.println("INSERT INTO script (script_id, script_type_id, tenant_id, name, requires_f)");
		System.out.printf("    VALUES (%d, %d, %d, '%s', %d);%n", id, scriptType.getId(), tenantId, name, requiresF);
	}

	@SuppressWarnings("unused")
	private static String generateDigest () throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		byte[] dataBytes = new byte[1024];
		dataBytes = UUID.randomUUID().toString().getBytes();

		md.update(dataBytes, 0, dataBytes.length);
		byte[] mdbytes = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

}
