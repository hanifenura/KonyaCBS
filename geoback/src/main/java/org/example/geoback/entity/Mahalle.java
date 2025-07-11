package org.example.geoback.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.locationtech.jts.geom.Geometry;


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

    @Column(name = "ilceref")
    private BigDecimal ilceref;

    @Column(name = "uavt_mahkn")
    private BigDecimal uavtMahkn;

    @Column(name = "adi_numara", length = 254)
    private String adiNumara;

    @Column(name = "aktif_mi", length = 254)
    private String aktifMi;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;


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

    public BigDecimal getIlceref() {
        return ilceref;
    }

    public void setIlceref(BigDecimal ilceref) {
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

}
