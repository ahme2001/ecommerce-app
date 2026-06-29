import instance from "./axios";

export const createPaymentIntent = async (cartId) => {
    const response = await instance.post("/client-key", {
        cartId: cartId,
    });

    return response.data;
};