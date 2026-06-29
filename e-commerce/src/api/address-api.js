import instance from './axios';

export const createAddress = async (addressData) => {
    const response = await instance.post('/addresses', addressData);
    return response.data;
}

export const getAddresses = async () => {
    const response = await instance.get('/addresses')
    return response.data
}

export const getAddressById = async (addressId) => {
    const response = await instance.get(`/addresses/${addressId}`)
    return response.data
}

export const deleteAddressById = async (addressId) => {
    const response = await instance.delete(`/addresses/${addressId}`)
    return response.data
}