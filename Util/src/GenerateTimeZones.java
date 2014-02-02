import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;


public class GenerateTimeZones {
	
	public static void main(String[] args) {
		Set<String> timeZones = new TreeSet<>();
		for (String timeZone : TimeZone.getAvailableIDs()) {
			if (timeZone.startsWith("US/")) {
			  timeZones.add(timeZone);
			}
		}
		int id = 1;
		for (String timeZone : timeZones) {
			System.out.println("INSERT INTO timezone (timezone_id, name)");
			System.out.printf("    VALUES (%d, '%s');%n", id++, timeZone);
		}
	}

}
