package org.example.geoback.controller;

import org.example.geoback.entity.Mahalle;
import org.example.geoback.repository.MahalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/data/mahalleler")

public class MahalleController {
    @Autowired
    private MahalleRepository mahalleRepository;

    @GetMapping
    public Page<Mahalle> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return mahalleRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Mahalle getById(@PathVariable Integer id) {
        return mahalleRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Mahalle create(@RequestBody Mahalle mahalle) {
        return mahalleRepository.save(mahalle);
    }

    @PutMapping("/{id}")
    public Mahalle update(@PathVariable Integer id, @RequestBody Mahalle mahalleDetails) {
        return mahalleRepository.findById(id).map(mahalle -> {
            mahalle.setFid(mahalleDetails.getFid());
            mahalle.setMahalleref(mahalleDetails.getMahalleref());
            mahalle.setIlceref(mahalleDetails.getIlceref());
            mahalle.setUavtMahkn(mahalleDetails.getUavtMahkn());
            mahalle.setAdiNumara(mahalleDetails.getAdiNumara());
            mahalle.setAktifMi(mahalleDetails.getAktifMi());
            mahalle.setTimeStamp(mahalleDetails.getTimeStamp());
           // mahalle.setGeoloc(mahalleDetails.getGeoloc()); // Eğer geometry alanı varsa
            return mahalleRepository.save(mahalle);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        mahalleRepository.deleteById(id);
    }
}
