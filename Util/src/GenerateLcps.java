import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class GenerateLcps {
	
	private static Node nyc = new Node(1, "New York City");
	private static Node los = new Node(2, "Los Angeles");
	private static Node chi = new Node(3, "Chicago");
	private static Node bos = new Node(4, "Boston");
	private static Node det = new Node(5, "Detroit");
	
	private static Node [] locations = { nyc, los, chi, bos, det };
	
	private static Node sprint = new Node(1, "Sprint");
	private static Node verizon = new Node(2, "Verizon");
	private static Node att = new Node(3, "AT&T");
	private static Node l3 = new Node(4, "Level 3");
	
	private static Node [] carriers = { sprint, verizon, att, l3 };
	
	private static Node chrome = new Node(1, "Chrome");
	private static Node firefox = new Node(2, "Firefox");
	private static Node ie = new Node(3, "Internet Explorer");
	private static Node androidNative = new Node(4, "Android Native");
	private static Node iosNative = new Node(5, "iOS Native");
	
	private static Node [] players = { chrome, firefox, ie, iosNative, androidNative };
	
	
	private static Node browser = new Node(1, "Browser");
	private static Node nativeApp = new Node(2, "Native App");
	
	private static Map<Node, Node> playerGroups = new HashMap<>();
	
	private static int ipv4Mask = 1;
	private static int ipv6Mask = 2;
	private static int smsMask = 4;
	
	
	static {
		playerGroups.put(chrome, browser);
		playerGroups.put(firefox, browser);
		playerGroups.put(ie, browser);
		playerGroups.put(androidNative, nativeApp);
		playerGroups.put(iosNative, nativeApp);
	}
	
	
	public static void main(String[] args) {
		
		List<Lcp> lcps = generateLcps();
		System.out.println();

		generateVucs(lcps);
		
		
	}

	private static void generateVucs(List<Lcp> lcps) {
		Map<LcpGroup, List<Lcp>> lcpGroups = new HashMap<>();
		
		for (Lcp lcp : lcps) {
			LcpGroup lcpGroup = LcpGroup.create()
					.withLocation(lcp.getLocation())
					.withCarrier(lcp.getCarrier())
					.withAgentType(playerGroups.get(lcp.getPlayer()))
					.build();
			List<Lcp> lcpList = lcpGroups.get(lcpGroup);
			if (lcpList == null) {
				lcpList = new LinkedList<>();
				lcpGroups.put(lcpGroup, lcpList);
			}
			lcpList.add(lcp);
		}
		
		int vuc_id = 1;
		int vuc_lcp_id = 1;
		Map<Integer, Integer> vucsPerGroup = new TreeMap<>();
        Map<Integer, Integer> supportsFPerVuc = new TreeMap<>();
		for (Map.Entry<LcpGroup, List<Lcp>> entry : lcpGroups.entrySet()) {
			LcpGroup lcpGroup = entry.getKey();
			List<Lcp> lcpList = entry.getValue();
			int numVuc = randomInt(5);
			Integer vucCount = vucsPerGroup.get(numVuc);
			if (vucCount == null) {
				vucCount = 0;
			}
			vucsPerGroup.put(numVuc, vucCount + 1);
			for (int i = 0; i < numVuc; i++) {
				int supportsF = generateSupportsF(lcpGroup);
				Integer supportsFCount = supportsFPerVuc.get(supportsF);
				if (supportsFCount == null) {
					supportsFCount = 0;
				}
				supportsFPerVuc.put(supportsF, supportsFCount + 1);
				System.out.printf("# VUC for %s%n", lcpGroup.toString());
				System.out.println("INSERT INTO vu_controller (vuc_id, supports_f, location_id)");
				System.out.printf("    VALUES (%d, %d, %d);%n", vuc_id++, supportsF, lcpGroup.getLocation().getId());
				
				for (Lcp lcp : lcpList) {
					System.out.println("INSERT INTO vu_controller_lcp (vuc_lcp_id, vuc_id, lcp_id)");
					System.out.printf("    VALUES (%d, %d, %d);%n", vuc_lcp_id++, vuc_id, lcp.getId());
					
				}
				
			}
		}
		
		System.out.println();
		for (Map.Entry<Integer, Integer> entry : vucsPerGroup.entrySet()) {
			System.out.println(entry);
		}
		
		System.out.println();
		for (Map.Entry<Integer, Integer> entry : supportsFPerVuc.entrySet()) {
			System.out.println(entry);
		}
		
	}
	
	private static int generateSupportsF(LcpGroup lcpGroup) {
		boolean supportsIpv4 = false;
		boolean supportsIpv6 = false;
		boolean supportsSms = false;
		int result = 0;
		switch (randomInt(3)) {
		case 1:
			supportsIpv4 = true;
			break;
		case 2:
			supportsIpv6 = true;
			break;
		default:
			supportsIpv4 = true;
			supportsIpv6 = true;
		}
		if (lcpGroup.getAgentType() != browser && randomInt(2) == 2) {
			supportsSms = true;
		}
		
		if (supportsIpv4) {
			result |= ipv4Mask;
		}
		if (supportsIpv6) {
			result |= ipv6Mask;
		}
		if (supportsSms) {
			result |= smsMask;
		}
		return result;
	}

	private static int randomInt(int maximum) {
		return randomInt(1, maximum);
	}

	private static int randomInt(int minimum, int maximum) {
		return minimum + (int) (Math.random() * maximum);
	}

	private static List<Lcp> generateLcps() {
		List<Lcp> lcps = getLcps(locations, carriers, players);
		
		Set<Lcp> lcpFilters = new HashSet<>();
		lcpFilters.addAll(getLcps(new Node [] {bos}, new Node [] { sprint, verizon, att}, new Node [] {iosNative, androidNative}));
		lcpFilters.addAll(getLcps(new Node [] {det}, new Node [] { l3 }, new Node [] {chrome, firefox, ie}));
		
		List<Lcp> filteredLcps = new ArrayList<>();

		for (Lcp lcp : lcps) {
			if (lcp.getLocation() == det || lcp.getLocation() == bos) {
				if (lcpFilters.contains(lcp)) {
					filteredLcps.add(lcp);
				}
			} else {
				filteredLcps.add(lcp);
			}
		}
		
		List<Lcp> result = new ArrayList<>();
		int count = 1;
		for (Lcp lcp : filteredLcps) {
			lcp = Lcp.create(lcp)
					.withId(count++)
					.build();
			result.add(lcp);
			System.out.printf("# %s%n", lcp.toString());
			System.out.println("INSERT INTO lcp (lcp_id, location_id, carrier_id, player_id)");
			System.out.printf("    VALUES (%d, %d, %d, %d);%n", lcp.getId(), lcp.getLocation().getId(), lcp.getCarrier().getId(), lcp.getPlayer().getId());
		}
		
		return result;
	}

	private static List<Lcp> getLcps(Node[] locations, Node[] carriers,
			Node[] players) {
		List<Lcp> lcps = new ArrayList<>();
		for (Node location : locations) {
			for (Node carrier : carriers) {
				for (Node player : players) {
					Lcp lcp = Lcp.create()
							.withLocation(location)
							.withCarrier(carrier)
							.withPlayer(player)
							.build();
					lcps.add(lcp);
				}
			}
		}
		return lcps;
	}
	
	

}
