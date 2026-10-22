package mg.grh.web;

import java.io.Serializable;

/** Utilisateur connecté, stocké en session. type = RH | DG | CANDIDAT | AUTRE */
public class SessionUser implements Serializable {
    private final String type;
    private final Integer id;
    private final String email;

    public SessionUser(String type, Integer id, String email) {
        this.type = type;
        this.id = id;
        this.email = email;
    }

    public String getType() { return type; }
    public Integer getId() { return id; }
    public String getEmail() { return email; }

    public String homeUrl() {
        return switch (type) {
            case "RH" -> "/admin/contrats";
            case "DG" -> "/dg/contrats";
            case "CANDIDAT" -> "/candidat/notifications";
            default -> "/login";
        };
    }
}
