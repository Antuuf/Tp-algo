package aed;

import java.util.List;

public class SistemaSIU {
    private Trie<Carreras> carreras;
    private Trie<Estudiantes> estudiantes;

    public SistemaSIU() {
        this.carreras = new Trie<>();
        this.estudiantes = new Trie<>();
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] nombresCarreras) {
        this();
        for (String nombreCarrera : nombresCarreras) {
            agregarCarrera(nombreCarrera);
        }
    }

    // Agregar una carrera al sistema
    public void agregarCarrera(String nombreCarrera) {
        Carreras nuevaCarrera = new Carreras(nombreCarrera);
        carreras.insertar(nombreCarrera, nuevaCarrera);
    }

    public List<String> obtenerTodasLasCarreras() {
        List<String> listaCarreras = carreras.obtenerClaves();
        listaCarreras.sort(String::compareTo); // Orden lexicográfico
        return listaCarreras;
    }

    // Agregar un estudiante al sistema
    public void agregarEstudiante(String LU) {
        Estudiantes nuevoEstudiante = new Estudiantes();
        estudiantes.insertar(LU, nuevoEstudiante);
    }

    public void inscribir(String LU, String nombreCarrera, String nombreMateria) {
        Estudiantes estudiante = estudiantes.obtenerDef(LU);
        Carreras carrera = carreras.obtenerDef(nombreCarrera);
    
        if (estudiante != null && carrera != null) {
            ParCarreraMateria[] info = { new ParCarreraMateria(nombreCarrera, nombreMateria) };
            Materia materia = carrera.obtenerMateria(nombreMateria);
    
            if (materia == null) {
                carrera.agregarMateria(nombreMateria, info);
                materia = carrera.obtenerMateria(nombreMateria);
            }
    
            estudiante.inscribirMateria(nombreMateria, info);
            materia.agregarAlumno(LU);
    
            // Inscribir en las equivalencias de otras carreras
            for (ParCarreraMateria par : materia.obtenerNombres()) {
                Carreras otraCarrera = carreras.obtenerDef(par.getCarrera());
                if (otraCarrera != null) {
                    Materia otraMateria = otraCarrera.obtenerMateria(par.getNombreMateria());
                    if (otraMateria != null) {
                        estudiante.inscribirMateria(par.getNombreMateria(), info);
                        otraMateria.agregarAlumno(LU);
                    }
                }
            }
        }
    }
    
    

    public void agregarDocente(CargoDocente cargo, String nombreCarrera, String nombreMateria) {
        Carreras carrera = carreras.obtenerDef(nombreCarrera);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(nombreMateria);
            if (materia != null) {
                String tipoDocente = cargo.name();
                materia.agregarDocente(tipoDocente);
    
                // Agregar docente a las equivalencias de otras carreras
                ParCarreraMateria[] equivalencias = materia.obtenerNombres();
                if (equivalencias != null) {
                    for (ParCarreraMateria par : equivalencias) {
                        Carreras otraCarrera = carreras.obtenerDef(par.getCarrera());
                        if (otraCarrera != null) {
                            Materia otraMateria = otraCarrera.obtenerMateria(par.getNombreMateria());
                            if (otraMateria != null) {
                                otraMateria.agregarDocente(tipoDocente);
                            }
                        }
                    }
                }
            }
        }
    }
    

    public int[] plantelDocente(String nombreCarrera, String nombreMateria) {
        Carreras carrera = carreras.obtenerDef(nombreCarrera);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(nombreMateria);
            if (materia != null) {
                return materia.obtenerDocentes();
            }
        }
        return new int[4];
    }

    public int inscriptos(String nombreCarrera, String nombreMateria) {
        int totalInscriptos = 0;
        Carreras carrera = carreras.obtenerDef(nombreCarrera);
    
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(nombreMateria);
            if (materia != null) {
                totalInscriptos += materia.obtenerCantidadAlumnos();
    
                // Contar inscriptos en las equivalencias de otras carreras
                ParCarreraMateria[] otrasMaterias = materia.obtenerNombres();
                if (otrasMaterias != null) {
                    for (ParCarreraMateria par : otrasMaterias) {
                        Carreras otraCarrera = carreras.obtenerDef(par.getCarrera());
                        if (otraCarrera != null) {
                            Materia otraMateria = otraCarrera.obtenerMateria(par.getNombreMateria());
                            if (otraMateria != null) {
                                totalInscriptos += otraMateria.obtenerCantidadAlumnos();
                            }
                        }
                    }
                }
            }
        }
    
        return totalInscriptos;
    }
    

    // Verificar si una materia excede el cupo permitido
    public boolean excedeCupo(String nombreCarrera, String nombreMateria) {
        Carreras carrera = carreras.obtenerDef(nombreCarrera);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(nombreMateria);
            if (materia != null) {
                return materia.excedeCupo();
            }
        }
        return false;
    }

    // Obtener lista de todas las materias de un estudiante
    public List<String> materiasInscriptas(String LU) {
        Estudiantes estudiante = estudiantes.obtenerDef(LU);
        if (estudiante != null) {
            return estudiante.materiasInscriptas();
        }
        return null;
    }

    // Eliminar un estudiante del sistema
    public void eliminarEstudiante(String LU) {
        estudiantes.borrar(LU);
    }

    // Método para obtener la lista de carreras
    public List<String> carreras() {
        return obtenerTodasLasCarreras();
    }

    public enum CargoDocente {
        AY2, AY1, JTP, PROF
    }
}
