package at.pertek.storika.inventory_service.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "storage_units")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"location", "compartments", "items"})
@Hidden
public class StorageUnit implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "storage_unit_id", nullable = false, updatable = false)
  private UUID storageUnitId;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "location_id", nullable = false, foreignKey = @ForeignKey(name = "fk_storage_unit_location"))
  private Location location;

  @OneToMany(mappedBy = "storageUnit", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Compartment> compartments = new HashSet<>();

  @OneToMany(mappedBy = "storageUnit", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Item> items = new HashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  // Helper methods for Compartments
  public void addCompartment(Compartment compartment) {
    compartments.add(compartment);
    compartment.setStorageUnit(this);
  }

  public void removeCompartment(Compartment compartment) {
    compartments.remove(compartment);
    compartment.setStorageUnit(null);
  }

  // Helper methods for Items
  public void addItem(Item item) {
    items.add(item);
    item.setStorageUnit(this);
  }

  public void removeItem(Item item) {
    items.remove(item);
    item.setStorageUnit(null);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StorageUnit other)) {
      return false;
    }

    return this.storageUnitId != null && this.storageUnitId.equals(other.storageUnitId);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(storageUnitId);
  }

}