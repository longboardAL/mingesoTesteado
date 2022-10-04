package edu.mtisw.testingwebapp;

import edu.mtisw.testingwebapp.entities.JustificativoEntity;
import edu.mtisw.testingwebapp.repositories.JustificativoRepository;
import edu.mtisw.testingwebapp.services.JustificativoService;
import edu.mtisw.testingwebapp.services.OficinaRRHH;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JustificativoTest {
    //@Autowired
    //private JustificativoRepository justificativoRepository;
    OficinaRRHH oficinaRRHH = new OficinaRRHH();
    JustificativoService justificativoService = new JustificativoService();
    JustificativoEntity justificativo = new JustificativoEntity();

    @Test
     void calcularHoraAtrasos() throws ParseException {
        DateFormat formato =  new SimpleDateFormat("yyyy/MM/dd HH:mm");
        java.util.Date entrada = formato.parse("2022/08/17 08:15");

        justificativo.setJustificativo("0");
        justificativo.setId(5L);
        justificativo.setFechaJustificada(entrada);
        justificativo.setDiasInasistencia("0");
        justificativo.setRut("17.765.876-2");
        ArrayList<JustificativoEntity> justificativoEntities = new ArrayList<>();
        justificativoEntities.add(justificativo);

        double sueldoFijoMensual = 1700000;
        double descuento = justificativoService.calculoHoraAtrasos(justificativo.getRut(), sueldoFijoMensual, justificativoEntities, oficinaRRHH);
        assertEquals(17000, descuento, 0.0);
    }
}
