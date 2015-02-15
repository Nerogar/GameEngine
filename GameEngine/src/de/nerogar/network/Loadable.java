package de.nerogar.network;

public interface Loadable {

	public void fromByteArray(byte[] data);

	public byte[] toByteArray();

}
