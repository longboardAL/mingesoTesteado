package edu.mtisw.testingwebapp;

import edu.mtisw.testingwebapp.entities.EmpleadoEntity;
import edu.mtisw.testingwebapp.entities.HoraExtraEntity;
import edu.mtisw.testingwebapp.entities.JustificativoEntity;
import edu.mtisw.testingwebapp.entities.PlanillaSueldoEntity;
import edu.mtisw.testingwebapp.repositories.EmpleadoRepository;
import edu.mtisw.testingwebapp.repositories.HoraExtraRepository;
import edu.mtisw.testingwebapp.repositories.JustificativoRepository;
import edu.mtisw.testingwebapp.repositories.PlanillaSueldoRepository;
import edu.mtisw.testingwebapp.services.HoraExtraService;
import edu.mtisw.testingwebapp.services.JustificativoService;
import edu.mtisw.testingwebapp.services.OficinaRRHH;
import edu.mtisw.testingwebapp.services.PlanillaSueldosServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanillaSueldosTest {

    EmpleadoEntity empleado = new EmpleadoEntity();
    EmpleadoEntity empleado2 = new EmpleadoEntity();

    HoraExtraEntity horaExtra = new HoraExtraEntity();
    HoraExtraEntity horaExtra2 = new HoraExtraEntity();
    JustificativoEntity justificativo = new JustificativoEntity();
    JustificativoEntity justificativo2 = new JustificativoEntity();
    PlanillaSueldosServices planillaSueldosServices = new PlanillaSueldosServices();
    OficinaRRHH oficinaRRHH = new OficinaRRHH();
    HoraExtraService horaExtraService = new HoraExtraService();
    JustificativoService justificativoService = new JustificativoService();
    PlanillaSueldoEntity planillaSueldo = new PlanillaSueldoEntity();

    @Test
    void generarPlanilla() throws ParseException {

        DateFormat formato =  new SimpleDateFormat("yyyy/MM/dd HH:mm");
        java.util.Date empresa = formato.parse("2018/09/03 18:00");
        java.util.Date empresa2 = formato.parse("2019/09/03 18:00");

        java.util.Date nacimiento = formato.parse("2000/03/26 18:00");
        java.util.Date nacimiento2 = formato.parse("1990/03/26 18:00");

        java.util.Date salida = formato.parse("2022/03/26 19:00");
        java.util.Date entrada = formato.parse("2022/08/17 08:15");

        empleado.setRut("17.765.876-2");
        empleado.setCategoria("A");
        empleado.setFechaIngresoEmpresa(empresa);
        empleado.setFechaNacimiento(nacimiento);
        empleado.setNombre("Angel Isaac");
        empleado.setApellidos("Avendaño Saa");
        empleado.setNumeroHijos(0);
        empleado.setSueldoMensual(1700000);

        empleado2.setRut("16.765.876-1");
        empleado2.setCategoria("B");
        empleado2.setFechaIngresoEmpresa(empresa2);
        empleado2.setFechaNacimiento(nacimiento2);
        empleado2.setNombre("juana Isaac");
        empleado2.setApellidos("perez Saa");
        empleado2.setNumeroHijos(0);
        empleado2.setSueldoMensual(1200000);

        horaExtra.setFechaAutorizada(salida);
        horaExtra.setAutorizacion("Autorizado");
        horaExtra.setRut("17.765.876-2");
        horaExtra.setId(5L);

        horaExtra2.setFechaAutorizada(salida);
        horaExtra2.setAutorizacion("No Autorizado");
        horaExtra2.setRut("16.765.876-1");
        horaExtra2.setId(6L);


        justificativo.setJustificativo("0");
        justificativo.setId(5L);
        justificativo.setFechaJustificada(entrada);
        justificativo.setDiasInasistencia("0");
        justificativo.setRut("17.765.876-2");

        justificativo2.setJustificativo("0");
        justificativo2.setId(6L);
        justificativo2.setFechaJustificada(entrada);
        justificativo2.setDiasInasistencia("0");
        justificativo2.setRut("16.765.876-1");


        ArrayList<JustificativoEntity> justificativoEntities = new ArrayList<>();
        ArrayList<EmpleadoEntity> empleadoEntities = new ArrayList<>();
        ArrayList<HoraExtraEntity>horaExtraEntities = new ArrayList<>();

        empleadoEntities.add(empleado);
        empleadoEntities.add(empleado2);
        horaExtraEntities.add(horaExtra);
        horaExtraEntities.add(horaExtra2);
        justificativoEntities.add(justificativo);
        justificativoEntities.add(justificativo2);

        ArrayList<PlanillaSueldoEntity> planillaSueldoEntities = new ArrayList<>();

        double sueldoFinal = planillaSueldosServices.calcularSueldo(0, empleadoEntities, horaExtraEntities, oficinaRRHH,justificativoEntities
                ,horaExtraService, justificativoService, planillaSueldoEntities);

        assertEquals(1400560, sueldoFinal, 0.0);
    }
}
