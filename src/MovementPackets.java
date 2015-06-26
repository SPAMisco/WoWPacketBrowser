import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovementPackets {
	public static HashMap<BigInteger, List<G3DVector>> waypoints = new HashMap<BigInteger, List<G3DVector>>();
	public static HashMap<Long, List<BigInteger>> guids = new HashMap<Long, List<BigInteger>>();
	
	// SMSG_ON_MONSTER_MOVE
	public static void parseSplineMovement(Packet packet) {
		ByteArrayInputStream data = new ByteArrayInputStream(packet.data);
		
		ObjectGUID guid = Readers.readPackedGUID(data); // guid
		
		if (!guid.isCreature())
			return;
		
		G3DVector pos = Readers.readVector3(data); // position
		Readers.readUInt32(data); // spline id
		Readers.readVector3(data); // destination
		Readers.readInt32(data); // flags
		Readers.readByte(data); // AnimTier
		
		Readers.readUInt32(data); // TierTransStartTime
		Readers.readInt32(data); // Elapsed
        Readers.readUInt32(data); // MoveTime
        Readers.readFloat(data); // JumpGravity
        Readers.readUInt32(data); // SpecialTime
        
        int pointsCount = Readers.readInt32(data); // PointsCount

        Readers.readByte(data); // Mode
        Readers.readByte(data); // VehicleExitVoluntary

        Readers.readPackedGUID(data); // TransportGUID
        Readers.readByte(data); // VehicleSeat

        int packedDeltasCount = Readers.readInt32(data); // PackedDeltasCount
        
        addWaypoint(guid, pos, 4);
		
        G3DVector endPos = null;
        for (int i = 0; i < pointsCount; i++) {
            G3DVector spot = Readers.readVector3(data);
            
            if (i == 0)
            	endPos = spot;
            
            addWaypoint(guid, spot, 1);
        }
        
        for (int i = 0; i < packedDeltasCount; i++) {
            G3DVector mid = new G3DVector((pos.x + endPos.x) * 0.5f, (pos.y + endPos.y) * 0.5f, (pos.z + endPos.z) * 0.5f, 0.0f);
        	G3DVector packedDelta = Readers.readPackedVector3(data);
        	packedDelta.x = mid.x - packedDelta.x;
        	packedDelta.y = mid.y - packedDelta.y;
        	packedDelta.z = mid.z - packedDelta.z;
        	
        	addWaypoint(guid, packedDelta, 2);
        }
        
        Readers.ResetBitReader();

        int face = Readers.ReadBits(data, 2);
        Readers.ReadBit(data);
        
        switch (face) {
        	case 1:
        		break;
        	case 2:
        		pos.o =  Readers.readFloat(data); // FaceDirection
        		break;
        	case 3:
        		pos.o =  Readers.readFloat(data); // FaceDirection
        		break;
        	default: break;
        }
	}
	
	public static void addWaypoint(ObjectGUID guid, G3DVector pos, int type) {
		pos.type = type;
        if (waypoints.containsKey(guid.getFull())) {
        	waypoints.get(guid.getFull()).add(pos);
        } else {
        	List<G3DVector> l = new ArrayList<G3DVector>();
        	l.add(pos);
        	waypoints.put(guid.getFull(), l);
        }
        
        if (guids.containsKey(guid.getEntry())) {
        	if (!guids.get(guid.getEntry()).contains(guid.getFull()))
        		guids.get(guid.getEntry()).add(guid.getFull());
        } else {
        	List<BigInteger> l = new ArrayList<BigInteger>();
        	l.add(guid.getFull());
        	guids.put(guid.getEntry(), l);
        }
	}
}






















