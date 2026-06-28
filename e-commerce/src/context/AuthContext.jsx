import { createContext, useEffect, useState, useContext } from "react";
import { login, register } from "../api/auth-api";


const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(null);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    
    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        const storedUser = localStorage.getItem("user");

        if (storedToken) {
            setToken(storedToken);
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    const handleLogin = async (data) => {
            await login(data).then((userData) => {
            setUser(userData);
            setToken(userData.jwtToken);
            localStorage.setItem("token", userData.jwtToken);
            localStorage.setItem("user", JSON.stringify(userData));
            return userData;
        }).catch((error) => {
            throw error;
        });
    };

    const handleRegister = async (data) => {
        await register(data).then(() => {
        }).catch((error) => {
            throw error;
        });
    };

    const handleLogout = () => {
        setToken(null);
        localStorage.removeItem("token");
        setUser(null);
        localStorage.removeItem("user");
    };

    return (
        <AuthContext.Provider value={{ token, user, loading, handleLogin, handleRegister, handleLogout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
}; 