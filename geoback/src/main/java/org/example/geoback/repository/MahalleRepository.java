package org.example.geoback.repository;

import org.example.geoback.entity.Mahalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MahalleRepository extends JpaRepository<Mahalle, Integer> {
    // İlçeye göre mahalleleri getir (alfabetik sıralı)
    @Query("SELECT m FROM Mahalle m WHERE m.ilceref = :ilceref ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlceref(@Param("ilceref") Integer ilceref);

    // İlçe adına göre mahalleleri getir (alfabetik sıralı)
    @Query("SELECT m FROM Mahalle m JOIN m.ilce i WHERE i.adiNumara = :ilceAdi ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlceAdiNumara(@Param("ilceAdi") String ilceAdi);

    // İlçe GID'sine göre mahalleleri getir (alfabetik sıralı)
    @Query("SELECT m FROM Mahalle m WHERE m.ilce.gid = :gid ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlce_Gid(@Param("gid") Integer gid);

    // İlçe ID'sine göre mahalleleri getir (alfabetik sıralı)
    @Query("SELECT m FROM Mahalle m WHERE m.ilce.id = :id ORDER BY m.adiNumara ASC")
    List<Mahalle> findByIlce_Id(@Param("id") Integer id);
}