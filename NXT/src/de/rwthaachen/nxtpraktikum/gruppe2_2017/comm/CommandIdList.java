package de.rwthaachen.nxtpraktikum.gruppe2_2017.comm;

/**
 * @author Gregor & Justus
 * 
 * This class inherits type definitions for command id's.
 */

public final class CommandIdList {
	/*
	 * This command id list belongs to the protocol number 0.
	 */
	public final static byte 
	COMMAND_SET 				= 1,
	COMMAND_GET 				= 2,
	COMMAND_GET_RETURN 			= 3,
	COMMAND_MOVE 				= 4,
	COMMAND_TURN 				= 5,
	COMMAND_MOVE_TO 			= 6,
	COMMAND_BALANCING 			= 7,
	COMMAND_LOG_INFO 			= 8,
	COMMAND_ERROR_CODE 			= 9,
	COMMAND_DISCONNECT 			= 10,
	COMMAND_PROTOCOL_VERSION 	= 11;
}
