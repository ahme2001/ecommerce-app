import { useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { getUserData } from "../api/auth-api";

export default function OAuth2RedirectHandler() {
    const [searchParams] = useSearchParams();
    const {setToken , setUser }  = useAuth()
    const token = searchParams.get("token");
    const navigate = useNavigate();

    useEffect(() => {
        const handleOAuthRedirect = async () => {
            if (!token) {
                navigate("/auth?error=oauth_failed");
                return;
            }

            try {
                localStorage.setItem("token", token);
                const user = await getUserData();
                localStorage.setItem("user", JSON.stringify(user));
                setToken(token);
                setUser(user);
                navigate("/");
            } catch (error) {
                console.error(error);
                navigate("/auth?error=oauth_failed");
            }
        };

        handleOAuthRedirect();
    }, [token, navigate, setToken, setUser]);

    return <div>Logging you in...</div>;
}