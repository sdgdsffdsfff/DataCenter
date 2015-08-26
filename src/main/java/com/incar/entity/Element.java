package com.incar.entity;

import com.incar.enums.DataType;

import javax.persistence.*;

@Entity
@Table(name = "t_element")
public class Element {
    private int id;
    private String name;
    private String beanProperty;
    private DataType elementType;
    private Element parent;
    private Integer groupOrder;
    //private Set<Element> elementGroup=new HashSet<Element>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = true, insertable = true, updatable = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "BEAN_PROPERTY", nullable = true, insertable = true, updatable = true, length = 100)
    public String getBeanProperty() {
        return beanProperty;
    }

    public void setBeanProperty(String beanProperty) {
        this.beanProperty = beanProperty;
    }

    @Column(name = "ELEMENT_TYPE", nullable = false, insertable = true, updatable = true)
    public DataType getElementType() {
        return elementType;
    }

    public void setElementType(DataType elementType) {
        this.elementType = elementType;
    }



    @Column(name = "GROUP_ORDER", nullable = true, insertable = true, updatable = true)
    public Integer getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }

    @ManyToOne
    @JoinColumn(name = "PARENT", referencedColumnName = "ID")
    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    /*public Set<Element> getElementGroup() {
        return elementGroup;
    }
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "parent")
    public void setElementGroup(Set<Element> elementGroup) {
        this.elementGroup = elementGroup;
    }*/
}
