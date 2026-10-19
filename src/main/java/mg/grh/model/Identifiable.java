package mg.grh.model;

/** Toutes les entités ont une clé primaire Integer nommée "id" côté Java. */
public interface Identifiable {
    Integer getId();
    void setId(Integer id);
}
