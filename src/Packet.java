public class Packet {
	public int opcode;
	public int lenght;
	public int packetDirection;
	public int tickCount;
	public int cindex;
	public byte[] data;
	
	public Packet(int opcode, int lenght, int packetDirection, int tickCount, int cindex, byte[] data) {
		this.opcode = opcode;
		this.lenght = lenght;
		this.data = data;
		this.packetDirection = packetDirection;
		this.tickCount = tickCount;
		this.cindex = cindex;
	}
}
