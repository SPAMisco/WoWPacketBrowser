import java.math.BigInteger;

public class ObjectGUID {
	private BigInteger high;
	private BigInteger low;
	
	public ObjectGUID(BigInteger low, BigInteger high) {
		this.high = high;
		this.low = low;
	}
	
	public long getEntry() {
		return high.shiftRight(6).and(new BigInteger("8388607")).longValue();
	}
	
	public long getLow() {
		return low.and(new BigInteger("1099511627775")).longValue();
	}
	
	public BigInteger getFull() {
		return high.add(low);
	}
	
	public BigInteger getHigh() {
		return high;
	}
	
	public boolean isCreature() {
		BigInteger mask = new BigInteger("63");
		BigInteger type = high.shiftRight(58).and(mask);
		return type.compareTo(new BigInteger("7")) == 0;
	}
}
