package negocio;

public class Validador {

    // Verifica que un texto no sea nulo ni esté vacío
    public static boolean esTextoValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    // Verifica que la cédula tenga exactamente 10 dígitos numéricos
    public static boolean esCedulaValida(String cedula) {
        if (cedula == null) {
            return false;
        }
        String limpia = cedula.trim();
        if (limpia.length() != 10) {
            return false;
        }
        for (int i = 0; i < limpia.length(); i++) {
            if (!Character.isDigit(limpia.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Verifica que un valor numérico sea mayor a cero
    public static boolean esPositivo(double numero) {
        if (numero > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Verifica que un valor esté dentro de un rango permitido
    public static boolean estaEnRango(double valor, double minimo, double maximo) {
        if (valor >= minimo && valor <= maximo) {
            return true;
        } else {
            return false;
        }
    }

    // Verifica formato HH:mm como las 14:30
    public static boolean esHoraValida(String hora) {
        if (hora == null) {
            return false;
        }
        String limpia = hora.trim();
        // Debe tener 5 caracteres y el separador ':' en medio
        if (limpia.length() != 5 || limpia.charAt(2) != ':') {
            return false;
        }

        // Verifica que los otros 4 caracteres sean números
        if (!Character.isDigit(limpia.charAt(0)) || !Character.isDigit(limpia.charAt(1)) ||
                !Character.isDigit(limpia.charAt(3)) || !Character.isDigit(limpia.charAt(4))) {
            return false;
        }

        int horas = Integer.parseInt(limpia.substring(0, 2));
        int minutos = Integer.parseInt(limpia.substring(3, 5));

        if (horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59) {
            return true;
        } else {
            return false;
        }
    }
}