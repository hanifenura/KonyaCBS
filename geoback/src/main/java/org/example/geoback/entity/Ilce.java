package org.example.geoback.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "adi_numara", length = 254)
    private String adiNumara;

    @Column(name = "aktif_mi", length = 254)
    private String aktifMi;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;

    // Getter ve Setter metodları

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
}
