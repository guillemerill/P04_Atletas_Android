package com.stucom.atletas.repository;

import com.stucom.atletas.domain.Atleta;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Atleta entity.
 */
@SuppressWarnings("unused")
public interface AtletaRepository extends JpaRepository<Atleta,Long> {

}
