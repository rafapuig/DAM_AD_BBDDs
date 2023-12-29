package dam.ad.model.personas;

public enum Sexo {
    HOMBRE("H"),
    MUJER("M");

    private final String inicial;

    Sexo(String inicial) {
        this.inicial = inicial;
    }

    public static Sexo fromInicial(String inicial) {
        return switch (inicial) {
            case "H" -> HOMBRE;
            case "M" -> MUJER;
            default -> throw new IllegalStateException("Unexpected value: " + inicial);
        };
    }

    public String getInicial() {
        return inicial;
    }
}
