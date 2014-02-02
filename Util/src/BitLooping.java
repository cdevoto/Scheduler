import java.util.LinkedList;
import java.util.List;


public class BitLooping {

	public static void main(String[] args) {
		long bitMaskField = 45L;
		List<Long> masks = getMasks(bitMaskField);
		for (Long mask : masks) {
			System.out.println(Long.toBinaryString(mask));
		}
	}

	private static List<Long> getMasks(long bitMap) {
		String bitMaskString = Long.toBinaryString(bitMap);
		int length = bitMaskString.length();
		List<Long> masks = new LinkedList<>();
		for (int i = 0; i < length; i++) {
			long mask = 1 << i;
			if (mask == (bitMap & mask)) {
				masks.add(mask);
			}
		}
		return masks;
	}

}
