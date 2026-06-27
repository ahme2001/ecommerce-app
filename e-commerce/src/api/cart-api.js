import instance from "./axios";

// Get user cart
export const getCart = async () => {
  const res = await instance.get("/carts/users/cart");
  return res.data;
};

// Add product to cart
export const addToCart = async (productId, quantity) => {
  const res = await instance.post(
    `/carts/products/${productId}/quantity/${quantity}`
  );
  return res.data;
};

// Update quantity (increment / decrement)
export const updateCartQuantity = async (productId, operation) => {
  const res = await instance.put(
    `/cart/products/${productId}/quantity/${operation}`
  );
  return res.data;
};

// Remove product from cart
export const removeFromCart = async (cartId, productId) => {
  const res = await instance.delete(
    `/carts/${cartId}/product/${productId}`
  );
  return res.data;
};