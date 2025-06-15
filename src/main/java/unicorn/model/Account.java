package unicorn.model;

import unicorn.util.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase que representa el modelo de una cuenta de usuario en el sistema.
 * Contiene todos los campos necesarios para gestionar información de usuarios, como nombre, email, teléfono,
 * nombre de usuario, contraseña y rol (estudiante, profesor y administrador).
 */
public class Account extends Base<Account> {
    private String id;
    private String nombre;
    private String apellido;
    private String phone;
    private String email;
    private String user;
    private String hashedPassword;
    private TipoCuenta tipoCuenta;
    private List<RoleHistory> roleHistory;
    private String substituteId;
    private String alternateEmail;
    private AccountStatus status;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.roleHistory = new ArrayList<>();
    }

    public Account(String nombre, String apellido, String phone, String email, String user, String plainPassword,
                    TipoCuenta tipo) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.phone = phone;
        setEmailBasedOnRole(email, tipo);
        this.user = user;
        this.hashedPassword = PasswordUtil.hashPassword(plainPassword);
        this.tipoCuenta = tipo;
        setStatus(tipo);

    }

    private void setStatus(TipoCuenta tipo){
        if (tipo == TipoCuenta.ESTUDIANTE) this.status = AccountStatus.PENDIENTE;
        else if (tipo == TipoCuenta.PROFESOR || tipo == TipoCuenta.ADMIN)  this.status = AccountStatus.ACTIVO;
    }

    public static class RoleHistory {
        private TipoCuenta role;
        private LocalDate startDate;
        private LocalDate endDate;

        public RoleHistory(TipoCuenta role, LocalDate startDate, LocalDate endDate) {
            this.role = role;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public TipoCuenta getRole() { return role; }
        public void setRole(TipoCuenta role) { this.role = role; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    }

    // Métodos de sustitución y promoción
    public Substitute promoteToAdmin(String substituteId, LocalDate startDate, LocalDate endDate) {
        if (!isProfesor()) {
            throw new IllegalStateException("Solo los profesores pueden ser promovidos a administrador");
        }
        if (this.substituteId != null) {
            throw new IllegalStateException("Este profesor ya tiene un sustituto asignado");
        }

        this.alternateEmail = this.email;
        this.email = this.email.replace("@prf.umss.edu", "@adm.umss.edu");
        this.tipoCuenta = TipoCuenta.ADMIN;
        this.setSubstituteId(substituteId);
        addRoleHistory(TipoCuenta.ADMIN, startDate, endDate);

        return new Substitute(this.id, substituteId, startDate, endDate);
    }

    public boolean revertToProfessor() {
        if (this.tipoCuenta != TipoCuenta.ADMIN || this.alternateEmail == null) {
            return false;
        }

        this.email = this.alternateEmail;
        this.alternateEmail = null;
        this.tipoCuenta = TipoCuenta.PROFESOR;
        this.substituteId = null;
        getCurrentTemporaryRole().ifPresent(role -> role.setEndDate(LocalDate.now()));

        return true;
    }

    public void addRoleHistory(TipoCuenta newRole, LocalDate startDate, LocalDate endDate) {
        // Verificar solapamiento
        boolean hasOverlap = roleHistory.stream().anyMatch(r ->
                (endDate == null || !r.getStartDate().isAfter(endDate)) &&
                        (r.getEndDate() == null || !startDate.isAfter(r.getEndDate())));

        if (hasOverlap) {
            throw new IllegalStateException("El nuevo rol se solapa con un rol existente");
        }
        roleHistory.add(new RoleHistory(newRole, startDate, endDate));
    }

    public Optional<RoleHistory> getCurrentTemporaryRole() {
        if (roleHistory == null) return Optional.empty();
        LocalDate now = LocalDate.now();
        return roleHistory.stream()
                .filter(r -> !now.isBefore(r.getStartDate()) &&
                        (r.getEndDate() == null || !now.isAfter(r.getEndDate())))
                .findFirst();
    }

    // Getters y Setters
    @Override public String getId() { return this.id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getFullName() { return nombre + " " + apellido; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getUser() { return user; }
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public List<RoleHistory> getRoleHistory() { return roleHistory; }
    public boolean hasSubstitute() { return this.substituteId != null; }
    public String getSubstituteId() { return substituteId; }
    public String getAlternateEmail() { return alternateEmail; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setSubstituteId(String substituteId) {
        if(!isAdmin()) throw new IllegalStateException("Solo administradores pueden asignar un sustituto.");

        this.substituteId = substituteId;
    }
    public void setAlternateEmail(String alternateEmail) {
        if (!isProfesor() && !isAdmin()) throw new IllegalStateException("Solo administradores y profesores pueden asignar un email alternativo.");
        this.alternateEmail = alternateEmail;
    }

    public void setPhone(String phone) {
        if (!AccountValidation.validatePhone(phone))
            throw new IllegalArgumentException("Teléfono inválido");
        this.phone = phone;
    }

    public void setUser(String user) {
        if (!AccountValidation.validateUsername(user))
            throw new IllegalArgumentException("Usuario inválido");
        this.user = user;
    }

    public void setPassword(String newPassword) {
        if (!AccountValidation.validatePasswordStrength(newPassword))
            throw new IllegalArgumentException("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
        this.hashedPassword = PasswordUtil.hashPassword(newPassword);
    }

    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public void setEmail(String email) {
        if (!AccountValidation.validateEmail(email))
            throw new IllegalArgumentException("Email inválido");
        this.email = email;
    }

    public boolean isAdmin() { return tipoCuenta == TipoCuenta.ADMIN; }
    public boolean isProfesor() { return tipoCuenta == TipoCuenta.PROFESOR; }
    public boolean isEstudiante() { return tipoCuenta == TipoCuenta.ESTUDIANTE; }

    public boolean verifyPassword(String inputPassword) {
        return PasswordUtil.verifyPassword(inputPassword, hashedPassword);
    }

    public void aprobarCuenta(){ this.status = AccountStatus.ACTIVO;}
    public void rechazarCuenta(){ this.status = AccountStatus.RECHAZADO;}
    public AccountStatus getStatus() { return status; }

    private void setEmailBasedOnRole(String email, TipoCuenta tipo) {
        if (!AccountValidation.validateRoleEmail(email, tipo)) {
            throw new IllegalArgumentException("El email no coincide con el dominio requerido para el rol " + tipo);
        }
        this.email = email;
    }

    // Serialización
    @Override
    public String toFile() {
        String roleHistoryStr = roleHistory.stream()
                .map(rh -> String.format("%s,%s,%s",
                        rh.getRole().name(),
                        rh.getStartDate(),
                        rh.getEndDate() != null ? rh.getEndDate().toString() : "null"))
                .collect(Collectors.joining(";"));


        return String.join("|",
                escapeForSerialization(id),
                escapeForSerialization(nombre),
                escapeForSerialization(apellido),
                escapeForSerialization(phone),
                escapeForSerialization(email),
                escapeForSerialization(user),
                hashedPassword,
                tipoCuenta.name(),
                substituteId != null ? substituteId : "null",
                alternateEmail != null ? alternateEmail : "null",
                status != null ? status.name() : "null",
                roleHistoryStr.isEmpty() ? "null" : roleHistoryStr
        );
    }

    @Override
    public Account fromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 12) {
            throw new IllegalArgumentException("Formato de línea inválido");
        }

        Account account = new Account();
        account.setId(parts[0]);
        account.setNombre(parts[1]);
        account.setApellido(parts[2]);
        account.phone = parts[3];
        account.email = parts[4];
        account.user = parts[5];
        account.setHashedPassword(parts[6]);
        account.setTipoCuenta(TipoCuenta.valueOf(parts[7]));
        account.substituteId = "null".equals(parts[8]) ? null : parts[8];
        account.alternateEmail = "null".equals(parts[9]) ? null : parts[9];
        account.status = "null".equals(parts[10]) ? null : AccountStatus.valueOf(parts[10]);

        // Procesar roleHistory
        account.roleHistory = new ArrayList<>();
        if (!"null".equals(parts[11])) {
            String[] roles = parts[11].split(";");
            for (String role : roles) {
                String[] roleData = role.split(",");
                TipoCuenta tipoCuenta = TipoCuenta.valueOf(roleData[0]);
                LocalDate startDate = LocalDate.parse(roleData[1]);
                LocalDate endDate = "null".equals(roleData[2]) ? null : LocalDate.parse(roleData[2]);
                account.roleHistory.add(new RoleHistory(tipoCuenta, startDate, endDate));
            }
        }

        return account;
    }

    @Override
    public String getInfo() {
        return String.format(
                "Usuario: %s (%s)\nTeléfono: %s\nRol: %s\nSustituto: %s",
                getFullName(), user, phone, tipoCuenta.getDescripcion(),
                hasSubstitute() ? substituteId : "Ninguno"
        );
    }
}