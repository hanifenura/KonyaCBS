package org.example.geoback.controller;

import org.example.geoback.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/location-info")
    public Map<String, String> getLocationInfo(
            @RequestParam double lat,
            @RequestParam double lng) {
        return locationRepository.findLocationInfo(lat, lng);
    }
}
