package se.omegapoint.productdirectory.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import se.omegapoint.productdirectory.models.enums.ProductStatus;
import se.omegapoint.productdirectory.models.enums.ProductType;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductType productType;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductStatus productStatus;

    @Column
    @NotNull
    private String description;

    protected Product() {
    }

    protected Product(ProductType productType, ProductStatus productStatus) {
        this.productType = productType;
        this.productStatus = productStatus;
    }

    public Integer getProductId() {
        return productId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
