package se.omegapoint.productdirectory.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;

@Entity
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer productId;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductType productType;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductStatus productStatus;
}
