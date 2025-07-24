package org.example.geoback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ilceler")
public class Ilce {

    @Id
    @Column(name = "gid", nullable = false)
    private Integer gid;

    @Column(name = "id")
    private BigDecimal id;

    @Column(name = "fid")
    private BigDecimal fid;

    @Column(name = "ilceref")
    private BigDecimal ilceref;

    //İlçeye bağlı mahalleler
    @OneToMany(mappedBy = "ilce", fetch = FetchType.LAZY)
    @JsonManagedReference  // JSON serialize ederken mahalle listesini gösterir
    private List<Mahalle> mahalleler;

    @Column(name = "adi_numara", length = 254)
    private String adiNumara;

    @Column(name = "aktif_mi", length = 254)
    private String aktifMi;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;

    @JsonIgnore
    @Column(name = "geoloc")
    private Geometry geometry;

    // Getter ve Setter Metodlari

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getFid() {
        return fid;
    }

    public void setFid(BigDecimal fid) {
        this.fid = fid;
    }

    public BigDecimal getIlceref() {
        return ilceref;
    }

    public void setIlceref(BigDecimal ilceref) {
        this.ilceref = ilceref;
    }

    public String getAdiNumara() {
        return adiNumara;
    }

    public void setAdiNumara(String adiNumara) {
        this.adiNumara = adiNumara;
    }

    public String getAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(String aktifMi) {
        this.aktifMi = aktifMi;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
