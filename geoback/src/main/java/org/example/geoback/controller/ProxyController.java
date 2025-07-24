package org.example.geoback.controller;

import org.example.geoback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class ProxyController {

    @Value("${geoserver.auth-key}")
    private String authKey;

    @Value("${geoserver.url}")
    private String geoserverUrl;

    @GetMapping("/geoserver-proxy")
    public ResponseEntity<byte[]> proxyGeoserverRequest(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            token = request.getParameter("authkey");
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }

        String role = JwtUtil.getRoleFromToken(token);
        System.out.println("Token role: " + role);

        String queryString = request.getQueryString();

        String targetUrl = geoserverUrl;

        if (queryString != null && !queryString.isEmpty()) {
            targetUrl += "?" + queryString;
            if (!queryString.contains("authkey=")) {
                targetUrl += "&authkey=" + token;
            }
        } else {
            targetUrl += "?authkey=" + token;
        }

        String decodedQuery = URLDecoder.decode(queryString != null ? queryString : "", StandardCharsets.UTF_8);
        System.out.println("Incoming decoded queryString: " + decodedQuery);
        System.out.println("Target URL: " + targetUrl);

        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        byte[] body = is.readAllBytes();
        is.close();
        System.out.println("Gelen query string: " + queryString);
        String contentType = connection.getContentType();

        return ResponseEntity.ok()
                // .contentType(MediaType.parseMediaType(contentType))
                // .header("Access-Control-Allow-Origin", "http://localhost:5173")
                // .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                //.header("Access-Control-Allow-Headers", "Authorization, Content-Type")
                .body(body);
    }

}
