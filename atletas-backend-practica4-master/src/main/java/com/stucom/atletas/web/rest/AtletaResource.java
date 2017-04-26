package com.stucom.atletas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stucom.atletas.domain.Atleta;

import com.stucom.atletas.repository.AtletaRepository;
import com.stucom.atletas.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Atleta.
 */
@RestController
@RequestMapping("/api")
public class AtletaResource {

    private final Logger log = LoggerFactory.getLogger(AtletaResource.class);
        
    @Inject
    private AtletaRepository atletaRepository;

    /**
     * POST  /atletas : Create a new atleta.
     *
     * @param atleta the atleta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new atleta, or with status 400 (Bad Request) if the atleta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/atletas")
    @Timed
    public ResponseEntity<Atleta> createAtleta(@Valid @RequestBody Atleta atleta) throws URISyntaxException {
        log.debug("REST request to save Atleta : {}", atleta);
        if (atleta.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("atleta", "idexists", "A new atleta cannot already have an ID")).body(null);
        }
        Atleta result = atletaRepository.save(atleta);
        return ResponseEntity.created(new URI("/api/atletas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("atleta", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /atletas : Updates an existing atleta.
     *
     * @param atleta the atleta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated atleta,
     * or with status 400 (Bad Request) if the atleta is not valid,
     * or with status 500 (Internal Server Error) if the atleta couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/atletas")
    @Timed
    public ResponseEntity<Atleta> updateAtleta(@Valid @RequestBody Atleta atleta) throws URISyntaxException {
        log.debug("REST request to update Atleta : {}", atleta);
        if (atleta.getId() == null) {
            return createAtleta(atleta);
        }
        Atleta result = atletaRepository.save(atleta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("atleta", atleta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /atletas : get all the atletas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of atletas in body
     */
    @GetMapping("/atletas")
    @Timed
    public List<Atleta> getAllAtletas() {
        log.debug("REST request to get all Atletas");
        List<Atleta> atletas = atletaRepository.findAll();
        return atletas;
    }

    /**
     * GET  /atletas/:id : get the "id" atleta.
     *
     * @param id the id of the atleta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the atleta, or with status 404 (Not Found)
     */
    @GetMapping("/atletas/{id}")
    @Timed
    public ResponseEntity<Atleta> getAtleta(@PathVariable Long id) {
        log.debug("REST request to get Atleta : {}", id);
        Atleta atleta = atletaRepository.findOne(id);
        return Optional.ofNullable(atleta)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /atletas/:id : delete the "id" atleta.
     *
     * @param id the id of the atleta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/atletas/{id}")
    @Timed
    public ResponseEntity<Void> deleteAtleta(@PathVariable Long id) {
        log.debug("REST request to delete Atleta : {}", id);
        atletaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("atleta", id.toString())).build();
    }

}
