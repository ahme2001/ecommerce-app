import instance from "./axios";

export const getProducts = async (params) => {
    const response = await instance.get("/public/products" , { params });
    return response.data;
}

export const getProductById = async (id) => {
  const res = await instance.get(`/public/products/${id}`);
  return res.data;
};

export const getProductsByKeyword = async (keyword,queryParams) => {
    const response = await instance.get(`/public/products/keyword/${keyword}` , { params: queryParams });
    return response.data;
}

export const getProductsByCategory = async (categoryId, queryParams) => {
    const response = await instance.get(`/public/categories/${categoryId}/products`, { params: queryParams });
    return response.data;
}

export const deleteProduct = async (productId) => {
    const response = await instance.delete(`/admin/products/${productId}`);
    return response.data;
}