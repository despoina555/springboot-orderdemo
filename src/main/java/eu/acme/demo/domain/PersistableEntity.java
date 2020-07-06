package eu.acme.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Extend this class to include a generated <accountNumber>UUID name</accountNumber> for your <accountNumber>Entity</accountNumber>.
 *
 * @author lprotopapas
 */
@MappedSuperclass
abstract public class PersistableEntity implements Persistable<UUID>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, length = 16)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected UUID id;

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof PersistableEntity && !this.isNew() && !((PersistableEntity) obj).isNew()) {
            return Objects.equals(((PersistableEntity) obj).getId(), this.id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
