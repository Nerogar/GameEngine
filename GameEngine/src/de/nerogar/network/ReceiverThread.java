package de.nerogar.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import de.nerogar.network.Packets.PacketContainer;
import de.nerogar.network.packets.Packet;

public class ReceiverThread extends Thread {

	private Socket socket;
	private SenderThread send;
	private ArrayList<Packet> packets = new ArrayList<Packet>();

	public ReceiverThread(Socket socket, SenderThread send) {
		setName("Reveiver Thread for " + socket.toString());
		this.socket = socket;
		this.send = send;
		this.setDaemon(true);
		this.start();
	}

	@Override
	public void run() {

		try {
			DataInputStream stream = new DataInputStream(socket.getInputStream());
			int packetId = 0;
			while (!isInterrupted() && packetId >= 0) {

				packetId = stream.readInt();

				PacketContainer packetContainer = Packets.byId(packetId);
				if (packetContainer == null) {
					System.out.println("received invalid packet id: " + packetId + ", ignored.");
				} else {
					int length = stream.readInt();

					ByteBuffer buffer = ByteBuffer.allocate(length);
					for (int i = 0; i < length; i++) {
						buffer.put(stream.readByte());
					}
					buffer.flip();

					Packet packet;
					packet = packetContainer.load(buffer.array());
					addPacket(packet);
				}

			}
		} catch (SocketException e) {
			// System.err.println("SocketException in ReceiverThread");
			// e.printStackTrace();
		} catch (IOException e) {
			// System.err.println("ReceiverThread crashed (maybe due to connection abort)");
			// e.printStackTrace();
		}

		send.interrupt();

	}

	private void addPacket(Packet packet) {
		synchronized (packets) {
			packets.add(packet);
		}
	}

	public ArrayList<Packet> getPackets() {
		return packets;
	}

}
