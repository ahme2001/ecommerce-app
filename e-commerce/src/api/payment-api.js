import instance from "./axios";

export const createPaymentIntent = async (cartId, addressId) => {
    const response = await instance.post("/client-key", {
        cartId: cartId,
        addressId: addressId
    });

    return response.data;
};