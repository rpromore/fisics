package com.sandbox.multiplayer;

import java.net.InetAddress;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;

public class Client extends Extasys.Network.TCP.Client.ExtasysTCPClient {

	public Client(String name, String description, InetAddress remoteHostIP,
			int remoteHostPort, int corePoolSize, int maximumPoolSize) {
		super(name, description, corePoolSize, maximumPoolSize);
		try {
			super.AddConnector(name, remoteHostIP, remoteHostPort, 10240,
					"#SPLITTER#");
		} catch (Exception ex) {
		}
	}

	@Override
	public void OnDataReceive(TCPConnector connector, DataFrame data) {
		// System.out.println("Data received: " + new String(data.getBytes()));
	}

	@Override
	public void OnConnect(TCPConnector connector) {
		System.out.println("Connected to server");
	}

	@Override
	public void OnDisconnect(TCPConnector connector) {
		System.out.println("Disconnected from server");
	}
}
