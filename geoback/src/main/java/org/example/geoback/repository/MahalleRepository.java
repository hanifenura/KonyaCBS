package org.example.geoback.repository;

import org.example.geoback.entity.Mahalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MahalleRepository extends JpaRepository<Mahalle, Integer> {
    @Query("SELECT m FROM Mahalle m WHERE m.ilceref = :ilceref")
    List<Mahalle> findByIlceref(@Param("ilceref") Integer ilceref);

    @Query("SELECT m FROM Mahalle m JOIN m.ilce i WHERE i.adiNumara = :ilceAdi")
    List<Mahalle> findByIlceAdiNumara(@Param("ilceAdi") String ilceAdi);

    List<Mahalle> findByIlce_Gid(Integer gid);

    List<Mahalle> findByIlce_Id(Integer id);
}