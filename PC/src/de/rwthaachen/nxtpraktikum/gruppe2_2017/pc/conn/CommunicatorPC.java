package de.rwthaachen.nxtpraktikum.gruppe2_2017.pc.conn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import de.rwthaachen.nxtpraktikum.gruppe2_2017.comm.AbstractCommunicator;
import lejos.pc.comm.NXTConnector;

public final class CommunicatorPC extends AbstractCommunicator
{
	private final NXTConnector link = new NXTConnector();
	private boolean connected;

	public CommunicatorPC() {
		// TODO Register handler
	}

	@Override
	public void connect() {
		if (!isConnected())
			if (link.connectTo()) {
				dataOut = new DataOutputStream(link.getOutputStream());
				dataIn = new DataInputStream(link.getInputStream());
				connected = true;
				System.out.println("NXT is connected");
			} else
				System.out.println("No NXT found");
	}

	public void sendSet(short param, double value) throws IOException {
		dataOut.writeByte(PACKET_ID_SET);
		dataOut.writeShort(param);
		dataOut.writeDouble(value);
		dataOut.flush();
	}

	@Override
	public void disconnect() {
		if (isConnected()) {
			try {
				link.close();
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
			connected = false;
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}
}
