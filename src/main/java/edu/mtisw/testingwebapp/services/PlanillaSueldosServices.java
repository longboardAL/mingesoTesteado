package edu.mtisw.testingwebapp.services;


import edu.mtisw.testingwebapp.entities.*;
import edu.mtisw.testingwebapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class PlanillaSueldosServices {

    @Autowired
    PlanillaSueldoRepository planillaSueldoRepository;
    @Autowired
    JustificativoRepository justificativoRepository;
    @Autowired
    HoraExtraRepository horaExtraRepository;
    @Autowired
    JustificativoService justificativoService;
    @Autowired
    OficinaRRHH oficinaRRHH;
    @Autowired
    HoraExtraService horaExtraService;
    @Autowired
    EmpleadoRepository empleadoRepository;
    @Autowired
    EmpleadoService empleadoService;

    public ArrayList<PlanillaSueldoEntity> obtenerPlanilla() {
        return (ArrayList<PlanillaSueldoEntity>) planillaSueldoRepository.findAll();
    }


    public double calcularSueldo(int i, ArrayList<EmpleadoEntity> empleadoEntities, ArrayList<HoraExtraEntity>horaExtraEntities,
                                 OficinaRRHH oficinaRRHH, ArrayList<JustificativoEntity> justificativoEntities, HoraExtraService horaExtraService,
                                 JustificativoService justificativoService, ArrayList<PlanillaSueldoEntity>planillaSueldoEntities) throws ParseException {


        String rut = empleadoEntities.get(i).getRut();
        String categoria = empleadoEntities.get(i).getCategoria();
        double sueldoMensual = empleadoEntities.get(i).getSueldoMensual();
        Date fechaIngreso = empleadoEntities.get(i).getFechaIngresoEmpresa();
        System.out.println("User: "+empleadoEntities.get(i));
        //*******************************************************************

        // variables a utilizar
        double sueldo = 0;
        double sueldoBruto = 0;
        double montoAniosServicios = 0;
        long aniosServicio = 0;
        long montoHorasExtras = 0;
        double montoDescuentos = 0;
        double cotizacionProvisional = 0;
        double cotizacionSalud = 0;
        double bonificaciones = 0;


        // Si el sueldo es positivo, proceder al siguiente paso
        if (sueldoMensual > 0) {

            // clacular y verificar  si las horas extras están validadas
            montoHorasExtras = horaExtraService.calculoHorasExtras(rut,categoria,horaExtraEntities,oficinaRRHH);
            System.out.println("horas extras: " + montoHorasExtras);

            //Las marcas de hora después de la hora de entrada tienen un descuento sobre el sueldo fijo mensual
            montoDescuentos = justificativoService.calculoHoraAtrasos(rut, sueldoMensual, justificativoEntities, oficinaRRHH);
            System.out.println("Sueldo: " + sueldoMensual + " Descuentos: " + montoDescuentos);

            //Calculo años de servicio
            aniosServicio = oficinaRRHH.aniosServicio(fechaIngreso);

            // Calculo del beneficio por años de servicio
            montoAniosServicios = oficinaRRHH.calcularMontoBonificacionAniosServicios(aniosServicio, sueldoMensual);
            bonificaciones = montoAniosServicios;
            System.out.println("Monto anios de servicio: " + montoAniosServicios + " Total descuentos: " + montoDescuentos);

            sueldo = sueldoMensual + bonificaciones + montoHorasExtras - montoDescuentos;

            // Descuentos que se aplican a todos los empleados sobre el sueldo final: Salud y provisión
            cotizacionProvisional = oficinaRRHH.calcularMontoDescuentoProvisional(sueldo);
            cotizacionSalud = oficinaRRHH.calcularMontoDescuentoSalud(sueldo);

            // sumar descuentos
            montoDescuentos = montoDescuentos + cotizacionProvisional + cotizacionSalud;
            System.out.println("Anios servicio: " + aniosServicio + " Provision: " + cotizacionProvisional + " Salud: " + cotizacionSalud);

            sueldo = sueldo - cotizacionProvisional - cotizacionSalud;
            sueldoBruto = sueldoMensual + bonificaciones + montoHorasExtras;

            guardarPlanillaEmpleado(planillaSueldoEntities, empleadoEntities, aniosServicio, bonificaciones, montoHorasExtras, montoDescuentos, cotizacionProvisional, cotizacionSalud, sueldo, i, sueldoBruto);
        }
        return sueldo;
    }

    public void guardarPlanillaEmpleado(ArrayList<PlanillaSueldoEntity>planillaSueldoEntities, ArrayList<EmpleadoEntity> empleadoEntities, long aniosServicio, double bonificaciones, long montoHorasExtras, double montoDescuentos,
                                        double cotizacionProvisional, double cotizacionSalud, double sueldo, int i, double sueldoBruto){

        PlanillaSueldoEntity planillaSueldoEntity1 = new PlanillaSueldoEntity();
        planillaSueldoEntity1.setRut(empleadoEntities.get(i).getRut());
        planillaSueldoEntity1.setNombres(empleadoEntities.get(i).getNombre());
        planillaSueldoEntity1.setApellidos(empleadoEntities.get(i).getApellidos());
        planillaSueldoEntity1.setCategoria(empleadoEntities.get(i).getCategoria());
        planillaSueldoEntity1.setAniosServicios(aniosServicio);
        planillaSueldoEntity1.setSueldoFijoMensual(empleadoEntities.get(i).getSueldoMensual());
        planillaSueldoEntity1.setMontoBonificacionAnios(bonificaciones);
        planillaSueldoEntity1.setMontoHorasExtras(montoHorasExtras);
        planillaSueldoEntity1.setMontodescuentos(montoDescuentos);
        planillaSueldoEntity1.setMontoBruto(sueldoBruto);
        planillaSueldoEntity1.setMontoProvisional(cotizacionProvisional);
        planillaSueldoEntity1.setMontoSalud(cotizacionSalud);
        planillaSueldoEntity1.setMontoFinal(sueldo);
        planillaSueldoEntities.add(planillaSueldoEntity1);

        if(!ExistePlanilla(planillaSueldoEntity1, planillaSueldoEntities)) {
            System.out.println("Aca la planilla: "+planillaSueldoEntity1);
            guardarPlanillaBd(planillaSueldoEntity1);
        }
    }

    public void guardarPlanillaBd(PlanillaSueldoEntity planilla) {
        planillaSueldoRepository.save(planilla);
    }

    public void generarPlanilla() throws ParseException {
        EmpleadoEntity empleado = new EmpleadoEntity();
        ArrayList<EmpleadoEntity> empleadoEntities = new ArrayList<>();
        empleadoEntities = empleadoService.obtenerEmpleados();

        HoraExtraEntity horaExtra = new HoraExtraEntity();
        ArrayList<HoraExtraEntity>horaExtraEntities = new ArrayList<>();
        horaExtraEntities = horaExtraService.obtenerHorasExtras();

        JustificativoEntity justificativo = new JustificativoEntity();
        ArrayList<JustificativoEntity> justificativoEntities = new ArrayList<>();
        justificativoEntities = justificativoService.obtenerJustificativos();

        PlanillaSueldoEntity planillaSueldo = new PlanillaSueldoEntity();
        ArrayList<PlanillaSueldoEntity>planillaSueldoEntities = new ArrayList<>();
        planillaSueldoEntities = obtenerPlanilla();
        System.out.println("Aca en planillas : "+horaExtraEntities);

        for(int i = 0; i < empleadoEntities.size(); i++){
            double sueldo = calcularSueldo(i,empleadoEntities,horaExtraEntities,oficinaRRHH,justificativoEntities,
                    horaExtraService, justificativoService, planillaSueldoEntities);
        }
    }

    public boolean ExistePlanilla(PlanillaSueldoEntity planillaSueldoEntity, ArrayList<PlanillaSueldoEntity>planillaSueldoEntities){
        boolean validar = false;
        for (int i = 0; i < planillaSueldoEntities.size(); i++){
            if(planillaSueldoEntities.get(i).getRut().equals(planillaSueldoEntity.getRut()) &&
                    planillaSueldoEntities.get(i).getMontoFinal() == planillaSueldoEntity.getMontoFinal()){
                validar = true;
            }
        }
        return validar;
    }
}