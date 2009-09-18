/**
 * 
 */
package de.ub0r.android.adBlock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * @author flx
 */
public class Proxy extends Service implements Runnable {

	private Thread proxy = null;
	private int port = 8088;
	private boolean stop = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
		if (this.proxy == null) {
			Toast.makeText(this, "starting proxy..", Toast.LENGTH_LONG).show();
			this.proxy = new Thread(this);
			this.proxy.start();
		} else {
			Toast.makeText(this, "proxy running", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "stopping proxy..", Toast.LENGTH_LONG).show();
		this.stop = true;
	}

	@Override
	public void run() {
		try {
			ServerSocket sock = new ServerSocket(this.port);
			Socket client;
			while (!this.stop) {
				client = sock.accept();
				if (client != null) {
					Thread t = new Thread(new Connection(client, this));
					t.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}