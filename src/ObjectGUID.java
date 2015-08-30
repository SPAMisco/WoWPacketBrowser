import java.math.BigInteger;

public class ObjectGUID {
	private BigInteger high;
	private BigInteger low;
	
	/**
	 * Constructor
	 * @param BigInteger low
	 * @param BigInteger high
	 */
	public ObjectGUID(BigInteger low, BigInteger high) {
		this.high = high;
		this.low = low;
	}
	
	/**
	 * Get entry if creature
	 * @return Long entry
	 */
	public long getEntry() {
		return high.shiftRight(6).and(new BigInteger("8388607")).longValue();
	}
	
	/**
	 * Get low guid
	 * @return Long low guid
	 */
	public long getLow() {
		return low.and(new BigInteger("1099511627775")).longValue();
	}
	
	/**
	 * Get full guid
	 * @return BigInteger full guid
	 */
	public BigInteger getFull() {
		return high.add(low);
	}
	
	/**
	 * Get high guid
	 * @return BigInteger guid
	 */
	public BigInteger getHigh() {
		return high;
	}
	
	/**
	 * Check if is creature
	 * @return Boolean true if is creature
	 */
	public boolean isCreature() {
		BigInteger mask = new BigInteger("63");
		BigInteger type = high.shiftRight(58).and(mask);
		return type.compareTo(new BigInteger("7")) == 0;
	}
}
