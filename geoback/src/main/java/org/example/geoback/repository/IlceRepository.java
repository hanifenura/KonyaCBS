package org.example.geoback.repository;

import org.example.geoback.entity.Ilce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IlceRepository extends JpaRepository<Ilce, Integer> {
    // sorgular buraya

    //List<Ilce> findByAdiNumara(String adiNumara);
}