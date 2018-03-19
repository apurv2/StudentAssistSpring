
package com.studentAssist.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.studentAssist.entities.Users;
import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.exception.StudenAssitException;

@Component
public class FBGraph {
	private String accessToken;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getFBGraph() throws IOException {
		String graph = null;
		String g = "https://graph.facebook.com/me?access_token=" + accessToken + "&fields=first_name"
				+ ",last_name,id,email";

		URL u = new URL(g);
		URLConnection c = u.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String inputLine;
		StringBuffer b = new StringBuffer();

		while ((inputLine = in.readLine()) != null)
			b.append(inputLine + "\n");
		in.close();

		graph = b.toString();
		System.out.println(graph);

		return graph;
	}

	public Map<String, String> getGraphData(String fbGraph) {
		Map<String, String> fbProfile = new HashMap<String, String>();
		JSONObject json = new JSONObject(fbGraph);
		fbProfile.put("id", json.getString("id"));
		fbProfile.put("first_name", json.getString("first_name"));
		if (json.has("email"))
			fbProfile.put("email", json.getString("email"));
		if (json.has("gender"))
			fbProfile.put("gender", json.getString("gender"));
		return fbProfile;
	}

	public Users getUserDetails(String accessToken) throws InvalidTokenException {

		Users user = new Users();
		try {

			this.accessToken = accessToken;
			String flag = getFBGraph();
			JSONObject json = new JSONObject(flag);

			user.setFirstName(json.getString("first_name"));
			user.setUserId(Long.valueOf(json.getString("id")));
			user.setLastName(json.getString("last_name"));

			// optional paramaters
			if (json.has("email"))
				user.setEmail(json.getString("email"));

		} catch (IOException e) {
			throw new InvalidTokenException();
		}
		return user;

	}

}
