package com.grh.config;

import com.grh.model.Entite;
import jakarta.persistence.EntityManager;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Convertit l'id posté par un &lt;select&gt; en entité, ex. "3" -> Employeur#3.
 * Une valeur vide donne null (relation facultative non renseignée).
 */
public class EntiteConverter implements ConditionalGenericConverter {

    private final EntityManager entityManager;

    public EntiteConverter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(String.class, Entite.class));
    }

    @Override
    public boolean matches(TypeDescriptor source, TypeDescriptor target) {
        return Entite.class.isAssignableFrom(target.getType());
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        String id = (String) source;
        if (!StringUtils.hasText(id)) {
            return null;
        }
        Object entite = entityManager.find(targetType.getType(), Integer.valueOf(id.trim()));
        if (entite == null) {
            throw new IllegalArgumentException(targetType.getType().getSimpleName() + " introuvable : " + id);
        }
        return entite;
    }
}
