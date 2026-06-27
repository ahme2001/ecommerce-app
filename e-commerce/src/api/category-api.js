import instance from "./axios";

export const getCategories = async (params) => {
    const response = await instance.get("/public/categories" , { params });
    return response.data;
}