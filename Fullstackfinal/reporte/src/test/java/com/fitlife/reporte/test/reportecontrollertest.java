package com.fitlife.reporte.test;

import com.fitlife.reporte.ReporteApplication;
import com.fitlife.reporte.model.reportemodel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ReporteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class reportecontrollertest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateAndGetById() throws Exception {
        reportemodel reporte = new reportemodel();
        reporte.setTitulo("JUnit Test");
        reporte.setDescripcion("Descripcion de prueba");
        reporte.setTipo("test");

        ResponseEntity<reportemodel> postResponse = restTemplate.postForEntity("/api/reportes", reporte, reportemodel.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        reportemodel created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        ResponseEntity<reportemodel> getResponse = restTemplate.getForEntity("/api/reportes/" + created.getId(), reportemodel.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getTitulo()).isEqualTo("JUnit Test");
    }
}
