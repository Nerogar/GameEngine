package de.nerogar.network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import de.nerogar.network.packets.Packet;

public class Connection {

	private Socket socket;
	private ReceiverThread recv;
	private SenderThread send;

	public Connection(Socket socket) {
		if (socket == null) {
			return;
		}
		this.socket = socket;
		this.send = new SenderThread(socket);
		this.recv = new ReceiverThread(socket, send);
	}

	public void send(Packet packet) {
		send.send(packet);
	}

	public ArrayList<Packet> get(int channelID) {
		ArrayList<Packet> packets = new ArrayList<Packet>();
		ArrayList<Packet> availablePackets = recv.getPackets();
		synchronized (availablePackets) {
			for (Iterator<Packet> iter = availablePackets.iterator(); iter.hasNext();) {
				Packet p = iter.next();
				if (Packets.byClass(p.getClass()).getChannel() == channelID) {
					packets.add(p);
					iter.remove();
				}
			}
		}
		return packets;
	}

	public synchronized void close() {
		// recv.stopThread();
		// send.stopThread();
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Could not close socket in Client.");
			e.printStackTrace();
		}
		System.out.println("SHUTDOWN: Connection - " + socket.toString());
	}

	public boolean isClosed() {
		if (socket == null) {
			return true;
		}
		return socket.isClosed();
	}

}
