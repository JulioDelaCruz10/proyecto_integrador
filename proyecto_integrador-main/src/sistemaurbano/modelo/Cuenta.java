package sistemaurbano.modelo;

public abstract class Cuenta {
    private String email;
    private String contrasenia;

    public Cuenta(String email, String contrasenia) {
        this.email = email;
        this.contrasenia = contrasenia;
    }

    public boolean validarLogin(String emailInput, String passInput) {
        return this.email.equalsIgnoreCase(emailInput) && this.contrasenia.equals(passInput);
    }

    // Método abstracto para asegurar el polimorfismo en la recuperación de credenciales
    public abstract String recuperarContrasenia();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}