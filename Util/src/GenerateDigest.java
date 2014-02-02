import java.security.MessageDigest;
import java.util.UUID;


public class GenerateDigest {

	public static void main(String[] args) throws Exception {
		 MessageDigest md = MessageDigest.getInstance("SHA-256");
	 
	        byte[] dataBytes = new byte[1024];
	        dataBytes = UUID.randomUUID().toString().getBytes();
	 
	        md.update(dataBytes, 0, dataBytes.length);
	        byte[] mdbytes = md.digest();
	 
	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        System.out.println("Hex format : " + sb.toString());
            System.out.println(sb.length());	 
	        
	}

}
