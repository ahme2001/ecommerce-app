import { useEffect, useState } from "react";
import { useLocation, useParams, Navigate } from "react-router-dom";
import { getProductById } from "../api/product-api";
import { useCart } from "../context/CartContext";


export default function ProductDetails() {
  const { id } = useParams();
  const location = useLocation();
  const { addToCart } = useCart();

  // 1. Try to get product from navigation state
  const [product, setProduct] = useState(location.state?.product || null);
  const [loading, setLoading] = useState(!location.state?.product);

  useEffect(() => {
    // If we already have product from state → skip API
    if (product) return;

    const fetchProduct = async () => {
      try {
        setLoading(true);
        const data = await getProductById(id);
        setProduct(data);
      } catch (err) {
        console.error("Failed to load product", err);
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  const imageSrc = product?.image && product.image.trim() !== ""
    ? product.image
    : "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300' viewBox='0 0 400 300'%3E%3Crect width='400' height='300' fill='%23f3f4f6'/%3E%3Ctext x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-size='24' fill='%236b7280'%3ENo Image Available%3C/text%3E%3C/svg%3E";


  // If still loading
  if (loading) {
    return <p className="text-center mt-10">Loading product...</p>;
  }

  // If product not found at all
  if (!product) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="max-w-6xl mx-auto bg-white p-6 rounded-lg shadow">

      {/* Layout */}
      <div className="grid md:grid-cols-2 gap-8">

        {/* Image */}
        <div>
          <img
            src={imageSrc}
            alt={product.productName}
            className="w-full h-96 object-cover rounded"
          />
        </div>

        {/* Info */}
        <div className="space-y-4">

          <h1 className="text-3xl font-bold">
            {product.productName}
          </h1>

          <p className="text-gray-600">
            {product.description}
          </p>

          <div className="mt-3 space-y-1 text-sm">
          <p className="text-2xl text-blue-600 font-semibold">
            <span className="font-medium">Price:</span> 
            {(product.specialPrice != null && product.specialPrice !== product.price) ? (
            <div>
                <span className="line-through text-gray-500 mr-2">
                    ${Number(product.price).toFixed(2)}
                </span>
                    ${Number(product.specialPrice).toFixed(2)}
            </div>
            ) : (
                <span>${Number(product.price).toFixed(2)}</span>
            )}
          </p>
          {(product.discount != null && product.discount !== 0) && (
            <p className="text-orange-500">
              <span className="font-medium">Discount:</span> {Number(product.discount).toFixed(2)}%
            </p>
          )}
          <p className={`font-medium ${product.quantity <= 0 ? "text-red-500" : "text-gray-700"}`}>
            Stock: {product.quantity}
          </p>
        </div>

          {/* Add to cart button placeholder */}
          <button className="bg-blue-600 text-white px-4 py-2 rounded"
            onClick={() => addToCart(product.productId ?? product.id)}
          >
            Add to Cart
          </button>

        </div>

      </div>

    </div>
  );
}