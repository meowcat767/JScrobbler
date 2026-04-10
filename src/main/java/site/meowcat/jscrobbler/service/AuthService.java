package site.meowcat.jscrobbler.service;

import site.meowcat.jscrobbler.sec.Secrets;

import java.awt.Desktop;
import java.net.URI;
public class AuthService {
    private static String currentToken;

    public static void openAuthPage() throws Exception {
        currentToken = LastFmService.getToken();
        String url = "https://www.last.fm/api/auth/?api_key=" + Secrets.API_KEY + "&token=" + currentToken;
        Desktop.getDesktop().browse(new URI(url));
    }

    public static String finishAuth() throws Exception {
        if (currentToken == null) {
            throw new IllegalStateException("No authentication in progress");
        }
        String sessionKey = LastFmService.getSessionKey(currentToken);
        currentToken = null; // Reset after successful session retrieval
        return sessionKey;
    }
}
