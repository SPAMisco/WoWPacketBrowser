import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class PackerParser {
	public static List<Packet> packets = new ArrayList<Packet>();
	
	/**
	 * Parse sniff file
	 * @param File sniffFile
	 */
    public static void parseSniffFile(File sniffFile) {
    	Path file = sniffFile.toPath();
    	try {
			ByteArrayInputStream sniff = new ByteArrayInputStream(Files.readAllBytes(file));
			
			// Skip PKT 3 bytes
			sniff.skip(3); 

			// PKT version
			short ver = Readers.readUInt16(sniff);
			
			// Skip sniffer ID 1 byte
			sniff.skip(1);
			
			// Client buid
			ClientVersion.sniffFileVersion = Readers.readUInt32(sniff);
			
			// Client locale
			String locale = Readers.readString(sniff, 4);
			
			// Skip session encryption key 40 bytes
			sniff.skip(40);
			
			// Start time
			sniff.skip(4); //skip unsigned integer
			
			// Start tick count
			sniff.skip(4); // skip unsigned integer
			
			// Optional data size
			int optionalDataSize = Readers.readInt32(sniff);
			
			// Optional data
			sniff.skip(optionalDataSize);
			
			System.out.println("Packet Version: 0x" + Integer.toHexString(ver));
			System.out.println("Client buid: " + ClientVersion.sniffFileVersion);
			System.out.println("Client locale: " + locale);
			
			while (sniff.available() != 0) {
				int packetDirection = Readers.readUInt32(sniff);
				int cindex = Readers.readInt32(sniff); 
				int tickCount = Readers.readUInt32(sniff); 
				int addtionalSize = Readers.readInt32(sniff); 
				int packetLen = Readers.readInt32(sniff); 
				
				sniff.skip(addtionalSize);
				
				int opcode = Readers.readInt32(sniff); 
				
				byte[] b = new byte[packetLen - 4];
				sniff.read(b, 0, packetLen - 4);
				packets.add(new Packet(opcode, packetLen, packetDirection, tickCount, cindex, b));
			}
			
			Opcode.initOpcodeTables(ClientVersion.sniffFileVersion);
			
			for (Packet p : packets) {
				if (!Opcode.existOpcode(p.opcode))
					continue;
				
				switch (Opcode.getOpcode(p.opcode)) {
					case Opcode.SMSG_ON_MONSTER_MOVE:
						MovementPackets.parseSplineMovement(p);
						break;
					case Opcode.SMSG_UPDATE_OBJECT:
						UpdatePackets.parseUpdateObject(p);
						break;
					default: break;
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
