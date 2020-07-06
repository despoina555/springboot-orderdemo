package eu.acme.demo.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Extend this class to include a generated <accountNumber>UUID name</accountNumber> for your <accountNumber>Entity</accountNumber>
 * as well as jpa auditing support.
 * 
 * @author lprotopapas
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract public class AuditableEntity extends PersistableEntity {

	private static final long serialVersionUID = 3674368264042228638L;
	@CreatedDate
	@Column(name = "created", nullable = false, columnDefinition = "DATETIME(6)")
	private Instant createdDate;
	@LastModifiedDate
	@Column(name = "updated", nullable = false, columnDefinition = "DATETIME(6)")
	private Instant updatedDate;

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public Instant getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Instant updatedDate) {
		this.updatedDate = updatedDate;
	}
}
