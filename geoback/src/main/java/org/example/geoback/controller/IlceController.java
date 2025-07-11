package org.example.geoback.controller;

import org.example.geoback.entity.Ilce;
import org.example.geoback.repository.IlceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/data/ilceler")

public class IlceController {
    @Autowired
    private IlceRepository ilceRepository;

    @GetMapping
    public Page<Ilce> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ilceRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Ilce getById(@PathVariable Integer id) {
        return ilceRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Ilce create(@RequestBody Ilce ilce) {
        return ilceRepository.save(ilce);
    }

    @PutMapping("/{id}")
    public Ilce update(@PathVariable Integer id, @RequestBody Ilce ilceDetails) {
        return ilceRepository.findById(id).map(ilce -> {
            ilce.setId(ilceDetails.getId());
            ilce.setFid(ilceDetails.getFid());
            ilce.setIlceref(ilceDetails.getIlceref());
            ilce.setAdiNumara(ilceDetails.getAdiNumara());
            ilce.setAktifMi(ilceDetails.getAktifMi());
            ilce.setTimeStamp(ilceDetails.getTimeStamp());
            return ilceRepository.save(ilce);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ilceRepository.deleteById(id);
    }

}
