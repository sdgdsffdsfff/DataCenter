package com.incar.repository;

import com.incar.entity.Supplier;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableJpaRepositories
public interface SupplierRepository extends CrudRepository<Supplier,Integer>{

}