import { Link } from "react-router-dom";
import { useCart } from "../../context/CartContext";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import ProfileModal from "../../pages/Profile";

export default function Navbar() {
  const { cart } = useCart();
  const { user, handleLogout } = useAuth();
  const navigate = useNavigate();
  const [openProfile, setOpenProfile] = useState(false);

  const handleLogoutClick = () => {
    handleLogout();
    navigate("/auth");
  }

  return (
    <>
      <nav className="bg-white shadow-md px-6 py-4 flex items-center justify-between">
        
        {/* Logo */}
        <Link to="/" className="text-xl font-bold text-blue-600">
          E-Commerce
        </Link>

        {/* Navigation Links */}
        <div className="flex gap-6 text-gray-700">
          <Link to="/" className="hover:text-blue-500">Products</Link>
          <Link to="/cart" className="hover:text-blue-500">Cart ({cart?.products.length || 0})</Link>
          {user ? (
            <div className="flex gap-3 items-center">

                {/* Profile button */}
                <button
                  onClick={() => setOpenProfile(true)}
                  className="text-sm px-3 py-1 bg-gray-200 rounded"
                >
                  Profile
                </button>

                <button onClick={handleLogoutClick} className="hover:text-blue-500">
                  Logout
                </button>
            </div>
          ) : (
            <Link to="/auth" className="hover:text-blue-500">
              Login
            </Link>
          )}
        </div>

      </nav>

    {/* Profile Modal */}
      {openProfile && (
        <ProfileModal onClose={() => setOpenProfile(false)} />
      )}
    </>
  );
}