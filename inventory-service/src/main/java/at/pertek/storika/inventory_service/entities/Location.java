package at.pertek.storika.inventory_service.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"storageUnits"})
@Hidden
public class Location implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "location_id", nullable = false, updatable = false)
  private UUID locationId;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<StorageUnit> storageUnits = new HashSet<>();

  // Helper methods for managing the bidirectional relationship with StorageUnit
  public void addStorageUnit(StorageUnit storageUnit) {
    storageUnits.add(storageUnit);
    storageUnit.setLocation(this);
  }

  public void removeStorageUnit(StorageUnit storageUnit) {
    storageUnits.remove(storageUnit);
    storageUnit.setLocation(null);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Location other)) {
      return false;
    }

    return this.locationId != null && this.locationId.equals(other.locationId);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(locationId);
  }

}