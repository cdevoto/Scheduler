package com.compuware.ruxit.synthetic.termination;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.compuware.ruxit.synthetic.engine.LifecycleAdapter;

public class SocketSignalTerminationSensor extends LifecycleAdapter implements TerminationSensor {
	private static final Logger log = LoggerFactory.getLogger(SocketSignalTerminationSensor.class);

	private static final String NAME = "Termination Sensor";
	private static final int [] TERMINATION_CODE = new int [] {9, 18, 2, 0, 0, 9};
	
	private int port;
	private int [] terminationCode;
	private ServerSocket server;
	private Thread monitorThread;
	private ExecutorService inputProcessingService;
	private volatile boolean terminated;

	private Set<TerminationListener> listeners = new HashSet<TerminationListener>();

	public SocketSignalTerminationSensor (int port) {
		this.port = port;
		this.terminationCode = TERMINATION_CODE;
	}
	
	@Override
	public String getName () {
		return NAME;
	}

	@Override
	public synchronized void addTerminationListener(TerminationListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public synchronized void removeTerminationListener(TerminationListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public int [] getTerminationCode() {
		return Arrays.copyOf(this.terminationCode, this.terminationCode.length);
	}

	@Override
	public void start() throws Exception {
		this.inputProcessingService = Executors.newCachedThreadPool();
		server = new ServerSocket(port);
		server.setSoTimeout(30000);
		log.info(String.format("The %s is listening on port %d.", NAME, port));
		monitorThread = new Thread(new MonitorTask(), "Termination-Sensor-Thread");
		monitorThread.setDaemon(true);
		monitorThread.start();
	}
	
	@Override
	public void stop() {
		this.terminated = true;
		this.inputProcessingService.shutdown();
		notifyListeners();
	}

	@Override
	public boolean isTerminated() {
		return terminated;
	}

	private synchronized void notifyListeners () {
		for (TerminationListener listener : listeners) {
			try {
			    listener.stop();
			} catch (Exception ex) {
				log.error("An error occurred during termination", ex);
			}
		}
	}
	
	private final class MonitorTask implements Runnable {

		@Override
		public void run() {
			try {
				while (!isTerminated()) {
					try {
						Socket socket = server.accept();
						InputStream in = socket.getInputStream();
						inputProcessingService.submit(new InputProcessorTask(in));
					} catch (SocketTimeoutException ex) {
						// No connection received in 30 seconds, which is just fine; just keep looping.
					} catch (IOException ex) {
						log.error(String.format("A problem occurred within the %s.", NAME), ex);
						stop();
					}
				}
			} finally {
				try {
					server.close();
				} catch (IOException e) {}
			}
			
		}
		
	}
	
	private final class InputProcessorTask implements Runnable {
		private InputStream in;
		
		public InputProcessorTask (InputStream in) {
			this.in = in;
		}

		@Override
		public void run() {
			try {
				boolean validTerminationCode = true;
				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < terminationCode.length; i++) {
				     int code = in.read();
				     buf.append(String.valueOf(code));
				     if (code != terminationCode[i]) {
				    	 validTerminationCode = false;
				    	 break;
				     }
				}
				if (validTerminationCode) {
					log.warn(String.format("The %s received a valid termination sequence.", NAME));
					stop();
				} else {
					log.warn(String.format("The %s received an invalid termination sequence: %s", NAME, buf.toString()));
				}
			} catch (SocketException ex) {
				// A different thread closed the socket
			} catch (IOException io) {
				log.error(String.format("A problem occurred within the %s.", NAME), io);
			} finally {
				try {
					in.close();
				} catch (IOException e) {} 
			}
		}
		
	}

}
