package br.com.sumware.sslpoke;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import br.com.sumware.sslpoke.net.TSLSocketConnectionFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Establish a SSL connection to a host and port, writes a byte and prints the
 * response. See
 * http://confluence.atlassian.com/display/JIRA/Connecting+to+SSL+services
 */
public class SSLPoke {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: " + SSLPoke.class.getName() + " <host> <port> --bc");
			System.exit(1);
		}
		try {
			if (args[1].equals("--bc")) {
				SSLPoke m = new SSLPoke();
				System.out.println(m.usingBouncycastle(args[0]));
			} else {
				SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(args[0], Integer.parseInt(args[1]));

				InputStream in = sslsocket.getInputStream();
				OutputStream out = sslsocket.getOutputStream();

				// Write a test byte to get a reaction :)
				out.write(1);

				while (in.available() > 0) {
					System.out.print(in.read());
				}
				System.out.println("Successfully connected");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Daniel Pereira 
	 *
	 */
	public String usingBouncycastle(String fmtUrl) {

		String body = null;

		try {

			URL url = new URL(fmtUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setSSLSocketFactory(new TSLSocketConnectionFactory());

			StringBuilder sb = new StringBuilder();
			try {
				con.setRequestMethod("GET");
				con.setDoOutput(true);
				con.setInstanceFollowRedirects(false);

				int status = con.getResponseCode();
				if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) {
					String location = con.getHeaderField("Location");
					URL newUrl = new URL(location);
					con = (HttpsURLConnection) newUrl.openConnection();
				}

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine + "\n\r");
				}
			} finally {
				con.disconnect();
			}

			body = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return body;

	}

}