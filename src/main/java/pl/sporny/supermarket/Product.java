package pl.sporny.supermarket;

import java.math.BigDecimal;

public class Product {

    private String name;
    private BigDecimal price;
    private int baseUnit;
    private int discount;
    private SpecialOffer specialOffer;

    private Product(ProductBuilder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.baseUnit = builder.baseUnit;
        this.discount = builder.discount;
        this.specialOffer = builder.specialOffer;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getBaseUnit() {
        return baseUnit;
    }

    public int getDiscount() {
        return discount;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }

    public static class ProductBuilder {
        private String name;
        private BigDecimal price;
        private int baseUnit = 1;
        private int discount = 0;
        private SpecialOffer specialOffer = SpecialOffer.NONE;

        public ProductBuilder(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public ProductBuilder baseUnit(int baseUnit) {
            this.baseUnit = baseUnit;
            return this;
        }

        public ProductBuilder discount(int discount) {
            this.discount = discount;
            return this;
        }

        public ProductBuilder specialOffer(SpecialOffer specialOffer) {
            this.specialOffer = specialOffer;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

}
