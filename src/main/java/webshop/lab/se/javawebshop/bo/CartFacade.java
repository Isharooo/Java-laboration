package webshop.lab.se.javawebshop.bo;

import jakarta.servlet.http.HttpSession;
import webshop.lab.se.javawebshop.db.ProductDAO;
import webshop.lab.se.javawebshop.ui.CartInfo;
import webshop.lab.se.javawebshop.ui.CartItemInfo;
import webshop.lab.se.javawebshop.ui.ProductInfo;

import java.util.ArrayList;
import java.util.List;

public class CartFacade {

    private ProductDAO productDAO;

    public CartFacade() {
        this.productDAO = new ProductDAO();
    }

    public CartInfo getCartInfo(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        return convertToCartInfo(cart);
    }

    public boolean addProduct(HttpSession session, int productId, int quantity) {
        Cart cart = getOrCreateCart(session);

        Product product = productDAO.getProductById(productId);

        if (product != null && product.getStock() >= quantity) {
            cart.addProduct(product, quantity);
            System.out.println("Lade till i varukorg: " + product.getName() + " (antal: " + quantity + ")");
            return true;
        } else {
            System.out.println("Kunde inte lägga till produkt - otillräckligt lager");
            return false;
        }
    }

    public void removeProduct(HttpSession session, int productId) {
        Cart cart = getOrCreateCart(session);
        cart.removeProduct(productId);
        System.out.println("Tog bort produkt från varukorg: " + productId);
    }

    public boolean updateQuantity(HttpSession session, int productId, int quantity) {
        Cart cart = getOrCreateCart(session);

        Product product = productDAO.getProductById(productId);

        if (product != null && product.getStock() >= quantity) {
            cart.updateQuantity(productId, quantity);
            System.out.println("Uppdaterade kvantitet för produkt " + productId + ": " + quantity);
            return true;
        } else {
            System.out.println("Kunde inte uppdatera - otillräckligt lager");
            return false;
        }
    }

    public void clearCart(HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.clear();
        System.out.println("Varukorg tömdes");
    }

    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private CartInfo convertToCartInfo(Cart cart) {
        List<CartItemInfo> cartItemInfoList = new ArrayList<>();

        for (Cart.CartItem item : cart.getItems().values()) {
            Product product = item.getProduct();

            ProductInfo productInfo = new ProductInfo(
                    product.getProductId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategoryName()
            );

            CartItemInfo cartItemInfo = new CartItemInfo(
                    productInfo,
                    item.getQuantity(),
                    item.getSubtotal()
            );

            cartItemInfoList.add(cartItemInfo);
        }

        return new CartInfo(
                cartItemInfoList,
                cart.getTotalPrice(),
                cart.getTotalQuantity(),
                cart.isEmpty()
        );
    }
}