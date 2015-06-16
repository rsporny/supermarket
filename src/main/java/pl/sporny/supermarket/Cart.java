package pl.sporny.supermarket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private BigDecimal total = BigDecimal.ZERO;
    private List<Product> inventory;
    private Map<String, Integer> scannedProducts = new HashMap<String, Integer>();

    public Cart(List<Product> products) {
        this.inventory = products;
    }

    public Map<String, Integer> getScannedProducts() {
        return scannedProducts;
    }

    public void scan(String name, int quantity) {
        if (scannedProducts.containsKey(name)) {
            int currentQuantity = scannedProducts.get(name);
            scannedProducts.put(name, quantity + currentQuantity);
        } else {
            scannedProducts.put(name, quantity);
        }
    }

    public BigDecimal total() {
        for (String scannedProduct : scannedProducts.keySet()) {
            for (Product product : inventory) {
                if (scannedProduct.equalsIgnoreCase(product.getName())) {
                    int quantity = scannedProducts.get(scannedProduct);
                    BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity))
                            .divide(BigDecimal.valueOf(product.getBaseUnit())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal discount = BigDecimal.valueOf(100 - product.getDiscount()).divide(new BigDecimal(100));
                    BigDecimal value = totalPrice.multiply(discount);
                    BigDecimal specialOfferDiscount = specialOfferDiscount(product.getSpecialOffer(), value, quantity,
                            product.getBaseUnit());
                    total = total.add(value.subtract(specialOfferDiscount).setScale(2, RoundingMode.HALF_UP));
                    break;
                }
            }
        }
        return total;
    }

    private BigDecimal specialOfferDiscount(SpecialOffer specialOffer, BigDecimal value, int quantity, int baseUnit) {
        switch (specialOffer) {
        case TWO_FOR_ONE:
            if (quantity > baseUnit) {
                int modulo = quantity % (2 * baseUnit);
                if (modulo == 0) {
                    return value.divide(new BigDecimal(2));
                } else {
                    int quantityInSpecialOffer = quantity - modulo;
                    BigDecimal valueWithoutSpecialOffer = value.divide(BigDecimal.valueOf(quantity)).multiply(
                            BigDecimal.valueOf(quantityInSpecialOffer));
                    return valueWithoutSpecialOffer.divide(new BigDecimal(2));
                }
            }
            break;
        case NONE:
            break;
        }
        return BigDecimal.ZERO;
    }

}
