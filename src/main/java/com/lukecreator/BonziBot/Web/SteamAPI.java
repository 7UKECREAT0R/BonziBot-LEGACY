package com.lukecreator.BonziBot.Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lukecreator.BonziBot.Private;

// May add more to this class in the future, but for right now it only has resolveVanityURL.
public class SteamAPI {

	public SteamAPI() {}
	
	public long resolveVanityURL(String vanityURL) {
		// { "response": { "steamid": "00000000000000000", "success": 1 } }
		String apiURL = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key="
				+ Private.STEAMAPI_KEY + "&vanityurl=" + vanityURL;
		try {
			String response = GET(apiURL);

			Object rawJson = new JSONParser().parse(response);
			JSONObject obj = (JSONObject)rawJson;
			JSONObject internal = (JSONObject)obj.get("response");
			
			long success = (long)internal.get("success");
			if(success == 0l) {
				return 0l;
			}
			long id = Long.parseLong((String)internal.get("steamid"));
			return id;
		} catch (IOException e) {
			return 0l;
		} catch (ParseException e) {
			System.out.println("Odd: ParseException in SteamAPI#resolveVanityURL.");
			return 0l;
		}
	}
	String GET(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection)obj.openConnection();
		con.setRequestMethod("GET");
		int resp = con.getResponseCode();
		if (resp == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(
				new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} else {
			return null;
		}
	}
}
