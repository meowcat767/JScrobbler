package site.meowcat.jscrobbler.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import site.meowcat.jscrobbler.sec.Secrets;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

public class LastFmService {
    private static final String API_ROOT = "https://ws.audioscrobbler.com/2.0/";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getToken() throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("method", "auth.getToken");
        params.put("api_key", Secrets.API_KEY);
        
        String signature = generateSignature(params);
        params.put("api_sig", signature);
        params.put("format", "json");

        String url = API_ROOT + "?" + encodeParams(params);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        return json.get("token").getAsString();
    }

    public static String getSessionKey(String token) throws Exception {
        Map<String, String> params = new TreeMap<>();
        params.put("method", "auth.getSession");
        params.put("api_key", Secrets.API_KEY);
        params.put("token", token);

        String signature = generateSignature(params);
        params.put("api_sig", signature);
        params.put("format", "json");

        String url = API_ROOT + "?" + encodeParams(params);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        if (json.has("session")) {
            return json.getAsJsonObject("session").get("key").getAsString();
        } else {
            throw new Exception("Failed to get session: " + response.body());
        }
    }

    private static String generateSignature(Map<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }
        sb.append(Secrets.getApiSecret());

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static String encodeParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
