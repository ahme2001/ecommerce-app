import axios from "axios";
import toast from "react-hot-toast";

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/api",
    headers : {
        "Content-Type": "application/json",
    }
});

instance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
    const message =
        error?.response?.data?.message ||
        error?.message ||
        "Something went wrong";

        // Show popup instead of console errors
        toast.error(message);

        return Promise.reject(error);
    }
);

export default instance;