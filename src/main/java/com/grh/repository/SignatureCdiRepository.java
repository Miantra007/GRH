package com.grh.repository;

import com.grh.model.ContratCdi;
import com.grh.model.SignatureCdi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignatureCdiRepository extends JpaRepository<SignatureCdi, Integer> {

    List<SignatureCdi> findByContrat(ContratCdi contrat);
}
