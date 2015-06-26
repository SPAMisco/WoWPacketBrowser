import java.io.ByteArrayInputStream;
import java.util.BitSet;

public class UpdatePackets {
	// SMSG_UPDATE_OBJECT
	public static void parseUpdateObject(Packet packet) {
		ByteArrayInputStream data = new ByteArrayInputStream(packet.data);
		
		int count = Readers.readUInt32(data);
		Readers.readUInt16(data); // map
		
		Readers.ResetBitReader();

		boolean HasDestroyObjects = Readers.ReadBit(data);
		if (HasDestroyObjects) {
			Readers.readUInt16(data); // Int0
            int DestroyObjectsCount = Readers.readUInt32(data);
            for (int i = 0; i < DestroyObjectsCount; i++)
            	Readers.readPackedGUID(data);
		}
		
		Readers.readUInt32(data);

        for (int i = 0; i < count; i++) {

        	byte type = Readers.readByte(data);
		
            switch (type) {
                case 0: {
                	ObjectGUID guid = Readers.readPackedGUID(data);
                	ReadValuesUpdateBlock(data, guid);
                  	break;
                }
                default: {
                	ObjectGUID guid = Readers.readPackedGUID(data);

                	Readers.readByte(data); // objType
                	
                	Readers.ResetBitReader();
                	
                	Readers.ReadBit(data);
                	Readers.ReadBit(data);
                	Readers.ReadBit(data);
                    Readers.ReadBit(data);

                    boolean hasMovementUpdate = Readers.ReadBit(data); // hasMovementUpdate
                    boolean hasMovementTransport = Readers.ReadBit(data); // hasMovementTransport
                    boolean hasStationaryPosition = Readers.ReadBit(data); // hasStationaryPosition
                    boolean hasCombatVictim = Readers.ReadBit(data); // hasCombatVictim
                    boolean hasServerTime = Readers.ReadBit(data); // hasServerTime
                    boolean hasVehicleCreate = Readers.ReadBit(data); // hasVehicleCreate
                    boolean hasAnimKitCreate = Readers.ReadBit(data); // hasAnimKitCreate
                    boolean hasRotation = Readers.ReadBit(data); // hasRotation
                    boolean hasAreaTrigger = Readers.ReadBit(data); // hasAreaTrigger
                    boolean hasGameObject = Readers.ReadBit(data); // hasGameObject

                    Readers.ReadBit(data); // ThisIsYou
                    Readers.ReadBit(data); // ReplaceActive

                    boolean sceneObjCreate = Readers.ReadBit(data); // sceneObjCreate
                    boolean scenePendingInstances =  Readers.ReadBit(data); // scenePendingInstances

                    int pauseTimesCount = Readers.readUInt32(data); // pauseTimesCount

                    if (hasMovementUpdate) // 392
                    {
                        ReadMovementStatusData(data);

                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        Readers.readFloat(data);
                        
                        int movementForceCount = Readers.readInt32(data);

                        for (int i1 = 0; i1 < movementForceCount; ++i1)
                        {
                        	Readers.readPackedGUID(data);
                        	Readers.readVector3(data);
                        	Readers.readInt32(data);
                        	Readers.readFloat(data);
                        	Readers.readByte(data);
                        }

                        Readers.ResetBitReader();

                        boolean HasSplineData = Readers.ReadBit(data);

                        if (HasSplineData)
                        {
                        	Readers.readInt32(data);
                        	G3DVector dest = Readers.readVector3(data);

                        	Readers.ResetBitReader();

                        	boolean hasMovementSplineMove = Readers.ReadBit(data);
                            if (hasMovementSplineMove)
                            {
                            	Readers.ResetBitReader();

                            	Readers.ReadBits(data, 25);
                                int face = Readers.ReadBits(data, 2);

                                boolean hasJumpGravity = Readers.ReadBit(data);
                                boolean hasSpecialTime = Readers.ReadBit(data);

                                Readers.ReadBits(data, 2);

                                boolean hasSplineFilterKey = Readers.ReadBit(data);

                                Readers.readUInt32(data);
                                Readers.readUInt32(data);

                                Readers.readFloat(data);
                                Readers.readFloat(data);

                                int pointsCount = Readers.readUInt32(data);

                                if (face == 3) // FaceDirection
                                	dest.o = Readers.readFloat(data);

                                if (face == 2) // FaceGUID
                                	Readers.readPackedGUID(data);

                                if (face == 1) // FaceSpot
                                	Readers.readVector3(data);

                                if (hasJumpGravity)
                                	Readers.readFloat(data);

                                if (hasSpecialTime)
                                	Readers.readInt32(data);

                                if (hasSplineFilterKey)
                                {
                                	int filterKeysCount = Readers.readUInt32(data);
                                    for (int i11 = 0; i11 < filterKeysCount; ++i11)
                                    {
                                    	Readers.readFloat(data);
                                    	Readers.readFloat(data);
                                    }

                                    Readers.ReadBits(data, 2);
                                }
                                
                                for (int j = 0; j < pointsCount; ++j) {
                                	G3DVector vec = Readers.readVector3(data);
                                	MovementPackets.addWaypoint(guid, vec, 8);
                                }

                                MovementPackets.addWaypoint(guid, dest, 16);
                            }
                        }
                    }

                    if (hasMovementTransport) // 456
                    {
                        Readers.readPackedGUID(data); // TransportGuid
                        
                        Readers.readVector3(data); // transportPos
                        Readers.readFloat(data); // rotation
                        
                        Readers.readByte(data); // seat
                        Readers.readUInt32(data); // Move Time

                        Readers.ResetBitReader();

                        boolean hasPrevMoveTime = Readers.ReadBit(data);
                        boolean hasVehicleRecID = Readers.ReadBit(data);

                        if (hasPrevMoveTime)
                        	Readers.readUInt32(data); // PrevMoveTime

                        if (hasVehicleRecID)
                        	Readers.readInt32(data); // VehicleRecID (Vehicle ID)
                    }

                    if (hasStationaryPosition) // 480
                    {
                        G3DVector Position = Readers.readVector3(data);
                        Position.o = Readers.readFloat(data);
                    }

                    if (hasCombatVictim) // 504
                    	Readers.readPackedGUID(data);

                    if (hasServerTime) // 516
                    	Readers.readInt32(data); // Server time

                    if (hasVehicleCreate) // 528 
                    {
                    	Readers.readUInt32(data); // Vehicle ID 
                    	Readers.readFloat(data); // InitialRawFacing
                    }

                    if (hasAnimKitCreate) // 538
                    {
                    	Readers.readUInt16(data); // AiID
                    	Readers.readUInt16(data); // MovementID
                    	Readers.readUInt16(data); // MeleeID
                    }

                    if (hasRotation) // 552
                    	data.skip(8); // GameObject Rotation

                    if (hasAreaTrigger) // 772
                    {
                        // CliAreaTrigger
                    	Readers.readInt32(data); // ElapsedMs

                    	Readers.readVector3(data); // RollPitchYaw1

                    	Readers.ResetBitReader();

                    	Readers.ReadBit(data); // HasAbsoluteOrientation
                    	Readers.ReadBit(data); // HasDynamicShape
                    	Readers.ReadBit(data); // HasAttached
                    	Readers.ReadBit(data); // HasFaceMovementDir
                    	Readers.ReadBit(data); // HasFollowsTerrain

                        boolean hasTargetRollPitchYaw = Readers.ReadBit(data);
                        boolean hasScaleCurveID = Readers.ReadBit(data);
                        boolean hasMorphCurveID = Readers.ReadBit(data);
                        boolean hasFacingCurveID = Readers.ReadBit(data);
                        boolean hasMoveCurveID = Readers.ReadBit(data);
                        boolean hasAreaTriggerSphere = Readers.ReadBit(data);
                        boolean hasAreaTriggerBox = Readers.ReadBit(data);
                        boolean hasAreaTriggerPolygon = Readers.ReadBit(data);
                        boolean hasAreaTriggerCylinder = Readers.ReadBit(data);
                        boolean hasAreaTriggerSpline = Readers.ReadBit(data);

                        if (hasTargetRollPitchYaw)
                        	Readers.readVector3(data); // TargetRollPitchYaw

                        if (hasScaleCurveID)
                        	Readers.readInt32(data);

                        if (hasMorphCurveID)
                        	Readers.readInt32(data);

                        if (hasFacingCurveID)
                        	Readers.readInt32(data);

                        if (hasMoveCurveID)
                        	Readers.readInt32(data);

                        if (hasAreaTriggerSphere) {
                        	Readers.readFloat(data);
                        	Readers.readFloat(data);
                        }

                        if (hasAreaTriggerBox) {
                        	Readers.readVector3(data);
                            Readers.readVector3(data);
                        }

                        if (hasAreaTriggerPolygon) {
                            int verticesCount = Readers.readInt32(data);
                            int verticesTargetCount = Readers.readInt32(data);
                            Readers.readFloat(data); // Height
                            Readers.readFloat(data); // HeightTarget

                            for (int j = 0; j < verticesCount; ++j) {
                            	// Vertices
                            	Readers.readFloat(data);
                            	Readers.readFloat(data);
                            }

                            for (int j = 0; j < verticesTargetCount; ++j) {
                            	// VerticesTarget
                            	Readers.readFloat(data);
                            	Readers.readFloat(data);
                            }
                        }

                        if (hasAreaTriggerCylinder)
                        {
                        	Readers.readFloat(data); // Radius
                        	Readers.readFloat(data); // RadiusTarget
                        	Readers.readFloat(data); // Height
                        	Readers.readFloat(data); // HeightTarget
                        	Readers.readFloat(data); // Float4
                        	Readers.readFloat(data); // Float5
                        }

                        if (hasAreaTriggerSpline) {
                        	Readers.readInt32(data); // TimeToTarget
                        	Readers.readInt32(data); // ElapsedTimeForMovement
                            int int8 = Readers.readInt32(data); // VerticesCount

                            for (int j = 0; j < int8; ++j)
                            	Readers.readVector3(data); // Points
                        }
                    }

                    if (hasGameObject) {
                    	Readers.readInt32(data);

                    	Readers.ResetBitReader();

                        boolean bit8 = Readers.ReadBit(data);
                        if (bit8)
                        	Readers.readInt32(data);
                    }

                    if (sceneObjCreate) {
                    	Readers.ResetBitReader();

                    	boolean hasSceneLocalScriptData = Readers.ReadBit(data);
                    	boolean petBattleFullUpdate = Readers.ReadBit(data);

                        if (hasSceneLocalScriptData)
                        {
                        	Readers.ResetBitReader();
                            int dataLength = Readers.ReadBits(data, 7);
                            data.skip(dataLength); // LUA script
                        }

                        if (petBattleFullUpdate)
                            BattlePetPackets.ReadPetBattleFullUpdate(data);
                    }

                    if (scenePendingInstances) {
                        int sceneInstanceIDs = Readers.readInt32(data);

                        for (int j = 0; j < sceneInstanceIDs; ++j)
                        	Readers.readInt32(data); // SceneInstanceIDs
                    }

                    for (int j = 0; j < pauseTimesCount; ++j)
                    	Readers.readInt32(data); // PauseTimes

                    ReadValuesUpdateBlock(data, guid);
                    break;  
                }
            }
        }
	}
	
	public static void ReadMovementStatusData(ByteArrayInputStream data) {
        Readers.readPackedGUID(data);

        Readers.readUInt32(data);
        
        Readers.readVector3(data); // Position
        Readers.readFloat(data);

        Readers.readFloat(data);
        Readers.readFloat(data);

        int int152 = Readers.readInt32(data);
        Readers.readInt32(data);

        for (int i = 0; i < int152; i++)
        	Readers.readPackedGUID(data);

        Readers.ResetBitReader();

        Readers.ReadBits(data, 30); // Movement flags
        Readers.ReadBits(data, 15); // Movement flags extra

        boolean hasTransport = Readers.ReadBit(data);
        boolean hasFall = Readers.ReadBit(data);
        Readers.ReadBit(data); // Has spline
        Readers.ReadBit(data);
        Readers.ReadBit(data);

        if (hasTransport)
        {
        	Readers.readPackedGUID(data);
        	
        	Readers.readVector3(data);
        	Readers.readFloat(data);
        	
        	Readers.readByte(data);
        	Readers.readInt32(data);

        	Readers.ResetBitReader();

        	boolean hasPrevMoveTime = Readers.ReadBit(data);
        	boolean hasVehicleRecID = Readers.ReadBit(data);

            if (hasPrevMoveTime)
            	Readers.readUInt32(data);

            if (hasVehicleRecID)
            	Readers.readUInt32(data);
        }

        if (hasFall)
        {
        	Readers.readUInt32(data);
        	Readers.readFloat(data);

        	Readers.ResetBitReader();
            boolean bit20 = Readers.ReadBit(data);
            if (bit20)
            {
            	Readers.readFloat(data);
            	Readers.readFloat(data);
            	Readers.readFloat(data);
            }
        }
    }
	
	public static void ReadValuesUpdateBlock(ByteArrayInputStream data, ObjectGUID guid) {
        byte maskSize = Readers.readByte(data);
        int[] updateMask = new int[maskSize];
        
        for (int i = 0; i < maskSize; i++)
            updateMask[i] = Readers.readInt32(data);
        
        BitSet bits = new BitSet();
        
        int bitIndex = 0;
        for (int i = 0; i < maskSize; i++) {
        	for (char c : Integer.toBinaryString(updateMask[i]).toCharArray()) {
        		bits.set(bitIndex, c == '1');
        		bitIndex++;
        	}
        }

        for (int i = 0; i < bitIndex; ++i) {
        	if (!bits.get(i))
                continue;
        	
        	Readers.readUInt32(data); // updateVal
        }
        
        maskSize = Readers.readByte(data);
        
        updateMask = new int[maskSize];
        for (int i = 0; i < maskSize; i++)
            updateMask[i] = Readers.readInt32(data);
        
        bits = new BitSet();
        
        bitIndex = 0;
        for (int i = 0; i < maskSize; i++) {
        	for (char c : Integer.toBinaryString(updateMask[i]).toCharArray()) {
        		bits.set(bitIndex, c == '1');
        		bitIndex++;
        	}
        }

        for (int i = 0; i < bitIndex; ++i) {
        	if (!bits.get(i))
                continue;
        	
            byte flag = Readers.readByte(data);

            if ((flag & 0x80) != 0)
            	Readers.readUInt16(data);

            int cnt = flag & 0x7F;
            int[] vals = new int[cnt];
            for (int j = 0; j < cnt; ++j)
                vals[j] = Readers.readInt32(data);
            
            BitSet _bits = new BitSet();
            
            int _bitIndex = 0;
            for (int k = 0; k < cnt; k++) {
            	for (char c : Integer.toBinaryString(vals[k]).toCharArray()) {
            		_bits.set(_bitIndex, c == '1');
            		_bitIndex++;
            	}
            }
            
            for (int k = 0; k < _bitIndex; ++k) {
            	if (!_bits.get(k))
                    continue;
            	
            	Readers.readUInt32(data); // updateValDyn
            }
        }
	}
}
