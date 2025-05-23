package at.pertek.storika.inventory_service.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "compartments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "storageUnit")
@Hidden
public class Compartment implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "compartment_id", nullable = false, updatable = false)
  private UUID compartmentId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "storage_unit_id", nullable = false, foreignKey = @ForeignKey(name = "fk_storage_unit"))
  private StorageUnit storageUnit;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Compartment other)) {
      return false;
    }

    return this.compartmentId != null && this.compartmentId.equals(other.compartmentId);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(compartmentId);
  }

}