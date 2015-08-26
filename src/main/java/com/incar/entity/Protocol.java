package com.incar.entity;

import com.incar.enums.ProtocolType;

import javax.persistence.*;

@Entity
@Table(name = "t_protocol")
public class Protocol {
    private int id;
    private String name;
    private String detail;
    private ProtocolType protocolType;
    private Supplier supplier;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, insertable = true, updatable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DETAIL", nullable = true, insertable = true, updatable = true, length = 500)
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "PROTOCOL_TYPE", nullable = true, insertable = true, updatable = true)
    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "ID")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
