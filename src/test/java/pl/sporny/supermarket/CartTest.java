package pl.sporny.supermarket;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CartTest {

    List<Product> inventory = new ArrayList<Product>();

    @Before
    public void setUp() {
        inventory.clear();
    }

    @Test
    public void testCheckoutOneProduct() {
        // given
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.50)).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Milk", 2);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(5.00));
    }

    @Test
    public void testCheckoutProductWithBaseUnit() {
        // given
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(3.00)).baseUnit(100).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Beef", 250);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(7.50));
    }

    @Test
    public void testCheckoutProductWhichPriceHasCents() {
        // given
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(2.99)).baseUnit(100).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Beef", 351);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(10.49));
    }

    @Test
    public void testCheckoutProductWithDiscount() {
        // given
        inventory.add(new Product.ProductBuilder("Banana", BigDecimal.valueOf(1.00)).discount(10).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Banana", 10);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(9.00));
    }

    @Test
    public void testCheckoutProductWithSpecialOffer() {
        // given
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.00)).specialOffer(
                SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Milk", 2);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(2.00));
    }

    @Test
    public void testCheckoutProductWithSpecialOfferWhenQuantityIsLowerThanBaseUnit() {
        // given
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(3.00)).baseUnit(100)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Beef", 99);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(2.97));
    }

    @Test
    public void testCheckoutProductWithSpecialOfferWhenQuantityEqaulsBaseUnit() {
        // given
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.00)).specialOffer(
                SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Milk", 1);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(2.00));
    }

    @Test
    public void testCheckoutProductTwiceAndCheckIfSpecialOfferApplies() {
        // given
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.00)).specialOffer(
                SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Milk", 1);
        cart.scan("Milk", 2);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(4.00));
    }

    @Test
    public void testCheckoutProductWithDiscountAndSpecialOffer() {
        // given
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.00)).discount(10)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Milk", 5);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(5.40));
    }

    @Test
    public void testCheckoutProductWithBaseUnitAndSpecialOffer() {
        // given
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(3.00)).baseUnit(100)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Beef", 450);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(7.50));
    }

    @Test
    public void testCheckoutProductWithBaseUnitDiscountAndSpecialOffer() {
        // given
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(2.95)).baseUnit(100).discount(30)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Beef", 1120);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(12.80));
    }

    @Test
    public void testCheckoutCart() {
        // given
        inventory.add(new Product.ProductBuilder("Butter", BigDecimal.valueOf(1.94)).build());
        inventory.add(new Product.ProductBuilder("Bread", BigDecimal.valueOf(1.60))
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        inventory.add(new Product.ProductBuilder("Banana", BigDecimal.valueOf(0.99)).discount(5).build());
        inventory.add(new Product.ProductBuilder("Milk", BigDecimal.valueOf(2.00)).discount(10)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        inventory.add(new Product.ProductBuilder("Beef", BigDecimal.valueOf(2.95)).baseUnit(100).discount(30)
                .specialOffer(SpecialOffer.TWO_FOR_ONE).build());
        inventory.add(new Product.ProductBuilder("Product which I won't scan", BigDecimal.valueOf(999.00)).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Butter", 3); // 5,82
        cart.scan("Bread", 2); // 1,6
        cart.scan("Banana", 4); // 3,76
        cart.scan("Milk", 2); // 1,8
        cart.scan("Beef", 1120); // 12,80
        cart.scan("Butter", 1); // scan butter again - 1,94

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.valueOf(27.72));
    }

    @Test
    public void testScanNonExistingProduct() {
        // given
        inventory.add(new Product.ProductBuilder("Butter", BigDecimal.valueOf(1.00)).build());
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Product not in inventory", 3);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void testScanProductWhenInventoryIsEmpty() {
        // given
        Cart cart = new Cart(inventory);

        // when
        cart.scan("Wine", 1);

        // then
        assertThat(cart.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

}
