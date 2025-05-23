package at.pertek.storika.inventory_service.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"category", "storageUnit", "compartment"})
@Hidden
public class Item implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "item_id", nullable = false, updatable = false)
  private UUID itemId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "unit")
  private String unit;

  @Column(name = "purchase_date")
  private LocalDate purchaseDate;

  @Column(name = "expiration_date")
  private LocalDate expirationDate;

  @Column(name = "notes")
  private String notes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_item_category"))
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "storage_unit_id", foreignKey = @ForeignKey(name = "fk_item_storage_unit"), nullable = false)
  private StorageUnit storageUnit;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "compartment_id", foreignKey = @ForeignKey(name = "fk_item_compartment"))
  private Compartment compartment;

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
    if (!(o instanceof Item other)) {
      return false;
    }
    return this.itemId != null && this.itemId.equals(other.getItemId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(itemId);
  }

}