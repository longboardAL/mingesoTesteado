package edu.mtisw.testingwebapp;

import edu.mtisw.testingwebapp.entities.EmpleadoEntity;
import edu.mtisw.testingwebapp.entities.HoraExtraEntity;
import edu.mtisw.testingwebapp.services.HoraExtraService;
import edu.mtisw.testingwebapp.services.OficinaRRHH;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HoraExtraTest {
    //@Autowired
    //private HoraExtraRepository horaExtraRepository;
    OficinaRRHH oficinaRRHH = new OficinaRRHH();
    HoraExtraService horaExtraService = new HoraExtraService();
    HoraExtraEntity horaExtra = new HoraExtraEntity();
    EmpleadoEntity empleado = new EmpleadoEntity();

    @Test
    void calcularHoraExtra() throws ParseException {
        DateFormat formato =  new SimpleDateFormat("yyyy/MM/dd HH:mm");
        java.util.Date empresa = formato.parse("2018/09/03 18:00");
        java.util.Date nacimiento = formato.parse("2022/03/26 18:00");
        java.util.Date salida = formato.parse("2022/03/26 19:00");

        empleado.setRut("17.765.876-2");
        empleado.setCategoria("A");
        empleado.setFechaIngresoEmpresa(empresa);
        empleado.setFechaNacimiento(nacimiento);
        empleado.setNombre("Angel Isaac");
        empleado.setApellidos("Avendaño Saa");
        empleado.setNumeroHijos(0);

        horaExtra.setFechaAutorizada(salida);
        horaExtra.setAutorizacion("Autorizado");
        horaExtra.setRut("17.765.876-2");
        horaExtra.setId(5L);

        ArrayList<EmpleadoEntity> empleadoEntities = new ArrayList<>();
        ArrayList<HoraExtraEntity>horaExtraEntities = new ArrayList<>();
        empleadoEntities.add(empleado);
        horaExtraEntities.add(horaExtra);

        long horaExtra = horaExtraService.calculoHorasExtras(empleado.getRut(),empleado.getCategoria(),horaExtraEntities,oficinaRRHH);
        assertEquals(25000, horaExtra, 0.0);
    }
}
