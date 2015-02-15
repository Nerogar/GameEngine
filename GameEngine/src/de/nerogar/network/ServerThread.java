package de.nerogar.network;

import java.io.IOException;
import java.net.*;
import java.util.*;

import de.nerogar.network.packets.Packet;
import de.nerogar.network.packets.PacketConnectionInfo;

public class ServerThread extends Thread {

	private ServerSocket socket;

	private List<Connection> connections = new ArrayList<>();
	private List<Connection> newConnections = new ArrayList<>();
	private List<Connection> pendingConnections = new ArrayList<>();

	public ServerThread(int port) throws BindException {
		try {
			socket = new ServerSocket(port);
		} catch (BindException e) {
			throw e;
		} catch (IOException e) {
			System.err.println("The server crashed brutally");
			e.printStackTrace();
		}
		this.start();
	}

	@Override
	public void run() {

		try {
			while (!isInterrupted()) {
				addPendingConnection(new Connection(socket.accept()));
			}
		} catch (SocketException e) {
			// System.err.println("SocketException in Server");
			// e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The server crashed brutally");
			e.printStackTrace();
		}

		stopThread();
		System.out.println("SHUTDOWN: Server - " + socket);

	}

	private void checkPendingConnections() {
		for (Iterator<Connection> iter = pendingConnections.iterator(); iter.hasNext();) {
			Connection conn = iter.next();
			if (conn.isClosed()) {
				iter.remove();
				continue;
			}

			ArrayList<Packet> connectionPackets = conn.get(0);

			// The only CONNECTION_INFO packet can be ConnectionInfo. If that's not the case, deal with the ClassCastException and fix it.
			// Also ignore any additional packets. Just the first ConnectionInfo packet gets processed
			if (connectionPackets.size() > 0) {
				PacketConnectionInfo packet = (PacketConnectionInfo) connectionPackets.get(0);
				if (packet.version == Packets.NETWORKING_VERSION) {
					addConnection(conn);
				} else {
					// Wrong Networking version
					conn.close();
				}
				iter.remove();
			}
		}
	}

	private void cleanupClosedConnections() {
		for (Iterator<Connection> iter = connections.iterator(); iter.hasNext();) {
			if (iter.next().isClosed()) {
				iter.remove();
			}
		}
		for (Iterator<Connection> iter = newConnections.iterator(); iter.hasNext();) {
			if (iter.next().isClosed()) {
				iter.remove();
			}
		}
	}

	private void addConnection(Connection conn) {
		connections.add(conn);
		newConnections.add(conn);
	}

	private void addPendingConnection(Connection conn) {
		conn.send(new PacketConnectionInfo(Packets.NETWORKING_VERSION));
		pendingConnections.add(conn);
	}

	public synchronized void broadcast(Packet packet) {
		for (Connection connection : connections) {
			connection.send(packet);
		}
	}

	public void stopThread() {
		for (Connection connection : connections) {
			connection.close();
		}
		interrupt();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Could not close Server-Socket");
				e.printStackTrace();
			}
		}
	}

	public List<Connection> getNewConnections() {
		cleanupClosedConnections();
		checkPendingConnections();
		List<Connection> connections = newConnections;
		newConnections = new ArrayList<>();
		return connections;
	}

	public int getPort() {
		return socket.getLocalPort();
	}

}
