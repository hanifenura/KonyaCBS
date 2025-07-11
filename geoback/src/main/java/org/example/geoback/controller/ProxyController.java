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

        String queryString = request.getQueryString();
        String targetUrl = geoserverUrl + "?" + queryString + "&authKey=" + authKey;

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
