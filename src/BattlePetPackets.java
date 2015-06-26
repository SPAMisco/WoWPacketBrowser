import java.io.ByteArrayInputStream;


public class BattlePetPackets {
	public static void ReadPetBattleFullUpdate(ByteArrayInputStream data) {
        for (int i = 0; i < 2; ++i)
            ReadPetBattlePlayerUpdate(data);

        for (int i = 0; i < 3; ++i)
            ReadPetBattleEnviroUpdate(data);

        Readers.readUInt16(data); // WaitingForFrontPetsMaxSecs
        Readers.readUInt16(data); // PvpMaxRoundTime

        Readers.readUInt32(data); // CurRound
        Readers.readUInt32(data); // NpcCreatureID
        Readers.readUInt32(data); // NpcDisplayID

        Readers.readByte(data); // CurPetBattleState
        Readers.readByte(data); // ForfeitPenalty

        Readers.readPackedGUID(data); // InitialWildPetGUID

        Readers.ReadBit(data); // IsPVP
        Readers.ReadBit(data); // CanAwardXP
	}

	public static void ReadPetBattleEnviroUpdate(ByteArrayInputStream data) {
        int aurasCount = Readers.readInt32(data);
        int statesCount = Readers.readInt32(data);

        for (int i = 0; i < aurasCount; ++i) {
        	Readers.readInt32(data); // AbilityID
            Readers.readInt32(data); // InstanceID
            Readers.readInt32(data); // RoundsRemaining
            Readers.readInt32(data); // CurrentRound
            Readers.readByte(data); // CasterPBOID
        }

        for (int i = 0; i < statesCount; ++i) {
        	Readers.readInt32(data); // StateID
        	Readers.readInt32(data); // StateValue
        }
	}

	public static void ReadPetBattlePlayerUpdate(ByteArrayInputStream data) {
		Readers.readPackedGUID(data); // CharacterID

		Readers.readInt32(data); // TrapAbilityID
		Readers.readInt32(data); // TrapStatus

		Readers.readUInt16(data); // RoundTimeSecs

		Readers.readByte(data); // FrontPet
		Readers.readByte(data); // InputFlags

		Readers.ResetBitReader();

        int petsCount = Readers.ReadBits(data, 2); // PetsCount

        for (int i = 0; i < petsCount; ++i)
            ReadPetBattlePetUpdate(data);
	}

	public static void ReadPetBattlePetUpdate(ByteArrayInputStream data) {
		Readers.readPackedGUID(data); // BattlePetGUID

		Readers.readInt32(data); // SpeciesID
		Readers.readInt32(data); // DisplayID
		Readers.readInt32(data); // CollarID

		Readers.readUInt16(data); // Level
		Readers.readUInt16(data); // Xp

		Readers.readInt32(data); // CurHealth
		Readers.readInt32(data); // MaxHealth
		Readers.readInt32(data); // Power
        Readers.readInt32(data); // Speed
        Readers.readInt32(data); // NpcTeamMemberID

        Readers.readUInt16(data); // BreedQuality
        Readers.readUInt16(data); // StatusFlags

        Readers.readByte(data); // Slot

        int abilitiesCount = Readers.readInt32(data);
        int aurasCount = Readers.readInt32(data);
        int statesCount = Readers.readInt32(data);

        for (int i = 0; i < abilitiesCount; ++i) {
        	Readers.readInt32(data); // AbilityID
        	Readers.readUInt16(data); // CooldownRemaining
        	Readers.readUInt16(data); // LockdownRemaining 
        	Readers.readByte(data); // AbilityIndex
        	Readers.readByte(data); // Pboid
        }

        for (int i = 0; i < aurasCount; ++i) {
        	Readers.readInt32(data); // AbilityID
            Readers.readInt32(data); // InstanceID
            Readers.readInt32(data); // RoundsRemaining
            Readers.readInt32(data); // CurrentRound
            Readers.readByte(data); // CasterPBOID
        }

        for (int i = 0; i < statesCount; ++i) {
        	Readers.readInt32(data); // StateID
        	Readers.readInt32(data); // StateValue
        }

        Readers.ResetBitReader();

        int bits57 = Readers.ReadBits(data, 7);
        data.skip(bits57); // Pet Name
	}
}
