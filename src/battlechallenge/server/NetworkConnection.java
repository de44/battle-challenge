package battlechallenge.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import battlechallenge.ConnectionLostException;
import battlechallenge.Coordinate;
import battlechallenge.Ship;

public class NetworkConnection {

	public static String SERVER_VERSION;
	public static Set<String> SUPPORTED_CLIENTS;
	public static final String SERVER_REQUEST_NAME;
	public static final String SERVER_REQUEST_PLACE_SHIPS;
	public static final String SERVER_REQUEST_TURN;
	public static final String SERVER_RESULT_DISQUALIFIED;
	public static final String SERVER_RESULT_WINNER;
	public static final String SERVER_RESULT_LOSER;
	
	static {
		SERVER_VERSION = "battleship_S-v0";
		SUPPORTED_CLIENTS = new HashSet<String>();
		SUPPORTED_CLIENTS.add("battleship_C-v0");
		SERVER_REQUEST_NAME = "N";
		SERVER_REQUEST_PLACE_SHIPS = "S";
		SERVER_REQUEST_TURN = "T";
		SERVER_RESULT_DISQUALIFIED = "D";
		SERVER_RESULT_WINNER = "W";
		SERVER_RESULT_LOSER= "L";
	}
	
	private Socket conn;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public NetworkConnection(Socket conn) {
		this.conn = conn;
		if (conn == null)
			throw new IllegalArgumentException("Socket cannot be null");
		try {
			oos = new ObjectOutputStream(conn.getOutputStream());
			ois = new ObjectInputStream(conn.getInputStream());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	public void kill() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (IOException e) { /* don't care if this messes up */}
			this.conn = null;
		}
	}
	
	public boolean setupHandshake() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(SERVER_VERSION);
			String client_version = (String)ois.readObject();
			if (client_version != null && SUPPORTED_CLIENTS.contains(client_version)) {
				return true;
			} else {
				// TODO: handle unsupported client version
				return false;
			}
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return false;
	}
	
	public String setPlayerCredentials() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(SERVER_VERSION.getBytes());
			String client_name = (String)ois.readObject();
			if (client_name != null && client_name.contains("Name:")) {
				return client_name.replace("Name:", "");
			} else {
				// TODO: handle unsupported client version
				return null;
			}
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Ship> placeShips(List<Ship> ships) throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(ships);
			@SuppressWarnings("unchecked")
			List<Ship> newShips = (List<Ship>)ois.readObject();
			if (newShips != null) {
				for (Ship s : newShips) {
					if (s == null) {
						// TODO: handle invalid input
					}
				}
				return newShips;
			} else {
				// TODO: handle null input from socket (I don't think this will happen)
			}
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean requestTurn() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			oos.writeObject(SERVER_REQUEST_TURN);
			return true;
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		}
		return false;
	}
	
	public Coordinate getTurn() throws ConnectionLostException {
		if (conn == null)
			throw new ConnectionLostException();
		try {
			// check to see if something is arrived in time. Otherwise, assume
			// no input
			if (ois.available() == 0)
				return null;
			Coordinate c = (Coordinate)ois.readObject();
			if (c != null) {
				return c;
			} else {
				// TODO: handle null input from socket (I don't think this will happen)
			}
		} catch (IOException e) {
			// TODO: cannot send server objects over the socket
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle server attack
			e.printStackTrace();
		} catch (ClassCastException e) {
			// Someone is trying to break our server or, our code is really broken.
			// TODO: handle invalid input
			e.printStackTrace();
		}
		return null;
	}
}
