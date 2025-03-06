package com.assurance.demo.web.repository;

import com.assurance.demo.web.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DevisRepository extends JpaRepository<Devis,Long> {

}
