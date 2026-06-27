import { Link } from "react-router-dom";
import { useCart } from "../../context/CartContext";

export default function Navbar() {
  const { cart } = useCart();

  return (
    <nav className="bg-white shadow-md px-6 py-4 flex items-center justify-between">
      
      {/* Logo */}
      <Link to="/" className="text-xl font-bold text-blue-600">
        E-Commerce
      </Link>

      {/* Navigation Links */}
      <div className="flex gap-6 text-gray-700">
        <Link to="/" className="hover:text-blue-500">Products</Link>
        <Link to="/cart" className="hover:text-blue-500">Cart ({cart?.products.length || 0})</Link>
        <Link to="/auth" className="hover:text-blue-500">Login</Link>
      </div>

    </nav>
  );
}