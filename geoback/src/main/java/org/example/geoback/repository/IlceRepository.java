package org.example.geoback.repository;

import org.example.geoback.entity.Ilce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface IlceRepository extends JpaRepository<Ilce, Integer> {

    //List<Ilce> findByAdiNumara(String adiNumara);

    List<Ilce> findIlceByIlceref(BigDecimal ilceref);

    @Override
    List<Ilce> findAll();
}