import instance from "./axios";

export const login = async (data) => {
    try {
        const response = await instance.post("/auth/signin",data);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}


export const register = async (data) => {
    try {
        const response = await instance.post("/auth/signup",data);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
}