package org.example.geoback.repository;

import org.example.geoback.entity.Mahalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MahalleRepository extends JpaRepository<Mahalle, Integer> {
    @Query("SELECT m FROM Mahalle m WHERE m.ilceref = :ilceref ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlceref(@Param("ilceref") BigDecimal ilceref);

    @Query("SELECT m FROM Mahalle m JOIN m.ilce i WHERE i.adiNumara = :ilceAdi ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlceAdiNumara(@Param("ilceAdi") String ilceAdi);

    @Query("SELECT m FROM Mahalle m WHERE m.ilce.gid = :gid ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlce_Gid(@Param("gid") Integer gid);

    @Query("SELECT m FROM Mahalle m WHERE m.ilce.id = :id ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlce_Id(@Param("id") BigDecimal id);
}