package org.example.geoback.controller;

import org.example.geoback.entity.Mahalle;
import org.example.geoback.repository.MahalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/data/mahalleler")

public class MahalleController {
    @Autowired
    private MahalleRepository mahalleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            return mahalleRepository.save(mahalle);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        mahalleRepository.deleteById(id);
    }

    @GetMapping("/analyze-selcuklu")
    public Map<String, Object> analyzeSelcuklu() {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> selcukluIlce = jdbcTemplate.queryForList(
                "SELECT gid, id, ilceref, adi_numara FROM ilceler WHERE adi_numara = 'SELÇUKLU'"
        );
        result.put("ilce_bilgisi", selcukluIlce);

        List<Map<String, Object>> mahalleler = jdbcTemplate.queryForList(
                "SELECT gid, ilceref, adi_numara FROM mahalleler WHERE ilceref IN (SELECT gid FROM ilceler WHERE adi_numara = 'SELÇUKLU')"
        );
        result.put("mahalleler", mahalleler);

        List<Map<String, Object>> allRelated = jdbcTemplate.queryForList(
                "SELECT m.gid as mahalle_gid, m.ilceref as mahalle_ilceref, m.adi_numara as mahalle_adi, " +
                        "i.gid as ilce_gid, i.id as ilce_id, i.ilceref as ilce_ilceref, i.adi_numara as ilce_adi " +
                        "FROM mahalleler m " +
                        "LEFT JOIN ilceler i ON m.ilceref = i.gid " +
                        "WHERE i.adi_numara = 'SELÇUKLU' OR m.ilceref IN (SELECT gid FROM ilceler WHERE adi_numara = 'SELÇUKLU')"
        );
        result.put("iliskili_kayitlar", allRelated);

        return result;
    }

    @GetMapping("/test-selcuklu")
    public Map<String, Object> testSelcuklu() {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> ilce = jdbcTemplate.queryForList(
                "SELECT * FROM ilceler WHERE adi_numara = 'SELÇUKLU'"
        );
        result.put("ilce_kaydi", ilce);

        List<Map<String, Object>> mahalleler1 = jdbcTemplate.queryForList(
                "SELECT * FROM mahalleler WHERE ilceref = 1"
        );
        result.put("mahalleler_ilceref_1", mahalleler1);

        List<Map<String, Object>> mahalleler2 = jdbcTemplate.queryForList(
                "SELECT * FROM mahalleler WHERE ilceref = 2"
        );
        result.put("mahalleler_ilceref_2", mahalleler2);

        List<Map<String, Object>> iliskiler = jdbcTemplate.queryForList(
                "SELECT m.gid as mahalle_gid, m.ilceref, m.adi_numara as mahalle_adi, " +
                        "i.gid as ilce_gid, i.id as ilce_id, i.adi_numara as ilce_adi " +
                        "FROM mahalleler m " +
                        "LEFT JOIN ilceler i ON m.ilceref = i.gid " +
                        "WHERE i.adi_numara = 'SELÇUKLU' OR m.ilceref IN (1, 2)"
        );
        result.put("iliskiler", iliskiler);

        return result;
    }

    @GetMapping("/by-ilce")
    public List<Mahalle> getMahallelerByIlce(@RequestParam Integer ilceref) {
        System.out.println("Gelen ilceref: " + ilceref);

        List<Mahalle> mahalleler = mahalleRepository.findByIlceref(ilceref);

        if (mahalleler.isEmpty()) {
            mahalleler = mahalleRepository.findByIlce_Gid(ilceref);

            if (mahalleler.isEmpty()) {
                mahalleler = mahalleRepository.findByIlce_Id(ilceref);
            }
        }

        System.out.println("Bulunan mahalle sayısı: " + mahalleler.size());
        return mahalleler;
    }

    @GetMapping("/selcuklu-debug")
    public Map<String, Object> getSelcukluDebug() {
        Map<String, Object> result = new HashMap<>();

        List<Mahalle> mahalleler = mahalleRepository.findByIlceAdiNumara("SELÇUKLU");
        result.put("mahalleler", mahalleler);

        result.put("mahalle_sayisi", mahalleler.size());

        return result;
    }

    @GetMapping("/check-ilce-mahalle")
    public Map<String, Object> checkRelationships() {
        Map<String, Object> result = new HashMap<>();
        List<Mahalle> allMahalleler = mahalleRepository.findAll();

        Map<Integer, List<String>> mahalleGroups = new HashMap<>();
        allMahalleler.forEach(m -> {
            if (m.getIlceref() != null) {
                int ilceRef = m.getIlceref().intValue();
                mahalleGroups.computeIfAbsent(ilceRef, k -> new ArrayList<>())
                        .add(String.format("%s (ilceref: %d)", m.getAdiNumara(), ilceRef));
            }
        });

        result.put("mahalleGroups", mahalleGroups);
        return result;
    }
}
