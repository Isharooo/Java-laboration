package webshop.lab.se.javawebshop.bo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Shopping Cart (Varukorg)
 * Används för att hålla produkter i sessionen (betyg 3)
 */
public class Cart {

    // Map: productId -> CartItem
    private Map<Integer, CartItem> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    /**
     * Lägger till en produkt i varukorgen
     * Om produkten redan finns ökar vi kvantiteten
     */
    public void addProduct(Product product, int quantity) {
        int productId = product.getProductId();

        if (items.containsKey(productId)) {
            // Produkten finns redan - öka kvantitet
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Ny produkt - lägg till
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
        }
    }

    /**
     * Tar bort en produkt från varukorgen
     */
    public void removeProduct(int productId) {
        items.remove(productId);
    }

    /**
     * Uppdaterar kvantitet för en produkt
     */
    public void updateQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            removeProduct(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }

    /**
     * Tömmer hela varukorgen
     */
    public void clear() {
        items.clear();
    }

    /**
     * Beräknar totalpriset för alla produkter i varukorgen
     */
    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    /**
     * Returnerar antal unika produkter i varukorgen
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Returnerar totalt antal produkter (summerar alla kvantiteter)
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Kontrollerar om varukorgen är tom
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returnerar alla items i varukorgen
     */
    public Map<Integer, CartItem> getItems() {
        return items;
    }

    /**
     * Inre klass för att representera en produkt i varukorgen
     */
    public static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getSubtotal() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
}