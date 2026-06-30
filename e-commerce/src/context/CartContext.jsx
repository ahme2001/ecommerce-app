import { createContext, useContext, useEffect, useState } from "react";
import {
  getCart,
  addToCart as apiAddToCart,
  updateCartQuantity,
  removeFromCart as apiRemoveFromCart,
} from "../api/cart-api";

const CartContext = createContext();

export function CartProvider({ children }) {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [addressId, setAddressId] = useState();

    // Load cart from backend
  
    const fetching = async () => {
            try {
            setLoading(true);
            const data = await getCart();
            setCart(data);
            } catch (err) {
            console.error("Failed to load cart", err);
            } finally {
            setLoading(false);
            }
        };

    useEffect(() => {
        const fetchCart = () => {
            fetching();
        };

        fetchCart();
    }, []);

    const addToCart = async (productId, quantity = 1) => {
        try {
        const updatedCart = await apiAddToCart(productId, quantity);
        setCart(updatedCart);
        } catch (err) {
        console.error("Add to cart failed", err);
        }
    };

    // Update quantity (increment / decrement)
    const changeQuantity = async (productId, operation) => {
        try {
        const updatedCart = await updateCartQuantity(productId, operation);
        setCart(updatedCart);
        } catch (err) {
        console.error("Update quantity failed", err);
        }
    };

    // Remove item
    const removeItem = async (cartId, productId) => {
        try {
        const updatedCart = await apiRemoveFromCart(cartId, productId);
        setCart(updatedCart);
        } catch (err) {
        console.error("Remove item failed", err);
        }
    };

    const cartTotal = cart?.totalPrice || 0.0;


  return (
    <CartContext.Provider
        value={{
            cart,
            loading,
            addressId,
            setAddressId,
            addToCart,
            changeQuantity,
            removeItem,
            cartTotal,
            fetching,
        }}
    >
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);