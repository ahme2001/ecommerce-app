import { useAuth } from "../context/AuthContext";
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {

    const { token, user, loading } = useAuth();

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!token || !user) {
        return <Navigate to="/auth" replace />;
    }

    return children;
}