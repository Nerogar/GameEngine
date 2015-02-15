package de.nerogar.network.packets;

import de.nerogar.network.Loadable;

public abstract class Packet implements Loadable {

	public abstract void fromByteArray(byte[] data);

	public abstract byte[] toByteArray();

}
