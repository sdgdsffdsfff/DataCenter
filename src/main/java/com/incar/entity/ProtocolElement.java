package com.incar.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_protocol_element")
public class ProtocolElement {
    private int id;
    private int elementOrder;
    private Element element;
    private Protocol protocol;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ELEMENT_ORDER", nullable = false, insertable = true, updatable = true)
    public int getElementOrder() {
        return elementOrder;
    }

    public void setElementOrder(int elementOrder) {
        this.elementOrder = elementOrder;
    }

    @ManyToOne
    @JoinColumn(name = "ELEMENT_ID", referencedColumnName = "ID")
    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    @ManyToOne
    @JoinColumn(name = "PROTOCOL_ID", referencedColumnName = "ID")
    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
