package com.sandbox.multiplayer;

import java.net.InetAddress;

import Extasys.DataFrame;
import Extasys.Network.TCP.Server.Listener.TCPClientConnection;

public class Server extends Extasys.Network.TCP.Server.ExtasysTCPServer {
	public Server(String name, String description, InetAddress listenerIP,
			int port, int maxConnections, int connectionsTimeOut,
			int corePoolSize, int maximumPoolSize) {
		super(name, description, corePoolSize, maximumPoolSize);
		try {
			// Add listener with message collector.
			this.AddListener("My listener", listenerIP, 5000, maxConnections,
					65535, connectionsTimeOut, 100, "#SPLITTER#");
		} catch (Exception ex) {
		}
	}

	@Override
	public void OnDataReceive(TCPClientConnection sender, DataFrame data) {
		/*
		 * byte[] reply = new byte[data.getLength() + 1];
		 * System.arraycopy(data.getBytes(), 0, reply, 0, data.getLength());
		 * reply[data.getLength()] = ((char) 2);
		 * 
		 * this.ReplyToAll(reply, 0, reply.length);
		 */

		this.ReplyToAll(new String(data.getBytes()) + "#SPLITTER#");
	}

	@Override
	public void OnClientConnect(TCPClientConnection client) {
		// New client connected.
		client.setName(client.getIPAddress()); // Set a name for this client if
												// you want to.
		System.out.println(client.getIPAddress() + " connected.");
		System.out.println("Total clients connected: "
				+ super.getCurrentConnectionsNumber());
	}

	@Override
	public void OnClientDisconnect(TCPClientConnection client) {
		// Client disconnected.
		System.out.println(client.getIPAddress() + " disconnected.");
	}
}
