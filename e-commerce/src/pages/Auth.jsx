import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { FcGoogle } from "react-icons/fc";

export default function Auth() {

  const [isLogin, setIsLogin] = useState(true);
  const { handleLogin, handleRegister } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);


  const [form, setForm] = useState({
    email: "",
    password: "",
    username: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    
    try {
      if (isLogin) {
        await handleLogin(form);
        navigate("/");
      } else {
        await handleRegister(form);
        setIsLogin(true);

      }
    } catch (err) {
      const message = err?.response?.data?.message;
      if (isLogin) {
        setError("Incorrect email or password");
      } else {
        setError(message || "Registration failed");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center h-screen">

      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 shadow rounded w-96 space-y-4"
      >

        <h2 className="text-xl font-bold">
          {isLogin ? "Login" : "Register"}
        </h2>

        {!isLogin && (
          <input
            className="w-full border p-2"
            placeholder="Email"
            onChange={(e) =>
              setForm({ ...form, email: e.target.value })
            }
          />
        )}

        <input
          className="w-full border p-2"
          placeholder="Username"
          onChange={(e) =>
            setForm({ ...form, username: e.target.value })
          }
        />

        <input
          type="password"
          className="w-full border p-2"
          placeholder="Password"
          onChange={(e) =>
            setForm({ ...form, password: e.target.value })
          }
        />

        <button
          disabled={loading}
          className="w-full bg-blue-600 text-white py-2 disabled:opacity-50"
        >
          {loading ? "Loading..." : "Submit"}
        </button>

        {/* Google Login */}
        <a
          href="http://localhost:8080/oauth2/authorization/google"
          className="block"
        >
          <button
            type="button"
            className="w-full flex items-center justify-center gap-3 border border-gray-300 rounded-lg py-3 hover:bg-gray-50 transition"
          >
            <FcGoogle className="text-2xl" />

            <span className="font-medium text-gray-700">
              Continue with Google
            </span>
          </button>
        </a>
        
        {error && (
          <div className="bg-red-100 text-red-600 p-2 rounded">
            {error}
          </div>
        )}

        <p
          className="text-center text-sm cursor-pointer text-blue-500"
          onClick={() => setIsLogin(!isLogin)}
        >
          {isLogin
            ? "Create account"
            : "Already have an account?"}
        </p>

      </form>
    </div>
  );
}