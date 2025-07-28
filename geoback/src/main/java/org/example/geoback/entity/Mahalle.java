package org.example.geoback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mahalleler")
public class Mahalle {
    @Id
    @Column(name = "gid", nullable = false)
    private Integer gid;

    @Column(name = "fid")
    private BigDecimal fid;

    @Column(name = "mahalleref")
    private BigDecimal mahalleref;

    @Column(name = "ilceref", insertable = false, updatable = false)
    private Integer ilceref;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ilceref", referencedColumnName = "gid", insertable = false, updatable = false)
    @JsonBackReference
    private Ilce ilce;

    @Column(name = "uavt_mahkn")
    private BigDecimal uavtMahkn;

    @Column(name = "adi_numara", length = 254)
    private String adiNumara;

    @Column(name = "aktif_mi", length = 254)
    private String aktifMi;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;

    @JsonIgnore
    @Column(name = "geoloc")
    private Geometry geometry;

    //Getter ve Setter Metodlari

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public BigDecimal getFid() {
        return fid;
    }

    public void setFid(BigDecimal fid) {
        this.fid = fid;
    }

    public BigDecimal getMahalleref() {
        return mahalleref;
    }

    public void setMahalleref(BigDecimal mahalleref) {
        this.mahalleref = mahalleref;
    }

    public Integer getIlceref() {
        return ilceref;
    }

    public void setIlceref(Integer ilceref) {
        this.ilceref = ilceref;
    }

    public BigDecimal getUavtMahkn() {
        return uavtMahkn;
    }

    public void setUavtMahkn(BigDecimal uavtMahkn) {
        this.uavtMahkn = uavtMahkn;
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
