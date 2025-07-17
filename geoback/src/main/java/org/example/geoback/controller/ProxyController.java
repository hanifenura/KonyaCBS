package org.example.geoback.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class ProxyController {
    @Value("${geoserver.auth-key}")
    private String authKey;

    @Value("${geoserver.url}")
    private String geoserverUrl;

    @GetMapping("/wms-proxy")
    public ResponseEntity<byte[]> proxyWmsRequest(HttpServletRequest request) throws Exception {
        // Header'dan token al
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            // Header yoksa URL parametresinden token almaya çalış
            token = request.getParameter("authkey");
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).build(); // Yetkisiz
        }

        // GeoServer URL'ini hazırla
        String queryString = request.getQueryString();
        String targetUrl = geoserverUrl + "?" + queryString;

        // Eğer queryString zaten authkey içermezse ekle
        if (!queryString.contains("authkey=")) {
            targetUrl += "&authkey=" + token;
        }

        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        byte[] body = is.readAllBytes();
        is.close();

        String contentType = connection.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(body);
    }


}
