package com.stucom.atletas.web.rest;

import com.stucom.atletas.AtletasApp;

import com.stucom.atletas.domain.Atleta;
import com.stucom.atletas.repository.AtletaRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AtletaResource REST controller.
 *
 * @see AtletaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtletasApp.class)
public class AtletaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_NACIONALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_NACIONALIDAD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AtletaRepository atletaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAtletaMockMvc;

    private Atleta atleta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AtletaResource atletaResource = new AtletaResource();
        ReflectionTestUtils.setField(atletaResource, "atletaRepository", atletaRepository);
        this.restAtletaMockMvc = MockMvcBuilders.standaloneSetup(atletaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Atleta createEntity(EntityManager em) {
        Atleta atleta = new Atleta()
                .nombre(DEFAULT_NOMBRE)
                .apellido(DEFAULT_APELLIDO)
                .nacionalidad(DEFAULT_NACIONALIDAD)
                .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        return atleta;
    }

    @Before
    public void initTest() {
        atleta = createEntity(em);
    }

    @Test
    @Transactional
    public void createAtleta() throws Exception {
        int databaseSizeBeforeCreate = atletaRepository.findAll().size();

        // Create the Atleta

        restAtletaMockMvc.perform(post("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atleta)))
            .andExpect(status().isCreated());

        // Validate the Atleta in the database
        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeCreate + 1);
        Atleta testAtleta = atletaList.get(atletaList.size() - 1);
        assertThat(testAtleta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAtleta.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testAtleta.getNacionalidad()).isEqualTo(DEFAULT_NACIONALIDAD);
        assertThat(testAtleta.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void createAtletaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = atletaRepository.findAll().size();

        // Create the Atleta with an existing ID
        Atleta existingAtleta = new Atleta();
        existingAtleta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtletaMockMvc.perform(post("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAtleta)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = atletaRepository.findAll().size();
        // set the field null
        atleta.setNombre(null);

        // Create the Atleta, which fails.

        restAtletaMockMvc.perform(post("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atleta)))
            .andExpect(status().isBadRequest());

        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = atletaRepository.findAll().size();
        // set the field null
        atleta.setApellido(null);

        // Create the Atleta, which fails.

        restAtletaMockMvc.perform(post("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atleta)))
            .andExpect(status().isBadRequest());

        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAtletas() throws Exception {
        // Initialize the database
        atletaRepository.saveAndFlush(atleta);

        // Get all the atletaList
        restAtletaMockMvc.perform(get("/api/atletas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atleta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].nacionalidad").value(hasItem(DEFAULT_NACIONALIDAD.toString())))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @Test
    @Transactional
    public void getAtleta() throws Exception {
        // Initialize the database
        atletaRepository.saveAndFlush(atleta);

        // Get the atleta
        restAtletaMockMvc.perform(get("/api/atletas/{id}", atleta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(atleta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()))
            .andExpect(jsonPath("$.nacionalidad").value(DEFAULT_NACIONALIDAD.toString()))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAtleta() throws Exception {
        // Get the atleta
        restAtletaMockMvc.perform(get("/api/atletas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAtleta() throws Exception {
        // Initialize the database
        atletaRepository.saveAndFlush(atleta);
        int databaseSizeBeforeUpdate = atletaRepository.findAll().size();

        // Update the atleta
        Atleta updatedAtleta = atletaRepository.findOne(atleta.getId());
        updatedAtleta
                .nombre(UPDATED_NOMBRE)
                .apellido(UPDATED_APELLIDO)
                .nacionalidad(UPDATED_NACIONALIDAD)
                .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restAtletaMockMvc.perform(put("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAtleta)))
            .andExpect(status().isOk());

        // Validate the Atleta in the database
        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeUpdate);
        Atleta testAtleta = atletaList.get(atletaList.size() - 1);
        assertThat(testAtleta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAtleta.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testAtleta.getNacionalidad()).isEqualTo(UPDATED_NACIONALIDAD);
        assertThat(testAtleta.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingAtleta() throws Exception {
        int databaseSizeBeforeUpdate = atletaRepository.findAll().size();

        // Create the Atleta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAtletaMockMvc.perform(put("/api/atletas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(atleta)))
            .andExpect(status().isCreated());

        // Validate the Atleta in the database
        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAtleta() throws Exception {
        // Initialize the database
        atletaRepository.saveAndFlush(atleta);
        int databaseSizeBeforeDelete = atletaRepository.findAll().size();

        // Get the atleta
        restAtletaMockMvc.perform(delete("/api/atletas/{id}", atleta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Atleta> atletaList = atletaRepository.findAll();
        assertThat(atletaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
