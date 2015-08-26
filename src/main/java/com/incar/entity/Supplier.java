package com.incar.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "t_supplier")
public class Supplier {
    private int id;
    private String name;
    private String detail;
    private Set<Protocol> protocols=new HashSet<Protocol>();

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

    @OneToMany(mappedBy = "supplier")
    public Set<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(Set<Protocol> protocols) {
        this.protocols = protocols;
    }
}
